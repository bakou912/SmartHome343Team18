package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.List;

/**
 * Model for the house layout.
 */
@Getter
@Setter
@Builder
public class HouseLayoutModel {
	private List<RoomRow> rows;
}
