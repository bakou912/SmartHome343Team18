package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Input for heating zones.
 */
@Getter
@Setter
public class HeatingZoneInput {

    @JsonProperty("name")
    private String name;
    
    @JsonProperty("roomLocations")
    private List<LocationPosition> roomLocations;
    
}
