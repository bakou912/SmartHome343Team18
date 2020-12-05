package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.model.heating.HeatingModel;

import com.smart.home.backend.model.heating.HeatingZone;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Heating zone removal command.
 */
public class RemoveHeatingZoneCommand extends SHHAbstractCommand<HeatingModel, Integer, Integer>{

    public RemoveHeatingZoneCommand() {
        super("Removing heating zone");
    }
    
    @Override
    public ResponseEntity<Integer> execute(HeatingModel heatingModel, Integer zoneId) {
        HeatingZone zone = heatingModel.removeZone(zoneId);
        
        if (zone == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        this.logAction("Removed heating zone " + zone.getName());
        return new ResponseEntity<>(zoneId, HttpStatus.OK);
    }
    
}   
