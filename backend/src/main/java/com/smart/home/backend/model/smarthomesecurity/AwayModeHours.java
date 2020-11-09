package com.smart.home.backend.model.smarthomesecurity;

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
	 * @param from
	 * @param to
	 */
	public AwayModeHours(LocalTime from, LocalTime to) {
		this.from = from;
		this.to = to;
	}
}
