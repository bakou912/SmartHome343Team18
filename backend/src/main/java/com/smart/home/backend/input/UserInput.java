package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.model.simulationparameters.location.PersonLocationPosition;
import com.smart.home.backend.model.simulationparameters.module.permission.CommandPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Input class for the user parameters
 */
@Getter
@Setter
public class UserInput {
    
    @JsonProperty("profile")
    private String profile;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("location")
    private PersonLocationPosition location;
    
    @JsonProperty("permissions")
    private List<CommandPermission> commandPermissions = new ArrayList<>();
}
