package com.smart.home.backend.model.simulationparameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.home.backend.input.ParametersInput;
import com.smart.home.backend.model.AbstractNotifier;
import com.smart.home.backend.model.heating.SeasonDates;
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
public class SystemParameters extends AbstractNotifier {
    
    private Double outsideTemp;
    private Double insideTemp;
    private Integer timeSpeed;
    private LocalDateTime date;
    private SeasonDates seasonDates;
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
     * Modify parameters.
     * @param parametersInput parameters input
     */
    public void modifyParameters(ParametersInput parametersInput) {
    
        if (parametersInput.getInsideTemp() != null) {
            this.setInsideTemp(parametersInput.getInsideTemp());
        }
    
        if (parametersInput.getOutsideTemp() != null) {
            this.support.firePropertyChange("outsideTemp", this.getOutsideTemp(), parametersInput.getOutsideTemp());
            this.setOutsideTemp(parametersInput.getOutsideTemp());
        }
    
        if (parametersInput.getDate() != null) {
            this.support.firePropertyChange("date", this.getDate(), parametersInput.getDate());
            this.setDate(parametersInput.getDate());
        }
    
        if (parametersInput.getSeasonDates() != null) {
            this.support.firePropertyChange("seasonDates", this.getSeasonDates(), parametersInput.getSeasonDates());
            this.setSeasonDates(parametersInput.getSeasonDates());
        }
    
        if (parametersInput.getTimeSpeed() != null) {
            this.support.firePropertyChange("timeSpeed", this.getTimeSpeed(), parametersInput.getTimeSpeed());
            this.setTimeSpeed(parametersInput.getTimeSpeed());
        }

        this.setDelay(1000L / this.getTimeSpeed());
        this.resetTimer();
    }
    
    /**
     * Accessor for timeSpeed
     * @param timeSpeed new time speed
     */
    public void setTimeSpeed(Integer timeSpeed) {
        this.timeSpeed = timeSpeed;
        this.setDelay(1000L / timeSpeed);
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
