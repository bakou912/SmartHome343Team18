package com.smart.home.backend.model.simulationParameters;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.model.simulationParameters.Profile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model for the SimulationParameters
 */
@Getter
@Setter
public class SimulationParametersModel{
    private Profile profile;
    private SystemParameters sysParams;
    public SimulationParametersModel(){
        Date currentDateTime=new Date();
        sysParams=SystemParameters.builder().insideTemp(23).outsideTemp(18).date(currentDateTime).build();
    }
    public void setDate(String day,String month,Integer year,Integer hour, Integer minutes){

    }
}
