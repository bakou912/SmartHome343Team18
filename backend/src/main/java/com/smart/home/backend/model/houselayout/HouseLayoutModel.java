package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.constant.DoorState;
import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.input.PersonInput;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.BaseModel;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.model.security.SecurityModel;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.model.simulationparameters.location.RoomItemLocationPosition;
import com.smart.home.backend.service.OutputConsole;
import lombok.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.smart.home.backend.constant.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Model for the house layout.
 */
@Getter
@Setter
@Component
public class HouseLayoutModel implements BaseModel, PropertyChangeListener {
	
	private static final String BACKYARD = "Backyard";
	private static final String ENTRANCE = "Entrance";
	
	private Integer nbPersonsInside;
	private List<RoomRow> rows;
	private OutsideLocation entrance;
	private OutsideLocation backyard;
	
	private PropertyChangeSupport support;

	/**
	 * Default constructor.
	 */
	@Autowired
	public HouseLayoutModel(SecurityModel securityModel) {
		nbPersonsInside = 0;
		this.rows = new ArrayList<>();
		this.backyard = new OutsideLocation(BACKYARD);
		this.entrance = new OutsideLocation(ENTRANCE);
		this.support = new PropertyChangeSupport(this);
		this.addPropertyChangeListener(securityModel);
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
	 * Setting a location's light's away mode value
	 * @param location location where to change light
	 * @param activated away mode value
	 */
	public void setAwayMode(Location location, boolean activated) {
		Light oldValue = null;
		Light newValue = null;
		
		location.getLight().setAwayMode(activated);
		
		if (activated) {
			newValue = location.getLight();
		} else {
			oldValue = location.getLight();
		}
		
		this.support.firePropertyChange("lightAwayMode", oldValue, newValue);
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
	
	/**
	 * Adding a person to the layout.
	 * @param location the person's desired location
	 * @param personInput person input
	 * @return person's id
	 */
	public Integer addPerson(Location location, PersonInput personInput) {
		if (!(location instanceof OutsideLocation)) {
			this.incrementNbPersonsInside();
		}
		
		if (location.getLight().getAutoMode().equals(true)) {
			location.getLight().setState(LightState.ON);
		}
		
		return location.addPerson(personInput);
	}
	
	/**
	 * Removing a person from the layout.
	 * @param location the person's desired location
	 * @param personId person's id
	 * @return Removed person's name
	 */
	public String removePerson(Location location, int personId) {
		String personName = null;
		Person person = location.getPersons().stream().filter(p -> p.getId().equals(personId)).findFirst().orElse(null);
		
		if (person != null) {
			personName = person.getName();
		}
		
		boolean removed = location.getPersons().removeIf(p -> p.getId().equals(personId));
		
		if (removed) {
			if (location.getLight().getAutoMode().equals(true) && location.getPersons().isEmpty()) {
				location.getLight().setState(LightState.OFF);
			}
			if (!(location instanceof OutsideLocation)) {
				this.decrementNbPersonsInside();
			}
		}
		
		
		return personName;
	}
	
	/**
	 * Incrementing the number of persons inside the house
	 */
	private void incrementNbPersonsInside() {
		int oldValue = this.getNbPersonsInside();
		int newValue = oldValue + 1;
		this.updateNbPersons(oldValue, newValue);
	}
	
	/**
	 * Decrementing the number of persons inside the house
	 */
	private void decrementNbPersonsInside() {
		int oldValue = this.getNbPersonsInside();
		if (oldValue > 0) {
			int newValue = oldValue - 1;
			this.updateNbPersons(oldValue, newValue);
		}
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
	 * Updating all propertyChangeListeners of change in nbPersons
	 */
	public void updateNbPersons(int oldValue, int newValue){
		this.setNbPersonsInside(newValue);
		this.support.firePropertyChange("nbPersonsInside", oldValue, newValue);
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
	
	/**
	 * Activating a room's away mode state.
	 * @param room targeted room
	 */
	private void activateRoomAwayMode(Room room) {
		for (Window window: room.getWindows()) {
			if (window.getState().equals(WindowState.BLOCKED)) {
				OutputConsole.log("SHC | " + room.getName() + "'s " + window.getDirection() + " window could not be closed, because it is obstructed");
			} else {
				window.setState(WindowState.CLOSED);
			}
		}
		for (Door door: room.getDoors()) {
			door.setState(DoorState.LOCKED);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("awayModeOn")) {
			for (RoomRow roomRow: this.getRows()) {
				for (Room room: roomRow.getRooms()) {
					this.activateRoomAwayMode(room);
				}
			}
		}
	}
}
