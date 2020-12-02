package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.model.heating.HeatingModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AddHeatingZoneCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneInput, HeatingZoneInput>{

    public AddHeatingZoneCommand() {
        super("add heating zone", true);
    }

    @Override
    public ResponseEntity<HeatingZoneInput> execute(HeatingModel heatingModel, HeatingZoneInput heatingZoneInput) {
        heatingModel.addZone(heatingZoneInput);
        this.logAction("New heating zone has been added");
        return new ResponseEntity<>(heatingZoneInput, HttpStatus.OK);
    }
    
}
