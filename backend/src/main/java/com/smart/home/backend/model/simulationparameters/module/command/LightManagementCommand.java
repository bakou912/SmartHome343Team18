package com.smart.home.backend.model.simulationparameters.module.command;

import com.smart.home.backend.input.LightInput;
import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Light;
import com.smart.home.backend.model.houselayout.directional.Window;
import org.springframework.http.ResponseEntity;

public class LightManagementCommand extends AbstractCommand<HouseLayoutModel, LightInput, Light> {
    public LightManagementCommand(){
        super("Light Management", true);
    }

    @Override
    public ResponseEntity<Light> execute(HouseLayoutModel houseLayoutModel, LightInput lightInput) {
        return null;
    }
}
