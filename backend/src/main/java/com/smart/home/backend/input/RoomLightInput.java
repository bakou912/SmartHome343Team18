package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import lombok.Getter;
import lombok.Setter;


/**
 * Input class for a room's light.
 */
@Getter
@Setter
public class RoomLightInput extends LightInput {
    
    @JsonProperty("location")
    private LocationPosition location;
    
}
