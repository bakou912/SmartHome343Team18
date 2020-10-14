package com.smart.home.backend.controller;


import com.smart.home.backend.constant.LightState;
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
import com.smart.home.backend.service.mapper.DoorsMapper;
import com.smart.home.backend.service.mapper.LightsMapper;
import com.smart.home.backend.service.mapper.RoomsMapper;
import com.smart.home.backend.service.mapper.WindowsMapper;
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
@CrossOrigin
@RestController
public class HouseLayoutController {
	
	private HouseLayoutModel houseLayoutModel;
	
	@Autowired
	HouseLayoutController(HouseLayoutModel houseLayoutModel) {
		this.houseLayoutModel = houseLayoutModel;
	}
	
	/**
	 * Creates a house layout model.
	 * 
	 * @param houseLayoutInput Input to construct a house layout
	 * @return Created house layout model
	 */
	@PostMapping("/layout")
	public ResponseEntity<HouseLayoutModel> loadLayout(@RequestBody HouseLayoutInput houseLayoutInput) {
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
	 * 
	 * Add new light to a room
	 * 
	 * @param rowId row number in houselayout
	 * @param roomId room number of row
	 * @return update house layout with new light in room. returns null if the room or row cannot be found.
	 */
	@PostMapping("layout/rows/{rowId}/rooms/{roomId}/lights")
	public ResponseEntity<HouseLayoutModel> addLight(@PathVariable int rowId, @PathVariable int roomId) {
		Room room = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (room == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<Light> lights = room.getLights();
		
		boolean validRemoval = lights.add(
				Light.builder()
						.id(lights.size())
						.state(LightState.OFF)
						.build()
		);

		if(validRemoval == false){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Delete a light in a room
	 * 
	 * @param rowId row number in houselayout
	 * @param roomId room number in row
	 * @param lightId id of light
	 * @return updated house layout. returns null if the room, row or light does not exist
	 */
	@DeleteMapping("layout/rows/{rowId}/rooms/{roomId}/lights/{lightId}")
	public ResponseEntity<HouseLayoutModel> removeLight(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "lightId") int lightId
	) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		boolean validRemoval = targetRoom.getLights().removeIf(light -> light.getId() == lightId);

		if(validRemoval == false){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Change the state of a light
	 * 
	 * @param rowId row number
	 * @param roomId room number
	 * @param lightId light id
	 * @return updated house layout with updated state of light. returns null if the room does not exist
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
		
		light.setState(lightInput.getState());

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Add new rooms to the house layout
	 * 
	 * @param rowId row number
	 * @param newRoomInputs new rooms to add
	 * @return updated house layout. returns null if the room or row does not exist
	 */
	@PostMapping("layout/rows/{rowId}/rooms")
	public ResponseEntity<HouseLayoutModel> addRooms(@PathVariable(value = "rowId") int rowId, @RequestBody RoomRowInput newRoomInputs) {

		RoomRow targetRow = houseLayoutModel.findRow(rowId);
		
		if (targetRow == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<Room> rooms = targetRow.getRooms();
		
		for(RoomInput roomInput : newRoomInputs.getRooms()) {
			rooms.add(
				Room.builder()
						.id(rooms.size())
						.name(roomInput.getName())
						.doors(DoorsMapper.map(roomInput.getDoorsOn()))
						.windows(WindowsMapper.map(roomInput.getWindowsOn()))
						.lights(LightsMapper.map(roomInput.getLights()))
						.build()
			);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Delete room from house layout
	 * 
	 * @param rowId row number
	 * @param roomId room number
	 * @return updated house layout. returns null if room or row does not exist
	 */
	@DeleteMapping("layout/rows/{rowId}/rooms/{roomId}")
	public ResponseEntity<HouseLayoutModel> removeRoom(@PathVariable(value = "rowId") int rowId, @PathVariable(value = "roomId") int roomId) {
		
		RoomRow targetRow = this.getHouseLayoutModel().findRow(rowId);
		
		if (targetRow == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		boolean response  = targetRow.getRooms().removeIf(room -> room.getId() == roomId);

		if(response == false){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * add a door within the given room
	 * 
	 * @param rowId row number
	 * @param roomId room number
	 * @param newDoor new door to be added
	 * @return updated houselayout where a door was added to new room. returns null if no available space in room or if the room, row does not exist.
	 */
	@PostMapping("/layout/rows/{rowId}/rooms/{roomId}/door")
	public ResponseEntity<HouseLayoutModel> addDoor(@PathVariable(value = "rowId") int rowId , @PathVariable(value="roomId") int roomId, @RequestBody DoorInput newDoor ){
		Room room = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (room == null || !this.getHouseLayoutModel().isDirectionAvailable(room, newDoor.getDirection())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<Door> doors = room.getDoors();
		
		doors.add(
			Door.builder()
				.id(doors.size())
				.direction(newDoor.getDirection())
				.state(newDoor.getState())
				.build()
		);

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
	/**
	 * remove a door within the given room
	 * 
	 * @param rowId row number
	 * @param roomId room number
	 * @return updated houseLayout. returns null if the door, room, row does not exist
	 */
	@DeleteMapping("/layout/rows/{rowId}/rooms/{roomId}/door/{doorId}")
	public ResponseEntity<HouseLayoutModel> removeDoor(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "doorId") int doorId
	){
		Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		boolean response  = targetRoom.getDoors().removeIf(door -> door.getId() == doorId);
		
		if(response == false){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Change door state.
	 * 
	 * @param rowId row number
	 * @param roomId room number
	 * @param doorInput door input to change state
	 * @return updated houseLayout. returns null if door, room, row does not exist/
	 */
	@PutMapping("/layout/rows/{rowId}/rooms/{roomId}/door/{doorId}")
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
		
		targetDoor.setDirection(doorInput.getDirection());
		targetDoor.setState(doorInput.getState());

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 *  Add new Window to room.
	 * @param rowId row number
	 * @param roomId room number
	 * @param newWindow new window to be added
	 * @return update houseLayout. Returns null if no space for window or if room, row does not exist
	 */
	@PostMapping("/layout/rows/{rowId}/rooms/{roomId}/window")
	public ResponseEntity<HouseLayoutModel> addWindow(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@RequestBody WindowInput newWindow
	) {
		Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<Window> windows = targetRoom.getWindows();
		
		windows.add(
			Window.builder()
				.id(windows.size())
				.direction(newWindow.getDirection())
				.state(newWindow.getState())
				.build()
		);
		
		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
	/**
	 * remove Window in a room
	 * 
	 * @param rowId row number
	 * @param roomId room number
	 * @param windowId id of window to be removed
	 * @return updated houseLayout. returns null if  window, room, or row does not exist.
	 */
	@DeleteMapping("/layout/rows/{rowId}/rooms/{roomId}/windows/{windowId}")
	public ResponseEntity<HouseLayoutModel> removeWindow(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "windowId") int windowId
	) {
		Room targetRoom = houseLayoutModel.findRoom(rowId, roomId);
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		boolean validRemoval = targetRoom.getWindows().removeIf(window -> window.getId() == windowId);

		if(validRemoval == false){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 *  changes state of window
	 * 
	 * @param rowId row number
	 * @param roomId room number
	 * @param windowId id of window
	 * @param windowInput window input
	 * @return updated houseLayout. returns null if  window, room, or row does not exist.
	 */
	@PutMapping("/layout/rows/{rowId}/rooms/{roomId}/windows/{windowId}")
	public ResponseEntity<HouseLayoutModel> changeWindowState(@PathVariable(value = "rowId") int rowId, @PathVariable(value = "roomId") int roomId, @PathVariable(value = "windowId") int windowId, @RequestBody WindowInput windowInput){
		Window targetWindow = this.getHouseLayoutModel().findWindow(rowId, roomId, windowId);
		
		if (targetWindow == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		targetWindow.setDirection(windowInput.getDirection());
		targetWindow.setState(windowInput.getState());
		
		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
	
}
