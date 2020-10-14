package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Input class for the simulation parameters only
 */
@Getter
@Setter
public class ParametersInput {
    @JsonProperty("outside")
    private String outsideTemp;

    @JsonProperty("insideTemp")
    private String insideTemp;

    @JsonProperty("location")
    private String location;

    @JsonProperty("date")
    private String date;
}
