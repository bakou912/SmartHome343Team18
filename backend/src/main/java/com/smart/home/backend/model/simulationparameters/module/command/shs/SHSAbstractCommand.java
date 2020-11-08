package com.smart.home.backend.model.simulationparameters.module.command.shs;

import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import lombok.Getter;

@Getter
public abstract class SHSAbstractCommand<X, Y, Z> extends AbstractCommand<X, Y, Z> {
	
	/**
	 * 2-parameter constructor
	 * @param name command name
	 * @param locationDependent wether the command is dependent on location
	 */
	protected SHSAbstractCommand(String name, Boolean locationDependent) {
		super(name, "SHS",locationDependent);
	}

}
