package com.smart.home.backend.model.simulationparameters.module.command.shp;

import com.smart.home.backend.input.AwayModeInput;
import com.smart.home.backend.model.security.SecurityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Away mode management command.
 */
public class AwayModeManagementCommand extends SHPAbstractCommand<SecurityModel, AwayModeInput, Boolean>{
    
    /**
     * Default constructor.
     */
    public AwayModeManagementCommand() {
        super("Away mode management", false);
    }

    @Override
    public ResponseEntity<Boolean> execute(SecurityModel securityModel, AwayModeInput awayModeInput){
        String stateString = awayModeInput.getState().equals(true) ? "ON" : "OFF";
        
        if (!securityModel.setAwayMode(awayModeInput.getState())) {
            String errorString = "Away mode could not be turned " + stateString;
            
            if (awayModeInput.getState().equals(true)) {
                errorString += ": there are people inside the house";
            }
    
            this.logAction(errorString);
    
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        this.logAction("Away mode turned " + stateString);
    
        return new ResponseEntity<>(securityModel.getAwayMode(), HttpStatus.OK);
    }
    
}
