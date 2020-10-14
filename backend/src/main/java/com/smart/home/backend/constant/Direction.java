package com.smart.home.backend.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import java.util.Arrays;

/**
 * Direction enum used for room items.
 */
@Getter
public enum Direction {
	
	NORTH("N"),
	EAST("E"),
	SOUTH("S"),
	WEST("W");
	
	private final String text;
	
	Direction(String text) {
		this.text = text;
	}
	
	/**
	 * Retrieves the enum object paired to a value.
	 * @param directionValue direction value
	 * @return Retrieved enum object
	 */
	@JsonCreator
	public static Direction get(String directionValue) {
		return Arrays.stream(Direction.values())
				.filter(d -> d.getText().equals(directionValue))
				.findFirst()
				.orElse(null);
	}
	
	@Override
	public String toString() {
		return text;
	}
}
