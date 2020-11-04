package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.model.simulationparameters.module.permission.CommandPermission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * User profile class that encapsulates user permissions.
 */
@Getter
@NoArgsConstructor
public class UserProfile {
	
	@Setter
	private String name;
	private List<CommandPermission> commandPermissions;
	
	/**
	 * 2-parameter constructor
	 * @param name profile name
	 * @param commandPermissions user permissions
	 */
	public UserProfile(String name, List<CommandPermission> commandPermissions) {
		this.name = name;
		this.commandPermissions = commandPermissions;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		return Objects.equals(this.name, ((UserProfile) o).name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
	
}
