package com.smart.home.backend.model.houselayout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import com.smart.home.backend.constant.LightState;

/**
 * Class for a room's light.
 */
@Getter
@Setter
@AllArgsConstructor
public class Light {
	
	@NonNull
	private LightState state;
	
	private Boolean autoMode;
	
	public Light() {
		this(LightState.OFF, false);
	}
	
}
