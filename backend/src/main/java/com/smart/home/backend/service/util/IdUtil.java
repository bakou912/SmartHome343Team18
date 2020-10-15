package com.smart.home.backend.service.util;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

@Getter
@Setter
@JsonIgnoreType
public class IdUtil {

	private Integer lastId;
	
	/**
	 * Default constructor
	 */
	public IdUtil() {
		this.lastId = 0;
	}
	
	public Integer newId() {
		return this.lastId++;
	}
}
