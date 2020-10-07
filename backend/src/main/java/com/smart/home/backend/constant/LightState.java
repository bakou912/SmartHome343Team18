package com.smart.home.backend.constant;

/**
 * State enum for lights.
 */
public enum LightState {
	
	ON("ON"),
	OFF("OFF");
	
	private final String text;
	
	LightState(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
}
