package com.smart.home.backend.constant;

import lombok.Getter;

/**
 * State enum for lights.
 */
@Getter
public enum SimulationState {
	
	ON("ON"),
	OFF("OFF");
	
	private final String text;
	
	SimulationState(String text) {
		this.text = text;
	}
	
}
