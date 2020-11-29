package com.smart.home.backend.model.heating;

import com.smart.home.backend.constant.HeatingZonePeriod;

import java.util.EnumMap;
import java.util.Map;

/**
 * Heating zone periods with their paired target temperature.
 */
public class HeatingZonePeriods {
	
	private final Map<HeatingZonePeriod, Double> periodMap;
	
	public HeatingZonePeriods() {
		this.periodMap = new EnumMap<>(HeatingZonePeriod.class);
		this.periodMap.put(HeatingZonePeriod.MORNING, 0.0);
		this.periodMap.put(HeatingZonePeriod.AFTERNOON, 0.0);
		this.periodMap.put(HeatingZonePeriod.NIGHT, 0.0);
	}
	
	/**
	 * Accessor for a period's target temperature
	 * @param period zone period
	 * @return Periods' target temperature
	 */
	public Double getTargetTemperature(HeatingZonePeriod period) {
		return this.periodMap.get(period);
	}
	
	/**
	 * Mutator for a period's target temperature
	 * @param period zone period
	 * @param targetTemperature new target temperature
	 */
	public void setTargetTemperature(HeatingZonePeriod period, double targetTemperature) {
		this.periodMap.put(period, targetTemperature);
	}
}
