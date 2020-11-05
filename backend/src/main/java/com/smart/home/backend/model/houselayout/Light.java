package com.smart.home.backend.model.houselayout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import com.smart.home.backend.model.ModelObject;
import com.smart.home.backend.constant.LightState;
import lombok.experimental.SuperBuilder;

/**
 * Class for a room's light.
 */
@Getter
@Setter
@AllArgsConstructor
public class Light {
	
	public Light() {
		this(LightState.OFF);
	}
	
	@NonNull
	private LightState state;
	
}
