package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.constant.Profile;
import com.smart.home.backend.input.UserInput;
import lombok.Getter;
import lombok.Setter;

/**
 * Model class for the User.
 */
@Getter
@Setter
public class User {
    
    private Profile profile;
    private String name;
    private Location location;

    public User(Profile profile, String name, Location location){
        this.profile = profile;
        this.name = name;
        this.location = location;
    }
    
    public User(UserInput userInput){
        this(userInput.getProfile(), userInput.getName(), userInput.getLocation());
    }
}
