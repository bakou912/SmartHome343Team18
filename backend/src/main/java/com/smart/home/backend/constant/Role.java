package com.smart.home.backend.constant;

import lombok.Getter;

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
}