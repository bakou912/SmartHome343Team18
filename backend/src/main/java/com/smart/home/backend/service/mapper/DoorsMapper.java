package com.smart.home.backend.service.mapper;

import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.DoorState;
import com.smart.home.backend.model.houselayout.Door;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for a list of doors.
 */
public class DoorsMapper {
	
	private DoorsMapper() {
		// Hiding constructor
	}
	
	/**
	 * Maps door directions to doors.
	 * @param doorDirections List of door directions
	 * @return Mapped doors
	 */
	public static List<Door> map(List<String> doorDirections) {
		List<Door> doors = new ArrayList<>();
		
		doorDirections.forEach(
				doorDirection -> doors.add(
						Door.builder()
								.state(DoorState.LOCKED)
								.direction(Direction.get(doorDirection))
								.build()
				)
		);
		
		return doors;
	}
}
