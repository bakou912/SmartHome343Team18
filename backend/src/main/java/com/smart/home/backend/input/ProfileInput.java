package com.smart.home.backend.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.Role;
import lombok.Getter;
import lombok.Setter;

/**
 * Input class for the profile parameters
 */
@Getter
@Setter
public class ProfileInput {
    @JsonProperty("role")
    private Role role;
}
