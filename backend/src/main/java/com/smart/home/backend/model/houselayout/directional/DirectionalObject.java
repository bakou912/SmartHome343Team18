package com.smart.home.backend.model.houselayout.directional;

import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.model.ModelObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class for a room's door.
 */
@Getter
@Setter
@SuperBuilder
public class DirectionalObject extends ModelObject {
	
	@NonNull
	protected Direction direction;
	
}
