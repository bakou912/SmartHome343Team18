package com.smart.home.backend.model.simulationparameters.module;

import java.util.ArrayList;

import com.smart.home.backend.model.simulationparameters.module.command.shp.AwayModeHoursManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shp.AwayModeManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shp.AuthorityTimerManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shp.LightAwayModeManagementCommand;

public class SHPModule extends Module {

    public SHPModule() {
        super("SHP", new ArrayList<>());
        this.getCommands().add(new AwayModeManagementCommand());
        this.getCommands().add(new AuthorityTimerManagementCommand());
        this.getCommands().add(new LightAwayModeManagementCommand());
        this.getCommands().add(new AwayModeHoursManagementCommand());
    }
    
    
}
