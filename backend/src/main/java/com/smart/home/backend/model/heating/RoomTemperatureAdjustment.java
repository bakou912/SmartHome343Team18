package com.smart.home.backend.model.heating;

import com.smart.home.backend.constant.RoomHeatingMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RoomTemperatureAdjustment {
	
	private final LocalDateTime date;
	private final RoomHeatingMode roomHeatingMode;
	private final double defaultTemperature;
	private final double outsideTemp;
	private final boolean systemOn;
	
}
