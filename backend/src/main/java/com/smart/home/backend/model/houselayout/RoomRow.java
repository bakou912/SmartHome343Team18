package com.smart.home.backend.model.houselayout;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.smart.home.backend.model.ModelObject;
import lombok.experimental.SuperBuilder;

/**
 * Class for a row of rooms.
 */
@Getter
@Setter
@SuperBuilder
public class RoomRow extends ModelObject {
	
	private List<Room> rooms;
	
}
