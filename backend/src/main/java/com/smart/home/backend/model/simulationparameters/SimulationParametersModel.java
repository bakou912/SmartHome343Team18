package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.ParametersInput;
import com.smart.home.backend.model.AbstractBaseModel;
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
    
    public void reset() {
        this.setUser(new User(this.getUserProfiles().get(0), "", new PersonLocationPosition()));
        this.setSysParams(new ParametersInput(0.0, 0.0, LocalDateTime.now(), 1));
    }
    
    /**
     * Setting new sys params values from input
     * @param parametersInput parameters input
     */
    public void setSysParams(ParametersInput parametersInput) {
        if (parametersInput.getInsideTemp() != null) {
            this.getSysParams().setInsideTemp(parametersInput.getInsideTemp());
        }

        if (parametersInput.getOutsideTemp() != null) {
            this.getSysParams().setOutsideTemp(parametersInput.getOutsideTemp());
        }

        if (parametersInput.getDate() != null) {
            this.support.firePropertyChange("date", this.getSysParams().getDate(), parametersInput.getDate());
            this.getSysParams().setDate(parametersInput.getDate());
        }

        if (parametersInput.getTimeSpeed() != null) {
            this.support.firePropertyChange("timeSpeed", this.getSysParams().getTimeSpeed(), parametersInput.getTimeSpeed());
            this.getSysParams().setTimeSpeed(parametersInput.getTimeSpeed());
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Complying with parent class
    }
}
