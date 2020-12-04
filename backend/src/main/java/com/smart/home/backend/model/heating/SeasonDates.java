package com.smart.home.backend.model.heating;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Class for the hours of away mode.
 */
@Getter
@Setter
@NoArgsConstructor
public class SeasonDates {
	
	private LocalDateTime summerStart = LocalDateTime.of(2020, 6,1, 0, 0);
	private LocalDateTime winterStart = LocalDateTime.of(2020, 11,1, 0, 0);
	
	/**
	 * 2-parameter constructor
	 * @param summerStart summerStart start date
	 * @param winterStart winterStart start date
	 */
	public SeasonDates(LocalDateTime summerStart, LocalDateTime winterStart) {
		this.summerStart = summerStart;
		this.winterStart = winterStart;
	}
	
}
