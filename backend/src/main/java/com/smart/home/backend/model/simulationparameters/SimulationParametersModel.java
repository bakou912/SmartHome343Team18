package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.ParametersInput;
import com.smart.home.backend.model.BaseModel;
import com.smart.home.backend.model.simulationparameters.location.PersonLocationPosition;
import com.smart.home.backend.model.simulationparameters.module.Modules;
import com.smart.home.backend.model.smarthomesecurity.SecurityModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;

/**
 * Model for the SimulationParameters
 */
@Getter
@Setter
@Component
@NoArgsConstructor
public class SimulationParametersModel implements BaseModel {
    
    private User user;
    private SystemParameters sysParams;
    private Modules modules;
    private UserProfiles userProfiles;
    
    private PropertyChangeSupport support;
    
    
    /**
     * 1-parameter constructor.
     * @param userProfiles user profiles
     */
    @Autowired
    public SimulationParametersModel(UserProfiles userProfiles, Modules modules, SecurityModel securityModel) {
        this.userProfiles = userProfiles;
        this.modules = modules;
        this.support = new PropertyChangeSupport(this);
        this.support.addPropertyChangeListener(securityModel);
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
        this.setSysParams(new ParametersInput(0.0, 0.0, LocalDateTime.now()));
    }
    
    /**
     * Setting new sys params values from input
     * @param parametersInput parameters input
     */
    public void setSysParams(ParametersInput parametersInput) {
        if (this.getSysParams() != null) {
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
        } else {
            this.sysParams = new SystemParameters(parametersInput);
            this.support.firePropertyChange("date", null, parametersInput.getDate());
        }
    }
    
}
