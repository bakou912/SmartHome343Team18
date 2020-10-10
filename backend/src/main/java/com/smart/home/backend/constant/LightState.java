package com.smart.home.backend.constant;

import lombok.Getter;

/**
 * State enum for lights.
 */
@Getter
public enum LightState {
	
	ON("ON"),
	OFF("OFF");
	
	private final String text;
	
	LightState(String text) {
		this.text = text;
	}
	
}
