package com.smart.home.backend.model.simulationparameters.module.command.shp;

import org.springframework.http.ResponseEntity;

/**
 * Light away mode management command.
 */
public class LightAwayModeManagementCommand extends SHPAbstractCommand<Void, Void, Void> {
    
    /**
     * Default constructor.
     */
    public LightAwayModeManagementCommand() {
        super("Light away mode management", false);
    }
    
    @Override
    public ResponseEntity<Void> execute(Void model, Void input){
        return null;
    }
    
}
