package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.Setter;
import com.smart.home.backend.model.ModelObject;
import com.smart.home.backend.constant.LightState;
import lombok.experimental.SuperBuilder;

/**
 * Class for a room's light.
 */
@Getter
@Setter
@SuperBuilder
public class Light extends ModelObject {
	
	private LightState state;
	
}
