package com.smart.home.backend.model.simulationparameters.module;

import com.smart.home.backend.model.simulationparameters.module.command.DoorManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.PersonManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.WindowManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.WindowObstructionCommand;

import java.util.ArrayList;

/**
 * The SHC module.
 */
public class SHCModule extends Module {
	
	/**
	 * Default constructor.
	 */
	public SHCModule() {
		super("SHC", new ArrayList<>());
		this.getCommands().add(new WindowObstructionCommand());
		this.getCommands().add(new WindowManagementCommand());
		this.getCommands().add(new PersonManagementCommand());
		this.getCommands().add(new DoorManagementCommand());
	}
	
}
