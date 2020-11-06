package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OutsideLightInput extends LightInput {
    
    @JsonProperty("location")
    private String location;
    
}
