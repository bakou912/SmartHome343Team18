package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.Setter;
import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.model.ModelObject;
import lombok.experimental.SuperBuilder;

/**
 * Class for a room's window.
 */
@Getter
@Setter
@SuperBuilder
public class Window extends ModelObject {
	
	private Direction direction;
	private WindowState state;
	
}
