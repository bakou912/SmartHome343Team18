package com.smart.home.backend.model.heating;

import com.smart.home.backend.model.houselayout.Room;

/**
 * Exception class for an already existing room in a zone.
 */
public class RoomAlreadyInZoneException extends Exception {
	
	/**
	 * 2-parameter constructor
	 * @param room room
	 * @param zone zone
	 */
	public RoomAlreadyInZoneException(Room room, HeatingZone zone) {
		super(room.getName() + " is already in zone " + zone.getName());
	}
	
}
