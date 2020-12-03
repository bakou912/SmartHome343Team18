package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneTemperatureInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.heating.HeatingZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Set zone temperature command
 */
public class SetZoneTemperatureCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneTemperatureInput, Double >{

    public SetZoneTemperatureCommand() {
        super("Setting zone temperature", true);
    }

    @Override
    public ResponseEntity<Double> execute(HeatingModel heatingModel, HeatingZoneTemperatureInput heatingTemperatureInput) {
        HeatingZone heatingZone = heatingModel.setZonePeriodTargetTemperature(heatingTemperatureInput.getZoneId(), heatingTemperatureInput.getHeatingZonePeriod(), heatingTemperatureInput.getTargetTemperature() );
        if (heatingZone != null) {
            this.logAction("Set temperature for zone " + heatingZone.getName() + " during period: " + heatingTemperatureInput.getHeatingZonePeriod());
        } else {
            this.logAction("Heating zone was not found");
        }
        return new ResponseEntity<>(heatingTemperatureInput.getTargetTemperature(), HttpStatus.OK);
    }

}
