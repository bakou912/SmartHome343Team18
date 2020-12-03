package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * room input for overriding temperature
 */
@Getter
@Setter
public class HeatingZoneRoomTemperatureInput {
    @JsonProperty("zoneId")
    private Integer zoneId;

    @JsonProperty("roomId")
    private Integer roomId;

    @JsonProperty("heatingZonePeriod")
    private Double overrideTemperature;
}
