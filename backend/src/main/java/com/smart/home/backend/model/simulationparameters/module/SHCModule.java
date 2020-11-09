package com.smart.home.backend.model.simulationparameters.module;

import com.smart.home.backend.model.simulationparameters.module.command.shc.*;

import java.util.ArrayList;

/**
 * SHC module class. Contains the list of commands for this module.
 */
public class SHCModule extends Module {
	
	/**
	 * Default constructor.
	 */
	public SHCModule() {
		super("SHC", new ArrayList<>());
		this.getCommands().add(new WindowManagementCommand());
		this.getCommands().add(new DoorManagementCommand());
		this.getCommands().add(new LightManagementCommand());
	}
	
}
