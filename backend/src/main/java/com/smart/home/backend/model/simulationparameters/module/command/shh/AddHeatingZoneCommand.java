package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.heating.HeatingZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Adding heating zone command
 */
public class AddHeatingZoneCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneInput, HeatingZone>{

    public AddHeatingZoneCommand() {
        super("Adding heating zone", true);
    }

    @Override
    public ResponseEntity<HeatingZone> execute(HeatingModel heatingModel, HeatingZoneInput heatingZoneInput) {
        HeatingZone heatingZone = heatingModel.addZone(heatingZoneInput);
        if (heatingZone != null) {
            this.logAction("Added new heating zone: " + heatingZone.getName());
        } else {
            this.logAction("Zone " + heatingZoneInput.getName() + " already exists and was not added");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(heatingZone, HttpStatus.OK);
    }
    
}
