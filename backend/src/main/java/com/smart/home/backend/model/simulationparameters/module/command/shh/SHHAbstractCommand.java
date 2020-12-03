package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import lombok.Getter;

/**
 * 
 */
@Getter
public class SHHAbstractCommand<X, Y, Z> extends AbstractCommand<X, Y, Z> {

    /**
     * 2-parameter constructor
     * @param name command name
     * @param locationDependent whether the command is dependent on location
     * 
     */
    protected SHHAbstractCommand(String name, Boolean locationDependent) {
        super(name, "SHH", locationDependent);
    }
    
}
