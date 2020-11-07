package com.smart.home.backend.model.simulationparameters.module.command;

import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.directional.Door;
import org.springframework.http.ResponseEntity;

/**
 * Door lock command.
 */
public class DoorLockManagementCommand extends AbstractCommand<HouseLayoutModel, DoorInput, Door> {
    /**
     * Default Constructor
     */
    public DoorLockManagementCommand(){
        super("Door Lock Management",true);
    }

    @Override
    public ResponseEntity<Door> execute(HouseLayoutModel houseLayoutModel, DoorInput doorInput) {
        return null;
    }
}
