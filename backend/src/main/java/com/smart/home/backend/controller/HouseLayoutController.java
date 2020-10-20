package com.smart.home.backend.controller;

import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.DoorState;
import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.input.HouseLayoutInput;
import com.smart.home.backend.input.LightInput;
import com.smart.home.backend.input.RoomInput;
import com.smart.home.backend.input.RoomRowInput;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Light;
import com.smart.home.backend.model.houselayout.RoomRow;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.service.mapper.RoomsMapper;
import com.smart.home.backend.model.houselayout.Room;
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
	 * Adding a light to a room.
	 * @param rowId row number in house layout
	 * @param roomId room number of row
	 * @param lightInput new light to add
	 * @return Updated house layout with new light in room. returns null if the room or row cannot be found.
	 */
	@PostMapping("layout/rows/{rowId}/rooms/{roomId}/lights")
	public ResponseEntity<HouseLayoutModel> addLight(
			@PathVariable int rowId,
			@PathVariable int roomId,
			@RequestBody LightInput lightInput
	) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		targetRoom.addLight(lightInput);
		
		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Removing a light from a room.
	 * @param rowId row number in house layout
	 * @param roomId room number in row
	 * @param lightId id of light
	 * @return Updated house layout. returns null if the room, row or light does not exist
	 */
	@DeleteMapping("layout/rows/{rowId}/rooms/{roomId}/lights/{lightId}")
	public ResponseEntity<HouseLayoutModel> removeLight(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "lightId") int lightId
	) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (targetRoom == null || !targetRoom.getLights().removeIf(light -> light.getId() == lightId)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Changing a light's state.
	 * @param rowId row number
	 * @param roomId room number
	 * @param lightId light id
	 * @return Updated house layout with updated state of light. returns null if the room does not exist
	 */
	@PutMapping("layout/rows/{rowId}/rooms/{roomId}/lights/{lightId}")
	public ResponseEntity<HouseLayoutModel> setLayoutLight(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable int lightId,
			@RequestBody LightInput lightInput
	) {
		Light light = houseLayoutModel.findLight(rowId, roomId, lightId);
		
		if (light == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		LightState lightState = lightInput.getState();
		
		if (lightState != null) {
			light.setState(lightState);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
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
	 * @param rowId row number
	 * @param roomId room number
	 * @param doorInput new door to be added
	 * @return Updated house layout where a door was added to new room. returns null if no available space in room or if the room, row does not exist.
	 */
	@PostMapping("/layout/rows/{rowId}/rooms/{roomId}/doors")
	public ResponseEntity<HouseLayoutModel> addDoor(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value="roomId") int roomId,
			@RequestBody DoorInput doorInput
	) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (targetRoom == null || !this.getHouseLayoutModel().isDirectionAvailable(targetRoom, doorInput.getDirection())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		targetRoom.addDoor(doorInput);

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
	/**
	 * Removing a door from a room.
	 * @param rowId row number
	 * @param roomId room number
	 * @return Updated houseLayout. returns null if the door, room, row does not exist
	 */
	@DeleteMapping("/layout/rows/{rowId}/rooms/{roomId}/doors/{doorId}")
	public ResponseEntity<HouseLayoutModel> removeDoor(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "doorId") int doorId
	) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);

		if (targetRoom == null || !targetRoom.getDoors().removeIf(door -> door.getId() == doorId)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Changing a door's state.
	 * @param rowId row number
	 * @param roomId room number
	 * @param doorInput door input to change state
	 * @return Updated houseLayout. returns null if door, room, row does not exist/
	 */
	@PutMapping("/layout/rows/{rowId}/rooms/{roomId}/doors/{doorId}")
	public ResponseEntity<HouseLayoutModel> changeDoorState(
			@PathVariable("rowId") int rowId,
			@PathVariable("roomId") int roomId,
			@PathVariable("doorId") int doorId,
			@RequestBody DoorInput doorInput
	) {
		Door targetDoor = this.getHouseLayoutModel().findDoor(rowId, roomId, doorId);
		
		if (targetDoor == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Direction direction = doorInput.getDirection();
		DoorState state = doorInput.getState();
		
		if (direction != null) {
			targetDoor.setDirection(direction);
		}
		
		if (state != null) {
			targetDoor.setState(state);
		}
		

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Adding a window to a room.
	 * @param rowId row number
	 * @param roomId room number
	 * @param windowInput new window to be added
	 * @return Updated houseLayout. Returns null if no space for window or if room, row does not exist
	 */
	@PostMapping("/layout/rows/{rowId}/rooms/{roomId}/windows")
	public ResponseEntity<HouseLayoutModel> addWindow(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@RequestBody WindowInput windowInput
	) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		targetRoom.addWindow(windowInput);
		
		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
	/**
	 * Removing a window from a room.
	 * @param rowId row number
	 * @param roomId room number
	 * @param windowId id of window to be removed
	 * @return Updated houseLayout. returns null if  window, room, or row does not exist.
	 */
	@DeleteMapping("/layout/rows/{rowId}/rooms/{roomId}/windows/{windowId}")
	public ResponseEntity<HouseLayoutModel> removeWindow(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "windowId") int windowId
	) {
		Room targetRoom = houseLayoutModel.findRoom(rowId, roomId);

		if (targetRoom == null || !targetRoom.getWindows().removeIf(window -> window.getId() == windowId)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Changing a window's state
	 * @param rowId row number
	 * @param roomId room number
	 * @param windowId id of window
	 * @param windowInput window input
	 * @return Updated houseLayout. returns null if  window, room, or row does not exist.
	 */
	@PutMapping("/layout/rows/{rowId}/rooms/{roomId}/windows/{windowId}")
	public ResponseEntity<HouseLayoutModel> changeWindowState(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "windowId") int windowId,
			@RequestBody WindowInput windowInput
	) {
		Window targetWindow = this.getHouseLayoutModel().findWindow(rowId, roomId, windowId);
		
		if (targetWindow == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Direction direction = windowInput.getDirection();
		WindowState state = windowInput.getState();
		
		if (direction != null) {
			targetWindow.setDirection(direction);
		}
		
		if (state != null) {
			targetWindow.setState(state);
		}
		
		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
}
