package com.smart.home.backend.model.houselayout;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.smart.home.backend.constant.LightState;

/**
 * Class for a room's light.
 */
@Getter
@Setter
@Builder
public class Light {
	
	private LightState state;
	
	
}
