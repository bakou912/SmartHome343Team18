package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneRoomInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RemoveRoomFromZoneCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneRoomInput, Integer> {

    public RemoveRoomFromZoneCommand() {
        super("Removing room from zone", true);
    }

    @Override
    public ResponseEntity<Integer> execute(HeatingModel heatingModel, HeatingZoneRoomInput heatingZoneRoomInput) {
        heatingModel.removeRoomFromZone(heatingZoneRoomInput.getZoneId(), new LocationPosition(heatingZoneRoomInput.getRowId(), heatingZoneRoomInput.getRoomId()) );
        this.logAction("Removing room from zone.");
        return new ResponseEntity<>(heatingZoneRoomInput.getRoomId(), HttpStatus.OK);
    }

    
    
}
