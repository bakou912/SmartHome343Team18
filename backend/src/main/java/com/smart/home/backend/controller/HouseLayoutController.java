package com.smart.home.backend.controller;


import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.input.HouseLayoutInput;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	 * 
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
	

	/**
	 * 
	 * Add new light to a room
	 * 
	 * @param rowNb row number in houselayout
	 * @param roomNb room number of row
	 * @return update house layout with new light in room. returns null if the room or row cannot be found.
	 */
	@PostMapping("layout/rows/{rowNb}/rooms/{roomNb}/lights")
	public HouseLayoutModel addLight(@PathVariable int rowNb, @PathVariable int roomNb) {

		boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
				for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
					if (roomRow.getId() == rowNb) {
						for (Room room : roomRow.getRooms()) {
							if (room.getId() == rowNb) {
								// found the target room. now we can add new light.
								found = true;

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
			}

		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
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
	public HouseLayoutModel removeLight(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable(value = "lightId") int lightId) {

		boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
				for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
					if (roomRow.getId() == rowNb) {
						for (Room room : roomRow.getRooms()) {
							if (room.getId() == roomNb) {
								// found the target room. add new light
								found = true;

								room.getLights().remove(lightId);
							}
						}
					}
				}
			}

		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
	}

	/**
	 * change the location of a light to a different room
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @param lightId light id
	 * @param newRoom new room to be added
	 * @return updated house layout with moved light to a new room. returns null if the room does not exist
	 */
	@PutMapping("layout/rows/{rowNb}/rooms/{roomNb}/lights/{lightId}")
	public HouseLayoutModel setLayoutLight(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable int lightId,
			@RequestBody Map<String, Integer> newRoom) {

		boolean found = false;

		// find the light in the given roomRow and roomId.
		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
				for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
					if (roomRow.getId() == rowNb) {
						for (Room room : roomRow.getRooms()) {
							if (room.getId() == roomNb) {
								for (Light light : room.getLights()) {
									if (light.getId() == lightId) {
										// found the target light we want to move. remove existing light from room and to new room
										found = true;

										Light existingLight = room.getLights().remove(lightId);
										// add the light to the new row and room

										this.getHouseLayoutModel()
											.getRows()
											.get(newRoom.get("roomRow"))
											.getRooms()
											.get(newRoom.get("roomNb")).getLights().add(existingLight);

									}
								}
							}
						}
					}
				}
			}

		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
	}

	/**
	 * Add new rooms to the house layout
	 * 
	 * @param rowNb row number
	 * @param newRoomInputs new rooms to add
	 * @return updated house layout. returns null if the room or row does not exist
	 */
	@PostMapping("layout/rows/{rowNb}/rooms")
	public HouseLayoutModel addRooms(@PathVariable(value = "rowNb") int rowNb, @RequestBody RoomRowInput newRoomInputs) {

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

		return this.getHouseLayoutModel();
	}

	/**
	 * Delete room from house layout
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @return updated house layout. returns null if room or row does not exist
	 */
	@DeleteMapping("layout/rows/{rowNb}/rooms/{roomNb}")
	public HouseLayoutModel removeRoom(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb) {

		Boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
				for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
					if (roomRow.getId() == rowNb) {
						for (Room room : roomRow.getRooms()) {
							if (room.getId() == roomNb) {
								// found the target room to add new light.
								found = true;
								this.getHouseLayoutModel()
									.getRows()
									.get(rowNb)
									.getRooms()
									.remove(roomNb);
							}
						}
					}
				}
			}

		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
	}

	/**
	 * move room to a different row
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @return updated house layout with room moved to a new RoomRow. returns null if the room or row does not exist
	 */
	@PostMapping("layout/rows/{rowNb}/rooms/{roomNb}")
	public HouseLayoutModel moveRoom(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb,
			@RequestBody Map<String, Integer> newRoom) {

		boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
				for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
					if (roomRow.getId() == rowNb) {
						for (Room room : roomRow.getRooms()) {
							if (room.getId() == roomNb) {
								// found the target room and row we want to move to new room row
								found = true;
								Room movingRoom = this.getHouseLayoutModel()
														.getRows()
														.get(rowNb)
														.getRooms()
														.remove(roomNb);

								this.getHouseLayoutModel()
									.getRows()
									.get(rowNb)
									.getRooms()
									.add(movingRoom);
							}
						}
					}
				}
			}

		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
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
	public HouseLayoutModel addDoor(@PathVariable(value = "rowNb") int rowNb , @PathVariable(value="roomNb") int roomNb, @RequestBody DoorInput newDoor ){

		boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
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
									found = true;
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
			}
		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
	}


	/**
	 * remove a door within the given room
	 * 
	 * @param rowNb row number
	 * @param roomNb room number
	 * @param doorToRemove door that should be removed
	 * @return updated houseLayout. returns null if the door, room, row does not exist
	 */
	@DeleteMapping("/layout/rows/{rowNb}/rooms/{roomNb}/door/{doorId}")
	public HouseLayoutModel removeDoor(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable(value = "doorId") int doorId){

		boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
				for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
					if (roomRow.getId() == rowNb) {
						for (Room room : roomRow.getRooms()) {
							if (room.getId() == roomNb) {
								// found the target room, but need to make sure the door is there							
								
								if(room.getDoors().get(doorId) != null){
									found = true;
									room.getDoors().remove(doorId);
								}							
							}
						}
					}
				}
			}
		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
	}

	/**
	 * change door state.
	 * 
	 * @param rowNbm row number
	 * @param roomNb room number
	 * @param newState new state for door 
	 * @return updated houseLayout. returns null if door, room, row does not exist/
	 */
	@PutMapping("/layout/rows/{rowNb}/rooms/{roomNb}/door/{doorId}")
	public HouseLayoutModel changeDoorState(@PathVariable("rowNb") int rowNb, @PathVariable("roomNb") int roomNb, @PathVariable("doorId") int doorId, @RequestBody DoorInput doorInput){
		
		boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
				for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
					if (roomRow.getId() == rowNb) {
						for (Room room : roomRow.getRooms()) {
							if (room.getId() == roomNb) {
								// found the target room, but need to make sure door exists								

								if(room.getDoors().get(doorId) != null){
									found = true;

									room.getDoors().get(doorId).setDirection(doorInput.getDirection());
									room.getDoors().get(doorId).setState(doorInput.getState());
								}
								
							}
						}
					}
				}
			}
		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
	}

	/**
	 *  Add new Window to room.
	 * @param rowNb row number
	 * @param roomNb room number
	 * @param newWindow new window to be added
	 * @return update houseLayout. Returns null if no space for window or if room, row does not exist
	 */
	@PostMapping("/layout/rows/{rowNb}/rooms/{roomNb}/window")
	public HouseLayoutModel addWindow(@PathVariable(value = "rowNb") int rowNb,@PathVariable(value = "roomNb") int roomNb, @RequestBody WindowInput newWindow){
		
		boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
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
									found = true;
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
		}

		if (found == false) {
			return null;
		}


		return this.getHouseLayoutModel();
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
	public HouseLayoutModel removeWindow(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable(value = "windowId") int windowId){
		boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
				for (RoomRow roomRow : this.houseLayoutModel.getRows()) {
					if (roomRow.getId() == rowNb) {
						for (Room room : roomRow.getRooms()) {
							if (room.getId() == roomNb) {
								// found the target room, but need to make sure the window is there							

								if(room.getWindows().get(windowId) != null){
									found = true;
									room.getWindows().remove(windowId);
								}							
							}
						}
					}
				}
			}
		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
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
	public HouseLayoutModel changeWindowState(@PathVariable(value = "rowNb") int rowNb, @PathVariable(value = "roomNb") int roomNb, @PathVariable(value = "windowId") int windowId, @RequestBody WindowInput windowInput){
		boolean found = false;

		if (this.getHouseLayoutModel() == null) {
			return null;
		} else {

			if (this.houseLayoutModel.getRows().size() == 0) {
				return null;
			} else {
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
			}
		}

		if (found == false) {
			return null;
		}

		return this.getHouseLayoutModel();
	}
}
