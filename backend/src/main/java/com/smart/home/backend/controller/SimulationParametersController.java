package com.smart.home.backend.controller;

import com.smart.home.backend.input.CommandPermissionInput;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.simulationparameters.*;
import com.smart.home.backend.model.simulationparameters.module.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
            this.getModel().editModel(parameters);
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
     * Fetching the existing Simulation Parameters Model.
     * @return System parameters model
     */
    @GetMapping("/parameters/profiles")
    public ResponseEntity<List<UserProfile>> getProfiles(){
        return new ResponseEntity<>(model.getUserProfiles().getProfiles(), HttpStatus.OK);
    }
    
    /**
     * Adding a command permission to a profile.
     * @param profileName profile's name
     * @param permissionInput permission input
     * @return Updated user profiles
     */
    @PostMapping("/parameters/profiles/{profileName}/permissions")
    public ResponseEntity<UserProfiles> addPermission(@PathVariable String profileName, @RequestBody CommandPermissionInput permissionInput){
        
        boolean permissionAdded = this.getModel().getUserProfiles().addPermissionToProfile(profileName, permissionInput);
        
        if (!permissionAdded) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(this.getModel().getUserProfiles(), HttpStatus.OK);
    }
    
    /**
     * Removing a command permission from a profile.
     * @param profileName profile's name
     * @param permissionName permission's name
     * @return Updated user profiles
     */
    @DeleteMapping("/parameters/profiles/{profileName}/permissions/{permissionName}")
    public ResponseEntity<UserProfiles> removePermission(@PathVariable String profileName, @PathVariable String permissionName) {
        
        boolean permissionAdded = this.getModel().getUserProfiles().removePermissionFromProfile(profileName, permissionName);
        
        if (!permissionAdded) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(this.getModel().getUserProfiles(), HttpStatus.OK);
    }
    
    /**
     * Modifying some profile's command permission.
     * @param profileName profile's name
     * @param commandName command's name
     * @param permissionInput permission input
     * @return Updated user profiles
     */
    @PutMapping("/parameters/profiles/{profileName}/permissions/{commandName}")
    public ResponseEntity<UserProfiles> modifyPermission(@PathVariable String profileName, @PathVariable String commandName, @RequestBody CommandPermissionInput permissionInput){
        permissionInput.setName(commandName);
        boolean permissionModified = this.getModel().getUserProfiles().modifyPermissionFromProfile(profileName, permissionInput);
        
        if (!permissionModified) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(this.getModel().getUserProfiles(), HttpStatus.OK);
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
     * Fetching the existing Simulation Parameters Model.
     * @return System parameters model
     */
    @GetMapping("/parameters/modules")
    public ResponseEntity<List<Module>> getModules(){
        return new ResponseEntity<>(model.getModules().getModules(), HttpStatus.OK);
    }
    
    /**
     * Validates the parameters input.
     * @param parameters parameters input
     * @return Validity of the parameters
     */
    private boolean areParametersValid(EditParametersInput parameters) {
        String profile = parameters.getUserInput().getProfile();
        Double insideTemp = parameters.getParametersInput().getInsideTemp();
        Double outsideTemp = parameters.getParametersInput().getOutsideTemp();
        LocalDateTime date = parameters.getParametersInput().getDate();
        
        return insideTemp != null && outsideTemp != null && profile != null && !profile.equals("") && date != null
                && insideTemp > -20 && insideTemp <= 30 && outsideTemp > -60 && outsideTemp < 50;
    }
}
