package com.smart.home.backend.model.simulationparameters.module.command.shc;

import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.input.LightInput;
import com.smart.home.backend.input.OutsideLightInput;
import com.smart.home.backend.input.RoomLightInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Light;
import com.smart.home.backend.model.houselayout.Location;
import com.smart.home.backend.service.OutputConsole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Light management command.
 */
public class LightManagementCommand extends SHCAbstractCommand<HouseLayoutModel, LightInput, Light> {
    
    /**
     * Default constructor.
     */
    public LightManagementCommand(){
        super("Light management", true);
    }

    @Override
    public ResponseEntity<Light> execute(HouseLayoutModel houseLayoutModel, LightInput lightInput) {
        Location location;
        
        if (lightInput instanceof OutsideLightInput) {
            location = houseLayoutModel.getOutsideLocation(((OutsideLightInput) lightInput).getLocation());
        } else {
            location = houseLayoutModel.findRoom(((RoomLightInput) lightInput).getLocation());
        }
        
        if (location == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Light modifiedLight = this.handleLightModes(houseLayoutModel, location, lightInput);
    
        if (modifiedLight.getAutoMode().equals(true)) {
            lightInput.setState(location.getPersons().isEmpty() ? LightState.OFF : LightState.ON);
        }
    
        if (lightInput.getState() != null && !lightInput.getState().equals(modifiedLight.getState())) {
            modifiedLight.setState(lightInput.getState());
            this.logAction("Turned " + location.getName() + "'s light " + lightInput.getState().toString());
        }
        
        return new ResponseEntity<>(modifiedLight, HttpStatus.OK);
    }
    
    /**
     * Handle the
     * @param houseLayoutModel house layout model
     * @param location targeted location
     * @param lightInput light input
     * @return Modified light
     */
    private Light handleLightModes(HouseLayoutModel houseLayoutModel, Location location, LightInput lightInput) {
        Light modifiedLight = location.getLight();
    
        if (lightInput.getAutoMode() != null && !lightInput.getAutoMode().equals(modifiedLight.getAutoMode())) {
            String autoModeString = lightInput.getAutoMode().equals(true) ? "ON" : "OFF";
            modifiedLight.setAutoMode(lightInput.getAutoMode());
            this.logAction("Turned " + location.getName() + "'s light's auto mode " + autoModeString);
        }
    
        if (lightInput.getAwayMode() != null && !lightInput.getAwayMode().equals(modifiedLight.getAwayMode())) {
            String awayModeString = lightInput.getAwayMode().equals(true) ? "ON" : "OFF";
            houseLayoutModel.setAwayMode(location, lightInput.getAwayMode());
            OutputConsole.log("SHP | Turned " + location.getName() + "'s light's away mode " + awayModeString);
        }
        
        return modifiedLight;
    }
    
}
