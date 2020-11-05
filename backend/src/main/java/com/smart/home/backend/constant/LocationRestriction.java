package com.smart.home.backend.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * LocationRestriction enum for Permission
 */
@Getter
public enum LocationRestriction {
    ROOM("ROOM"),
    INSIDE("INSIDE"),
    OUTSIDE("OUTSIDE"),
    NONE("NONE");
    
    private final String text;

    LocationRestriction(String text){
        this.text = text;
    }

    /**
     * Retrieves the enum object paired to a value.
     * @param restrictionValue restriction value
     * @return Retrieved enum object
     */
    public static LocationRestriction get(String restrictionValue) {
        return Arrays.stream(LocationRestriction.values()).filter(d -> d.getText().equals(restrictionValue)).findFirst().orElse(null);
    }
}
