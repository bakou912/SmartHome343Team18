package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Input class for the simulation parameters only
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParametersInput {
    
    @JsonProperty("outsideTemp")
    private Double outsideTemp;

    @JsonProperty("insideTemp")
    private Double insideTemp;

    @JsonProperty("dateTime")
    private LocalDateTime date;

}
