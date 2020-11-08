package com.smart.home.backend.model.simulationparameters.module.command;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@Getter
public abstract class AbstractCommand<X, Y, Z> implements Command<X, Y, Z> {
	
	private final String name;
	private final Boolean locationDependent;
	
	/**
	 * 2-parameter constructor
	 * @param name command name
	 * @param locationDependent wether the command is dependent on location
	 */
	protected AbstractCommand(String name, Boolean locationDependent) {
		this.name = name;
		this.locationDependent = locationDependent;
	}
	
	@Override
	public ResponseEntity<Z> execute(X model, Y input) {
		return null;
	}

}
