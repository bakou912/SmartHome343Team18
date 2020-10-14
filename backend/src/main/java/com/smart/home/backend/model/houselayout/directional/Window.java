package com.smart.home.backend.model.houselayout.directional;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import com.smart.home.backend.constant.WindowState;
import lombok.experimental.SuperBuilder;

/**
 * Class for a room's window.
 */
@Getter
@Setter
@SuperBuilder
public class Window extends DirectionalObject {
	
	@NonNull
	private WindowState state;
	
}
