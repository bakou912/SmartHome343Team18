package com.smart.home.backend.controller;

import com.smart.home.backend.constant.SimulationState;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.input.*;
import com.smart.home.backend.model.houselayout.Person;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.model.simulationcontext.SimulationContextModel;
import com.smart.home.backend.model.simulationparameters.SystemParameters;
import com.smart.home.backend.model.simulationparameters.User;
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
@RestController
public class SimulationContextController {
	
	private SimulationContextModel simulationContextModel;
	private HouseLayoutController houseLayoutController;

	@Autowired
	public SimulationContextController(SimulationContextModel simulationContextModel, HouseLayoutController houseLayoutController) {
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
	 * Toggling simulation state.
	 * @return New simulation state
	 */
	@PutMapping("/context/state")
	public ResponseEntity<SimulationState> toggleState() {
		return new ResponseEntity<>(this.getSimulationContextModel().toggleState(), HttpStatus.OK);
	}
	
	/**
	 * Modifying user.
	 * @param userInput User input
	 * @return Updated user. Null if not found
	 */
	@PutMapping("/context/parameters/user")
	public ResponseEntity<User> modifyUser(@RequestBody UserInput userInput) {
		User user = this.getSimulationContextModel().getSimulationParametersModel().getUser();
		
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (userInput.getLocation() != null) {
			user.setLocation(userInput.getLocation());
		}
		if (userInput.getLocation() != null) {
			user.setName(userInput.getName());
		}
		if (userInput.getProfile() != null) {
			user.setProfile(userInput.getProfile());
		}
		
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	/**
	 * Modifying system parameters.
	 * @param parametersInput Parameters input
	 * @return Updated system parameters. Null if not found
	 */
	@PutMapping("/context/parameters/sysparams")
	public ResponseEntity<SystemParameters> modifyParams(@RequestBody ParametersInput parametersInput) {
		SystemParameters params = this.getSimulationContextModel().getSimulationParametersModel().getSysParams();
		
		if (params == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (parametersInput.getInsideTemp() != null) {
			params.setInsideTemp(parametersInput.getInsideTemp());
		}
		if (parametersInput.getOutsideTemp() != null) {
			params.setOutsideTemp(parametersInput.getOutsideTemp());
		}
		if (parametersInput.getDate() != null) {
			params.setDate(parametersInput.getDate());
		}
		
		return new ResponseEntity<>(params, HttpStatus.OK);
	}
	
	/**
	 * Resetting the simulation context model.
	 * @return Response status code
	 */
	@DeleteMapping("/context")
	public ResponseEntity<SimulationContextModel> resetContext() {
		this.getSimulationContextModel().reset();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * Adding a person to a room.
	 * @param rowId  row number in house layout
	 * @param roomId room number of row
	 * @return Person's id
	 */
	@PostMapping("context/layout/rows/{rowId}/rooms/{roomId}/persons")
	public ResponseEntity<Integer> addPersonToRoom(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@RequestBody PersonInput personInput
	) {
		Room targetRoom = this.getSimulationContextModel().getHouseLayoutModel().findRoom(rowId, roomId);
		
		if(targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		this.houseLayoutController.getHouseLayoutModel().updateDetectedPerson(true); //for now this should be fine. We can redo addPerson another time.

		return new ResponseEntity<>(targetRoom.addPerson(personInput), HttpStatus.OK);
	}
	
	/**
	 * Adding a person outside the house.
	 * @param personInput Person input
	 * @return Person's id
	 */
	@PostMapping("context/layout/outside/persons")
	public ResponseEntity<Integer> addPersonOutside(@RequestBody PersonInput personInput) {
		return new ResponseEntity<>(this.getSimulationContextModel().getHouseLayoutModel().getOutside().addPerson(personInput), HttpStatus.OK);
	}
	
	/**
	 * Modifying a person.
	 * @param rowId row number in house layout
	 * @param roomId room number in row
	 * @param personInput person input
	 * @return Updated person. returns null if the room, row or person does not exist
	 */
	@PutMapping("context/layout/rows/{rowId}/rooms/{roomId}/persons/{oldName}")
	public ResponseEntity<Person> modifyPerson(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "oldName") String oldName,
			@RequestBody PersonInput personInput
	) {
		Room targetRoom = this.getSimulationContextModel().getHouseLayoutModel().findRoom(rowId, roomId);
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Person foundPerson = targetRoom.getPersons()
				.stream()
				.filter(person -> person.getName().equals(oldName))
				.findFirst()
				.orElse(null);
		
		if (foundPerson == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		foundPerson.setName(personInput.getName());
		
		return new ResponseEntity<>(foundPerson, HttpStatus.OK);
	}
	
	/**
	 * Removing a person from a room.
	 * @param rowId row number in house layout
	 * @param roomId room number in row
	 * @param personId person id
	 * @return updated house layout. returns null if the room, row or person does not exist
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
    public ResponseEntity<Window> blockWindow(
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
	public ResponseEntity<Window> unBlockWindow(
			@PathVariable(value = "rowId") int rowId,
			@PathVariable(value = "roomId") int roomId,
			@PathVariable(value = "windowId") int windowId
	) {
		WindowInput unblockedWindowInput = new WindowInput();
		unblockedWindowInput.setState(WindowState.CLOSED);
		
		return this.getChangeWindowStateResponse(rowId, roomId, windowId, unblockedWindowInput);
	}
	
	/**
	 * Calling the window state changing method.
	 * @param rowId row number
	 * @param roomId room number
	 * @param windowId id of window
	 * @return Updated simulation context. returns null if window, room, or row does not exist.
	 */
	private ResponseEntity<Window> getChangeWindowStateResponse(int rowId, int roomId, int windowId, WindowInput windowInput) {
		ResponseEntity<Window> layoutResponse = this.getHouseLayoutController().changeWindowState(rowId, roomId, windowId, windowInput);
		
		if (!layoutResponse.getStatusCode().equals(HttpStatus.OK)) {
			return new ResponseEntity<>(layoutResponse.getStatusCode());
		}
		
		return new ResponseEntity<>(layoutResponse.getBody(), HttpStatus.OK);
	}
	

	
}
