package com.smart.home.backend.constant;

import lombok.Getter;

/**
 * State enum for windows.
 */
@Getter
public enum WindowState {
	
	OPEN("O"),
	CLOSED("C"),
	BLOCKED("B");
	
	private final String text;
	
	WindowState(String text) {
		this.text = text;
	}
	
}
