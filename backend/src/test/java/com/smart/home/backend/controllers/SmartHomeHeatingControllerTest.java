package com.smart.home.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import com.smart.home.backend.constant.HeatingZonePeriod;
import com.smart.home.backend.controller.SmartHomeHeatingController;
import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.input.HeatingZoneRoomInput;
import com.smart.home.backend.input.HeatingZoneRoomTemperatureInput;
import com.smart.home.backend.input.HeatingZoneTemperatureInput;
import com.smart.home.backend.input.HouseLayoutInput;
import com.smart.home.backend.input.RoomInput;
import com.smart.home.backend.input.RoomRowInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.RoomRow;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.service.mapper.RoomsMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.ResponseEntity;
import com.smart.home.backend.constant.Direction;

public class SmartHomeHeatingControllerTest {
    
    SmartHomeHeatingController smartHomeHeatingController;    
    
    @BeforeEach
    public void beforeEach(){
        HeatingModel heatingModel = new HeatingModel(new HouseLayoutModel());

        smartHomeHeatingController = new SmartHomeHeatingController(heatingModel);
    }

    /**
     * Tests setting zones
     */
    @Test
    public void setZones(){
        HeatingZoneInput heatingZoneInput = instantiateHeatingZoneInput();

        ResponseEntity<Boolean> heatingZoneAdded = smartHomeHeatingController.addHeatingZone(heatingZoneInput);
        assertEquals(true, heatingZoneAdded.getBody());
    }

    /**
     * Tests setting desired temperature for zone
     */
    @Test
    public void setDesiredTemperatureForZone(){
        HeatingZoneTemperatureInput heatingZoneTemperatureInput = instantiateHeatingZoneTemperatureInput();

        ResponseEntity<Double> heatingZoneAdded = smartHomeHeatingController
                .setZoneTemperature(heatingZoneTemperatureInput);

        assertEquals(50.00, heatingZoneAdded.getBody());
    }

    @Test
    public void readRoomTemperature(){
        
        initiateHouseLayout();

        smartHomeHeatingController.addHeatingZone(instantiateHeatingZoneInput());

        assertEquals(0.0, this.smartHomeHeatingController.readRoomTemperature(instantiateHeatingZoneRoomInput()).getBody()); 
    }

    @Test
    public void setRoomTemperature(){
        
        initiateHouseLayout();

        smartHomeHeatingController.addHeatingZone(instantiateHeatingZoneInput());
        smartHomeHeatingController.overrideRoomTemperature(initializeHeatingZoneRoomTemperature());

        assertEquals(20.0, this.smartHomeHeatingController.readRoomTemperature(instantiateHeatingZoneRoomInput()).getBody()); 
    }

    /**
     * Helper method to instantiate locationPositions
     * @return list of locationPosition
     */
    public HeatingZoneInput instantiateHeatingZoneInput(){
        HeatingZoneInput heatingZoneInput = new HeatingZoneInput();
        heatingZoneInput.setName("A");
        List<LocationPosition> locationPositions = new ArrayList<LocationPosition>();
        locationPositions.add(new LocationPosition(0,0));
        locationPositions.add(new LocationPosition(0,1));
        locationPositions.add(new LocationPosition(0,2));
        heatingZoneInput.setRoomLocations(locationPositions);

        return heatingZoneInput;
    }

    /**
     * Helper method to instantiate heating zone temperature input
     * @return heatingZoneTemperatureInput
     */
    public HeatingZoneTemperatureInput instantiateHeatingZoneTemperatureInput(){
        HeatingZoneTemperatureInput heatingZoneTemperatureInput = new HeatingZoneTemperatureInput();
        heatingZoneTemperatureInput.setZoneId(0);
        heatingZoneTemperatureInput.setTargetTemperature(50.00);
        heatingZoneTemperatureInput.setHeatingZonePeriod(HeatingZonePeriod.AFTERNOON);
        
        return heatingZoneTemperatureInput;
    }

    /**
     * Helper method to instantiate a room in a heating zone
     * @return heatingZoneRoomInput
     */
    public HeatingZoneRoomInput instantiateHeatingZoneRoomInput(){
        HeatingZoneRoomInput heatingZoneRoomInput = new HeatingZoneRoomInput();
        heatingZoneRoomInput.setRoomId(0);
        heatingZoneRoomInput.setRowId(0);
        heatingZoneRoomInput.setZoneId(0);

        return heatingZoneRoomInput;
    }

    /**
     * Helper method to initate houseLayout
     */
    public void initiateHouseLayout(){
        List<RoomRowInput> roomRowInputs = createValidLayout().getRows();
		List<RoomRow> roomRows = new ArrayList<>();

        
        for (int i = 0; i < roomRowInputs.size(); i++) {
            RoomRowInput roomRowInput = roomRowInputs.get(i);
			RoomRow roomRow = RoomRow.builder()
            .id(i)
            .rooms(RoomsMapper.map(roomRowInput.getRooms()))
            .build();
			
			roomRow.getRoomId().setLastId(roomRow.getRooms().size());
			
			roomRows.add(roomRow);
		}
        
        this.smartHomeHeatingController.getHeatingModel().getHouseLayoutModel().setRows(roomRows);
    }


    /**
     * Helper method to create valid house layout
     * @return A valid House Layout
     */
    public HouseLayoutInput createValidLayout(){
        HouseLayoutInput houseLayoutInput = new HouseLayoutInput();
        List<RoomRowInput> roomRowInputs = new ArrayList<>();
        RoomRowInput roomRowInput = new RoomRowInput();
        roomRowInput.setRooms(new ArrayList<>());

        String[] roomNames = {"Kitchen", "Living Room", "Garage"};

        for(String room : roomNames){
            RoomInput RoomInput = new RoomInput();
            RoomInput.setName(room);
            
            List<Direction> doorDirections = new ArrayList<>();
            doorDirections.add(Direction.NORTH);
            doorDirections.add(Direction.EAST);
            RoomInput.setDoorsOn(doorDirections);
            
            List<Direction> windowDirections = new ArrayList<>();
            windowDirections.add(Direction.WEST);
            windowDirections.add(Direction.SOUTH);
            RoomInput.setWindowsOn(windowDirections);
    
            roomRowInput.getRooms().add(RoomInput);
        }
        
        roomRowInputs.add(roomRowInput);
        roomRowInputs.add(roomRowInput);
    
        houseLayoutInput.setRows(roomRowInputs);
    
        return houseLayoutInput;
    }

    public HeatingZoneRoomTemperatureInput initializeHeatingZoneRoomTemperature(){
        HeatingZoneRoomTemperatureInput heatingZoneRoomTemperature = new HeatingZoneRoomTemperatureInput();
        heatingZoneRoomTemperature.setRoomId(0);
        heatingZoneRoomTemperature.setZoneId(0);
        heatingZoneRoomTemperature.setOverrideTemperature(20.00);
        return heatingZoneRoomTemperature;
    }

    
}

