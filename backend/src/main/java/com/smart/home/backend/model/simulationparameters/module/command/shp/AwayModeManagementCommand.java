package com.smart.home.backend.model.simulationparameters.module.command.shp;

import com.smart.home.backend.input.AwayModeInput;
import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import com.smart.home.backend.model.smarthomesecurity.SecurityModel;

import com.smart.home.backend.service.OutputConsole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Away mode management command.
 */
public class AwayModeManagementCommand extends AbstractCommand<SecurityModel, AwayModeInput, Boolean>{
    
    /**
     * Default constructor.
     */
    public AwayModeManagementCommand() {
        super("Away mode management", false);
    }

    @Override
    public ResponseEntity<Boolean> execute(SecurityModel securityModel, AwayModeInput awayModeInput){
        if (!securityModel.setAwayMode(awayModeInput.getState())) {
            OutputConsole.log("Away mode could not be set to " + awayModeInput.getState().toString());
            
            if (awayModeInput.getState().equals(true)) {
                OutputConsole.log("There are people inside the house");
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        return new ResponseEntity<>(securityModel.getAwayMode(), HttpStatus.OK);
    }
    
}
