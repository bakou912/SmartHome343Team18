package com.smart.home.backend.controller;

import com.smart.home.backend.constant.Profile;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.simulationparameters.User;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationparameters.SystemParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Simulation Parameters Controller
 */
@Getter
@Setter
@RestController
public class SimulationParametersController {
    
    private SimulationParametersModel model;
    
    @Autowired
    public SimulationParametersController(SimulationParametersModel model) {
        this.model = model;
    }

    /**
     * Creating a simulation parameters model.
     * @param parameters Simulation parameters input
     * @return SimulationParameterModel created
     */
    @PostMapping("/parameters")
    public ResponseEntity<SimulationParametersModel> editSimulationParameters(@RequestBody EditParametersInput parameters){
        if (this.areParametersValid(parameters)){
            this.getModel().setUser(new User(parameters.getUserInput()));
            this.getModel().setSysParams(new SystemParameters(parameters.getParametersInput()));
            return new ResponseEntity<>(model, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Fetching the existing Simulation Parameters Model.
     * @return System parameters model
     */
    @GetMapping("/parameters")
    public ResponseEntity<SimulationParametersModel> getSimulationParameters(){
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
    
    /**
     * Resetting the house layout model.
     * @return Response status code
     */
    @DeleteMapping("/parameters")
    public ResponseEntity<HouseLayoutModel> resetLayout() {
        this.getModel().reset();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * Validates the parameters input.
     * @param parameters parameters input
     * @return Validity of the parameters
     */
    private boolean areParametersValid(EditParametersInput parameters) {
        Profile profile = parameters.getUserInput().getProfile();
        Double insideTemp = parameters.getParametersInput().getInsideTemp();
        Double outsideTemp = parameters.getParametersInput().getOutsideTemp();
        LocalDateTime date = parameters.getParametersInput().getDate();
        
        return insideTemp != null && outsideTemp != null && profile != null && date != null
                && insideTemp > -20 && insideTemp <= 30 && outsideTemp > -60 && outsideTemp < 50;
    }
}
