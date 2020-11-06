package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Input class for the simulation parameters only
 */
@Getter
@Setter
public class ParametersInput {
    
    @JsonProperty("outsideTemp")
    private Double outsideTemp;

    @JsonProperty("insideTemp")
    private Double insideTemp;

    @JsonProperty("dateTime")
    private LocalDateTime date;

}
