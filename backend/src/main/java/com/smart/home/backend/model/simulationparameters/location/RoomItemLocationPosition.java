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
public class RoomItemLocationPosition extends LocationPosition {
    
    private Integer itemId;
    
    public RoomItemLocationPosition(Integer rowId, Integer roomId, Integer itemId) {
        super(rowId, roomId, false);
        this.itemId = itemId;
    }
}
