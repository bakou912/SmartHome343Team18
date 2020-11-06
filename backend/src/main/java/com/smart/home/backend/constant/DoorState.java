package com.smart.home.backend.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

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
