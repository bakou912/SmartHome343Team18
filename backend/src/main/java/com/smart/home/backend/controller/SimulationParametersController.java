package com.smart.home.backend.controller;

import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.ParametersInput;
import com.smart.home.backend.model.simulationParameters.SimulationParametersModel;
import org.springframework.web.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Getter
@Setter
@CrossOrigin
@RestController
public class SimulationParametersController {
    private SimulationParametersModel model=new SimulationParametersModel();

    @PostMapping("/Parameters")
    private SimulationParametersModel editSimulationParameters(@RequestParam EditParametersInput parameters){
        model.setUserLocation(parameters.getProfileInputs().getLocation());
        model.setProfileRole(parameters.getProfileInputs().getRole());
        model.setLocation(parameters.getParametersInput().getLocation());
        model.setDate(parameters.getParametersInput().getDate());
        return model;
    }
    /**
     *
     * @return System parameters model
     */
    @GetMapping("/Parameters")
    private SimulationParametersModel getSimulationParameters(){
        return model;
    }
}
