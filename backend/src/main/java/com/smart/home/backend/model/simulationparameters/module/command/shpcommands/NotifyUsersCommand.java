package com.smart.home.backend.model.simulationparameters.module.command.shpcommands;

import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;

import org.springframework.http.ResponseEntity;

public class NotifyUsersCommand extends AbstractCommand<HouseLayoutModel, Void, Void> {
    
    public NotifyUsersCommand() {
        super("Notify Users", true);
        // TODO Auto-generated constructor stub
    }

    public ResponseEntity<Boolean> execute(HouseLayoutModel houseLayoutModel){
        return null;
    }
}
