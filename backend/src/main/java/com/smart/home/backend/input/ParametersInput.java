package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.tools.jlink.internal.plugins.StripNativeCommandsPlugin;
import lombok.Getter;
import lombok.Setter;

/**
 * Input class for the simulation parameters only
 */
@Getter
@Setter
public class ParametersInput {
    @JsonProperty("outside")
    private Double outsideTemp;

    @JsonProperty("insideTemp")
    private Double insideTemp;

    @JsonProperty("location")
    private String location;

    @JsonProperty("day")
    private String day;

    @JsonProperty("month")
    private String month;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("hour")
    private Integer hour;

    @JsonProperty("minutes")
    private Integer minutes;

}
