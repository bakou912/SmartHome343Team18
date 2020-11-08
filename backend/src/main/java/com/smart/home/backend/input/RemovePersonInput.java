package com.smart.home.backend.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RemovePersonInput {
    
    private Integer id;
    private Boolean outside;
}
