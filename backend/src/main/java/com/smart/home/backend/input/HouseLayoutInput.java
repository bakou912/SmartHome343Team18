package com.smart.home.backend.input;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Input class for house layout loading.
 */
@Getter
@Setter
public class HouseLayoutInput {
	
	@JsonProperty("rows")
	public List<RoomRowInput> rows = null;
	
}
