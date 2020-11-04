package com.smart.home.backend.model.simulationparameters.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for a room item's Location.
 */
@Getter
@Setter
@NoArgsConstructor
public class RoomItemLocation extends Location {
    
    private Integer itemId;
    
    public RoomItemLocation(Integer rowId, Integer roomId, Integer itemId) {
        super(rowId, roomId);
        this.itemId = itemId;
    }
}
