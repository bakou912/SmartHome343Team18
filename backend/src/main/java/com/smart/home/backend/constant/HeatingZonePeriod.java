package com.smart.home.backend.constant;

import lombok.Getter;

/**
 * Period enum for heating zones.
 */
@Getter
public enum HeatingZonePeriod {
	
	MORNING("MORNING"),
	AFTERNOON("AFTERNOON"),
	NIGHT("NIGHT");
	
	private final String text;
	
	HeatingZonePeriod(String text) {
		this.text = text;
	}

}
