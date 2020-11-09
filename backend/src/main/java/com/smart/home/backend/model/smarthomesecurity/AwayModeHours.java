package com.smart.home.backend.model.smarthomesecurity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

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
