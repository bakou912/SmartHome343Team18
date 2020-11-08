package com.smart.home.backend.model.simulationparameters.module.command.shs;

import com.smart.home.backend.input.CommandPermissionInput;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationparameters.UserProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Permission removal command.
 */
public class RemovePermissionCommand extends SHSAbstractCommand<SimulationParametersModel, CommandPermissionInput, UserProfiles> {
    
    /**
	 * Default constructor.
     */
    public RemovePermissionCommand(){
        super("Remove permission", true);
    }

    @Override
    public ResponseEntity<UserProfiles> execute(SimulationParametersModel simulationParametersModel, CommandPermissionInput commandPermissionInput) {
        boolean permissionRemoved = simulationParametersModel.getUserProfiles().removePermissionFromProfile(commandPermissionInput);
    
        if (!permissionRemoved) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        this.logAction("Removed " + commandPermissionInput.getProfileName() + "'s permission for the " + commandPermissionInput.getName() + " command");
    
        return new ResponseEntity<>(simulationParametersModel.getUserProfiles(), HttpStatus.OK);
    }
    
}
