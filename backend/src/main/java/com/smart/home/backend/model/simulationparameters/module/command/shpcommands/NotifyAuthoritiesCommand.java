package com.smart.home.backend.model.simulationparameters.module.command.shpcommands;

import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;

import org.springframework.http.ResponseEntity;

public class NotifyAuthoritiesCommand extends AbstractCommand<HouseLayoutModel, Void, Void> {
    
    public NotifyAuthoritiesCommand() {
        super("Notify Authorities Command", true);
    }

    public ResponseEntity<Boolean> execute(){
        return null;
    }
}
