package com.smart.home.backend.model.simulationparameters.module.command.shh;

/**
 * Adding heating zone command
 */
public class SetDefaultSeasonTempsCommand extends SHHAbstractCommand<Void, Void, Void>{

    public SetDefaultSeasonTempsCommand() {
        super("Setting default season temperatures");
    }
    
}
