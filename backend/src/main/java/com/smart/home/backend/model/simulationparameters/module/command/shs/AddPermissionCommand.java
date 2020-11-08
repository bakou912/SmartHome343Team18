package com.smart.home.backend.model.simulationparameters.module.command.shs;

import com.smart.home.backend.input.CommandPermissionInput;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationparameters.UserProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Permission addition command.
 */
public class AddPermissionCommand extends SHSAbstractCommand<SimulationParametersModel, CommandPermissionInput, UserProfiles> {
    
    /**
	 * Default constructor.
     */
    public AddPermissionCommand(){
        super("Add permission", true);
    }

    @Override
    public ResponseEntity<UserProfiles> execute(SimulationParametersModel simulationParametersModel, CommandPermissionInput commandPermissionInput) {
        boolean permissionAdded = simulationParametersModel.getUserProfiles().addPermissionToProfile(commandPermissionInput);
    
        if (!permissionAdded) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        this.logAction(
                "Added " + commandPermissionInput.getProfileName()
                        + "'s permission for the " + commandPermissionInput.getName()
                        + " command with restriction " +  commandPermissionInput.getLocationRestriction().toString()
        );
    
        return new ResponseEntity<>(simulationParametersModel.getUserProfiles(), HttpStatus.OK);
    }
    
}
