package com.smart.home.backend.model.simulationparameters.module;

import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class Module {
	
	private final String name;
	private final List<AbstractCommand> commands;
	
	/**
	 * 2-parameter constructor.
	 * @param name module name
	 * @param commands available commands
	 */
	protected Module(String name, List<AbstractCommand> commands) {
		this.name = name;
		this.commands = commands;
	}
	
}
