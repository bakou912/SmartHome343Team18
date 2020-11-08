package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.LightState;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LightInput {
    
    @JsonProperty("state")
    private LightState state;
    
    @JsonProperty("autoMode")
    private Boolean autoMode;
    
}
