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
    private double outsideTemp;
    private double insideTemp;
    private LocalDateTime date;

    /**
     * 3-parameter constructor.
     * @param outsideTemp outside temperature
     * @param insideTemp inside temperature
     * @param date Date and time
     */
    public SystemParameters(double outsideTemp, double insideTemp, LocalDateTime date) {
        this.outsideTemp = outsideTemp;
        this.insideTemp = insideTemp;
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
                parameters.getDate()
        );
    }
}
