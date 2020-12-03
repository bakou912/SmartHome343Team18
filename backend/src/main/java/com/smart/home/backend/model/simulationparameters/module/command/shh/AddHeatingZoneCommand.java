package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.model.heating.HeatingModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AddHeatingZoneCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneInput, Boolean>{

    public AddHeatingZoneCommand() {
        super("add heating zone", true);
    }

    @Override
    public ResponseEntity<Boolean> execute(HeatingModel heatingModel, HeatingZoneInput heatingZoneInput) {
        Boolean success = heatingModel.addZone(heatingZoneInput);
        this.logAction("New heating zone has been added");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
    
}
