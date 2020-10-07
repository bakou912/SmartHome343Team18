package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.util.List;

/**
 * Class for a row of rooms.
 */
@Getter
@Setter
@Builder
public class RoomRow {
	
	private List<Room> rooms;
	
}
