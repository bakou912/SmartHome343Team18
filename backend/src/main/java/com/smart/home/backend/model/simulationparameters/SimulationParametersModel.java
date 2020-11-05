package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.model.BaseModel;
import com.smart.home.backend.model.simulationparameters.location.PersonLocation;
import com.smart.home.backend.model.simulationparameters.module.Modules;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Model for the SimulationParameters
 */
@Getter
@Setter
@Component
public class SimulationParametersModel implements BaseModel {
    
    private User user;
    private SystemParameters sysParams;
    private Modules modules;
    private UserProfiles userProfiles;
    
    /**
     * 1-parameter constructor.
     * @param userProfiles user profiles
     */
    @Autowired
    public SimulationParametersModel(UserProfiles userProfiles, Modules modules) {
        this.userProfiles = userProfiles;
        this.modules = modules;
        this.reset();
    }

    /**
     * 2- parameter constructor.
     * @param user user
     * @param sysParams system parameters
     */
    public SimulationParametersModel(User user, SystemParameters sysParams) {
        this.user = user;
        this.sysParams = sysParams;
    }
    
    public void editModel(EditParametersInput parameters) {
        UserProfile profile = this.userProfiles.get(parameters.getUserInput().getProfile());
        this.setUser(new User(profile, parameters.getUserInput().getName(), parameters.getUserInput().getLocation()));
        this.setSysParams(new SystemParameters(parameters.getParametersInput()));
    }
    
    public void reset() {
        this.setUser(new User(this.userProfiles.get(0), "", new PersonLocation()));
        this.setSysParams(new SystemParameters(0.0, 0.0, LocalDateTime.now()));
    }
    
}
