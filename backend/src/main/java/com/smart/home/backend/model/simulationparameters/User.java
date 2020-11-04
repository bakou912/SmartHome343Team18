package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.input.UserInput;
import com.smart.home.backend.model.simulationparameters.location.PersonLocation;
import lombok.Getter;
import lombok.Setter;

/**
 * Model class for the User.
 */
@Getter
@Setter
public class User {
    
    private UserProfile profile;
    private String name;
    private PersonLocation location;
    
    /**
     * 3-parameter constructor.
     * @param profile user's profile
     * @param name user's name
     * @param location user's location
     */
    public User(UserProfile profile, String name, PersonLocation location) {
        this.profile = profile;
        this.name = name;
        this.location = location;
    }
    
    /**
     * 1-parameter constructor.
     * @param userInput user input
     */
    public User(UserInput userInput) {
        this(
                new UserProfile(userInput.getProfile(), userInput.getCommandPermissions()),
                userInput.getName(),
                userInput.getLocation()
        );
    }
}
