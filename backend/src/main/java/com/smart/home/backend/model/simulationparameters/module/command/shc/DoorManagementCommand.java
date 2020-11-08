package com.smart.home.backend.model.simulationparameters.module.command.shc;

import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.DoorState;
import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.directional.Door;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Door management command.
 */
public class DoorManagementCommand extends SHCAbstractCommand<HouseLayoutModel, DoorInput, Door> {
    
    /**
     * Default Constructor
     */
    public DoorManagementCommand() {
        super("Door management", true);
    }

    @Override
    public ResponseEntity<Door> execute(HouseLayoutModel houseLayoutModel, DoorInput doorInput) {
        Door targetDoor = houseLayoutModel.findDoor(doorInput.getLocation());
    
        if (targetDoor == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        DoorState state = doorInput.getState();
    
        if (state != null) {
            targetDoor.setState(state);
    
            this.logAction("Changed door state to " + state.toString());
        }
    
        return new ResponseEntity<>(targetDoor, HttpStatus.OK);
    }
    
}
