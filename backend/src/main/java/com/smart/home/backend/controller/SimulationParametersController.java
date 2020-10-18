package com.smart.home.backend.controller;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.simulationParameters.Profile;
import com.smart.home.backend.model.simulationParameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationParameters.SystemParameters;
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

    /**
     * Creating a simulation parameters model.
     * @param parameters Simulation parameters input
     * @return SimulationParameterModel created
     */
    @PostMapping("/parameters")
    public ResponseEntity<SimulationParametersModel> editSimulationParameters(@RequestBody EditParametersInput parameters){
        if (this.areParametersValid(parameters)){
            model = SimulationParametersModel.builder()
                    .profile(new Profile(parameters.getProfileInput().getRole()))
                    .sysParams(new SystemParameters(parameters.getParametersInput()))
                    .build();
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
        this.setModel(null);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * Validates the parameters input.
     * @param parameters parameters input
     * @return Validity of the parameters
     */
    private boolean areParametersValid(EditParametersInput parameters) {
        Role role = parameters.getProfileInput().getRole();
        Double insideTemp = parameters.getParametersInput().getInsideTemp();
        Double outsideTemp = parameters.getParametersInput().getOutsideTemp();
        LocalDateTime date = parameters.getParametersInput().getDate();
        
        return insideTemp != null && outsideTemp != null && role != null && date != null
                && insideTemp > -20 && insideTemp <= 30 && outsideTemp > -60 && outsideTemp < 50;
    }
}
