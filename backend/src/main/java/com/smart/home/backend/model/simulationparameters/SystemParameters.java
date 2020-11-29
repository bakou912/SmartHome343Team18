package com.smart.home.backend.model.simulationparameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.home.backend.input.ParametersInput;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * System Parameters Model
 */
@Getter
@Setter
@Component
public class SystemParameters {
    
    private Double outsideTemp;
    private Double insideTemp;
    private Integer timeSpeed;
    private LocalDateTime date;
    private long delay;
    private boolean incrementing;
    
    @JsonIgnore
    private ScheduledExecutorService executorService;
    @JsonIgnore
    private ScheduledFuture<?> scheduledFuture;
    @JsonIgnore
    private final DateIncrementTask dateIncrementTask;
    
    /**
     * 1-parameter constructor.
     * @param dateIncrementTask date increment task
     */
    public SystemParameters(DateIncrementTask dateIncrementTask) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.dateIncrementTask = dateIncrementTask;
        this.dateIncrementTask.setSystemParameters(this);
        this.incrementing = false;
    }
    
    /**
     * 1-parameter constructor.
     * @param parameters parameters input
     */
    public void modifyParameters(ParametersInput parameters) {
        this.setOutsideTemp(parameters.getOutsideTemp());
        this.setInsideTemp(parameters.getInsideTemp());
        this.setTimeSpeed(parameters.getTimeSpeed());
        this.setDate(parameters.getDate());
        this.setDelay(1000L / this.getTimeSpeed());
        this.scheduledFuture = this.executorService.scheduleAtFixedRate(this.getDateIncrementTask(), this.getDelay(), this.getDelay(),TimeUnit.MILLISECONDS);
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
     * Resets the timer by cancelling the scheduled task and starting a new one.
     */
    private void resetTimer() {
        if (!this.getExecutorService().isShutdown() && this.getScheduledFuture() != null) {
            this.getScheduledFuture().cancel(true);
        }
        this.setScheduledFuture(this.getExecutorService().scheduleAtFixedRate(this.dateIncrementTask, this.delay, this.delay,TimeUnit.MILLISECONDS));
    }
    
}
