package com.smart.home.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.controller.HouseLayoutController;
import com.smart.home.backend.input.HouseLayoutInput;
import com.smart.home.backend.input.RoomInput;
import com.smart.home.backend.input.RoomRowInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
/**
 * Test for Use Case 3.1.1
 * 
 */
@ExtendWith(MockitoExtension.class)
public class HouseLayoutControllerTest {
    
    HouseLayoutController houseLayoutController = new HouseLayoutController( new HouseLayoutModel());

    @Test
    public void loadLayout(){

        ResponseEntity<HouseLayoutModel> loadResponse = houseLayoutController.loadLayout(instantiateLayout());
        assertEquals( 3 , loadResponse.getBody().getRows().size());
        assertEquals( 2 , loadResponse.getBody().getRows().get(0).getRooms().get(0).getDoors().size());
        assertEquals( 3 , loadResponse.getBody().getRows().get(0).getRooms().get(0).getLights().size());
        assertEquals( 2 , loadResponse.getBody().getRows().get(0).getRooms().get(0).getWindows().size());
    }


    @Test
    public void getLayout(){

        houseLayoutController.loadLayout(instantiateLayout());
        ResponseEntity<HouseLayoutModel> getResponse = houseLayoutController.getLayout();
        assertEquals( 3 , getResponse.getBody().getRows().size());
        assertEquals( 2 , getResponse.getBody().getRows().get(0).getRooms().get(0).getDoors().size());
        assertEquals( 3 , getResponse.getBody().getRows().get(0).getRooms().get(0).getLights().size());
        assertEquals( 2 , getResponse.getBody().getRows().get(0).getRooms().get(0).getWindows().size());

    }

    /**
     * Helper method to instantiate a house layout
     * 
     * @return a House Layout
     */
    public HouseLayoutInput instantiateLayout(){

        HouseLayoutInput houseLayoutInput = Mockito.mock(HouseLayoutInput.class);
        List<RoomRowInput> roomRowInputs = new ArrayList<>();
        RoomRowInput roomRowInput = new RoomRowInput();

        String[] roomNames = {"Kitchen", "Living Room", "Garage"};

        for(String room : roomNames){
            RoomInput RoomInput = new RoomInput();
            RoomInput.setName(room);
            RoomInput.setLights(3);
    
            
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
