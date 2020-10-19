package com.smart.home.backend.model.simulationparameters;

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
    private LocalDateTime date;

    /**
     * Constructor
     * @param outsideTemp temperature
     * @param insideTemp
     * @param date date and time
     */
    public SystemParameters(Double outsideTemp, Double insideTemp, LocalDateTime date) {
        this.outsideTemp = outsideTemp;
        this.insideTemp = insideTemp;
        this.date = date;
    }
}
