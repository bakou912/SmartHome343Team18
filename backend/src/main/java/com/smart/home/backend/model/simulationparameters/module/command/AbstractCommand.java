package com.smart.home.backend.model.simulationparameters.module.command;

import com.smart.home.backend.service.OutputConsole;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public abstract class AbstractCommand<X, Y, Z> implements Command<X, Y, Z> {
	
	private final String name;
	private final String moduleName;
	private final Boolean locationDependent;
	
	/**
	 * 2-parameter constructor
	 * @param name command name
	 * @param locationDependent wether the command is dependent on location
	 */
	protected AbstractCommand(String name, String moduleName, Boolean locationDependent) {
		this.name = name;
		this.moduleName = moduleName;
		this.locationDependent = locationDependent;
	}
	
	protected void logAction(String line) {
		OutputConsole.log(this.getModuleName() + " | " + line);
	}
	
	@Override
	public ResponseEntity<Z> execute(X model, Y input) {
		return null;
	}

}
