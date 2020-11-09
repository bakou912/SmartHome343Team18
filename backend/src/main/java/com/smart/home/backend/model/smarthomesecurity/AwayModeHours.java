package com.smart.home.backend.model.smarthomesecurity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class AwayModeHours {
	private Time from;
	private Time to;
	
	public AwayModeHours() {
		from = Time.valueOf("06:00:00");
		to = Time.valueOf("18:00:00");
	}
}
