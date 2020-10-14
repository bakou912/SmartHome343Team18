package com.smart.home.backend.model.houselayout.directional;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import com.smart.home.backend.constant.DoorState;
import lombok.experimental.SuperBuilder;

/**
 * Class for a room's door.
 */
@Getter
@Setter
@SuperBuilder
public class Door extends DirectionalObject {
	
	@NonNull
	private DoorState state;
	
}
