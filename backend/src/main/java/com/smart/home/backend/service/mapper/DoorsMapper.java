package com.smart.home.backend.service.mapper;

import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.DoorState;
import com.smart.home.backend.model.houselayout.directional.Door;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for a list of doors.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoorsMapper {
	
	/**
	 * Maps door directions to doors.
	 * @param doorDirections List of door directions
	 * @return Mapped doors
	 */
	public static List<Door> map(List<Direction> doorDirections) {
		List<Door> doors = new ArrayList<>();
		
		for (int i = 0; i < doorDirections.size(); i++) {
			Direction doorDirection = doorDirections.get(i);
			
			doors.add(
					Door.builder()
							.id(i)
							.state(DoorState.CLOSED)
							.direction(doorDirection)
							.build()
			);
		}
		
		return doors;
	}
}
