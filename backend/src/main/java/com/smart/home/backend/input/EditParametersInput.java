package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Input class for all the simulation parameters
 */
@Getter
@Setter
public class EditParametersInput {
    @JsonProperty("profileInputs")
    private ProfileInput profileInputs;

    @JsonProperty("parametersInput")
    private ParametersInput parametersInput;
}
