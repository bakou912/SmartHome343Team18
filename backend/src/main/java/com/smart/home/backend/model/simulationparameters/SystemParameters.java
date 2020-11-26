package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.input.ParametersInput;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Timer;

/**
 * System Parameters Model
 */
@Getter
@Setter
public class SystemParameters {
    
    private Double outsideTemp;
    private Double insideTemp;
    private Integer timeSpeed;
    private LocalDateTime date;
    private long delay;
    
    private final Timer timer;
    private final DateIncrementTask dateIncrementTask;
    
    /**
     * 4-parameter constructor.
     * @param outsideTemp outside temperature
     * @param insideTemp inside temperature
     * @param timeSpeed time speed
     * @param date Date and time
     */
    public SystemParameters(Double outsideTemp, Double insideTemp, Integer timeSpeed, LocalDateTime date) {
        this.outsideTemp = outsideTemp;
        this.insideTemp = insideTemp;
        this.timeSpeed = timeSpeed;
        this.date = date;
        this.delay = 1000L / timeSpeed;
        this.timer = new Timer();
        this.dateIncrementTask = new DateIncrementTask(this.getDate());
        this.timer.scheduleAtFixedRate(this.dateIncrementTask, this.delay, this.delay);
    }
    
    /**
     * 1-parameter constructor.
     * @param parameters parameters input
     */
    public SystemParameters(ParametersInput parameters) {
        this(
                parameters.getOutsideTemp(),
                parameters.getInsideTemp(),
                parameters.getTimeSpeed(),
                parameters.getDate()
        );
    }
    
    /**
     * Accessor for timeSpeed
     * @param timeSpeed new time speed
     */
    public void setTimeSpeed(Integer timeSpeed) {
        this.timeSpeed = timeSpeed;
        this.setDelay(1000L / timeSpeed);
        this.resetTimer();
    }
    
    /**
     * Resets the timer by cancelling the schedule and starting a new one.
     */
    private void resetTimer() {
        this.getTimer().cancel();
        this.getTimer().scheduleAtFixedRate(this.dateIncrementTask, this.getDelay(), this.getDelay());
    }
    
}
