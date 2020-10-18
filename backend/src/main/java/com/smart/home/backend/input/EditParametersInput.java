package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Input class for all the simulation parameters
 */
@Getter
@Setter
public class EditParametersInput {
    @JsonProperty("profileInput")
    private ProfileInput profileInput;

    @JsonProperty("parametersInput")
    private ParametersInput parametersInput;
}
