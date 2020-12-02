package com.smart.home.backend.model.simulationparameters.module.command.shh;

import com.smart.home.backend.input.HeatingZoneRoomInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AddRoomToZoneCommand extends SHHAbstractCommand<HeatingModel, HeatingZoneRoomInput, Integer>{

    public AddRoomToZoneCommand() {
        super("adding room to zone", true);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public ResponseEntity<Integer> execute(HeatingModel heatingModel, HeatingZoneRoomInput heatingZoneRoomInput) {
        heatingModel.addRoomToZone(heatingZoneRoomInput.getZoneId(), new LocationPosition(heatingZoneRoomInput.getRowId(), heatingZoneRoomInput.getRoomId()) );
        this.logAction("Adding room to zone.");
        return new ResponseEntity<>(heatingZoneRoomInput.getRoomId(), HttpStatus.OK);
    }
}
