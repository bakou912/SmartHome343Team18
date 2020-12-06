package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.constant.RoomHeatingMode;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;

import com.smart.home.backend.service.OutputConsole;
import com.smart.home.backend.service.util.IdUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * Class for a room.
 */
@Getter
@SuperBuilder
public class Room extends Location {
	
	@Setter
	private String name;
	@Setter
	@Builder.Default
	private List<Window> windows = new ArrayList<>();
	@Setter
	@Builder.Default
	private List<Door> doors = new ArrayList<>();
	@Setter
	@Builder.Default
	private Double temperature = 0.0;
	@Setter
	@Builder.Default
	private RoomHeatingMode heatingMode = RoomHeatingMode.ZONE;
	@Setter
	@Builder.Default
	private Boolean havc = true;
	@Setter
	@Nullable
	private Integer rowId;
	
	private final IdUtil doorId = new IdUtil();
	private final IdUtil windowId = new IdUtil();
	
	/**
	 * Finds a door with the corresponding id.
	 * @param id Searched door's id
	 * @return Found light
	 */
	@Nullable
	public Door findDoor(int id) {
		return this.getDoors()
				.stream()
				.filter(door -> door.getId() == id)
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Finds a window with the corresponding id.
	 * @param id Searched window's id
	 * @return Found light
	 */
	@Nullable
	public Window findWindow(int id) {
		return this.getWindows()
				.stream()
				.filter(window -> window.getId() == id)
				.findFirst()
				.orElse(null);
	}

	/**
	 * Adds a door to the door list
	 * @param doorInput door input
	 */
	public void addDoor(DoorInput doorInput) {
		this.getDoors().add(
				Door.builder()
						.direction(doorInput.getDirection())
						.state(doorInput.getState())
						.id(this.getDoorId().newId())
						.build()
		);
	}
	
	/**
	 * Adds a window to the window list
	 * @param windowInput window input
	 */
	public void addWindow(WindowInput windowInput) {
		this.getWindows().add(
				Window.builder()
						.direction(windowInput.getDirection())
						.state(windowInput.getState())
						.id(this.getWindowId().newId())
						.build()
		);
	}
	
	/**
	 * Adjusts the room temperature during summer time
	 * @param outsideTemp outside temperature
	 * @param isSummer true if its summer time false if its not summer time
	 * @param targetTemperature target temperature
	 */
	public void adjustRoomSummerBreeze(Double outsideTemp, boolean isSummer, double targetTemperature) {
		boolean summerBreeze = isSummerBreeze(outsideTemp, targetTemperature, isSummer);
		
		if (summerBreeze) {
			this.setHavc(false);
		} else {
			this.setHavc(isHavcOn(targetTemperature));
		}
		
		for (Window window : this.getWindows()) {
			WindowState newState = summerBreeze ? WindowState.OPEN : WindowState.CLOSED;
			if (!newState.equals(window.getState()) && !window.getState().equals(WindowState.BLOCKED) ) {
				window.setState(newState);
				OutputConsole.log("SHH | Changed window state to " + window.getState());
			}
			
		}
	}
	
	/**
	 * Decides whether the windows should be open or not during summer.
	 * Handles issues with windows
	 * @param outsideTemp outside temperature
	 * @param targetTemperature target temperature
	 * @param isSummer true if its summer time false if its not summer time
	 * @return whether to open windows or not
	 */
	private boolean isSummerBreeze(Double outsideTemp, Double targetTemperature, boolean isSummer){
		if (isSummer && (targetTemperature < this.getTemperature()) && (outsideTemp < this.getTemperature()) && this.getHeatingMode() != RoomHeatingMode.AWAY) {
			for (Window window : this.getWindows()) {
				if (window.getState() == WindowState.BLOCKED) {
					OutputConsole.log("SHH | Window " + window.getDirection() + " in " + this.getName() + " is blocked. Cancelled window opening command.");
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	
	/**
	 * Method to determine if the HAVC should be on or off
	 * @param targetTemperature target temperature
\	 * @return true if HAVC should on and false if it should be off
	 */
	private boolean isHavcOn(double targetTemperature) {
		boolean targetReached = Math.abs(this.getTemperature() - targetTemperature) <= 0.1;
		boolean targetReacquired = Math.abs(this.getTemperature() - targetTemperature) >= 0.25;
		return (this.getHavc() && !targetReached) || (!this.getHavc() && targetReacquired);
	}
	
}
