package com.smart.home.backend.controller;

import com.smart.home.backend.input.*;
import com.smart.home.backend.model.houselayout.*;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.model.simulationparameters.location.RoomItemLocationPosition;
import com.smart.home.backend.model.simulationparameters.module.command.shc.DoorManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shc.LightManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shc.WindowManagementCommand;
import com.smart.home.backend.service.mapper.RoomsMapper;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	
	@Autowired
	public HouseLayoutController(HouseLayoutModel houseLayoutModel) {
		this.houseLayoutModel = houseLayoutModel;
	}
	
	/**
	 * Creating a house layout model.
	 * @param houseLayoutInput Input to construct a house layout
	 * @return Created house layout model
	 */
	@PostMapping("/layout")
	public ResponseEntity<HouseLayoutModel> loadLayout(@RequestBody HouseLayoutInput houseLayoutInput) {
		List<RoomRowInput> roomRowInputs = houseLayoutInput.getRows();
		List<RoomRow> roomRows = new ArrayList<>();
		
		for (int i = 0; i < roomRowInputs.size(); i++) {
			RoomRowInput roomRowInput = roomRowInputs.get(i);
			RoomRow roomRow = RoomRow.builder()
					.id(i)
					.rooms(RoomsMapper.map(roomRowInput.getRooms()))
					.build();
			
			roomRow.getRoomId().setLastId(roomRow.getRooms().size());
			
			roomRows.add(roomRow);
		}
		
		this.getHouseLayoutModel().setRows(roomRows);
		
		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
	/**
	 * Fetching the actual house layout model.
	 * @return Existing house layout model
	 */
	@GetMapping("/layout")
	public ResponseEntity<HouseLayoutModel> getLayout() {
		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
	/**
	 * Resetting the house layout model.
	 * @return Response status code
	 */
	@DeleteMapping("/layout")
	public ResponseEntity<HouseLayoutModel> resetLayout() {
		this.getHouseLayoutModel().reset();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Adding a room to a row.
	 * @param rowId row number
	 * @param roomInput new room to add
	 * @return Updated house layout. returns null if the room or row does not exist
	 */
	@PostMapping("layout/rows/{rowId}/rooms")
	public ResponseEntity<HouseLayoutModel> addRoom(@PathVariable(value = "rowId") int rowId, @RequestBody RoomInput roomInput) {

		RoomRow targetRow = houseLayoutModel.findRow(rowId);
		
		if (targetRow == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		targetRow.addRoom(roomInput);

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Deleting a room from a row.
	 * @param rowId row number
	 * @param roomId room number
	 * @return Updated house layout. returns null if room or row does not exist
	 */
	@DeleteMapping("layout/rows/{rowId}/rooms/{roomId}")
	public ResponseEntity<HouseLayoutModel> removeRoom(@PathVariable(value = "rowId") int rowId, @PathVariable(value = "roomId") int roomId) {
		RoomRow targetRow = this.getHouseLayoutModel().findRow(rowId);

		if (targetRow == null || !targetRow.getRooms().removeIf(room -> room.getId() == roomId)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Adding a door in a room.
	 * @param locationPosition room's location
	 * @param doorInput new door to be added
	 * @return Updated house layout where a door was added to new room. returns null if no available space in room or if the room, row does not exist.
	 */
	@PostMapping("/layout/rows/{rowId}/rooms/{roomId}/doors")
	public ResponseEntity<HouseLayoutModel> addDoor(LocationPosition locationPosition, @RequestBody DoorInput doorInput) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(locationPosition);
		
		if (targetRoom == null || !this.getHouseLayoutModel().isDirectionAvailable(targetRoom, doorInput.getDirection())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		targetRoom.addDoor(doorInput);

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
	/**
	 * Removing a door from a room.
	 * @param location door's location
	 * @return Updated houseLayout. returns null if the door, room, row does not exist
	 */
	@DeleteMapping("/layout/rows/{rowId}/rooms/{roomId}/doors/{itemId}")
	public ResponseEntity<HouseLayoutModel> removeDoor(RoomItemLocationPosition location) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(location);

		if (targetRoom == null || !targetRoom.getDoors().removeIf(door -> door.getId().equals(location.getItemId()))) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Changing a door's state.
	 * @param location door's location
	 * @return Updated houseLayout. returns null if door, room, row does not exist
	 */
	@PutMapping("/layout/rows/{rowId}/rooms/{roomId}/doors/{itemId}")
	public ResponseEntity<Door> changeDoorState(RoomItemLocationPosition location, @RequestBody DoorInput doorInput) {
		doorInput.setLocation(location);
		return new DoorManagementCommand().execute(this.getHouseLayoutModel(), doorInput);
	}

	/**
	 * Adding a window to a room.
	 * @param locationPosition room's location
	 * @param windowInput new window to be added
	 * @return Updated houseLayout. Returns null if no space for window or if room, row does not exist
	 */
	@PostMapping("/layout/rows/{rowId}/rooms/{roomId}/windows")
	public ResponseEntity<HouseLayoutModel> addWindow(LocationPosition locationPosition, @RequestBody WindowInput windowInput) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(locationPosition);
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		targetRoom.addWindow(windowInput);
		
		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
	/**
	 * Removing a window from a room.
	 * @param location window's location
	 * @return Updated houseLayout. returns null if  window, room, or row does not exist.
	 */
	@DeleteMapping("/layout/rows/{rowId}/rooms/{roomId}/windows/{itemId}")
	public ResponseEntity<HouseLayoutModel> removeWindow(RoomItemLocationPosition location) {
		Room targetRoom = houseLayoutModel.findRoom(location);

		if (targetRoom == null || !targetRoom.getWindows().removeIf(window -> window.getId().equals(location.getItemId()))) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Changing a window's state
	 * @param location window's location
	 * @param windowInput window input
	 * @return Updated window. returns null if  window, room, or row does not exist.
	 */
	@PutMapping("/layout/rows/{rowId}/rooms/{roomId}/windows/{itemId}")
	public ResponseEntity<Window> changeWindowState(RoomItemLocationPosition location, @RequestBody WindowInput windowInput) {
		windowInput.setLocation(location);
		return new WindowManagementCommand().execute(this.getHouseLayoutModel(), windowInput);
	}

	/**
	 * Changing a light's state in a room.
	 * @param location light's location
	 * @param lightInput light input
	 * @return updated light. returns null if light, room, or row does not exist.
	 */
	@PutMapping("/layout/rows/{rowId}/rooms/{roomId}/light")
	public ResponseEntity<Light> modifyRoomLight(RoomItemLocationPosition location, @RequestBody RoomLightInput lightInput){
		lightInput.setLocation(location);
		return new LightManagementCommand().execute(this.getHouseLayoutModel(), lightInput);
	}
	
	/**
	 * Changing a light's state outside.
	 * @return Updated light
	 */
	@PutMapping("layout/outside/light")
	public ResponseEntity<Light> modifyOutsideLight(@RequestBody OutsideLightInput lightInput) {
		return new LightManagementCommand().execute(this.getHouseLayoutModel(), lightInput);
	}
	
}
