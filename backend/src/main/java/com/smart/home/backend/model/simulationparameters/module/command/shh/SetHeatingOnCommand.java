package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingOnInput;
import com.smart.home.backend.model.heating.HeatingModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Overriding room temperature command
 */
public class SetHeatingOnCommand extends SHHAbstractCommand<HeatingModel, HeatingOnInput, Boolean>{

    public SetHeatingOnCommand() {
        super("Setting HAVC status");
    }
    
    @Override
    public ResponseEntity<Boolean> execute(HeatingModel heatingModel, HeatingOnInput heatingOnInput ) {
        heatingModel.setOn(heatingOnInput.getOn());
        this.logAction("Turned HAVC " + (heatingModel.getOn() ? "ON" : "OFF"));
        return new ResponseEntity<>(heatingModel.getOn(), HttpStatus.OK);
    }
    
}
