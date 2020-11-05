package com.smart.home.backend.service.mapper;

import com.smart.home.backend.input.RoomInput;
import com.smart.home.backend.model.houselayout.Room;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for a list of rooms.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomsMapper {
	
	/**
	 * Maps a number of lights to lights.
	 * @param roomInputs List of input rooms
	 * @return Mapped rooms
	 */
	public static List<Room> map(List<RoomInput> roomInputs) {
		List<Room> rooms = new ArrayList<>();
		
		for (int i = 0; i < roomInputs.size(); i++) {
			RoomInput roomInput = roomInputs.get(i);
			
			Room room = Room.builder()
					.id(i)
					.name(roomInput.getName())
					.doors(DoorsMapper.map(roomInput.getDoorsOn()))
					.windows(WindowsMapper.map(roomInput.getWindowsOn()))
					.build();
			
			room.getDoorId().setLastId(room.getDoors().size());
			room.getWindowId().setLastId(room.getWindows().size());
			room.getPersonId().setLastId(room.getPersons().size());
			rooms.add(room);
		}
		
		return rooms;
	}
	
}
