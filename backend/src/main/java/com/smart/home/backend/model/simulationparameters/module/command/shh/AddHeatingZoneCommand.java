package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.heating.HeatingZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Adding heating zone command
 */
public class AddHeatingZoneCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneInput, String>{

    public AddHeatingZoneCommand() {
        super("add heating zone", true);
    }

    @Override
    public ResponseEntity<String> execute(HeatingModel heatingModel, HeatingZoneInput heatingZoneInput) {
        HeatingZone heatingZone = heatingModel.addZone(heatingZoneInput);
        if(heatingZone!=null){
            this.logAction("New heating zone has been added :" + heatingZone.getName());
        }else{
            this.logAction("Zone already exists");
        }
        return new ResponseEntity<>(heatingZone.getName(), HttpStatus.OK);
    }
    
}
