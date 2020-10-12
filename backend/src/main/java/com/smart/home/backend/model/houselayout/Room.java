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

}
