package com.smart.home.backend.model.simulationparameters.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for a Location.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    
    private Integer rowId;
    private Integer roomId;
    private Boolean outside;
    
}
