package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneTemperatureInput;
import com.smart.home.backend.model.heating.HeatingModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SetZoneTemperatureCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneTemperatureInput, Double >{

    public SetZoneTemperatureCommand() {
        super("Setting zone temperature", true);
    }

    
    @Override
    public ResponseEntity<Double> execute(HeatingModel heatingModel, HeatingZoneTemperatureInput heatingTemperatureInput) {
        heatingModel.setZonePeriodTargetTemperature(heatingTemperatureInput.getZoneId(), heatingTemperatureInput.getHeatingZonePeriod(), heatingTemperatureInput.getTargetTemperature() );
        this.logAction("Temperature has been set");
        return new ResponseEntity<>(heatingTemperatureInput.getTargetTemperature(), HttpStatus.OK);
    }


}
