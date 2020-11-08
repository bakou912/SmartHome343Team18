package com.smart.home.backend.model.simulationparameters.module.command.shc;

import com.smart.home.backend.input.DoorInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import org.springframework.http.ResponseEntity;

/**
 * Door management command.
 */
public class DoorManagementCommand extends AbstractCommand<HouseLayoutModel, DoorInput, Door> {
    
    /**
     * Default Constructor
     */
    public DoorManagementCommand() {
        super("Door management", true);
    }

    @Override
    public ResponseEntity<Door> execute(HouseLayoutModel houseLayoutModel, DoorInput doorInput) {
        return null;
    }
    
}
