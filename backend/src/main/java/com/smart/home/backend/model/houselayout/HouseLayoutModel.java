package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.smart.home.backend.constant.Direction;
import org.springframework.lang.Nullable;

/**
 * Model for the house layout.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseLayoutModel {
	
	@NonNull
	private List<RoomRow> rows;
	
	/**
	 * Finds a row with the corresponding id.
	 * @param id Searched row's id
	 * @return Found row
	 */
	@Nullable
	public RoomRow findRow(int id) {
		RoomRow foundRow = null;
		
		if (this.getRows() != null && !this.getRows().isEmpty()) {
			foundRow = this.getRows()
					.stream()
					.filter(roomRow -> roomRow.getId() == id)
					.findFirst()
					.orElse(null);
		}
		
		return foundRow;
	}
	
	/**
	 * Finds a room with the corresponding row and room ids.
	 * @param rowId Row's id
	 * @param roomId Searched room's id
	 * @return Found room
	 */
	@Nullable
	public Room findRoom(int rowId, int roomId) {
		RoomRow foundRow = this.findRow(rowId);
		Room foundRoom = null;
		
		if (foundRow != null) {
			foundRoom = foundRow.findRoom(roomId);
		}
		
		return foundRoom;
	}
	
	/**
	 * Finds a light with the corresponding row, room and light ids.
	 * @param rowId Row id
	 * @param roomId Room id
	 * @param lightId Searched light's id
	 * @return Found light
	 */
	@Nullable
	public Light findLight(int rowId, int roomId, int lightId) {
		Room foundRoom = this.findRoom(rowId, roomId);
		Light foundLight = null;
		
		if (foundRoom != null) {
			foundLight = foundRoom.findLight(lightId);
		}
		
		return foundLight;
	}
	
	/**
	 * Finds a door with the corresponding row, room and door ids.
	 * @param rowId Row id
	 * @param roomId Room id
	 * @param doorId Searched door's id
	 * @return Found door
	 */
	@Nullable
	public Door findDoor(int rowId, int roomId, int doorId) {
		Room foundRoom = this.findRoom(rowId, roomId);
		Door foundDoor = null;
		
		if (foundRoom != null) {
			foundDoor = foundRoom.findDoor(doorId);
		}
		
		return foundDoor;
	}
	
	/**
	 * Finds a window with the corresponding row, room and window ids.
	 * @param rowId Row id
	 * @param roomId Room id
	 * @param windowId Searched window's id
	 * @return Found window
	 */
	@Nullable
	public Window findWindow(int rowId, int roomId, int windowId) {
		Room foundRoom = this.findRoom(rowId, roomId);
		Window foundWindow = null;
		
		if (foundRoom != null) {
			foundWindow = foundRoom.findWindow(windowId);
		}
		
		return foundWindow;
	}
	
	/**
	 * Checks to see if the direction is available in room.
	 * 
	 * @param room The room
	 * @param direction direction of placement within room
	 * @return True if that direction is available. False otherwise.
	 */
	public boolean isDirectionAvailable(Room room, Direction direction){
		List<Direction> availableDirections = new ArrayList<>();
		
		//get direction for doors and windows
		for (Door door : room.getDoors()) {
			availableDirections.add(door.getDirection());
		}
		
		for (Window window : room.getWindows()) {
			availableDirections.add(window.getDirection());
		}
		
		return !availableDirections.contains(direction);
	} 
}
