package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.constant.Role;
import lombok.Getter;
import lombok.Setter;

/**
 * Model class for the Profile
 */
@Getter
@Setter
public class Profile{
    private Role role;

    public Profile(Role role){
        this.role = role;
    }
}
