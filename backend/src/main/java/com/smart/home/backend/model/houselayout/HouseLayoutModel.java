package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.model.BaseModel;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;
import lombok.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class HouseLayoutModel implements BaseModel {
	
	private List<RoomRow> rows = new ArrayList<>();
	private Outside outside = new Outside();
	
	private PropertyChangeSupport support; 


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
	 * Checking if the direction is available in room.
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
	
	@Override
	public void reset() {
		this.setRows(new ArrayList<>());
		this.setOutside(new Outside());
	}



	/**
	 * add a PropertyChangeListener, essentially an observable due to deprecation
	 * @param pcl
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
	}
	
	/**
	 * remove a propertyChangeListner, essentially an observable due to deprecation
	 * 
	 * @param pcl
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
	

	/**
	 * update all propteryChangeListeners of change in awayMode only if no one is home.
	 * @param value
	 */
	public void updateAwayMode( Boolean detected){

		for (RoomRow row: this.getRows()) {
			for (Room room : row.getRooms()) {
				if(room.getPersons().size()>0){
					System.out.println("Cannot activate Away mode because there are still people home. Please remove them to activate AwayMode.");
				}
			}
		}

		if( detected == false){
			System.out.println("Activating Away mode!");
		}else{
			System.out.println("Deactivating Away mode.");
		}

		this.support.firePropertyChange("awayMode", null, detected);
	}
	/**
	 * update all propteryChangeListeners of change in DetectedPerson
	 * @param value
	 */
	public void updateDetectedPerson(Boolean detected){
		this.support.firePropertyChange("detectedPerson", null, detected);
	}

	/**
	 * update duration of auhtoritiesTimer
	 * @param duration
	 */
	public void updateAuthoritiesTimer(java.time.Duration duration){
		this.support.firePropertyChange("alertAuthoritiesTime", null, duration);
	}
}
