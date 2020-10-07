package com.smart.home.backend.input;

import lombok.Getter;
import lombok.Setter;import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Getter
@Setter
public class HouseLayoutRow {
	
	@JsonProperty("rooms")
	private List<HouseLayoutRoom> rooms = null;
	
}
