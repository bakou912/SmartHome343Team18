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
	
	/**
	 * Retrieves the enum object paired to a value.
	 * @param stateValue state value
	 * @return Retrieved enum object
	 */
	@JsonCreator
	public static DoorState get(String stateValue) {
		return Arrays.stream(DoorState.values())
				.filter(d -> d.getText().equals(stateValue))
				.findFirst()
				.orElse(null);
	}
	
	@Override
	public String toString() {
		return text;
	}

}
