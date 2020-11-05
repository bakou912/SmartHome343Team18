package com.smart.home.backend.model.simulationparameters.module;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class Modules {
	
	private final List<Module> modules;
	
	public Modules() {
		this.modules = new ArrayList<>();
		this.modules.add(new SHCModule());
	}
	
	/**
	 * Retrieving a module by index.
	 * @param index index of the module
	 * @return Found module
	 */
	public Module get(int index) {
		return this.modules.get(index);
	}
	
	/**
	 * Retrieving a module by name.
	 * @param name name of the module
	 * @return Found module. null if not found
	 */
	public Module get(String name) {
		return this.modules.stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
	}
	
}
