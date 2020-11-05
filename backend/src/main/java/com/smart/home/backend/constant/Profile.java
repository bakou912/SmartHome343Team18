package com.smart.home.backend.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * Profile enum for User
 */
@Getter
public enum Profile {
    PARENT("PARENT"),
    CHILD("CHILD"),
    VISITOR("VISITOR"),
    STRANGER("STRANGER");
    
    private final String text;

    Profile(String text){
        this.text = text;
    }

    /**
     * Retrieves the enum object paired to a value.
     * @param profileValue profile value
     * @return Retrieved enum object
     */
    public static Profile get(String profileValue) {
        return Arrays.stream(Profile.values()).filter(d -> d.getText().equals(profileValue)).findFirst().orElse(null);
    }
}
