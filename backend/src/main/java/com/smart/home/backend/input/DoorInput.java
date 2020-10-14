package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.DoorState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoorInput {
    
    @JsonProperty("direction")
    private Direction direction;

    @JsonProperty("state")
    private DoorState state;
}
