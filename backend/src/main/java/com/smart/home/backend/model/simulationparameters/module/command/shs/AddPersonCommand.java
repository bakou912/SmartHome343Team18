package com.smart.home.backend.model.simulationparameters.module.command.shs;

import com.smart.home.backend.input.OutsidePersonInput;
import com.smart.home.backend.input.PersonInput;
import com.smart.home.backend.input.RoomPersonInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.OutsideLocation;
import com.smart.home.backend.model.houselayout.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Person addition command.
 */
public class AddPersonCommand extends SHSAbstractCommand<HouseLayoutModel, PersonInput, Integer> {
    
    /**
     * Default Constructor
     */
    public AddPersonCommand() {
        super("Person management", true);
    }

    @Override
    public ResponseEntity<Integer> execute(HouseLayoutModel houseLayoutModel, PersonInput personInput) {
        if (personInput.getName() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        if (houseLayoutModel.isInHouse(personInput.getName())) {
            this.logAction(personInput.getName() + " is already in the simulation and could not be added");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        if (personInput instanceof OutsidePersonInput) {
            return this.addPersonOutside(houseLayoutModel, (OutsidePersonInput) personInput);
        } else {
            return this.addPersonToRoom(houseLayoutModel, (RoomPersonInput) personInput);
        }
    }
    
    /**
     * Adding a person to a room.
     * @param houseLayoutModel house layout model
     * @param personInput person to add
     * @return Person's id
     */
    private ResponseEntity<Integer> addPersonToRoom(HouseLayoutModel houseLayoutModel, RoomPersonInput personInput) {
        Room targetRoom = houseLayoutModel.findRoom(personInput.getLocation());
    
        if (targetRoom == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        this.logPersonInLocation(personInput.getName(), targetRoom.getName());
    
        return new ResponseEntity<>(houseLayoutModel.addPerson(targetRoom, personInput), HttpStatus.OK);
    }
    
    /**
     * Adding a person outside.
     * @param houseLayoutModel house layout model
     * @param personInput person to add
     * @return Person's id
     */
    private ResponseEntity<Integer> addPersonOutside(HouseLayoutModel houseLayoutModel, OutsidePersonInput personInput) {
        OutsideLocation outsideLocation = houseLayoutModel.getOutsideLocation(personInput.getLocation());
        this.logPersonInLocation(personInput.getName(), outsideLocation.getName());
    
        return new ResponseEntity<>(houseLayoutModel.addPerson(outsideLocation, personInput), HttpStatus.OK);
    }
    
    /**
     * Logging the addition of a person in a location.
     * @param personName person's name
     * @param locationName location's name
     */
    private void logPersonInLocation(String personName, String locationName) {
        this.logAction(personName + " added in the " + locationName);
    }
    
}
