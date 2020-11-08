package com.smart.home.backend.model.simulationparameters.module.command.shc;

import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.input.LightInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Light;
import com.smart.home.backend.model.houselayout.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Light management command.
 */
public class LightManagementCommand extends SHCAbstractCommand<Location, LightInput, Light> {
    
    /**
     * Default constructor.
     */
    public LightManagementCommand(){
        super("Light management", true);
    }

    @Override
    public ResponseEntity<Light> execute(Location location, LightInput lightInput) {
        if (location == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Light modifiedLight = location.getLight();
        
        if (lightInput.getAutoMode() != null && !lightInput.getAutoMode().equals(modifiedLight.getAutoMode())) {
            String autoModeString = lightInput.getAutoMode().equals(true) ? "ON" : "OFF";
            modifiedLight.setAutoMode(lightInput.getAutoMode());
            this.logAction("Turned " + location.getName() + "'s light's auto mode " + autoModeString);
        }
        
        if (modifiedLight.getAutoMode().equals(true)) {
            lightInput.setState(location.getPersons().isEmpty() ? LightState.OFF : LightState.ON);
        }
    
        if (lightInput.getState() != null && !lightInput.getState().equals(modifiedLight.getState())) {
            modifiedLight.setState(lightInput.getState());
            this.logAction("Turned " + location.getName() + "'s light " + lightInput.getState().toString());
        }
        
        return new ResponseEntity<>(modifiedLight, HttpStatus.OK);
    }
    
}
