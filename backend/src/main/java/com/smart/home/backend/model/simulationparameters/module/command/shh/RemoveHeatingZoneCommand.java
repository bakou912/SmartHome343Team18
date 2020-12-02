package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.model.heating.HeatingModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RemoveHeatingZoneCommand extends SHHAbstractCommand<HeatingModel, Integer, Integer>{

    public RemoveHeatingZoneCommand() {
        super("removed heating zone", true);
    }
    
    @Override
    public ResponseEntity<Integer> execute(HeatingModel heatingModel, Integer zoneId) {
        heatingModel.removeZone(zoneId);
        this.logAction("Heating zone has been removed.");
        return new ResponseEntity<>(zoneId, HttpStatus.OK);
    }
}   
