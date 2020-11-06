package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.BaseModel;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.model.simulationparameters.location.RoomItemLocationPosition;
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
@Component
public class HouseLayoutModel implements BaseModel {
	
	private static final String BACKYARD = "Backyard";
	private static final String ENTRANCE = "Entrance";
	
	private List<RoomRow> rows;
	private OutsideLocation entrance;
	private OutsideLocation backyard;
	
	private PropertyChangeSupport support;
	
	/**
	 * Default constructor.
	 */
	public HouseLayoutModel() {
		this.rows = new ArrayList<>();
		this.backyard = new OutsideLocation(BACKYARD);
		this.entrance = new OutsideLocation(ENTRANCE);
		this.support = new PropertyChangeSupport(this);
	}
	
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
	 * @param locationPosition row's location
	 * @return Found room
	 */
	@Nullable
	public Room findRoom(LocationPosition locationPosition) {
		RoomRow foundRow = this.findRow(locationPosition.getRowId());
		Room foundRoom = null;
		
		if (foundRow != null) {
			foundRoom = foundRow.findRoom(locationPosition.getRoomId());
		}
		
		return foundRoom;
	}
	
	/**
	 * Finds a door with the corresponding row, room and door ids.
	 * @param location door's location
	 * @return Found door
	 */
	@Nullable
	public Door findDoor(RoomItemLocationPosition location) {
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
	public Window findWindow(RoomItemLocationPosition location) {
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
		RoomItemLocationPosition location = windowInput.getLocation();
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
	
	/**
	 * Checks if a person is in the house.
	 * @param personName person's name
	 * @return Wether the person is in the house or not
	 */
	public boolean isInHouse(String personName) {
		boolean present = false;
		
		for (RoomRow roomRow: this.getRows()) {
			for (Room room: roomRow.getRooms()) {
				if (room.getPersons().stream().anyMatch(p -> p.getName().equals(personName))) {
					return true;
				}
			}
		}
		
		if (this.getBackyard().getPersons().stream().anyMatch(p -> p.getName().equals(personName))) {
			present = true;
		}
		
		if (this.getEntrance().getPersons().stream().anyMatch(p -> p.getName().equals(personName))) {
			present = true;
		}
		
		return present;
	}
	
	@Override
	public void reset() {
		this.setRows(new ArrayList<>());
		this.setBackyard(new OutsideLocation(BACKYARD));
		this.setEntrance(new OutsideLocation(ENTRANCE));
	}

	/**
	 * Adds a PropertyChangeListener, essentially an observable due to deprecation
	 * @param pcl property change listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
	}
	
	/**
	 * Removes a propertyChangeListener, essentially an observable due to deprecation
	 * @param pcl property change listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
	
	/**
	 * Updates all propteryChangeListeners of change in awayMode only if no one is home.
	 * @param activate wether to activate or deactivate away mode
	 */
	public void updateAwayMode(boolean activate){

		for (RoomRow row: this.getRows()) {
			for (Room room : row.getRooms()) {
				if (!room.getPersons().isEmpty()) {
					System.out.println("Cannot activate Away mode because there are still people home. Please remove them to activate AwayMode.");
				}
			}
		}
		
		if(activate) {
			System.out.println("Deactivating Away mode.");
		} else {
			System.out.println("Activating Away mode!");
		}
		
		this.support.firePropertyChange("awayMode", null, activate);
	}
	
	/**
	 * Updates all propteryChangeListeners of change in DetectedPerson
	 * @param detected wether someone was detected or not
	 */
	public void updateDetectedPerson(boolean detected){
		this.support.firePropertyChange("detectedPerson", null, detected);
	}

	/**
	 * Updates duration of auhtoritiesTimer
	 * @param duration duration to alert authoratities
	 */
	public void updateAuthoritiesTimer(java.time.Duration duration){
		this.support.firePropertyChange("alertAuthoritiesTime", null, duration);
	}
	
	/**
	 * Retrieves an outside location using its name.
	 * @param locationName location's name
	 * @return Corresponding outside location
	 */
	public OutsideLocation getOutsideLocation(String locationName) {
		OutsideLocation outsideLocation = this.getBackyard();
		
		if (locationName.equals(ENTRANCE)) {
			outsideLocation = this.getEntrance();
		}
		
		return outsideLocation;
	}
	
}
