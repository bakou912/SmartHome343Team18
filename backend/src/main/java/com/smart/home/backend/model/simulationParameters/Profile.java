package com.smart.home.backend.model.simulationParameters;

import com.smart.home.backend.model.ModelObject;
import com.smart.home.backend.constant.Role;
import lombok.experimental.SuperBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuperBuilder
public class Profile extends ModelObject{
//    private String name;
//    private String username;
//    private String password;
    private Role role;
    private String location;
}