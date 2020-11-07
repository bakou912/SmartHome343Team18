package com.smart.home.backend.model.simulationparameters.module.command.shpcommands;

import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import com.smart.home.backend.model.smarthomesecurity.SecurityModel;

import org.springframework.http.ResponseEntity;

public class AwayModeCommand extends AbstractCommand<HouseLayoutModel, SecurityModel, Void>{

    public AwayModeCommand() {
        super("Away Mode", false);
        // TODO Auto-generated constructor stub
    }

    public ResponseEntity<Boolean> execute(HouseLayoutModel houseLayoutModel){
        return null;
    }
    
}
