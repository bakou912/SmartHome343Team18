package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import java.util.List;
import com.smart.home.backend.model.ModelObject;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * Class for a room.
 */
@Getter
@Setter
@SuperBuilder
public class Room extends ModelObject {
	
	@NonNull
	private String name;
	@NonNull
	private List<Light> lights;
	@NonNull
	private List<Window> windows;
	@NonNull
	private List<Door> doors;
	
	/**
	 * Finds a light with the corresponding id.
	 * @param id Searched light's id
	 * @return Found light
	 */
	@Nullable
	public Light findLight(int id) {
		return this.getLights()
				.stream()
				.filter(light -> light.getId() == id)
				.findFirst()
				.orElse(null);
	}
	
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

}
