package com.smart.home.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.controller.HouseLayoutController;
import com.smart.home.backend.input.HouseLayoutInput;
import com.smart.home.backend.input.RoomInput;
import com.smart.home.backend.input.RoomRowInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.smarthomesecurity.SecurityModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

/**
 * Tests for Use Case 3.1.1: upload house layout
 */
class HouseLayoutControllerTest {
    
    HouseLayoutController houseLayoutController;
    
    @BeforeEach
    void beforeEach() {
        houseLayoutController = new HouseLayoutController(new HouseLayoutModel(new SecurityModel()));
    }

    @Test
    void loadLayout(){
        ResponseEntity<HouseLayoutModel> loadResponse = houseLayoutController.loadLayout(this.mockValidLayout());
        assertNotNull(loadResponse.getBody());
        assertEquals( 3 , loadResponse.getBody().getRows().size());
        assertEquals( 2 , loadResponse.getBody().getRows().get(0).getRooms().get(0).getDoors().size());
        assertEquals( 2 , loadResponse.getBody().getRows().get(0).getRooms().get(0).getWindows().size());
    }

    @Test
    void getLayout(){
        houseLayoutController.loadLayout(this.mockValidLayout());
        ResponseEntity<HouseLayoutModel> getResponse = houseLayoutController.getLayout();
        assertNotNull(getResponse.getBody());
        assertEquals( 3 , getResponse.getBody().getRows().size());
        assertEquals( 2 , getResponse.getBody().getRows().get(0).getRooms().get(0).getDoors().size());
        assertEquals( 2 , getResponse.getBody().getRows().get(0).getRooms().get(0).getWindows().size());
    }

    /**
     * Helper method to mock a valid house layout
     * @return A valid House Layout
     */
    public HouseLayoutInput mockValidLayout(){

        HouseLayoutInput houseLayoutInput = Mockito.mock(HouseLayoutInput.class);
        List<RoomRowInput> roomRowInputs = new ArrayList<>();
        RoomRowInput roomRowInput = new RoomRowInput();

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
    
            roomRowInput.setRooms(new ArrayList<>());
            roomRowInput.getRooms().add(RoomInput);
            roomRowInputs.add(roomRowInput);
        }

        when(houseLayoutInput.getRows()).thenReturn(roomRowInputs);
        
        return houseLayoutInput;
    }

}
