package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import com.smart.home.backend.model.ModelObject;
import lombok.experimental.SuperBuilder;

/**
 * Class for a people in a room
 */
@Getter
@Setter
@SuperBuilder
public class Person extends ModelObject {
	
	@NonNull
	private String name;
	
}
