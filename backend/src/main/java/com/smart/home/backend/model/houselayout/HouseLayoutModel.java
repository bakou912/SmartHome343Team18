package com.smart.home.backend.model.houselayout;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.smart.home.backend.constant.Direction;

/**
 * Model for the house layout.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseLayoutModel {
	
	private List<RoomRow> rows;
	
	/**
	 * Finds a row with the corresponding id.
	 * @param id Searched row's id
	 * @return Found row
	 */
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
	 * @param rowId Row id
	 * @param roomId Room id
	 * @param direction direction of placement within room
	 * @return True if that direction is available. False otherwise.
	 */
	public Boolean availableDirection(int rowId, int roomId, Direction direction){
		List<Direction> availableDirections = new ArrayList<>();

		Room room = this.findRoom(rowId, roomId);

		//get direction for doors and windows
		for(Door door : room.getDoors()){
			availableDirections.add(door.getDirection());
		}
		
		for(Window window : room.getWindows()){
			availableDirections.add(window.getDirection());
		}

		if(availableDirections.contains(direction)){
			return false;
		}

		return true;
	} 
}
