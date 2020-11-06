package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.BaseModel;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.model.simulationparameters.location.Location;
import com.smart.home.backend.model.simulationparameters.location.RoomItemLocation;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.smart.home.backend.constant.Direction;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Model for the house layout.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class HouseLayoutModel implements BaseModel {
	
	private List<RoomRow> rows = new ArrayList<>();
	private Outside outside = new Outside();
	
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
	 * @param location row's location
	 * @return Found room
	 */
	@Nullable
	public Room findRoom(Location location) {
		RoomRow foundRow = this.findRow(location.getRowId());
		Room foundRoom = null;
		
		if (foundRow != null) {
			foundRoom = foundRow.findRoom(location.getRoomId());
		}
		
		return foundRoom;
	}
	
	/**
	 * Finds a door with the corresponding row, room and door ids.
	 * @param location door's location
	 * @return Found door
	 */
	@Nullable
	public Door findDoor(RoomItemLocation location) {
		Room foundRoom = this.findRoom(location);
		Door foundDoor = null;
		
		if (foundRoom != null) {
			foundDoor = foundRoom.findDoor(location.getItemId());
		}
		
		return foundDoor;
	}
	
	/**
	 * Finds a window with the corresponding row, room and window ids.
	 * @param location window's location
	 * @return Found window
	 */
	@Nullable
	public Window findWindow(RoomItemLocation location) {
		Room foundRoom = this.findRoom(location);
		Window foundWindow = null;
		
		if (foundRoom != null) {
			foundWindow = foundRoom.findWindow(location.getItemId());
		}
		
		return foundWindow;
	}
	
	/**
	 * Checking if the direction is available in room.
	 * @param room The room
	 * @param direction direction of placement within room
	 * @return True if that direction is available. False otherwise.
	 */
	public boolean isDirectionAvailable(Room room, Direction direction){
		List<Direction> availableDirections = new ArrayList<>();
		
		for (Door door : room.getDoors()) {
			availableDirections.add(door.getDirection());
		}
		
		for (Window window : room.getWindows()) {
			availableDirections.add(window.getDirection());
		}
		
		return !availableDirections.contains(direction);
	}
	
	/**
	 * Modifies a Window.
	 * @param windowInput window input
	 * @return Modified window. Null if not found
	 */
	public Window modifyWindowState(WindowInput windowInput) {
		RoomItemLocation location = windowInput.getLocation();
		Window targetWindow = this.findWindow(location);
		
		if (targetWindow == null) {
			return null;
		}
		
		Direction direction = windowInput.getDirection();
		WindowState state = windowInput.getState();
		
		if (direction != null) {
			targetWindow.setDirection(direction);
		}
		
		if (state != null) {
			targetWindow.setState(state);
		}
		
		return targetWindow;
	}
	@Override
	public void reset() {
		this.setRows(new ArrayList<>());
		this.setOutside(new Outside());
	}
}
