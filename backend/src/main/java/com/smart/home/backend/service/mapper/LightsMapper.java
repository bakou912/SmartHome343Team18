package com.smart.home.backend.service.mapper;

import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.model.houselayout.Light;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for a list of lights.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LightsMapper {
	
	/**
	 * Maps a number of lights to lights.
	 * @param nbLights Number of lights
	 * @return Mapped lights
	 */
	public static List<Light> map(Integer nbLights) {
		List<Light> lights = new ArrayList<>();
		
		for (int i = 0; i < nbLights; i++) {
			lights.add(
					Light.builder()
							.id(i)
							.state(LightState.OFF)
							.build()
			);
		}
		
		return lights;
	}
	
}
