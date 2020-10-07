package com.smart.home.backend.service.mapper;

import com.smart.home.backend.input.HouseLayoutRoom;
import com.smart.home.backend.model.houselayout.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for a list of rooms.
 */
public class RoomsMapper {
	
	private RoomsMapper() {
		// Hiding constructor
	}
	
	/**
	 * Maps a number of lights to lights.
	 * @param houseLayoutRooms List of input rooms
	 * @return Mapped rooms
	 */
	public static List<Room> map(List<HouseLayoutRoom> houseLayoutRooms) {
		List<Room> rooms = new ArrayList<>();
		
		houseLayoutRooms.forEach(
				room -> rooms.add(
						Room.builder()
								.name(room.getName())
								.doors(DoorsMapper.map(room.getDoorsOn()))
								.windows(WindowsMapper.map(room.getWindowsOn()))
								.lights(LightsMapper.map(room.getLights()))
								.build()
				)
		);
		
		return rooms;
	}
	
}
