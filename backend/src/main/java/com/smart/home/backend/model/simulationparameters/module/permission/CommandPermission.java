package com.smart.home.backend.model.simulationparameters.module.permission;

import com.smart.home.backend.constant.LocationRestriction;
import com.smart.home.backend.input.CommandPermissionInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Command permission class for UserProfile.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommandPermission {
	
	private String name;
	private LocationRestriction locationRestriction;
	
	/**
	 * 2-parameter constructor.
	 * @param name the permission's command name
	 * @param locationRestriction the permission's restriction based on location
	 */
	public CommandPermission(String name, LocationRestriction locationRestriction) {
		this.name = name;
		this.locationRestriction = locationRestriction;
	}
	
	/**
	 * 1-parameter constructor from input.
	 * @param commandPermissionInput command permission input
	 */
	public CommandPermission(CommandPermissionInput commandPermissionInput) {
		this(commandPermissionInput.getName(), commandPermissionInput.getLocationRestriction());
	}
	
}
