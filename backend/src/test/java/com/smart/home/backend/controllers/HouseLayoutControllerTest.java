package com.smart.home.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.DoorState;
import com.smart.home.backend.constant.LightState;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.controller.HouseLayoutController;
import com.smart.home.backend.input.*;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Light;
import com.smart.home.backend.model.houselayout.directional.Door;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.model.simulationparameters.location.RoomItemLocationPosition;
import com.smart.home.backend.model.smarthomesecurity.SecurityModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class HouseLayoutControllerTest {
    
    HouseLayoutController houseLayoutController;
    
    /**
     * Tests for Use Case 3.1.1: Upload house layout
     */
    @Nested
    @DisplayName("Modify house layout context")
    class UploadHouseLayout {
    
        @BeforeEach
        void beforeEach() {
            houseLayoutController = new HouseLayoutController(new HouseLayoutModel(new SecurityModel()));
        }
    
        /**
         * Test for the house layout loading.
         */
        @Test
        void loadLayout() {
            ResponseEntity<HouseLayoutModel> loadResponse = houseLayoutController.loadLayout(createValidLayout());
            assertNotNull(loadResponse.getBody());
            assertEquals(2, loadResponse.getBody().getRows().size());
            assertEquals(2, loadResponse.getBody().getRows().get(0).getRooms().get(0).getDoors().size());
            assertEquals(2, loadResponse.getBody().getRows().get(0).getRooms().get(0).getWindows().size());
        }
    
        /**
         * Test for retrieving the house layout.
         */
        @Test
        void getLayout() {
            houseLayoutController.loadLayout(createValidLayout());
            ResponseEntity<HouseLayoutModel> getResponse = houseLayoutController.getLayout();
            assertNotNull(getResponse.getBody());
            assertEquals(2, getResponse.getBody().getRows().size());
            assertEquals(2, getResponse.getBody().getRows().get(0).getRooms().get(0).getDoors().size());
            assertEquals(2, getResponse.getBody().getRows().get(0).getRooms().get(0).getWindows().size());
        }
        
    }
    
    /**
     * Tests for Use Case 3.4.1: Using core functionality
     */
    @Nested
    @DisplayName("Modify house layout items")
    class ModifyHouseLayoutItems {
        
        @BeforeEach
        void beforeEach() {
            houseLayoutController = new HouseLayoutController(new HouseLayoutModel(new SecurityModel()));
            houseLayoutController.loadLayout(createValidLayout());
        }
    
        /**
         * Test for the modification of a light.
         */
        @Test
        void modifyLight() {
            Light modifiedLight;
            LocationPosition locationPosition;
            
            // Verifying light state change
            RoomLightInput changeStateInput = new RoomLightInput();
            changeStateInput.setState(LightState.ON);
            locationPosition = new LocationPosition(0,0);
            modifiedLight = houseLayoutController.modifyRoomLight(locationPosition, changeStateInput).getBody();
            assert modifiedLight != null;
            assertEquals(LightState.ON, modifiedLight.getState());
            assertEquals(LightState.ON, houseLayoutController.getHouseLayoutModel().findRoom(locationPosition).getLight().getState());
    
            // Verifying light auto mode change
            RoomLightInput changeAutoModeInput = new RoomLightInput();
            changeAutoModeInput.setAutoMode(true);
            locationPosition = new LocationPosition(0,1);
            modifiedLight = houseLayoutController.modifyRoomLight(locationPosition, changeAutoModeInput).getBody();
            assert modifiedLight != null;
            assertEquals(true, modifiedLight.getAutoMode());
            assertEquals(true, houseLayoutController.getHouseLayoutModel().findRoom(locationPosition).getLight().getAutoMode());
    
            // Verifying light away mode change
            RoomLightInput changeAwayModeInput = new RoomLightInput();
            changeAwayModeInput.setAwayMode(true);
            locationPosition = new LocationPosition(0,2);
            modifiedLight = houseLayoutController.modifyRoomLight(locationPosition, changeAwayModeInput).getBody();
            assert modifiedLight != null;
            assertEquals(true, modifiedLight.getAwayMode());
            assertEquals(true, houseLayoutController.getHouseLayoutModel().findRoom(locationPosition).getLight().getAwayMode());
        }
    
        /**
         * Test for the modification of a door's state.
         */
        @Test
        void modifyDoorState() {
            RoomItemLocationPosition locationPosition = new RoomItemLocationPosition(0,0,0);
            DoorInput doorInput = new DoorInput();
            doorInput.setState(DoorState.LOCKED);
            Door modifiedDoor = houseLayoutController.changeDoorState(locationPosition, doorInput).getBody();
            assert modifiedDoor != null;
            assertEquals(DoorState.LOCKED, modifiedDoor.getState());
            assertEquals(DoorState.LOCKED, houseLayoutController.getHouseLayoutModel().findDoor(locationPosition).getState());
        }
    
        /**
         * Test for the modification of a window's state.
         */
        @Test
        void modifyWindowState() {
            RoomItemLocationPosition locationPosition = new RoomItemLocationPosition(0,1,0);
            WindowInput windowInput = new WindowInput();
            windowInput.setState(WindowState.OPEN);
            Window modifiedWindow = houseLayoutController.changeWindowState(locationPosition, windowInput).getBody();
            assert modifiedWindow != null;
            assertEquals(WindowState.OPEN, modifiedWindow.getState());
            assertEquals(WindowState.OPEN, houseLayoutController.getHouseLayoutModel().findWindow(locationPosition).getState());
        }
        
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

}
