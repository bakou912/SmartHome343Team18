package com.smart.home.backend.model.simulationparameters.module.command.shc;

import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import lombok.Getter;

@Getter
public abstract class SHCAbstractCommand<X, Y, Z> extends AbstractCommand<X, Y, Z> {
	
	/**
	 * 2-parameter constructor
	 * @param name command name
	 * @param locationDependent wether the command is dependent on location
	 */
	protected SHCAbstractCommand(String name, Boolean locationDependent) {
		super(name, "SHC", locationDependent);
	}

}
