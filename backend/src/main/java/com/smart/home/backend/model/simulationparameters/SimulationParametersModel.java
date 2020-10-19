package com.smart.home.backend.model.simulationparameters;

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
public class SimulationParametersModel{
    private Profile profile;
    private SystemParameters sysParams;

    /**
     * Constructor
     * @param profile
     * @param sysParams
     */
    public SimulationParametersModel(Profile profile, SystemParameters sysParams) {
        this.profile = profile;
        this.sysParams = sysParams;
    }
}
