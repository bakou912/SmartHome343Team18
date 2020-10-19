package com.smart.home.backend.controller;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.RoomInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationContext.SimulationContextModel;
import com.smart.home.backend.model.simulationParameters.Profile;
import com.smart.home.backend.model.simulationParameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationParameters.SystemParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.time.LocalDateTime;
import com.smart.home.backend.model.simulationContext.SimulationContextModel;

/**
 * Simulation Context Controller
 */
@Getter
@Setter
@CrossOrigin
@RestController
public class SimulationContextController {

    private SimulationContextModel simulationContextModel;
    
    private HouseLayoutModel houseLayoutModel;

    @Autowired
	SimulationContextController(HouseLayoutModel houseLayoutModel, SimulationContextModel simulationContextModel ) {
        this.houseLayoutModel = houseLayoutModel;
        this.simulationContextModel = simulationContextModel;
    }
    
    /**
	 *
	 * Add new person to a room.
	 * 
	 * @param rowId row number in house layout
	 * @param roomId room number of row
	 * @return update house layout with new light in room. returns null if the room or row cannot be found.
	 */
    @PostMapping("simulation/rows/{rowId}/rooms/{roomId}/addPerson")
	public ResponseEntity<SimulationContextModel> addPerson(
            @PathVariable int roomId,
            @PathVariable int rowId,
			@RequestBody RoomInput roomInput
	) {
        Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
        int personId = roomInput.getPerson();
		
		if (targetRoom == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
        
        targetRoom.addPerson();
		
        return new ResponseEntity<>(this.getSimulationContextModel(), HttpStatus.OK);
    }

// /**
// 	 * Delete a person in a room
// 	 * 
// 	 * @param rowId row number in house layout
// 	 * @param roomId room number in row
// 	 * @param PersonId id of light
// 	 * @return updated house layout. returns null if the room, row or person does not exist
// 	 */
// 	@DeleteMapping("simulation/rows/{rowId}/rooms/{roomId}/person/{personid}")
// 	public ResponseEntity<SimulationContextModel> removePerson(
//         @PathVariable int roomId,
//         @PathVariable int rowId,
//         @RequestBody RoomInput roomInput
// 	) {
// 		boolean badRequest = false;
// 		Room targetRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
		
// 		if (targetRoom == null) {
// 			badRequest = true;
// 		}
		
// 		badRequest = badRequest || !targetRoom.getLights().removeIf(light -> light.getId() == lightId);

// 		if (badRequest){
// 			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
// 		}

// 		return new ResponseEntity<SimulationContextModel>(this.getSimulationContextModel(), HttpStatus.OK);
// 	}

}
