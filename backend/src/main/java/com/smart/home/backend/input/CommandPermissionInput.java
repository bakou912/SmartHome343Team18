package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.LocationRestriction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandPermissionInput {
	
	@JsonProperty("name")
	String name;
	
	String profileName;
	
	@JsonProperty("locationRestriction")
	LocationRestriction locationRestriction;
	
}
