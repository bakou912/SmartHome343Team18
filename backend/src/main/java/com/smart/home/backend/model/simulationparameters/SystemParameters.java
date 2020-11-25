package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.input.ParametersInput;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
}
