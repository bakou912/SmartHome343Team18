package com.smart.home.backend.constant;

/**
 * State enum for doors.
 */
public enum DoorState {
	
	LOCKED("L"),
	UNLOCKED("U");
	
	private final String text;
	
	DoorState(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}

}
