package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneRoomInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.heating.HeatingZone;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Room removal from a heating zone command.
 */
public class RemoveRoomFromZoneCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneRoomInput, Integer> {

    public RemoveRoomFromZoneCommand() {
        super("Removing room from zone", true);
    }

    @Override
    public ResponseEntity<Integer> execute(HeatingModel heatingModel, HeatingZoneRoomInput heatingZoneRoomInput) {
        HeatingZone zone = heatingModel.findZone(heatingZoneRoomInput.getZoneId());
        Room removedRoom = heatingModel.removeRoomFromZone(heatingZoneRoomInput.getZoneId(), new LocationPosition(heatingZoneRoomInput.getRowId(), heatingZoneRoomInput.getRoomId()) );
        this.logAction("Removed " + removedRoom.getName() + " from zone " + zone.getName());
        return new ResponseEntity<>(heatingZoneRoomInput.getRoomId(), HttpStatus.OK);
    }
    
}
