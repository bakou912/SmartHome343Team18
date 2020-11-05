package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.input.RoomInput;
import com.smart.home.backend.service.mapper.DoorsMapper;
import com.smart.home.backend.service.mapper.WindowsMapper;
import com.smart.home.backend.service.util.IdUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import java.util.List;
import com.smart.home.backend.model.ModelObject;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * Class for a row of rooms.
 */
@Getter
@Setter
@SuperBuilder
public class RoomRow extends ModelObject {
	
	@NonNull
	private List<Room> rooms;
	
	private final IdUtil roomId = new IdUtil();
	
	/**
	 * Finds a room with the corresponding id.
	 * @param roomId Searched room's id
	 * @return Found room
	 */
	@Nullable
	public Room findRoom(int roomId) {
		return this.getRooms()
				.stream()
				.filter(roomRow -> roomRow.getId() == roomId)
				.findFirst()
				.orElse(null);
	}
	
	public void addRoom(RoomInput roomInput) {
		this.getRooms().add(
				Room.builder()
						.name(roomInput.getName())
						.doors(DoorsMapper.map(roomInput.getDoorsOn()))
						.windows(WindowsMapper.map(roomInput.getWindowsOn()))
						.id(this.getRoomId().newId())
						.build()
		);
	}
	
}
