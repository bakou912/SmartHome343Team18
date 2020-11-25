package com.smart.home.backend.model.heating;

import com.smart.home.backend.model.houselayout.Room;

import java.util.List;
import java.util.TimerTask;

/**
 * Timed task to alert authorities.
 */
public class TemperatureAdjustmentTask extends TimerTask {
	
	private final List<HeatingZone> zones;
	
	/**
	 * 1-parameter constructor.
	 * @param zones zones
	 */
	public TemperatureAdjustmentTask(List<HeatingZone> zones) {
		this.zones = zones;
	}
	
	@Override
	public void run() {
		for (HeatingZone zone: zones) {
			zone.adjustRoomTemperatures();
		}
	}
	
}
