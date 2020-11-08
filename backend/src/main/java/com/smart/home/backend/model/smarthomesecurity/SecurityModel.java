package com.smart.home.backend.model.smarthomesecurity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import com.smart.home.backend.service.OutputConsole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * Security module that listens to the SHS for intruders during away mode
 */
@AllArgsConstructor
@Getter
@Setter
@Component
public class SecurityModel implements PropertyChangeListener{
    
    private Boolean awayMode;
    private Duration alertAuthoritiesTime;
    private Integer nbPersonsInside;
    
    /**
     * Default constructor.
     */
    public SecurityModel() {
        this.awayMode = false;
        this.nbPersonsInside = 0;
        this.alertAuthoritiesTime = Duration.ZERO;
    }
    
    /**
     * Handling property changes from observed objects.
     * @param evt property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {
        
        switch((evt.getPropertyName())) {
            case "nbPersonsInside":
                this.setNbPersonsInside((Integer) evt.getNewValue());
                break;
            default:
                break;
        }
        
    }
    
    /**
     * Setting away mode.
     * @param awayMode new away mode value
     * @return Wether the away mode was changed or not
     */
    public boolean setAwayMode(boolean awayMode) {
        if (!this.getAwayMode() && awayMode && this.getNbPersonsInside() > 0) {
            return false;
        }
        
        this.awayMode = awayMode;
        
        return true;
    }
    
    /**
     * Set specific lights on
     */
    public void setLightsDuringAway(){
        //TODO : tell SHC module to turn on the light in specific rooms

    }

    /**
     * Send notification of a person detected to the console and to authorities.
     */
    public void sendNotification() throws IOException {
        if (this.getAlertAuthoritiesTime() != null) {
           OutputConsole.log("Alerting authorities in: " + this.getAlertAuthoritiesTime().toString());
            Timer timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run(){
                            OutputConsole.log("Alerting authorities");
                        }
                    },
                    this.getAlertAuthoritiesTime().getSeconds()
            );
        }
    }

}
