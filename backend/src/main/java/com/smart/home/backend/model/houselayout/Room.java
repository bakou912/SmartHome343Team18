package com.smart.home.backend.model.houselayout;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Class for a room.
 */
@Getter
@Setter
@Builder
public class Room {
	
	private String name;
	private List<Light> lights;
	private List<Window> windows;
	private List<Door> doors;

}
