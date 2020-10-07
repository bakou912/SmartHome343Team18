package com.smart.home.backend.constant;

/**
 * State enum for windows.
 */
public enum WindowState {
	
	OPEN("O"),
	CLOSED("C"),
	BLOCKED("B");
	
	private final String text;
	
	WindowState(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
}
