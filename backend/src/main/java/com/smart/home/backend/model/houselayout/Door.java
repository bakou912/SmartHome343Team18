package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.Setter;
import com.smart.home.backend.model.ModelObject;
import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.DoorState;
import lombok.experimental.SuperBuilder;

/**
 * Class for a room's door.
 */
@Getter
@Setter
@SuperBuilder
public class Door extends ModelObject {
	
	private Direction direction;
	private DoorState state;
	
}
