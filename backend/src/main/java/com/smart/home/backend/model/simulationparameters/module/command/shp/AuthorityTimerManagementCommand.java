package com.smart.home.backend.model.simulationparameters.module.command.shp;

import com.smart.home.backend.model.simulationparameters.module.command.AbstractCommand;
import com.smart.home.backend.model.smarthomesecurity.SecurityModel;

import com.smart.home.backend.service.OutputConsole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.Duration;

/**
 * Authority timer management command.
 */
public class AuthorityTimerManagementCommand extends AbstractCommand<SecurityModel, Duration, Duration> {
    
    /**
     * Default constructor.
     */
    public AuthorityTimerManagementCommand() {
        super("Authority alert time management", false);
    }
    
    @Override
    public ResponseEntity<Duration> execute(SecurityModel securityModel, Duration duration){
        OutputConsole.log("Changing time to call authorities to: " + duration.toString());
        securityModel.setAlertAuthoritiesTime(duration);
        return new ResponseEntity<>(securityModel.getAlertAuthoritiesTime(), HttpStatus.OK);
    }
    
}
