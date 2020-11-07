package com.smart.home.backend.model.simulationparameters.module.command.shpcommands;

import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import com.smart.home.backend.model.smarthomesecurity.SecurityModel;

import org.springframework.http.ResponseEntity;

public class TimerCommand extends AbstractCommand<HouseLayoutModel, SecurityModel, Void>{

    public TimerCommand() {
        super("Timer", false);
        // TODO Auto-generated constructor stub
    }

    public ResponseEntity<Boolean> execute(){
        return null;
    }
    
}
