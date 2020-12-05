package com.smart.home.backend.model.houselayout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.home.backend.constant.RoomHeatingMode;
import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;

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
	@JsonIgnore
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
	
}
