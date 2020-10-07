package com.smart.home.backend.input;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class HouseLayoutRoom {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("windowsOn")
	private List<String> windowsOn = null;
	
	@JsonProperty("lights")
	private Integer lights;
	
	@JsonProperty("doorsOn")
	private List<String> doorsOn = null;
	
}
