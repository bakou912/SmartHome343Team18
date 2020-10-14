package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.smart.home.backend.model.ModelObject;
import lombok.experimental.SuperBuilder;

/**
 * Class for a room.
 */
@Getter
@Setter
@SuperBuilder
public class Room extends ModelObject {
	
	private String name;
	private List<Light> lights;
	private List<Window> windows;
	private List<Door> doors;
	
	/**
	 * Finds a light with the corresponding id.
	 * @param id Searched light's id
	 * @return Found light
	 */
	public Light findLight(int id) {
		Light foundLight = null;
		
		if (this.getLights() != null && !this.getLights().isEmpty()) {
			foundLight = this.getLights()
					.stream()
					.filter(light -> light.getId() == id)
					.findFirst()
					.orElse(null);
		}
		
		return foundLight;
	}
	
	/**
	 * Finds a door with the corresponding id.
	 * @param id Searched door's id
	 * @return Found light
	 */
	public Door findDoor(int id) {
		Door foundDoor = null;
		
		if (this.getDoors() != null && !this.getDoors().isEmpty()) {
			foundDoor = this.getDoors()
					.stream()
					.filter(door -> door.getId() == id)
					.findFirst()
					.orElse(null);
		}
		
		return foundDoor;
	}
	
	/**
	 * Finds a window with the corresponding id.
	 * @param id Searched window's id
	 * @return Found light
	 */
	public Window findWindow(int id) {
		Window foundWindow = null;
		
		if (this.getWindows() != null && !this.getWindows().isEmpty()) {
			foundWindow = this.getWindows()
					.stream()
					.filter(window -> window.getId() == id)
					.findFirst()
					.orElse(null);
		}
		
		return foundWindow;
	}

}
