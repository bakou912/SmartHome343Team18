package com.smart.home.backend.model.simulationparameters.module.command.shp;

import com.smart.home.backend.model.smarthomesecurity.SecurityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

/**
 * Authority timer management command.
 */
public class AuthorityTimerManagementCommand extends SHPAbstractCommand<SecurityModel, Integer, Integer> {
    
    /**
     * Default constructor.
     */
    public AuthorityTimerManagementCommand() {
        super("Authorities alert time management", false);
    }
    
    @Override
    public ResponseEntity<Integer> execute(SecurityModel securityModel, Integer duration){
        securityModel.setAlertAuthoritiesTime(Duration.ofSeconds(duration));
        this.logAction("Authorities alert time changed to " + duration + " seconds");
        return new ResponseEntity<>((int) securityModel.getAlertAuthoritiesTime().getSeconds(), HttpStatus.OK);
    }
    
}
