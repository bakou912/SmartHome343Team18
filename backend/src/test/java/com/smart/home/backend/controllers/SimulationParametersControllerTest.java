package com.smart.home.backend.controllers;

import com.smart.home.backend.constant.Profile;
import com.smart.home.backend.controller.SimulationParametersController;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.ParametersInput;
import com.smart.home.backend.input.UserInput;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationparameters.UserProfile;
import com.smart.home.backend.model.simulationparameters.UserProfiles;
import com.smart.home.backend.model.simulationparameters.location.PersonLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Tests for Use Case 3.2.1: Setting simulation parameters
 */
@ExtendWith(MockitoExtension.class)
class SimulationParametersControllerTest {
    
    final String PROFILE_NAME = "profile_name";
    
    @Mock
    UserProfiles userProfiles;
    
    @InjectMocks
    SimulationParametersModel simulationParametersModel;
    
    SimulationParametersController controller;
    
    @BeforeEach
    void beforeEach() {
        controller = new SimulationParametersController(simulationParametersModel);
    }

    /**
     * Test for valid Inside Temperatures
     */
    @Test
    void validInsideTempTest(){
        when(userProfiles.get(PROFILE_NAME)).thenReturn(new UserProfile(PROFILE_NAME, new ArrayList<>()));
        ResponseEntity<SimulationParametersModel> editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, 23.5, 33.0, LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5, editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.0, editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"), editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(PROFILE_NAME, editSimulationParameters.getBody().getUser().getProfile().getName());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, -15, 33.0, LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(-15, editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.0, editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime. parse("2020-04-08T12:30"), editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(PROFILE_NAME, editSimulationParameters.getBody().getUser().getProfile().getName());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, 0, 33.0, LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(0, editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.0, editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"), editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(PROFILE_NAME, editSimulationParameters.getBody().getUser().getProfile().getName());
    }

    /**
     * Test for invalid inside Temperatures
     */
    @Test
    void invalidInsideTempTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, 233.5, 33.0, LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, -90, 33.0, LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());
    }

    /**
     * Test for valid outside temperature
     */
    @Test
    void validOutsideTempTest(){
        when(userProfiles.get(PROFILE_NAME)).thenReturn(new UserProfile(PROFILE_NAME, new ArrayList<>()));
        ResponseEntity<SimulationParametersModel> editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, 23.5, 33.6, LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5, editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6, editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"), editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(PROFILE_NAME, editSimulationParameters.getBody().getUser().getProfile().getName());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, 23.5, -25, LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5, editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(-25, editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"), editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(PROFILE_NAME, editSimulationParameters.getBody().getUser().getProfile().getName());
    }

    /**
     * Test for invalid outside temperatures
     */
    @Test
    void invalidOutsideTempTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME,23.5,330.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, 23.5, -233.0, LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());
    }

    /**
     * Test for valid profiles
     */
    @Test
    void validProfileTest(){
        when(userProfiles.get("profile_name_valid")).thenReturn(new UserProfile("profile_name_valid", new ArrayList<>()));
        ResponseEntity<SimulationParametersModel> editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters("profile_name_valid", 23.5, 33.6, LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5, editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6, editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"), editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals("profile_name_valid", editSimulationParameters.getBody().getUser().getProfile().getName());
    }

    /**
     * Test for invalid profiles
     */
    @Test
    void invalidProfileTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(null, 23.5, 330.0, LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());
    }

    /**
     * Test for valid dates
     */
    @Test
    void validDatesTest(){
        when(userProfiles.get(PROFILE_NAME)).thenReturn(new UserProfile(PROFILE_NAME, new ArrayList<>()));
        ResponseEntity<SimulationParametersModel> editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, 23.5, 33.6, LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5, editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6, editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"), editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(PROFILE_NAME, editSimulationParameters.getBody().getUser().getProfile().getName());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, 23.5, 33.6, LocalDateTime.parse("2020-04-08T04:25:05")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5, editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6, editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T04:25:05"), editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(PROFILE_NAME, editSimulationParameters.getBody().getUser().getProfile().getName());
    }

    /**
     * Test for invalid Dates
     */
    @Test
    void invalidDatesTest(){
        LocalDateTime date;
        try{
            date = LocalDateTime.parse("2020-04-08T90:30");
        }
        catch (DateTimeParseException e){
            date = null;
        }
        ResponseEntity<SimulationParametersModel> editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(Profile.PARENT.toString(),23.5,330.0,date));
        assertNull(editSimulationParameters.getBody());

        try{
            date = LocalDateTime.parse("2020-15-15T10:30");
        }
        catch (DateTimeParseException e){
            date = null;
        }
        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(Profile.PARENT.toString(),23.5,0.0,date));
        assertNull(editSimulationParameters.getBody());
        try{
            date = LocalDateTime.parse("");
        }
        catch (DateTimeParseException e){
            date = null;
        }
        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(Profile.PARENT.toString(),23.5,0.0,date));
        assertNull(editSimulationParameters.getBody());
    }
    
    /**
     * Get parameters test
     */
    @Test
    void getSimulationParametersTest(){
        when(userProfiles.get(PROFILE_NAME)).thenReturn(new UserProfile(PROFILE_NAME, new ArrayList<>()));
        controller.editSimulationParameters(instantiateSimulationParameters(PROFILE_NAME, 23.5, 33.0, LocalDateTime.parse("2020-04-08T12:30")));
        SimulationParametersModel simulationParametersModel = controller.getSimulationParameters().getBody();
        assertNotNull(simulationParametersModel);
        assertEquals(23.5, simulationParametersModel.getSysParams().getInsideTemp());
        assertEquals(33.0, simulationParametersModel.getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"), simulationParametersModel.getSysParams().getDate());
        assertEquals(PROFILE_NAME, simulationParametersModel.getUser().getProfile().getName());
    }

    /**
     * Helper method to instantiate the Simulation Parameters input
     */
    EditParametersInput instantiateSimulationParameters(String profile, double insideTemp, double outsideTemp, LocalDateTime date){
        EditParametersInput parameters = Mockito.mock(EditParametersInput.class);
        UserInput userInput = new UserInput();
        userInput.setLocation(new PersonLocation());
        userInput.setProfile(profile);
        ParametersInput paramInput = new ParametersInput();
        paramInput.setInsideTemp(insideTemp);
        paramInput.setOutsideTemp(outsideTemp);
        paramInput.setDate(date);
        when(parameters.getUserInput()).thenReturn(userInput);
        when(parameters.getParametersInput()).thenReturn(paramInput);
        return parameters;
    }
}
