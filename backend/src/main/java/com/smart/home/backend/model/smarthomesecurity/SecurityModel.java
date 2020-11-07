package com.smart.home.backend.model.smarthomesecurity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Security module that listens to the SHS for intruders during away mode
 */
@AllArgsConstructor
@Getter
@Setter
public class SecurityModel implements PropertyChangeListener{
    
    private Boolean awayMode; 
	private Boolean personDetected;
    private Duration alertAuthoritiesTime;

    /**
     * Runs when one of the 3 properties have been updated in the SimulationContext's support object.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("awayMode")) {
            if ((Boolean) evt.getNewValue() == true) {
                System.out.println("telling SHC to close all windows and lock all doors");
                //TODO : call SHC to close all windows and lock all doors
            }
        } else if (evt.getPropertyName().equals("personDetected")) {
            if (evt.getNewValue().equals(true)) {
                this.sendNotification();
            }
        } else if (evt.getPropertyName().equals("alertAuthoritiesTime")) {
            System.out.println("Setting duration for alerting authorities");
            this.setAlertAuthoritiesTime((Duration) evt.getNewValue());
        }
    }

    /**
     * Set specific lights on
     * @param state
     */
    public void setLightsDuringAway(){
        //TODO : tell SHC module to turn on the light in specific rooms

    }

    /**
     * Send notification of a person detected to the console and to authorities.
     */
    public void sendNotification(){
        //TODO : send notification to console module
        System.out.println("A Person was detected in the house while on away mode!");
        System.out.println("Sending notification to output console.");

        if (this.getAlertAuthoritiesTime()!= null) {
            System.out.println("Alerting authorities in: " + this.getAlertAuthoritiesTime().toString());
            Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                @Override
                public void run(){
                    System.out.println("Alerting authorities"); 
                        System.out.println("Alerting authorities"); 
                    System.out.println("Alerting authorities"); 
                    //TODO: send police notification to console module

                }
            }, this.getAlertAuthoritiesTime().getSeconds());
        }
    }

}
