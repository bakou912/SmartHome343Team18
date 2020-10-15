package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.input.LightInput;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.service.mapper.DoorsMapper;
import com.smart.home.backend.service.mapper.LightsMapper;
import com.smart.home.backend.service.mapper.WindowsMapper;
import com.smart.home.backend.service.util.IdUtil;
import lombok.Builder;
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
@SuperBuilder
public class Room extends ModelObject {
	
	@NonNull
	@Setter
	private String name;
	@NonNull
	@Setter
	private List<Light> lights;
	@NonNull
	@Setter
	private List<Window> windows;
	@NonNull
	@Setter
	private List<Door> doors;
	
	private final IdUtil lightId = new IdUtil();
	private final IdUtil doorId = new IdUtil();
	private final IdUtil windowId = new IdUtil();
	
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
	
	public void addDoor(DoorInput doorInput) {
		this.getDoors().add(
				Door.builder()
						.direction(doorInput.getDirection())
						.state(doorInput.getState())
						.id(this.getDoorId().newId())
						.build()
		);
	}
	
	public void addWindow(WindowInput windowInput) {
		this.getWindows().add(
				Window.builder()
						.direction(windowInput.getDirection())
						.state(windowInput.getState())
						.id(this.getWindowId().newId())
						.build()
		);
	}
	
	public void addLight(LightInput lightInput) {
		this.getLights().add(
				Light.builder()
						.state(lightInput.getState())
						.id(this.getLightId().newId())
						.build()
		);
	}

}
