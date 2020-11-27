package com.smart.home.backend.model.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Class for the hours of away mode.
 */
@Getter
@Setter
@NoArgsConstructor
public class AwayModeHours {
	
	private LocalTime from;
	private LocalTime to;
	
	/**
	 * 2-parameter constructor
	 * @param from beginning hour
	 * @param to ending hour
	 */
	public AwayModeHours(LocalTime from, LocalTime to) {
		this.from = from;
		this.to = to;
	}
	
}
