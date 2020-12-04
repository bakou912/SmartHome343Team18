package com.smart.home.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import com.smart.home.backend.constant.HeatingZonePeriod;
import com.smart.home.backend.controller.HeatingController;
import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.input.HeatingZoneRoomTemperatureInput;
import com.smart.home.backend.input.HeatingZoneTemperatureInput;
import com.smart.home.backend.input.HouseLayoutInput;
import com.smart.home.backend.input.RoomInput;
import com.smart.home.backend.input.RoomRowInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.heating.HeatingZone;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.RoomRow;
import com.smart.home.backend.model.simulationparameters.DateIncrementTask;
import com.smart.home.backend.model.simulationparameters.SystemParameters;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.service.mapper.RoomsMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import com.smart.home.backend.constant.Direction;

class HeatingControllerTest {
    
    HeatingController heatingController;
    
    @BeforeEach
    public void beforeEach(){
        DateIncrementTask dateIncrementTask = new DateIncrementTask();
        HeatingModel heatingModel = new HeatingModel(new HouseLayoutModel());
        SystemParameters systemParameters = new SystemParameters(dateIncrementTask);
        heatingController = new HeatingController(heatingModel,systemParameters);
    }

    /**
     * Tests setting zones
     */
    @Test
    void setZones(){
        HeatingZoneInput heatingZoneInput = instantiateHeatingZoneInput();

        ResponseEntity<HeatingZone> heatingZoneAdded = heatingController.addHeatingZone(heatingZoneInput);
        assertEquals(heatingZoneInput.getName(), heatingZoneAdded.getBody().getName());
    }

    /**
     * Tests setting desired temperature for zone
     */
    @Test
    void setDesiredTemperatureForZone(){
        HeatingZoneTemperatureInput heatingZoneTemperatureInput = instantiateHeatingZoneTemperatureInput();

        ResponseEntity<Double> heatingZoneAdded = heatingController.setZoneTemperature(
                0, HeatingZonePeriod.AFTERNOON, heatingZoneTemperatureInput
        );

        assertEquals(50.00, heatingZoneAdded.getBody());
    }

    @Test
    void readRoomTemperature(){
        
        initiateHouseLayout();

        heatingController.addHeatingZone(instantiateHeatingZoneInput());

        assertEquals(0.0, this.heatingController.readRoomTemperature(new LocationPosition(0,0)).getBody());
    }

    @Test
    void setRoomTemperature(){
        
        initiateHouseLayout();

        heatingController.addHeatingZone(instantiateHeatingZoneInput());
        heatingController.overrideRoomTemperature(new LocationPosition(0,0), initializeHeatingZoneRoomTemperature());

        assertEquals(20.0, this.heatingController.readRoomTemperature(new LocationPosition(0,0)).getBody());
    }

    /**
     * Helper method to instantiate locationPositions
     * @return list of locationPosition
     */
    private HeatingZoneInput instantiateHeatingZoneInput(){
        HeatingZoneInput heatingZoneInput = new HeatingZoneInput();
        heatingZoneInput.setName("A");
        List<LocationPosition> locationPositions = new ArrayList<>();
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
    private HeatingZoneTemperatureInput instantiateHeatingZoneTemperatureInput(){
        HeatingZoneTemperatureInput heatingZoneTemperatureInput = new HeatingZoneTemperatureInput();
        heatingZoneTemperatureInput.setTargetTemperature(50.00);
        
        return heatingZoneTemperatureInput;
    }

    /**
     * Helper method to initiate houseLayout
     */
    private void initiateHouseLayout(){
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
        
        this.heatingController.getHeatingModel().getHouseLayoutModel().setRows(roomRows);
    }


    /**
     * Helper method to create valid house layout
     * @return A valid House Layout
     */
    private HouseLayoutInput createValidLayout(){
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
    
    private HeatingZoneRoomTemperatureInput initializeHeatingZoneRoomTemperature(){
        HeatingZoneRoomTemperatureInput heatingZoneRoomTemperature = new HeatingZoneRoomTemperatureInput();
        heatingZoneRoomTemperature.setOverrideTemperature(20.00);
        return heatingZoneRoomTemperature;
    }

    
}

