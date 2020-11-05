package com.smart.home.backend.service.userprofile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.home.backend.model.simulationparameters.UserProfile;
import com.smart.home.backend.model.simulationparameters.UserProfiles;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Service that manages the persistence of user profiles. Listens to UserProfiles changes.
 */
@Component
public class UserProfileService implements PropertyChangeListener {
	
	private final File userProfilesFile;
	private final ObjectMapper objectMapper;
	
	/**
	 * Default constructor.
	 * @throws FileNotFoundException Thrown when the user profiles file is not found
	 */
	public UserProfileService() throws FileNotFoundException {
		this.userProfilesFile = ResourceUtils.getFile("classpath:userprofile/user_profiles.json");
		this.objectMapper = new ObjectMapper();
	}
	
	/**
	 * Deserializes the user profiles file.
	 * @return Loaded user profiles
	 * @throws IOException Thrown when reading the file fails
	 */
	public List<UserProfile> loadUserProfiles() throws IOException {
		return objectMapper.readValue(this.userProfilesFile, new TypeReference<List<UserProfile>>(){});
	}
	
	@SneakyThrows
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		UserProfiles userProfiles = (UserProfiles) evt.getNewValue();
		this.objectMapper.writer(new DefaultPrettyPrinter()).writeValue(this.userProfilesFile, userProfiles.getProfiles());
	}
	
}
