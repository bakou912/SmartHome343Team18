package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneRoomInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Adding room to a zone command
 */
public class AddRoomToZoneCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneRoomInput, Room>{

    public AddRoomToZoneCommand() {
        super("Adding room to zone", true);
    }
    
    @Override
    public ResponseEntity<Room> execute(HeatingModel heatingModel, HeatingZoneRoomInput heatingZoneRoomInput) {
        Room foundRoom = heatingModel.addRoomToZone(heatingZoneRoomInput.getZoneId(), new LocationPosition(heatingZoneRoomInput.getRowId(), heatingZoneRoomInput.getRoomId()) );
        if(foundRoom!= null){
            this.logAction("Added room to zone: " + foundRoom.getName());
        }else{
            this.logAction("Room not found");
        }
        return new ResponseEntity<>(foundRoom, HttpStatus.OK);
    }
}
