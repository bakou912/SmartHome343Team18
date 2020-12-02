package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.HeatingZonePeriod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeatingZoneTemperatureInput {
    
    @JsonProperty("zoneId")
    private Integer zoneId;

    @JsonProperty("targetTemperature")
    private Double targetTemperature;

    @JsonProperty("heatingZonePeriod")
    private HeatingZonePeriod heatingZonePeriod;
}
