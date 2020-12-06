package com.smart.home.backend.model.heating;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.HeatingZonePeriod;
import com.smart.home.backend.constant.RoomHeatingMode;
import com.smart.home.backend.model.ModelObject;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.service.OutputConsole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class for a heating zone.
 */
@Getter
@Setter
@SuperBuilder
public class HeatingZone extends ModelObject {
	
	private static final Double INCREMENT_VALUE_HAVC = 0.1;
	private static final Double INCREMENT_VALUE = 0.05;
	
	@Builder.Default
	private String name = "";
	
	@Builder.Default
	private List<Room> rooms = new ArrayList<>();
	
	@Builder.Default
	@JsonIgnore
	private HeatingZonePeriods periods = new HeatingZonePeriods();
	
	/**
	 * Adds a room to the rooms list.
	 * @param room room to add
	 * @return Whether the room was added
	 */
	public boolean addRoom(Room room) {
		if (this.getRooms().contains(room)) {
			return false;
		}
		
		return this.getRooms().add(room);
	}
	
	/**
	 * Removes a room from the rooms list.
	 * @param room room to remove
	 * @return Removed room
	 */
	public Room removeRoom(Room room) {
		this.getRooms().remove(room);
		return room;
	}
	
	/**
	 * Adjusts rooms' temperatures according to the target temperature and different parameters.
	 * @param adjustment adjustment parameters
	 */
	public void adjustRoomTemperatures(RoomTemperatureAdjustment adjustment) {
		double targetTemperature = this.determineTargetTemperature(adjustment.getDate(), adjustment.getGlobalHeatingMode(), adjustment.getDefaultTemperature());
		for (Room room: rooms) {
			if (!room.getHeatingMode().equals(RoomHeatingMode.OVERRIDDEN)) {
				adjustTemperature(adjustment, targetTemperature, room);
			}
			this.pipeBurstWarning(room);
		}
	}
	
	/**
	 * The temperature adjustment for a single room.
	 * @param adjustment adjustment parameters
	 * @param targetTemperature target temperature
	 * @param room room to adjust
	 */
	private void adjustTemperature(RoomTemperatureAdjustment adjustment, double targetTemperature, Room room) {
		if (adjustment.isSystemOn()) {
			room.adjustRoomSummerBreeze(adjustment.getOutsideTemp(), adjustment.isSummer(), targetTemperature);
		}
		double tempDelta = ((room.getHavc() && adjustment.isSystemOn()) ? targetTemperature : adjustment.getOutsideTemp()) - room.getTemperature();
		int multiplier = 0;
		double increment = ((room.getHavc() && adjustment.isSystemOn()) ? INCREMENT_VALUE_HAVC : INCREMENT_VALUE);
		if (tempDelta <= -increment) {
			multiplier = -1;
		} else if (tempDelta >= increment) {
			multiplier = 1;
		}
		
		room.setTemperature(room.getTemperature() + multiplier * increment);
	}
	
	/**
	 * Writes to the console a message if the is a risk of pipe burst
	 * @param room room to check for risk of pipe burst
	 */
	private void pipeBurstWarning(Room room) {
		if (room.getTemperature() <= 0){
			OutputConsole.log("SHH | WARNING !!! Freezing temperatures in the " + room.getName() + " pipes might burst");
		}
	}

	/**
	 * Determine which temperature to use.
	 * @param date current date
	 * @param globalHeatingMode the system's current global heating mode
	 * @param defaultTemperature default temperature for AWAY mode
	 * @return Which temperature to use
	 */
	private double determineTargetTemperature(LocalDateTime date, RoomHeatingMode globalHeatingMode, double defaultTemperature) {
		int hour = date.getHour();
		double temperature;
		
		if (globalHeatingMode.equals(RoomHeatingMode.ZONE)) {
			HeatingZonePeriod period;
			if (hour >= 5 && hour <= 11) {
				period = HeatingZonePeriod.MORNING;
			} else if (hour > 11 && hour <= 21) {
				period = HeatingZonePeriod.AFTERNOON;
			} else {
				period = HeatingZonePeriod.NIGHT;
			}
			temperature = this.getPeriods().getTargetTemperature(period);
		} else {
			temperature = defaultTemperature;
		}
		
		return temperature;
	}
	
	/**
	 * Json creator for the period maps
	 * @return periodMap
	 */
	@JsonCreator
	@JsonProperty("periods")
	public Map<HeatingZonePeriod, Double> getPeriodMap() {
		return this.getPeriods().getPeriodMap();
	}
	
}
