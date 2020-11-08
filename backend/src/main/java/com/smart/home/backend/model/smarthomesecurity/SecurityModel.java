package com.smart.home.backend.model.smarthomesecurity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.util.Timer;

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
    private Boolean alertDetected;
    
    /**
     * Default constructor.
     */
    public SecurityModel() {
        this.awayMode = false;
        this.alertDetected = false;
        this.nbPersonsInside = 0;
        this.alertAuthoritiesTime = Duration.ofSeconds(10);
    }
    
    /**
     * Handling property changes from observed objects.
     * @param evt property change event
     */
    @SneakyThrows
    public void propertyChange(PropertyChangeEvent evt) {
    
        if("nbPersonsInside".equals((evt.getPropertyName()))) {
            this.setNbPersonsInside((Integer) evt.getNewValue());
            
            if (this.awayMode.equals(true)) {
                this.sendNotification();
            }
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
    public void sendNotification() {
        if (this.getAlertDetected().equals(false)) {
            this.setAlertDetected(true);
            OutputConsole.log(
                    "SHP | A person was detected inside the house. Alerting authorities in "
                            + this.getAlertAuthoritiesTime().getSeconds() + " seconds"
            );
            Timer timer = new Timer();
            timer.schedule(new AuthoritiesCallTask(this), this.getAlertAuthoritiesTime().getSeconds() * 1000);
        }
    }

}
