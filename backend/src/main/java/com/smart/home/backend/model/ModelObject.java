package com.smart.home.backend.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ModelObject {
	
	@NonNull
	protected Integer id;
	
}
