package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Input for a room in a heating zone
 */
@Getter
@Setter
public class HeatingZoneRoomInput {
    
    @JsonProperty("rowId")
    private Integer rowId;

    @JsonProperty("roomId")
    private Integer roomId;

    @JsonProperty("zoneId")
    private Integer zoneId;

}
