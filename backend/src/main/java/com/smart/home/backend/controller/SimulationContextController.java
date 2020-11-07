package com.smart.home.backend.controller;

import com.smart.home.backend.constant.SimulationState;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.input.*;
import com.smart.home.backend.model.houselayout.OutsideLocation;
import com.smart.home.backend.model.houselayout.Person;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.model.simulationcontext.SimulationContextModel;
import com.smart.home.backend.model.simulationparameters.SystemParameters;
import com.smart.home.backend.model.simulationparameters.User;
import com.smart.home.backend.model.simulationparameters.UserProfile;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.model.simulationparameters.location.PersonLocationPosition;
import com.smart.home.backend.model.simulationparameters.location.RoomItemLocationPosition;
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

	@Autowired
	public SimulationContextController(SimulationContextModel simulationContextModel) {
		this.simulationContextModel = simulationContextModel;
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
			user.setProfile(new UserProfile(userInput.getProfile(), userInput.getCommandPermissions()));
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
	 * @param locationPosition room's location
	 * @return Person's id
	 */
	@PostMapping("context/layout/rows/{rowId}/rooms/{roomId}/persons")
	public ResponseEntity<Integer> addPersonToRoom(LocationPosition locationPosition, @RequestBody PersonInput personInput) {
		Room targetRoom = this.getSimulationContextModel().getHouseLayoutModel().findRoom(locationPosition);
		
		if (targetRoom == null || personInput.getName() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (this.getSimulationContextModel().getHouseLayoutModel().isInHouse(personInput.getName())) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		this.simulationContextModel.getHouseLayoutModel().updateDetectedPerson(true); //for now this should be fine. We can redo addPerson another time.

		return new ResponseEntity<>(targetRoom.addPerson(personInput), HttpStatus.OK);
	}
	
	/**
	 * Adding a person outside the house.
	 * @param personInput Person input
	 * @return Person's id
	 */
	@PostMapping("context/layout/outside/persons")
	public ResponseEntity<Integer> addPersonOutside(@RequestBody OutsidePersonInput personInput) {
		if (personInput.getName() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (this.getSimulationContextModel().getHouseLayoutModel().isInHouse(personInput.getName())) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<>(this.getSimulationContextModel().getHouseLayoutModel().getOutsideLocation(personInput.getLocation()).addPerson(personInput), HttpStatus.OK);
	}
	
	/**
	 * Removing a person from outside.
	 * @param personId person's id
	 * @return updated house layout. returns null if the room, row or person does not exist
	 */
	@DeleteMapping("context/layout/outside/persons/{personId}")
	public ResponseEntity<SimulationContextModel> removePersonOutside(@PathVariable Integer personId) {
		
		if (!this.getSimulationContextModel().removePersonOutside(personId)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(this.getSimulationContextModel(), HttpStatus.OK);
	}
	
	/**
	 * Modifying a person.
	 * @param location person's location
	 * @param personInput person input
	 * @return Updated person. returns null if the room, row or person does not exist
	 */
	@PutMapping("context/layout/rows/{rowId}/rooms/{roomId}/persons/{name}")
	public ResponseEntity<Person> modifyPerson(PersonLocationPosition location, @RequestBody PersonInput personInput) {
		Room targetRoom = this.getSimulationContextModel().getHouseLayoutModel().findRoom(location);
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Person foundPerson = targetRoom.getPersons()
				.stream()
				.filter(person -> person.getName().equals(location.getName()))
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
	 * @param location person's location
	 * @return updated house layout. returns null if the room, row or person does not exist
	 */
	@DeleteMapping("context/layout/rows/{rowId}/rooms/{roomId}/persons/{itemId}")
	public ResponseEntity<SimulationContextModel> removePersonFromRoom(RoomItemLocationPosition location) {
		Room targetRoom = this.getSimulationContextModel().getHouseLayoutModel().findRoom(location);
		
		if (targetRoom == null || !targetRoom.getPersons().removeIf(person -> person.getId().equals(location.getItemId()))) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(this.getSimulationContextModel(), HttpStatus.OK);
	}
        
    /**
     * Blocking a window.
     * @param location window's location
     * @return Updated simulation context. returns null if window, room, or row does not exist.
    */
    @PutMapping("context/layout/rows/{rowId}/rooms/{roomId}/windows/{itemId}/block")
    public ResponseEntity<Window> blockWindow(RoomItemLocationPosition location) {
    	WindowInput blockedWindowInput = new WindowInput();
		blockedWindowInput.setState(WindowState.BLOCKED);
	
		return this.getChangeWindowStateResponse(location, blockedWindowInput);
	}
	
	/**
	 * Unlocking a window.
	 * @param location window's location
	 * @return Updated simulation context. returns null if window, room, or row does not exist.
	 */
	@PutMapping("context/layout/rows/{rowId}/rooms/{roomId}/windows/{itemId}/unblock")
	public ResponseEntity<Window> unBlockWindow(RoomItemLocationPosition location) {
		WindowInput unblockedWindowInput = new WindowInput();
		unblockedWindowInput.setState(WindowState.CLOSED);
		
		return this.getChangeWindowStateResponse(location, unblockedWindowInput);
	}

	/**
	 * Opens a window.
	 * @param location window's location
	 * @return Updated simulation context. returns null if window, room, or row does not exist.
	 */
	@PutMapping("context/layout/rows/{rowId}/rooms/{roomId}/windows/{itemId}/open")
	public ResponseEntity<Window> openWindow(RoomItemLocationPosition location) {
		WindowInput unblockedWindowInput = new WindowInput();
		unblockedWindowInput.setState(WindowState.OPEN);

		return this.getChangeWindowStateResponse(location, unblockedWindowInput);
	}
	
	/**
	 * Calling the window state changing method.
	 * @param location window's location
	 * @return Updated simulation context. returns null if window, room, or row does not exist.
	 */
	private ResponseEntity<Window> getChangeWindowStateResponse(RoomItemLocationPosition location, WindowInput windowInput) {
		windowInput.setLocation(location);
		Window modifyWindow = this.getSimulationContextModel().getHouseLayoutModel().modifyWindowState(windowInput);
		
		if (modifyWindow == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(modifyWindow, HttpStatus.OK);
	}
	
}
