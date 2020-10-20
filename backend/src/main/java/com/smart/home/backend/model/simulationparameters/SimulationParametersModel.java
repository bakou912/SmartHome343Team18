package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.model.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Model for the SimulationParameters
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@Component
public class SimulationParametersModel implements BaseModel {
    
    private User user;
    private SystemParameters sysParams;

    /**
     * Constructor.
     * @param user user
     * @param sysParams system parameters
     */
    public SimulationParametersModel(User user, SystemParameters sysParams) {
        this.user = user;
        this.sysParams = sysParams;
    }
    
    public void reset() {
        this.setUser(null);
        this.setSysParams(null);
    }
}
