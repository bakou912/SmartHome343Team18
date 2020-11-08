package com.smart.home.backend.model.simulationparameters.module.command.shc;

import com.smart.home.backend.input.LightInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Light;
import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import org.springframework.http.ResponseEntity;

/**
 * Light management command.
 */
public class LightManagementCommand extends AbstractCommand<HouseLayoutModel, LightInput, Light> {
    public LightManagementCommand(){
        super("Light management", true);
    }

    @Override
    public ResponseEntity<Light> execute(HouseLayoutModel houseLayoutModel, LightInput lightInput) {
        return null;
    }
}
