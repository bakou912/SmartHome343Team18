package com.smart.home.backend.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

/**
 * Mode enum used for room heating.
 */
@Getter
public enum RoomHeatingMode {
	
	OVERRIDDEN("OVERRIDDEN"),
	ZONE("ZONE"),
	AWAY("AWAY");
	
	private final String text;
	
	RoomHeatingMode(String text) {
		this.text = text;
	}
	
	/**
	 * Retrieves the enum object paired to a value.
	 * @param directionValue direction value
	 * @return Retrieved enum object
	 */
	@JsonCreator
	public static RoomHeatingMode get(String directionValue) {
		return Arrays.stream(RoomHeatingMode.values())
				.filter(d -> d.getText().equals(directionValue))
				.findFirst()
				.orElse(null);
	}
	
	@Override
	public String toString() {
		return text;
	}
}
