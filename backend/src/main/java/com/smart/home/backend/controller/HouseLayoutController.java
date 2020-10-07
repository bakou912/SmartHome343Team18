package com.smart.home.backend.controller;

import com.smart.home.backend.input.HouseLayoutInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.RoomRow;
import com.smart.home.backend.service.mapper.RoomsMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

/**
 * House Layout Controller
 */
@Getter
@Setter
@RestController
public class HouseLayoutController {
	
	private HouseLayoutModel houseLayoutModel;
	
	/**
	 * Creates a house layout model.
	 * @param houseLayoutInput Input to construct a house layout
	 * @return Created house layout model
	 */
	@PostMapping("/layout")
	public HouseLayoutModel loadLayout(@RequestBody HouseLayoutInput houseLayoutInput) {
		List<RoomRow> roomRows = new ArrayList<>();
		
		houseLayoutInput.rows.forEach(
				houseLayoutRow -> roomRows.add(
						RoomRow.builder()
								.rooms(RoomsMapper.map(houseLayoutRow.getRooms()))
								.build()
				)
		);
		
		this.setHouseLayoutModel(
				HouseLayoutModel.builder()
						.rows(roomRows)
						.build()
		);
		
		return this.getHouseLayoutModel();
	}
}
