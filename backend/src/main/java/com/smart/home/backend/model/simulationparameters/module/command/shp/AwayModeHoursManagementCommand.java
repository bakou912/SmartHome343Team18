package com.smart.home.backend.model.simulationparameters.module.command.shp;

import com.smart.home.backend.model.security.AwayModeHours;
import com.smart.home.backend.model.security.SecurityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Away mode hours management command.
 */
public class AwayModeHoursManagementCommand extends SHPAbstractCommand<SecurityModel, AwayModeHours, AwayModeHours> {
    
    /**
     * Default constructor.
     */
    public AwayModeHoursManagementCommand() {
        super("Away mode light hours management", false);
    }
    
    @Override
    public ResponseEntity<AwayModeHours> execute(SecurityModel securityModel, AwayModeHours awayModeHours) {
        if (awayModeHours.getFrom() != null) {
            securityModel.getAwayModeHours().setFrom(awayModeHours.getFrom());
            this.logAction("Away mode light start hour changed to " + awayModeHours.getFrom());
        }
        
        if (awayModeHours.getTo() != null) {
            securityModel.getAwayModeHours().setTo(awayModeHours.getTo());
            this.logAction("Away mode light stop hour changed to " + awayModeHours.getTo());
        }
        
        return new ResponseEntity<>(securityModel.getAwayModeHours(), HttpStatus.OK);
    }
    
}
