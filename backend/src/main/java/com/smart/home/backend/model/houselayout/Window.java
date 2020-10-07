package com.smart.home.backend.model.houselayout;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.WindowState;

/**
 * Class for a room's window.
 */
@Getter
@Setter
@Builder
public class Window {
	
	private Direction direction;
	private WindowState state;
	
}
