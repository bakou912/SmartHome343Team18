package com.smart.home.backend.controller;

import com.smart.home.backend.input.HouseLayoutInput;
import com.smart.home.backend.input.RoomRowInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.RoomRow;
import com.smart.home.backend.service.mapper.RoomsMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * House Layout Controller
 */
@Getter
@Setter
@CrossOrigin
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
		List<RoomRowInput> roomRowInputs = houseLayoutInput.rows;
		List<RoomRow> roomRows = new ArrayList<>();
		
		for (int i = 0; i < roomRowInputs.size(); i++) {
			RoomRowInput roomRowInput = roomRowInputs.get(i);
			
			roomRows.add(
					RoomRow.builder()
							.id(i)
							.rooms(RoomsMapper.map(roomRowInput.getRooms()))
							.build()
			);
		}
		
		this.setHouseLayoutModel(
				HouseLayoutModel.builder()
						.rows(roomRows)
						.build()
		);
		
		return this.getHouseLayoutModel();
	}
	
	/**
	 * Fetching the actual house layout model.
	 * @return Existing house layout model
	 */
	@GetMapping("/layout")
	public HouseLayoutModel getLayout() {
		return this.getHouseLayoutModel();
	}
	
}
