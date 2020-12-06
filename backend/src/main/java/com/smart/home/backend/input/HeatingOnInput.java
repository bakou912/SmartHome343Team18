package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Input class for house layout loading.
 */
@Getter
@Setter
public class HeatingOnInput {
	
	@JsonProperty("on")
	private Boolean on;
	
}
