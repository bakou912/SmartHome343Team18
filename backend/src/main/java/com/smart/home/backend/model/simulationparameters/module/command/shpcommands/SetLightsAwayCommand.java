package com.smart.home.backend.model.simulationparameters.module.command.shpcommands;

import com.smart.home.backend.input.LightInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Light;
import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;

import org.springframework.http.ResponseEntity;

public class SetLightsAwayCommand extends AbstractCommand<HouseLayoutModel, LightInput, Light> {

    public SetLightsAwayCommand() {
        super("Set Lights Away", true);
        // TODO Auto-generated constructor stub
    }

    public ResponseEntity<Light> execute(HouseLayoutModel houseLayoutModel, LightInput lightInput){
        return null;
    }
    
}
