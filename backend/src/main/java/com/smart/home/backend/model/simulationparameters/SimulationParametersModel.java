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
    
    private Profile profile;
    private SystemParameters sysParams;

    /**
     * Constructor.
     * @param profile profile
     * @param sysParams system parameters
     */
    public SimulationParametersModel(Profile profile, SystemParameters sysParams) {
        this.profile = profile;
        this.sysParams = sysParams;
    }
    
    public void reset() {
        this.setProfile(null);
        this.setSysParams(null);
    }
}
