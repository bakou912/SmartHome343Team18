package com.smart.home.backend.input;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class HouseLayoutInput {
	
	@JsonProperty("rows")
	public List<HouseLayoutRow> rows = null;
	
}
