package com.smart.home.backend.model.simulationparameters;

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
        this.setLocation(location);
    }
    
    /**
     * Mutator for location
     * @param location location value to be assigned
     */
    public void setLocation(PersonLocation location) {
        if (location.getOutside() != null && (location.getOutside() || location.getName().equals("Outside"))) {
            this.location.setOutside(true);
        } else {
            this.location = location;
            this.location.setOutside(false);
        }
    }
    
}
