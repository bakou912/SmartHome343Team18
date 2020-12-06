package com.smart.home.backend.model.heating;

import com.smart.home.backend.model.houselayout.Room;

public class RoomAlreadyInZoneException extends Exception {
	
	public RoomAlreadyInZoneException(Room room, HeatingZone zone) {
		super(room.getName() + " is already in zone " + zone.getName());
	}
	
}
