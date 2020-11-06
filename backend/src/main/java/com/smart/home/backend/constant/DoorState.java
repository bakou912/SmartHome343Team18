package com.smart.home.backend.constant;

import lombok.Getter;

/**
 * State enum for doors.
 */
@Getter
public enum DoorState {
	
	OPEN("O"),
	CLOSED("C"),
	LOCKED("L");
	
	private final String text;
	
	DoorState(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}

}
