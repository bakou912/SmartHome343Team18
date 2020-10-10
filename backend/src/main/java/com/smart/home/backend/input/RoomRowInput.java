package com.smart.home.backend.input;

import lombok.Getter;
import lombok.Setter;import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Input class for a house layout room row.
 */
@Getter
@Setter
public class RoomRowInput {
	
	@JsonProperty("rooms")
	private List<RoomInput> rooms = null;
	
}
