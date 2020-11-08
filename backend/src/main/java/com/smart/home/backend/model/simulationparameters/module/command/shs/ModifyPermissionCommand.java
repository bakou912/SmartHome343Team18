package com.smart.home.backend.model.simulationparameters.module.command.shs;

import com.smart.home.backend.input.CommandPermissionInput;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationparameters.UserProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Permission restriction modification command.
 */
public class ModifyPermissionCommand extends SHSAbstractCommand<SimulationParametersModel, CommandPermissionInput, UserProfiles> {
    
    /**
	 * Default constructor.
     */
    public ModifyPermissionCommand(){
        super("Modify permission restriction", true);
    }

    @Override
    public ResponseEntity<UserProfiles> execute(SimulationParametersModel simulationParametersModel, CommandPermissionInput commandPermissionInput) {
        boolean permissionModified = simulationParametersModel.getUserProfiles().modifyPermissionFromProfile(commandPermissionInput);
    
        if (!permissionModified) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        this.logAction(
                "Changed " + commandPermissionInput.getProfileName()
                        + "'s permission restriction for the " + commandPermissionInput.getName()
                        + " command to " + commandPermissionInput.getLocationRestriction().toString()
        );
    
        return new ResponseEntity<>(simulationParametersModel.getUserProfiles(), HttpStatus.OK);
    }
    
}
