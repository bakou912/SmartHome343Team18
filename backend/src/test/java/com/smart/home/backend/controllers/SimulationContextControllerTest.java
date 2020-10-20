package com.smart.home.backend.controllers;

import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.Profile;
import com.smart.home.backend.constant.SimulationState;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.controller.HouseLayoutController;
import com.smart.home.backend.controller.SimulationContextController;
import com.smart.home.backend.input.*;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.houselayout.directional.Window;
import com.smart.home.backend.model.simulationcontext.SimulationContextModel;
import com.smart.home.backend.model.simulationparameters.Location;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationparameters.SystemParameters;
import com.smart.home.backend.model.simulationparameters.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Tests for Use Cases in 3.3: Modify simulation context
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JUnit 5 Nested Example")
class SimulationContextControllerTest {
    
    @Mock
    HouseLayoutModel houseLayoutModel;
    
    @Mock
    SimulationParametersModel simulationParametersModel;
    
    @Mock
    HouseLayoutController houseLayoutController;
    
    @InjectMocks
    SimulationContextController simulationContextController;
    
    /**
     * Tests for use case 3.3.1: Modify simulation context
     */
    @Nested
    @DisplayName("Modify simulation context")
    class ModifySimulationContext {
    
        @BeforeEach
        void beforeEach() {
            simulationContextController = new SimulationContextController(
                    new SimulationContextModel(
                            houseLayoutModel,
                            simulationParametersModel
                    ),
                    houseLayoutController
            );
        }
    
        /**
         * Test for toggling simulation state
         */
        @Test
        void togglingState() {
            SimulationState initialState = simulationContextController.getSimulationContextModel().getState();
            assertEquals(SimulationState.OFF, initialState);
    
            assertEquals(SimulationState.ON, simulationContextController.toggleState().getBody());
            assertEquals(SimulationState.OFF, simulationContextController.toggleState().getBody());
        }
    
        /**
         * Test for modifying user with valid input
         */
        @Test
        void validModifyUser() {
            when(simulationContextController.getSimulationContextModel().getSimulationParametersModel().getUser()).thenReturn(
                    new User(Profile.PARENT, "oldname", new Location())
            );
            
            UserInput modifyInput = new UserInput();
            Location newLocation = new Location();
            newLocation.setName("newroom");
            newLocation.setRowId(0);
            newLocation.setRoomId(1);
            modifyInput.setProfile(Profile.CHILD);
            modifyInput.setName("newname");
            modifyInput.setLocation(newLocation);
            
            User modifiedUser = simulationContextController.modifyUser(modifyInput).getBody();
        
            assertNotNull(modifiedUser);
            assertEquals(Profile.CHILD, modifiedUser.getProfile());
            assertEquals("newname", modifiedUser.getName());
            assertEquals(newLocation, modifiedUser.getLocation());
        }
    
        /**
         * Test for modifying user when user is not set
         */
        @Test
        void invalidModifyUser() {
            when(simulationContextController.getSimulationContextModel().getSimulationParametersModel().getUser()).thenReturn(null);
        
            ResponseEntity<User> responseEntity = simulationContextController.modifyUser(null);
        
            assertNotNull(responseEntity);
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    
        /**
         * Test for modifying system parameters with valid input
         */
        @Test
        void validModifySystemParameters() {
            when(simulationContextController.getSimulationContextModel().getSimulationParametersModel().getSysParams()).thenReturn(
                    new SystemParameters(0.0, 0.0, LocalDateTime.now())
            );
        
            ParametersInput modifyInput = new ParametersInput();
            LocalDateTime newDate = LocalDateTime.parse("2020-04-08T12:30");
            modifyInput.setDate(newDate);
            modifyInput.setInsideTemp(20.0);
            modifyInput.setOutsideTemp(25.0);
            
            SystemParameters modifiedParameters = simulationContextController.modifyParams(modifyInput).getBody();
        
            assertNotNull(modifiedParameters);
            assertEquals(newDate, modifiedParameters.getDate());
            assertEquals(20.0, modifiedParameters.getInsideTemp());
            assertEquals(25.0, modifiedParameters.getOutsideTemp());
        }
    
        /**
         * Test for modifying system params when system params are not set
         */
        @Test
        void invalidModifySystemParameters() {
            when(simulationContextController.getSimulationContextModel().getSimulationParametersModel().getSysParams()).thenReturn(null);
        
            ResponseEntity<SystemParameters> responseEntity = simulationContextController.modifyParams(null);
        
            assertNotNull(responseEntity);
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }
    
    /**
     * Tests for use case 3.3.2: Modify house layout context
     */
    @Nested
    @DisplayName("Modify house layout context")
    class ModifyHouseLayoutContext {
        
        @BeforeEach
        void beforeEach() {
            simulationContextController = new SimulationContextController(
                    new SimulationContextModel(
                            houseLayoutModel,
                            simulationParametersModel
                    ),
                    houseLayoutController
            );
        }
    
        /**
         * Test for adding a person with valid input
         */
        @Test
        void validAddPerson() {
            Room foundRoom = Room.builder().id(0).build();
            when(simulationContextController.getSimulationContextModel().getHouseLayoutModel().findRoom(0, 0)).thenReturn(foundRoom);
            
            PersonInput personInput = new PersonInput();
            personInput.setName("personname");
            
            assertEquals(0, simulationContextController.addPerson(0, 0, personInput).getBody());
            assertEquals(1, simulationContextController.addPerson(0, 0, personInput).getBody());
            assertEquals(2, simulationContextController.addPerson(0, 0, personInput).getBody());
        }
    
        /**
         * Test for adding a person when room is not set
         */
        @Test
        void invalidAddPerson() {
            when(simulationContextController.getSimulationContextModel().getHouseLayoutModel().findRoom(0, 0)).thenReturn(null);
    
            PersonInput personInput = new PersonInput();
            personInput.setName("personname");
            
            ResponseEntity<Integer> responseEntity = simulationContextController.addPerson(0, 0, personInput);
        
            assertNotNull(responseEntity);
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    
        /**
         * Test for blocking a window with valid input
         */
        @Test
        void validBlockWindow() {
            Window newWindow = Window.builder()
                    .id(0)
                    .state(WindowState.BLOCKED)
                    .direction(Direction.EAST)
                    .build();
            ResponseEntity<Window> windowResponse = new ResponseEntity<>(newWindow, HttpStatus.OK);
            when(simulationContextController.getHouseLayoutController().changeWindowState(anyInt(), anyInt(), anyInt(), any(WindowInput.class))).thenReturn(windowResponse);
    
            ResponseEntity<Window> responseEntity = simulationContextController.blockWindow(0, 0, 0);
    
            assertNotNull(responseEntity);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(newWindow, responseEntity.getBody());
        }
    
        /**
         * Test for unblocking a window with valid input
         */
        @Test
        void validUnblockWindow() {
            Window newWindow = Window.builder()
                    .id(0)
                    .state(WindowState.CLOSED)
                    .direction(Direction.EAST)
                    .build();
            ResponseEntity<Window> windowResponse = new ResponseEntity<>(newWindow, HttpStatus.OK);
            when(simulationContextController.getHouseLayoutController().changeWindowState(anyInt(), anyInt(), anyInt(), any(WindowInput.class))).thenReturn(windowResponse);
        
            ResponseEntity<Window> responseEntity = simulationContextController.unBlockWindow(0, 0, 0);
        
            assertNotNull(responseEntity);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(newWindow, responseEntity.getBody());
        }
        
    }

}
