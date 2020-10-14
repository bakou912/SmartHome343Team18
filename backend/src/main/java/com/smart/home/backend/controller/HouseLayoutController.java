package com.smart.home.backend.controller;


import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.input.HouseLayoutInput;
import com.smart.home.backend.input.LightInput;
import com.smart.home.backend.input.RoomInput;
import com.smart.home.backend.input.RoomRowInput;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.Door;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Light;
import com.smart.home.backend.model.houselayout.RoomRow;
import com.smart.home.backend.model.houselayout.Window;
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
	 * @param rowNb row number in houselayout
	 * @param roomNb room number of row
	 * @return update house layout with new light in room. returns null if the room or row cannot be found.
	 */
	@PostMapping("layout/rows/{rowNb}/rooms/{roomNb}/lights")
	public ResponseEntity<HouseLayoutModel> addLight(@PathVariable int rowNb, @PathVariable int roomNb) {

		if (this.getHouseLayoutModel() != null && this.getHouseLayoutModel().getRows().size() > 0) {
			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == rowNb) {

							int newId = room.getLights()
											.size();

							Light newLight = Light.builder()
													.id(newId)
													.state(LightState.OFF)
													.build();

							room.getLights()
								.add(newLight);
						}
					}
				}
			}
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Delete a light in a room
	 * 
	 * @param rowNb row number in houselayout
	 * @param roomNb room number in row
	 * @param lightId id of light
	 * @return updated house layout. returns null if the room, row or light does not exist
	 */
	@DeleteMapping("layout/rows/{rowNb}/rooms/{roomNb}/lights/{lightId}")
	public ResponseEntity<HouseLayoutModel>  removeLight(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable(value = "lightId") int lightId) {

		if (this.getHouseLayoutModel() != null && this.getHouseLayoutModel().getRows().size() > 0) {
			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == roomNb) {
							// found the target room. add new light

							room.getLights().remove(lightId);
						}
					}
				}
			}
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}


		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Change the state of a light
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @param lightId light id
	 * @return updated house layout with updated state of light. returns null if the room does not exist
	 */
	@PutMapping("layout/rows/{rowNb}/rooms/{roomNb}/lights/{lightId}")
	public ResponseEntity<HouseLayoutModel> setLayoutLight(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable int lightId,
			@RequestBody LightInput lighInput) {

		// find the light in the given roomRow and roomId.
		if (this.getHouseLayoutModel() != null && this.getHouseLayoutModel().getRows().size() > 0) {
			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == roomNb) {
							for (Light light : room.getLights()) {
								if (light.getId() == lightId) {

									room.getLights().get(lightId).setState(lighInput.getState());
								}
							}
						}
					}
				}
			}
		}else{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Add new rooms to the house layout
	 * 
	 * @param rowNb row number
	 * @param newRoomInputs new rooms to add
	 * @return updated house layout. returns null if the room or row does not exist
	 */
	@PostMapping("layout/rows/{rowNb}/rooms")
	public ResponseEntity<HouseLayoutModel> addRooms(@PathVariable(value = "rowNb") int rowNb, @RequestBody RoomRowInput newRoomInputs) {

		List<RoomInput> newRooms =  newRoomInputs.getRooms();
		RoomRow targetRow = this.getHouseLayoutModel().getRows().get(rowNb);

		for(RoomInput room : newRooms){
			int newId = targetRow.getRooms().size();
			targetRow.getRooms().add(
				Room.builder()
								.id(newId)
								.name(room.getName())
								.doors(DoorsMapper.map(room.getDoorsOn()))
								.windows(WindowsMapper.map(room.getWindowsOn()))
								.lights(LightsMapper.map(room.getLights()))
								.build()
			);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Delete room from house layout
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @return updated house layout. returns null if room or row does not exist
	 */
	@DeleteMapping("layout/rows/{rowNb}/rooms/{roomNb}")
	public ResponseEntity<HouseLayoutModel> removeRoom(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb) {

		if (this.getHouseLayoutModel() != null && this.getHouseLayoutModel().getRows().size() > 0 ) {

			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == roomNb) {
							// found the target room to add new light.
							this.getHouseLayoutModel()
								.getRows()
								.get(rowNb)
								.getRooms()
								.remove(roomNb);
						}
					}
				}
			}
		}else{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}


		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * add a door within the given room
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @param newDoor new door to be added
	 * @return updated houselayout where a door was added to new room. returns null if no available space in room or if the room, row does not exist.
	 */
	@PostMapping("/layout/rows/{rowNb}/rooms/{roomNb}/door")
	public ResponseEntity<HouseLayoutModel> addDoor(@PathVariable(value = "rowNb") int rowNb , @PathVariable(value="roomNb") int roomNb, @RequestBody DoorInput newDoor ){

		if (this.getHouseLayoutModel() != null || this.getHouseLayoutModel().getRows().size() == 0) {

			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == roomNb) {
							// found the target room, but need to make sure door states are available								

							boolean available = true;

							//check if no existing door or window occupies the state of new Door
							for(Door door : room.getDoors()){
								if(door.getDirection() == newDoor.getDirection()){
									available = false;
								}
							}
							

							for(Window window : room.getWindows()){
								if(window.getDirection() == newDoor.getDirection()){
									available = false;
								}
							}

							//if available, add the door
							if(available == true){
								int newId = room.getDoors().size();
								room.getDoors().add(
									Door.builder()
										.id(newId)
										.direction(newDoor.getDirection())
										.state(newDoor.getState())
										.build()
								);
							}
						

							
						}
					}
				}
			}
		}else{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}


	/**
	 * remove a door within the given room
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @return updated houseLayout. returns null if the door, room, row does not exist
	 */
	@DeleteMapping("/layout/rows/{rowNb}/rooms/{roomNb}/door/{doorId}")
	public ResponseEntity<HouseLayoutModel> removeDoor(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable(value = "doorId") int doorId){

		if (this.getHouseLayoutModel() != null && this.getHouseLayoutModel().getRows().size() > 0) {
			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == roomNb) {
							// found the target room, but need to make sure the door is there							
							
							if(room.getDoors().get(doorId) != null){
								room.getDoors().remove(doorId);
							}							
						}
					}
				}
			}
		}
		else{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);	
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 * Change door state.
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @param doorInput door input to change state
	 * @return updated houseLayout. returns null if door, room, row does not exist/
	 */
	@PutMapping("/layout/rows/{rowNb}/rooms/{roomNb}/door/{doorId}")
	public ResponseEntity<HouseLayoutModel> changeDoorState(@PathVariable("rowNb") int rowNb, @PathVariable("roomNb") int roomNb, @PathVariable("doorId") int doorId, @RequestBody DoorInput doorInput){
		
		if (this.getHouseLayoutModel() != null && this.getHouseLayoutModel().getRows().size() > 0) {
			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == roomNb) {
							// found the target room, but need to make sure door exists								

							if(room.getDoors().get(doorId) != null){

								room.getDoors().get(doorId).setDirection(doorInput.getDirection());
								room.getDoors().get(doorId).setState(doorInput.getState());
							}
							
						}
					}
				}
			}
		}
		else{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 *  Add new Window to room.
	 * @param rowNb row number
	 * @param roomNb room number
	 * @param newWindow new window to be added
	 * @return update houseLayout. Returns null if no space for window or if room, row does not exist
	 */
	@PostMapping("/layout/rows/{rowNb}/rooms/{roomNb}/window")
	public ResponseEntity<HouseLayoutModel> addWindow(@PathVariable(value = "rowNb") int rowNb,@PathVariable(value = "roomNb") int roomNb, @RequestBody WindowInput newWindow){
		
		if (this.getHouseLayoutModel() != null && this.getHouseLayoutModel().getRows().size() > 0 ) {
			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == roomNb) {
							// found the target room, but need to make sure window states are available								

							boolean available = true;
							
							//check if no existing door or window occupies the state of new window
							for(Door door : room.getDoors()){
								if(door.getDirection() == newWindow.getDirection()){
									available = false;
								}
							}
							
							for(Window window : room.getWindows()){
								if(window.getDirection() == newWindow.getDirection()){
									available = false;
								}
							}
							
							//if available, add the window
							if(available == true){
								int newId = room.getWindows().size();
								room.getWindows().add(
									Window.builder()
										.id(newId)
										.direction(newWindow.getDirection())
										.state(newWindow.getState())
										.build()
								);
							}
						}
					}
				}
			}	
		}
		else{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}


		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}


	/**
	 * remove Window in a room
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @param windowId id of window to be removed
	 * @return updated houseLayout. returns null if  window, room, or row does not exist.
	 */
	@DeleteMapping("/layout/rows/{rowNb}/rooms/{roomNb}/windows/{windowId}")
	public ResponseEntity<HouseLayoutModel> removeWindow(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable(value = "windowId") int windowId){

		if (this.getHouseLayoutModel() != null && this.getHouseLayoutModel().getRows().size() > 0) {

			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == roomNb) {
							// found the target room, but need to make sure the window is there							

							if(room.getWindows().get(windowId) != null){
								room.getWindows().remove(windowId);
							}							
						}
					}
				}
			}
			
		}else{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}

	/**
	 *  changes state of window
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @param windowId id of window
	 * @param windowInput window input
	 * @return updated houseLayout. returns null if  window, room, or row does not exist.
	 */
	@PutMapping("/layout/rows/{rowNb}/rooms/{roomNb}/windows/{windowId}")
	public ResponseEntity<HouseLayoutModel> changeWindowState(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable(value = "windowId") int windowId, @RequestBody WindowInput windowInput){
		boolean found = false;

		if (this.getHouseLayoutModel() != null && this.getHouseLayoutModel().getRows().size() > 0) {

			for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
				if (roomRow.getId() == rowNb) {
					for (Room room : roomRow.getRooms()) {
						if (room.getId() == roomNb) {
							// found the target room, but need to make sure window exists								

							if(room.getDoors().get(windowId) != null){
								found = true;

								room.getWindows().get(windowId).setDirection(windowInput.getDirection());
								room.getWindows().get(windowId).setState(windowInput.getState());
							}
							
						}
					}
				}
			}
			
		}else{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		if (found == false) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
	}
}
