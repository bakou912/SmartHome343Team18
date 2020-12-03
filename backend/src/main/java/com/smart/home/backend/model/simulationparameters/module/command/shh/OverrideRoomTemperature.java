package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneRoomTemperatureInput;
import com.smart.home.backend.model.heating.HeatingModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OverrideRoomTemperature extends SHHAbstractCommand<HeatingModel, HeatingZoneRoomTemperatureInput, Double>{

    public OverrideRoomTemperature() {
        super("Overriding room's temperature", true);
    }
    
    @Override
    public ResponseEntity<Double> execute(HeatingModel heatingModel,HeatingZoneRoomTemperatureInput heatingZoneRoomTemperature ) {
        heatingModel.overrideRoomTemeprature(heatingZoneRoomTemperature.getZoneId(), heatingZoneRoomTemperature.getRoomId(), heatingZoneRoomTemperature.getOverrideTemperature());
        this.logAction("Overriding room's temperature");
        return new ResponseEntity<>(heatingZoneRoomTemperature.getOverrideTemperature(), HttpStatus.OK);
    }
}
