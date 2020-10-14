package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.smart.home.backend.model.ModelObject;
import lombok.experimental.SuperBuilder;

/**
 * Class for a row of rooms.
 */
@Getter
@Setter
@SuperBuilder
public class RoomRow extends ModelObject {
	
	private List<Room> rooms;
	
	/**
	 * Finds a room with the corresponding id.
	 * @param roomId Searched room's id
	 * @return Found room
	 */
	public Room findRoom(int roomId) {
		Room foundRoom = null;
		
		if (this.getRooms() != null && !this.getRooms().isEmpty()) {
			foundRoom = this.getRooms()
					.stream()
					.filter(roomRow -> roomRow.getId() == roomId)
					.findFirst()
					.orElse(null);
		}
		
		return foundRoom;
	}
	
}
