package com.smart.home.backend.model.simulationParameters;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.model.simulationParameters.Profile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model for the SimulationParameters
 */
@Getter
@Setter
@Builder
public class SimulationParametersModel{
    private Profile profile;
    private SystemParameters sysParams;

    public SimulationParametersModel(Profile profile, SystemParameters sysParams) {
        this.profile = profile;
        this.sysParams = sysParams;
    }
}
