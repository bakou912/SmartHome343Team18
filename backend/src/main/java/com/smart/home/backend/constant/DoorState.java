package com.smart.home.backend.constant;

import lombok.Getter;

/**
 * State enum for doors.
 */
@Getter
public enum DoorState {
	
	LOCKED("L"),
	UNLOCKED("U");
	
	private final String text;
	
	DoorState(String text) {
		this.text = text;
	}

}
