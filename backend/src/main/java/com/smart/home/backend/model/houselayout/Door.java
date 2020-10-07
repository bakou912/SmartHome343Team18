package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.DoorState;

/**
 * Class for a room's door.
 */
@Getter
@Setter
@Builder
public class Door {
	
	private Direction direction;
	private DoorState state;
	
}
