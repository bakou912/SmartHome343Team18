package com.smart.home.backend.controller;

import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.input.PersonInput;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationcontext.SimulationContextModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Simulation Context Controller
 */
@Getter
@Setter
@CrossOrigin
@RestController
public class SimulationContextController {
	
	private SimulationContextModel simulationContextModel;
	private HouseLayoutController houseLayoutController;
	
	@Autowired
	SimulationContextController(SimulationContextModel simulationContextModel, HouseLayoutController houseLayoutController) {
		this.simulationContextModel = simulationContextModel;
		this.houseLayoutController = houseLayoutController;
	}
	
	/**
	 * Fetching the actual simulation context model.
	 * @return Existing simulation context model
	 */
	@GetMapping("/context")
	public ResponseEntity<SimulationContextModel> getContext() {
		return new ResponseEntity<>(this.getSimulationContextModel(), HttpStatus.OK);
	}
	
	/**
	 * Add a person to a room.
	 *
	 * @param rowId  row number in house layout
	 * @param roomId room number of row
	 * @return Updated simulation context
	 */
	@PostMapping("context/layout/rows/{rowId}/rooms/{roomId}/persons")
	public ResponseEntity<SimulationContextModel> addPerson(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@RequestBody PersonInput personInput
	) {
		Room targetRoom = this.getSimulationContextModel().getHouseLayoutModel().findRoom(rowId, roomId);
		
		if(targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		targetRoom.addPerson(personInput);
		
		return new ResponseEntity<>(this.getSimulationContextModel(), HttpStatus.OK);
	}
	
	/**
	 * Removing a light from a room.
	 * @param rowId row number in house layout
	 * @param roomId room number in row
	 * @param personId person id
	 * @return updated house layout. returns null if the room, row or light does not exist
	 */
	@DeleteMapping("context/layout/rows/{rowId}/rooms/{roomId}/persons/{personId}")
	public ResponseEntity<SimulationContextModel> removePerson(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "personId") int personId
	) {
		Room targetRoom = this.getSimulationContextModel().getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (targetRoom == null || !targetRoom.getPersons().removeIf(person -> person.getId() == personId)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(this.getSimulationContextModel(), HttpStatus.OK);
	}
        
    /**
     * Blocking a window.
     * @param rowId row number
     * @param roomId room number
     * @param windowId id of window
     * @return Updated simulation context. returns null if window, room, or row does not exist.
    */
    @PutMapping("context/layout/rows/{rowId}/rooms/{roomId}/windows/{windowId}/block")
    public ResponseEntity<SimulationContextModel> blockWindow(
    		@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "windowId") int windowId
	) {
    	WindowInput blockedWindowInput = new WindowInput();
		blockedWindowInput.setState(WindowState.BLOCKED);
	
		return this.getChangeWindowStateResponse(rowId, roomId, windowId, blockedWindowInput);
	}
	
	/**
	 * Unlocking a window.
	 * @param rowId row number
	 * @param roomId room number
	 * @param windowId id of window
	 * @return Updated simulation context. returns null if window, room, or row does not exist.
	 */
	@PutMapping("context/layout/rows/{rowId}/rooms/{roomId}/windows/{windowId}/unblock")
	public ResponseEntity<SimulationContextModel> unBlockWindow(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "windowId") int windowId
	) {
		WindowInput unblockedWindowInput = new WindowInput();
		unblockedWindowInput.setState(WindowState.CLOSED);
		
		return this.getChangeWindowStateResponse(rowId, roomId, windowId, unblockedWindowInput);
	}
	
	private ResponseEntity<SimulationContextModel> getChangeWindowStateResponse(int rowId, int roomId, int windowId, WindowInput windowInput) {
		ResponseEntity<HouseLayoutModel> layoutResponse = this.getHouseLayoutController().changeWindowState(rowId, roomId, windowId, windowInput);
		
		if (!layoutResponse.getStatusCode().equals(HttpStatus.OK)) {
			return new ResponseEntity<>(layoutResponse.getStatusCode());
		}
		
		return new ResponseEntity<>(this.getSimulationContextModel(), HttpStatus.OK);
	}
	
}
