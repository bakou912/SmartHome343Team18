package com.smart.home.backend.model.simulationparameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.home.backend.input.CommandPermissionInput;
import com.smart.home.backend.model.simulationparameters.module.permission.CommandPermission;
import com.smart.home.backend.service.userprofile.UserProfileService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;

/**
 * UserProfiles component used to keep track of the existing user profiles.
 */
@Component
public class UserProfiles {
	
	@Getter
	private final List<UserProfile> profiles;
	@JsonIgnore
	private final PropertyChangeSupport support; // used to add an observer
	
	@Autowired
	public UserProfiles(UserProfileService userProfileService) throws IOException {
		profiles = userProfileService.loadUserProfiles();
		this.support = new PropertyChangeSupport(this);
		support.addPropertyChangeListener(userProfileService);
	}
	
	/**
	 * Retrieving a user profile by index.
	 * @param index index of the user profile
	 * @return Found user profile
	 */
	public UserProfile get(int index) {
		return this.profiles.get(index);
	}
	
	/**
	 * Retrieving a user profile by name.
	 * @param name name of the user profile
	 * @return Found user profile. null if not found
	 */
	public UserProfile get(String name) {
		return this.profiles.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
	}
	
	/**
	 * Adding a user profile to the list.
	 * @param userProfile user profile to add
	 * @return Wether the profile was added or not
	 */
	public boolean add(UserProfile userProfile) {
		boolean added = this.profiles.remove(userProfile);
		
		if (added) {
			support.firePropertyChange("profiles", this, this);
		}
		
		return added;
	}
	
	/**
	 * Removing a user profile from the list.
	 * @param userProfile user profile to remove
	 * @return Wether the profile was removed or not
	 */
	public boolean remove(UserProfile userProfile) {
		boolean removed = this.profiles.remove(userProfile);
		
		if (removed) {
			support.firePropertyChange("profiles", this, this);
		}
		
		return removed;
	}
	
	/**
	 * Adding a command permission to a profile.
	 * @param permissionInput restriction input
	 * @return Wether the restriction was added or not
	 */
	public boolean addPermissionToProfile(CommandPermissionInput permissionInput) {
		UserProfile foundProfile = this.get(permissionInput.getProfileName());
		
		if (foundProfile == null || foundProfile.getCommandPermissions().stream().anyMatch(r -> r.getName().equals(permissionInput.getName()))) {
			return false;
		}
		
		foundProfile.getCommandPermissions().add(new CommandPermission(permissionInput));
		
		support.firePropertyChange("profiles", null, this);
		
		return true;
	}
	
	/**
	 * Modifying a profile's command permission.
	 * @param permissionInput permission input
	 * @return Wether the permission was added or not
	 */
	public boolean modifyPermissionFromProfile(CommandPermissionInput permissionInput) {
		UserProfile foundProfile = this.get(permissionInput.getProfileName());
		CommandPermission commandPermission;
		
		if (foundProfile == null || (commandPermission = foundProfile.getCommandPermissions().stream().filter(r -> r.getName().equals(permissionInput.getName())).findFirst().orElse(null)) == null) {
			return false;
		}
		
		commandPermission.setLocationRestriction(permissionInput.getLocationRestriction());
		
		support.firePropertyChange("profiles", null, this);
		
		return true;
	}
	
	/**
	 * Removing a command permission from a profile.
	 * @param commandPermissionInput permission input
	 * @return Wether the permission was removed or not
	 */
	public boolean removePermissionFromProfile(CommandPermissionInput commandPermissionInput) {
		UserProfile foundProfile = this.get(commandPermissionInput.getProfileName());
		
		if (foundProfile == null || !foundProfile.getCommandPermissions().removeIf(r -> r.getName().equals(commandPermissionInput.getName()))) {
			return false;
		}
		
		support.firePropertyChange("profiles", null, this);
		
		return true;
	}
	
}
