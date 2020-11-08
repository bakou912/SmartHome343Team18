package com.smart.home.backend.model.simulationparameters.module.command;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface Command<X, Y, Z> {
	
	/**
	 * The command's execution method.
	 * @param model model on which the command makes changes
	 * @param input command input
	 * @return Response for the command execution
	 */
	ResponseEntity<Z> execute(X model, Y input);

}
