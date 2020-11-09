package com.smart.home.backend.model.smarthomesecurity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.model.houselayout.Light;
import com.smart.home.backend.service.OutputConsole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AwayModeHours awayModeHours;
    private Time currentTime;
    private List<Light> awayModeLights;
    
    /**
     * 1-parameter constructor.
     */
    @Autowired
    public SecurityModel() {
        awayModeHours = new AwayModeHours();
        this.awayMode = false;
        this.alertDetected = false;
        this.nbPersonsInside = 0;
        this.alertAuthoritiesTime = Duration.ofSeconds(10);
        this.currentTime = Time.valueOf("12:00:00");
        this.awayModeLights = new ArrayList<>();
    }
    
    /**
     * Handling property changes from observed objects.
     * @param evt property change event
     */
    @SneakyThrows
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "date":
                this.setCurrentTime(Time.valueOf(((LocalDateTime) evt.getNewValue()).toLocalTime()));
                break;
            case "nbPersonsInside":
                this.setNbPersonsInside((Integer) evt.getNewValue());
    
                if (this.awayMode.equals(true)) {
                    this.sendNotification();
                }
                break;
            case "lightAwayMode":
                Light newValue = (Light) evt.getNewValue();
                Light oldValue = (Light) evt.getOldValue();
                
                if (newValue == null) {
                    this.getAwayModeLights().remove(oldValue);
                } else {
                    this.getAwayModeLights().add(newValue);
                }
                break;
            default:
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
        
        if (this.getAwayMode().equals(true)) {
            LightState lightState = this.isLightOnTime() ? LightState.ON : LightState.OFF;
    
            for (Light light: this.getAwayModeLights()) {
                light.setState(lightState);
            }
    
            new AwayModeNotifier().notifyAwayModeOn();
        }
        
        return true;
    }
    
    /**
     * Checking that the current time is in the lights on hours.
     * @return Wether lights should be on or not.
     */
    private boolean isLightOnTime() {
        return this.getCurrentTime().compareTo(this.getAwayModeHours().getFrom()) >= 0 && this.getCurrentTime().compareTo(this.getAwayModeHours().getTo()) < 0;
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
