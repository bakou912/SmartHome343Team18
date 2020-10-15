package com.smart.home.backend.controller;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.model.simulationParameters.Profile;
import com.smart.home.backend.model.simulationParameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationParameters.SystemParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
            if (insideTemp >- 20 && insideTemp <= 30 && outsideTemp >-60 && outsideTemp < 50){
                model = SimulationParametersModel.builder()
                        .profile(new Profile(role))
                        .sysParams(new SystemParameters(outsideTemp,insideTemp,date))
                        .build();
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
