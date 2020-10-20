package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.Profile;
import com.smart.home.backend.model.simulationparameters.Location;
import lombok.Getter;
import lombok.Setter;

/**
 * Input class for the user parameters
 */
@Getter
@Setter
public class UserInput {
    
    @JsonProperty("profile")
    private Profile profile;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("location")
    private Location location;
    
}
