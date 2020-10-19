package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.input.LightInput;
import com.smart.home.backend.input.PersonInput;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;

import com.smart.home.backend.service.util.IdUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
	
	@Setter
	private String name;
	@Setter
	@Builder.Default
	private List<Light> lights = new ArrayList<>();
	@Setter
	@Builder.Default
	private List<Window> windows = new ArrayList<>();
	@Setter
	@Builder.Default
	private List<Door> doors = new ArrayList<>();
	@Setter
	@Builder.Default
	private List<Person> persons = new ArrayList<>();
	
	private final IdUtil lightId = new IdUtil();
	private final IdUtil doorId = new IdUtil();
	private final IdUtil windowId = new IdUtil();
	private final IdUtil personId = new IdUtil();
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
	
	@Nullable
	public Person findPerson(int id) {
		return this.getPersons()
				.stream()
				.filter(person -> person.getId() == id)
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
	 * Adds a light to the light list
	 * @param lightInput light input
	 */
	public void addLight(LightInput lightInput) {
		this.getLights().add(
				Light.builder()
						.state(lightInput.getState())
						.id(this.getLightId().newId())
						.build()
		);
	}
	
	/**
	 * Adds a person to the person list
	 * @param personInput person input
	 */
	public void addPerson(PersonInput personInput) {
		this.getPersons().add(
				Person.builder()
						.id(this.getPersonId().newId())
						.name(personInput.getName())
						.build()
		);
	}
}
