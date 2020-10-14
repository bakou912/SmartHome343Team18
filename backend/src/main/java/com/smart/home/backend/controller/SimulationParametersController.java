package com.smart.home.backend.controller;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.ParametersInput;
import com.smart.home.backend.model.simulationParameters.SimulationParametersModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Getter
@Setter
@CrossOrigin
@RestController
public class SimulationParametersController {
    private SimulationParametersModel model;

    public SimulationParametersController(){
        model=new SimulationParametersModel();
    }

    @PostMapping("/parameters")
    public ResponseEntity<SimulationParametersModel> editSimulationParameters(@RequestBody EditParametersInput parameters){
        Role role=parameters.getProfileInputs().getRole();
        Double insideTemp=parameters.getParametersInput().getInsideTemp();
        Double outsideTemp=parameters.getParametersInput().getOutsideTemp();
        String day=parameters.getParametersInput().getDay();
        String month=parameters.getParametersInput().getMonth();
        Integer year=parameters.getParametersInput().getYear();
        Integer hour=parameters.getParametersInput().getHour();
        Integer minutes=parameters.getParametersInput().getMinutes();
        if (insideTemp!=null && outsideTemp!=null && role!=null){
            model.getProfile().builder().role(role);
            model.getSysParams().setInsideTemp(insideTemp);
            model.getSysParams().setOutsideTemp(outsideTemp);
            return new ResponseEntity<>(model, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    /**
     *
     * @return System parameters model
     */
    @GetMapping("/parameters")
    public ResponseEntity<SimulationParametersModel> getSimulationParameters(){
        return new ResponseEntity<>(model,HttpStatus.OK);
    }
}
