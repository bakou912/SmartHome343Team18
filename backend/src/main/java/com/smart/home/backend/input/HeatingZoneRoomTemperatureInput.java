package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import lombok.Getter;
import lombok.Setter;

/**
 * room input for overriding temperature
 */
@Getter
@Setter
public class HeatingZoneRoomTemperatureInput {
    
    @JsonIgnore
    private LocationPosition locationPosition;

    @JsonProperty("temperature")
    private Double overrideTemperature;
}
