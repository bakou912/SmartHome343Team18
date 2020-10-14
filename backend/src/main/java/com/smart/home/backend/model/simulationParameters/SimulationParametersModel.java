package com.smart.home.backend.model.simulationParameters;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.model.simulationParameters.Profile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the SimulationParameters
 */
@Getter
@Setter
public class SimulationParametersModel{
    private Profile profile;
    private SystemParameters sysParams;

    public void setProfileRole(Role role){
        profile.setRole(role);
    }
    public void setUserLocation(String location){
        profile.setLocation(location);
    }
    public void setLocation(String location){
        sysParams.setLocation(location);
    }
    public void setDate(String dateTime){
        //TODO: implement setDate
    }
    public String getUser(){
        //TODO:implement getDate
        throw new NotImplementedException();
    }
    public String getLocation(){
        //TODO:implement getDate
        throw new NotImplementedException();
    }
    public String getDate(){
        //TODO:implement getDate
        throw new NotImplementedException();
    }
    public String getUserLocation(){
        //TODO:implement getDate
        throw new NotImplementedException();
    }
}
