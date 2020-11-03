package com.smart.home.backend.model.smarthomesecurity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.*;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.smart.home.backend.model.houselayout.Room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmartHomeSecurity implements PropertyChangeListener {

    private Boolean awayMode;
    private Boolean personDetected;
    private Duration alertAuthoritiesTime;


    public SmartHomeSecurity() {
		this.awayMode = false;
		this.personDetected = false;
	}    

    /**
     * runs when one of the 3 properties have been updated in the SimulationContext's support object.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if(evt.getPropertyName().equals("awayMode")){
            this.setAwayMode((Boolean) evt.getNewValue());
            if (this.getAwayMode() == true){
                System.out.println("telling SHC to close all windows and lock all doors");
                //TODO : call SHC to close all windows and lock all doors
            }
        }else if(evt.getPropertyName().equals("personDetected")){
            if(evt.getNewValue().equals(true)){
                this.sendNotification();
            }
        }else if(evt.getPropertyName().equals("alertAuthoritiesTime")){
            this.setAlertAuthoritiesTime((Duration) evt.getNewValue());
        }

    }


    /**
     * set specific lights on
     * @param state
     */
    public void setLightsDuringAway(Map<Room, Integer> lights){
        //TODO : tell SHC module to turn on specific lights in specific room using lightId

    }

    /**
     * send notification of a person detected to the console and to authorities.
     */
    public void sendNotification(){

        if(this.getAwayMode() == true){
            //TODO : send notification to console module
            System.out.println("A Person was detected in the house while on away mode!");
            System.out.println("Sending notification to output console.");

            
            if(this.getAlertAuthoritiesTime() != null){
                System.out.println("Alerting authorities in: "  + this.getAlertAuthoritiesTime().toString());
                Timer timer = new Timer();
                timer.schedule(new TimerTask(){
                    @Override
                    public void run(){
                        System.out.println("Alerting authorities"); 
                        //TODO: send police notification to console module

                    }
                }, this.getAlertAuthoritiesTime().getSeconds());
            }
        }
    }
    
   


}
