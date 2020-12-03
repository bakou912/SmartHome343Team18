package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneRoomTemperatureInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.houselayout.Room;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Overriding room temperature command
 */
public class OverrideRoomTemperatureCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneRoomTemperatureInput, Double>{

    public OverrideRoomTemperatureCommand() {
        super("Overriding room's temperature", true);
    }
    
    @Override
    public ResponseEntity<Double> execute(HeatingModel heatingModel,HeatingZoneRoomTemperatureInput heatingZoneRoomTemperature ) {
        Room FoundRoom = heatingModel.overrideRoomTemeprature(heatingZoneRoomTemperature.getZoneId(), heatingZoneRoomTemperature.getRoomId(), heatingZoneRoomTemperature.getOverrideTemperature());
        this.logAction("Overriding temperature for room :" + FoundRoom.getName());
        return new ResponseEntity<>(heatingZoneRoomTemperature.getOverrideTemperature(), HttpStatus.OK);
    }
}
