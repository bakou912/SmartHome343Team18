package com.smart.home.backend.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 Role for Profile
 */
@Getter
public enum Role{
    PARENT("PARENT"),
    CHILD("CHILD"),
    VISITOR("VISITOR"),
    STRANGER("STRANGER");
    private final String role;

    Role(String role){this.role=role;}

    /**
     * Retrieves the enum object paired to a value.
     * @param roleValue direction value
     * @return Retrieved enum object
     */
    public static Role get(String roleValue) {
        return Arrays.stream(Role.values()).filter(d -> d.getRole().equals(roleValue)).findFirst().get();
    }
}