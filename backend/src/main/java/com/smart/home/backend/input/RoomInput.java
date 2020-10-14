package com.smart.home.backend.input;

import com.smart.home.backend.constant.Direction;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Input class for a house layout room.
 */
@Getter
@Setter
public class RoomInput {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("windowsOn")
	private List<Direction> windowsOn = null;
	
	@JsonProperty("lights")
	private Integer lights;
	
	@JsonProperty("doorsOn")
	private List<Direction> doorsOn = null;
	
}
