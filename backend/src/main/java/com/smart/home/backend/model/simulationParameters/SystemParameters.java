package com.smart.home.backend.model.simulationParameters;

import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.ParametersInput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

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
