package com.smart.home.backend.model.heating;

import com.smart.home.backend.constant.HeatingZonePeriod;
import com.smart.home.backend.model.ModelObject;
import com.smart.home.backend.model.houselayout.Room;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for a heating zone.
 */
@Getter
@Setter
@SuperBuilder
public class HeatingZone extends ModelObject {
	
	private static final Double INCREMENT_VALUE = 0.1;
	
	@Builder.Default
	private String name = "";
	
	@Builder.Default
	private List<Room> rooms = new ArrayList<>();
	
	@Builder.Default
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
	 * @return Whether the room was removed
	 */
	public boolean removeRoom(Room room) {
		return this.getRooms().remove(room);
	}
	
	/**
	 * Adjusts rooms' temperatures according to the target temperature.
	 */
	public void adjustRoomTemperatures(LocalDateTime date) {
		double targetTemperature = this.determineTargetTemperature(date);
		
		for (Room room: rooms) {
			double tempDelta = targetTemperature - room.getTemperature();
			int multiplier = 0;
			
			if (tempDelta <= -INCREMENT_VALUE) {
				multiplier = -1;
			} else if (tempDelta >= INCREMENT_VALUE) {
				multiplier = 1;
			}
			
			room.setTemperature(room.getTemperature() + multiplier * INCREMENT_VALUE);
		}
	}
	
	/**
	 * Determine which zone period is active at some date.
	 * @param date some date
	 * @return Which zone period is active at some date
	 */
	private double determineTargetTemperature(LocalDateTime date) {
		int hour = date.getHour();
		HeatingZonePeriod period;
		
		if (hour >= 5 && hour <= 11) {
			period = HeatingZonePeriod.MORNING;
		} else if (hour > 11 && hour <= 21) {
			period = HeatingZonePeriod.AFTERNOON;
		} else {
			period = HeatingZonePeriod.NIGHT;
		}
		
		return this.getPeriods().getTargetTemperature(period);
	}
	
}
