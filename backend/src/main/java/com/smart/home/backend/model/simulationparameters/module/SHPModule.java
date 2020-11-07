package com.smart.home.backend.model.simulationparameters.module;

import java.util.ArrayList;
import com.smart.home.backend.model.simulationparameters.module.command.shpcommands.AwayModeCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shpcommands.TimerCommand;

public class SHPModule extends Module {

    public SHPModule() {
        super("SHP", new ArrayList<>());
        this.getCommands().add(new AwayModeCommand());
        this.getCommands().add(new TimerCommand());
    }
    
    
}
