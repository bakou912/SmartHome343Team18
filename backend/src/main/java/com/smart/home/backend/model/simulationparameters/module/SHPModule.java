package com.smart.home.backend.model.simulationparameters.module;

import java.util.ArrayList;
import com.smart.home.backend.model.simulationparameters.module.command.shpcommands.NotifyAuthoritiesCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shpcommands.NotifyUsersCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shpcommands.SetLightsAwayCommand;

public class SHPModule extends Module {

    public SHPModule() {
        super("SHP", new ArrayList<>());
        this.getCommands().add(new NotifyAuthoritiesCommand());
        this.getCommands().add(new NotifyUsersCommand());
        this.getCommands().add(new SetLightsAwayCommand());
    }
    
    
}
