package com.smart.home.backend.model.simulationparameters.module.command.shs;

import com.smart.home.backend.model.houselayout.OutsideLocation;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationcontext.SimulationContextModel;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.model.simulationparameters.location.PersonLocationPosition;
import com.smart.home.backend.model.simulationparameters.location.RoomItemLocationPosition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Person addition command.
 */
public class RemovePersonCommand extends SHSAbstractCommand<SimulationContextModel, LocationPosition, SimulationContextModel> {
    
    /**
     * Default Constructor
     */
    public RemovePersonCommand() {
        super("Person management", true);
    }

    @Override
    public ResponseEntity<SimulationContextModel> execute(SimulationContextModel simulationContextModel, LocationPosition locationPosition) {
        if (locationPosition instanceof PersonLocationPosition) {
            return this.removePersonOutside(simulationContextModel, (PersonLocationPosition) locationPosition);
        } else {
            return this.removePersonFromRoom(simulationContextModel, (RoomItemLocationPosition) locationPosition);
        }
    }
    
    /**
     * Removing a person from a room.
     * @param simulationContextModel simulation context model
     * @param locationPosition person's location
     * @return Modified simulation context
     */
    private ResponseEntity<SimulationContextModel> removePersonFromRoom(SimulationContextModel simulationContextModel, RoomItemLocationPosition locationPosition) {
        Room targetRoom = simulationContextModel.getHouseLayoutModel().findRoom(locationPosition);
        String personName;
    
        if (targetRoom == null || (personName = simulationContextModel.getHouseLayoutModel().removePerson(targetRoom, locationPosition.getItemId())) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        this.logPersonInLocation(personName, targetRoom.getName());
    
        return new ResponseEntity<>(simulationContextModel, HttpStatus.OK);
    }
    
    /**
     * Removing a person outside
     * @param simulationContextModel simulation context model
     * @param locationPosition person's location
     * @return Modified simulation context
     */
    private ResponseEntity<SimulationContextModel> removePersonOutside(SimulationContextModel simulationContextModel, PersonLocationPosition locationPosition) {
        String personName;
        OutsideLocation outsideLocation = simulationContextModel.getHouseLayoutModel().getOutsideLocation(locationPosition.getName());
    
        if ((personName = simulationContextModel.getHouseLayoutModel().removePerson(outsideLocation, locationPosition.getPersonId())) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        this.logPersonInLocation(personName, locationPosition.getName());
        
        return new ResponseEntity<>(simulationContextModel, HttpStatus.OK);
    }
    
    /**
     * Logging the addition of a person in a location.
     * @param personName person's name
     * @param locationName location's name
     */
    private void logPersonInLocation(String personName, String locationName) {
        this.logAction(personName + " removed from the " + locationName);
    }
    
}
