package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.ParametersInput;
import com.smart.home.backend.model.AbstractBaseModel;
import com.smart.home.backend.model.heating.SeasonDates;
import com.smart.home.backend.model.simulationparameters.location.PersonLocationPosition;
import com.smart.home.backend.model.simulationparameters.module.Modules;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.time.LocalDateTime;

/**
 * Model for the SimulationParameters
 */
@Getter
@Setter
@Component
@NoArgsConstructor
public class SimulationParametersModel extends AbstractBaseModel {
    
    private User user;
    private SystemParameters sysParams;
    private Modules modules;
    private UserProfiles userProfiles;
    
    /**
     * 3-parameter constructor.
     * @param userProfiles user profiles
     * @param modules modules
     * @param systemParameters system parameters
     */
    @Autowired
    public SimulationParametersModel(UserProfiles userProfiles, Modules modules, SystemParameters systemParameters) {
        this.userProfiles = userProfiles;
        this.modules = modules;
        this.sysParams = systemParameters;
        this.reset();
    }
    
    /**
     * Edit the simulation parameters.
     * @param parameters parameters input
     */
    public void editModel(EditParametersInput parameters) {
        UserProfile profile = this.getUserProfiles().get(parameters.getUserInput().getProfile());
        this.setUser(new User(profile, parameters.getUserInput().getName(), parameters.getUserInput().getLocation()));
        this.setSysParams(parameters.getParametersInput());
    }
    
    @Override
    public void reset() {
        this.setUser(new User(this.getUserProfiles().get(0), "", new PersonLocationPosition()));
        this.setSysParams(new ParametersInput(0.0, 0.0, LocalDateTime.now(), 1, new SeasonDates()));
    }
    
    /**
     * Setting new sys params values from input
     * @param parametersInput parameters input
     */
    public void setSysParams(ParametersInput parametersInput) {
        this.getSysParams().modifyParameters(parametersInput);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Complying with parent class
    }
}
