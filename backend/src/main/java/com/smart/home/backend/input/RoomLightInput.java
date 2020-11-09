package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.model.simulationparameters.location.RoomItemLocationPosition;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoomLightInput extends LightInput {
    
    @JsonProperty("location")
    private RoomItemLocationPosition location;
    
}
