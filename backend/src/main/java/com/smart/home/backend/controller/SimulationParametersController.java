package com.smart.home.backend.controller;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.model.simulationparameters.Profile;
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
@CrossOrigin
@RestController
public class SimulationParametersController {
    
    private SimulationParametersModel model;
    
    @Autowired
    public SimulationParametersController(SimulationParametersModel model) {
        this.model = model;
    }

    /**
     * Creates a Simulation Parameters Model and validates the incoming data
     * @param parameters input to construct the simulation parameters model
     * @return SimulationParameterModel created
     */
    @PostMapping("/parameters")
    public ResponseEntity<SimulationParametersModel> editSimulationParameters(@RequestBody EditParametersInput parameters){
        Role role = parameters.getProfileInput().getRole();
        Double insideTemp = parameters.getParametersInput().getInsideTemp();
        Double outsideTemp = parameters.getParametersInput().getOutsideTemp();
        LocalDateTime date = parameters.getParametersInput().getDate();
        if (insideTemp != null && outsideTemp != null && role != null && date != null){
            if (insideTemp > -20 && insideTemp <= 30 && outsideTemp > -60 && outsideTemp < 50){
                this.getModel().setProfile(new Profile(role));
                this.getModel().setSysParams(new SystemParameters(outsideTemp,insideTemp,date));
                
                return new ResponseEntity<>(model, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    /**
     *  Fetching the existing Simulation Parameters Model
     * @return System parameters model
     */
    @GetMapping("/parameters")
    public ResponseEntity<SimulationParametersModel> getSimulationParameters(){
        return new ResponseEntity<>(model,HttpStatus.OK);
    }
}
