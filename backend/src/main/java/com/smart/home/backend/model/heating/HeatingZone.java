package com.smart.home.backend.model.heating;

import com.smart.home.backend.model.ModelObject;
import com.smart.home.backend.model.houselayout.Room;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
	private Double targetTemperature = 0.0;
	
	@Builder.Default
	private List<Room> rooms = new ArrayList<>();
	
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
	public void adjustRoomTemperatures() {
		for (Room room: rooms) {
			double tempDelta = this.getTargetTemperature() - room.getTemperature();
			int multiplier = 0;
			
			if (tempDelta <= -INCREMENT_VALUE) {
				multiplier = -1;
			} else if (tempDelta >= INCREMENT_VALUE) {
				multiplier = 1;
			}
			
			room.setTemperature(room.getTemperature() + multiplier * INCREMENT_VALUE);
		}
	}
	
}
