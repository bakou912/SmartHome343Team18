package com.smart.home.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import com.smart.home.backend.constant.HeatingZonePeriod;
import com.smart.home.backend.constant.RoomHeatingMode;
import com.smart.home.backend.input.*;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.heating.HeatingZone;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.houselayout.RoomRow;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.service.mapper.RoomsMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.smart.home.backend.constant.Direction;

/**
 * Tests for
 * Use Case 3.6.1: Manage zones
 * Use Case 3.6.2: Control room temperatures
 */
class HeatingControllerTest {
    
    HeatingController heatingController;
    
    @BeforeEach
    public void beforeEach() {
        HeatingModel heatingModel = new HeatingModel(new HouseLayoutModel());
        heatingController = new HeatingController(heatingModel);
        initiateHouseLayout();
    }

    /**
     * Tests setting zones
     */
    @Test
    void addZone() {
        HeatingZoneInput heatingZoneInput = instantiateHeatingZoneInput();

        HeatingZone heatingZoneAdded = heatingController.addHeatingZone(heatingZoneInput).getBody();
        assertEquals(heatingZoneInput.getName(), heatingZoneAdded.getName());
    }
    
    /**
     * Tests removing a zone
     */
    @Test
    void removeZone() {
        HeatingZoneInput heatingZoneInput = instantiateHeatingZoneInput();
        
        HeatingZone heatingZoneAdded = heatingController.addHeatingZone(heatingZoneInput).getBody();
        Integer zoneIdRemoved = heatingController.removeHeatingZone(heatingZoneAdded.getId()).getBody();
        assertEquals(zoneIdRemoved, heatingZoneAdded.getId());
    }

    /**
     * Tests setting desired temperature for zone
     */
    @Test
    void setTargetTemperatureForZone() {
        HeatingZoneTemperatureInput heatingZoneTemperatureInput = instantiateHeatingZoneTemperatureInput();

        ResponseEntity<Double> heatingZoneAdded = heatingController.setZoneTemperature(
                0, HeatingZonePeriod.AFTERNOON, heatingZoneTemperatureInput
        );

        assertEquals(50.00, heatingZoneAdded.getBody());
    }
    
    /**
     * Tests setting desired temperatures for away mode in the winter and summer.
     */
    @Test
    void setSeasonTemperatures() {
        TemperatureInput temperatureInput = new TemperatureInput();
        temperatureInput.setTemperature(25.0);
        
        Double modifiedTemp = heatingController.setWinterTemperature(temperatureInput).getBody();
        assertEquals(25.0, modifiedTemp);
    
        temperatureInput.setTemperature(27.0);
        modifiedTemp = heatingController.setSummerTemperature(temperatureInput).getBody();
        assertEquals(27.0, modifiedTemp);
    }
    
    /**
     * Tests reading a room`s temperature
     */
    @Test
    void readRoomTemperature() {
        heatingController.addHeatingZone(instantiateHeatingZoneInput());

        assertEquals(0.0, this.heatingController.readRoomTemperature(new LocationPosition(0,0)).getBody());
    }
    
    /**
     * Tests overriding a room`s temperature
     */
    @Test
    void overrideRoomTemperature() {
        LocationPosition roomPosition = new LocationPosition(0,0);

        heatingController.addHeatingZone(instantiateHeatingZoneInput());
        heatingController.overrideRoomTemperature(roomPosition, initializeHeatingZoneRoomTemperature());
        Room foundRoom = heatingController.getHeatingModel().getHouseLayoutModel().findRoom(roomPosition);
        assertEquals(20.0, this.heatingController.readRoomTemperature(new LocationPosition(0,0)).getBody());
        assertEquals(RoomHeatingMode.OVERRIDDEN, foundRoom.getHeatingMode());
    }
    
    /**
     * Tests removing the override for a room`s temperature
     */
    @Test
    void removeRoomTemperatureOverride() {
        LocationPosition roomPosition = new LocationPosition(0,0);
        
        heatingController.addHeatingZone(instantiateHeatingZoneInput());
        heatingController.overrideRoomTemperature(roomPosition, initializeHeatingZoneRoomTemperature());
        RoomHeatingMode heatingMode = heatingController.removeRoomOverride(roomPosition).getBody();
        assertEquals(RoomHeatingMode.ZONE, heatingMode);
    }
    
    /**
     * Tests adding a room that is not already in the zone.
     */
    @Test
    void addRoomToZoneValid() {
        HeatingZone zone = heatingController.addHeatingZone(instantiateHeatingZoneInput()).getBody();
        HeatingZoneRoomInput input = new HeatingZoneRoomInput();
        input.setRowId(0);
        input.setRoomId(0);
        Room addedRoom = heatingController.addRoomToZone(zone.getId(), input).getBody();
        assertTrue(zone.getRooms().contains(addedRoom));
    }
    
    /**
     * Tests adding a room that is already in the zone.
     */
    @Test
    void addRoomToZoneAlreadyInZone() {
        HeatingZone zone = heatingController.addHeatingZone(instantiateHeatingZoneInput()).getBody();
        HeatingZoneRoomInput input = new HeatingZoneRoomInput();
        input.setRowId(0);
        input.setRoomId(0);
        heatingController.addRoomToZone(zone.getId(), input);
        ResponseEntity<Room> response = heatingController.addRoomToZone(zone.getId(), input);
        assertEquals(null, response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Helper method to instantiate locationPositions
     * @return list of locationPosition
     */
    private HeatingZoneInput instantiateHeatingZoneInput() {
        HeatingZoneInput heatingZoneInput = new HeatingZoneInput();
        heatingZoneInput.setName("A");
        heatingZoneInput.setRoomLocations(new ArrayList<>());

        return heatingZoneInput;
    }

    /**
     * Helper method to instantiate heating zone temperature input
     * @return heatingZoneTemperatureInput
     */
    private HeatingZoneTemperatureInput instantiateHeatingZoneTemperatureInput() {
        HeatingZoneTemperatureInput heatingZoneTemperatureInput = new HeatingZoneTemperatureInput();
        heatingZoneTemperatureInput.setTargetTemperature(50.00);
        
        return heatingZoneTemperatureInput;
    }

    /**
     * Helper method to initiate houseLayout
     */
    private void initiateHouseLayout() {
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
    private HouseLayoutInput createValidLayout() {
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
    
    private HeatingZoneRoomTemperatureInput initializeHeatingZoneRoomTemperature() {
        HeatingZoneRoomTemperatureInput heatingZoneRoomTemperature = new HeatingZoneRoomTemperatureInput();
        heatingZoneRoomTemperature.setOverrideTemperature(20.00);
        return heatingZoneRoomTemperature;
    }
    
}

