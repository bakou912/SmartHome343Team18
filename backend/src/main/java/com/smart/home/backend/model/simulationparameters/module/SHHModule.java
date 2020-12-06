package com.smart.home.backend.model.simulationparameters.module;


import com.smart.home.backend.model.simulationparameters.module.command.shh.*;

import java.util.ArrayList;

/**
 * SHH module class. Contains the list of commands for this module.
 */
public class SHHModule extends Module {
    
    /**
     * Default constructor
     */
    public SHHModule() {
        super("SHH", new ArrayList<>());
        this.getCommands().add(new AddHeatingZoneCommand());
        this.getCommands().add(new AddRoomToZoneCommand());
        this.getCommands().add(new OverrideRoomTemperatureCommand());
        this.getCommands().add(new RemoveHeatingZoneCommand());
        this.getCommands().add(new RemoveRoomFromZoneCommand());
        this.getCommands().add(new SetHeatingOnCommand());
        this.getCommands().add(new SetZoneTemperatureCommand());
        this.getCommands().add(new SetDefaultSeasonTempsCommand());
    }
    
}
