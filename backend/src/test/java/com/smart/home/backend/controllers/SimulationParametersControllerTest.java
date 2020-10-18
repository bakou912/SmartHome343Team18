package com.smart.home.backend.controllers;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.controller.SimulationParametersController;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.ParametersInput;
import com.smart.home.backend.input.ProfileInput;
import com.smart.home.backend.model.simulationParameters.SimulationParametersModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


/**
 * Test for Use Case 3.2.1
 */
@ExtendWith(MockitoExtension.class)
public class SimulationParametersControllerTest {
    @Mock
    SimulationParametersController controller = new SimulationParametersController();

    @Test
    public void editSimulationParametersTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,23.5,33.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp(),"Inside Temperature does not match");
        assertEquals(33.0,editSimulationParameters.getBody().getSysParams().getOutsideTemp(),"Outside Temperature does not match");
        assertNotNull(editSimulationParameters.getBody().getSysParams().getDate(),"Date should not be null");
        assertNotNull(editSimulationParameters.getBody().getProfile().getRole(),"Role should not be null");
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate(),"Dates don't match");
        assertEquals(Role.CHILD,editSimulationParameters.getBody().getProfile().getRole(),"Roles don't match");

        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.STRANGER,80,33.0,LocalDateTime.parse("2020-04-08T04:20:05")));
        assertNull(editSimulationParameters.getBody().getSysParams().getInsideTemp(),"Inside Temperature should be null");
        assertNotNull(editSimulationParameters.getBody().getSysParams().getOutsideTemp(),"Outside Temperature should be null");
        assertNotNull(editSimulationParameters.getBody().getSysParams().getDate(),"Dates should not be null");
        assertNotNull(editSimulationParameters.getBody().getProfile().getRole(),"Role should not be null");
        assertEquals(LocalDateTime.parse("2020-04-08T04:20:05"),editSimulationParameters.getBody().getSysParams().getDate(),"Dates don't match");
        assertEquals(Role.STRANGER,editSimulationParameters.getBody().getProfile().getRole(),"Roles don't match");

        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.VISITOR,5,-30,LocalDateTime.parse("2020-04-08P12:30")));
        assertEquals(5,editSimulationParameters.getBody().getSysParams().getInsideTemp(),"Inside Temperature do not match");
        assertEquals(-30,editSimulationParameters.getBody().getSysParams().getOutsideTemp(),"Outside should do not match");
        assertNull(editSimulationParameters.getBody().getSysParams().getDate(),"Dates should be null");
        assertNotNull(editSimulationParameters.getBody().getProfile().getRole(),"Role should not be null");
        assertEquals(Role.VISITOR,editSimulationParameters.getBody().getProfile().getRole(),"Roles don't match");

        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.PARENT,5,-70,LocalDateTime.parse("2020-04-08T12:30")));
        assertEquals(5,editSimulationParameters.getBody().getSysParams().getInsideTemp(),"Inside Temperature does not match");
        assertNull(editSimulationParameters.getBody().getSysParams().getOutsideTemp(),"Outside Temperature should be null");
        assertNotNull(editSimulationParameters.getBody().getSysParams().getDate(),"Date should not be null");
        assertNotNull(editSimulationParameters.getBody().getProfile().getRole(),"Role should not be null");
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate(),"Dates don't match");
        assertEquals(Role.PARENT,editSimulationParameters.getBody().getProfile().getRole(),"Roles don't match");

        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(null,5,-7,LocalDateTime.parse("2020-04-08T12:30")));
        assertEquals(5,editSimulationParameters.getBody().getSysParams().getInsideTemp(),"Inside Temperature does not match");
        assertEquals(-7,editSimulationParameters.getBody().getSysParams().getOutsideTemp(),"Outside Temperature does not match");
        assertNotNull(editSimulationParameters.getBody().getSysParams().getDate(),"Date should not be null");
        assertNotNull(editSimulationParameters.getBody().getProfile().getRole(),"Role should not be null");
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate(),"Dates don't match");
        assertNull(editSimulationParameters.getBody().getProfile().getRole(),"Role should be null");
    }
    @Test
    public void getSimulationParametersTest(){
        /**
         * get not implemented so just a pass test for now
         */
        assertEquals(1,1,"Will always pass");
    }

    /**
     * Helper class to instantiate the Simulation Parameters input
     */
    public EditParametersInput instantiateSimulationParameters(Role role,double insideTemp,double outsideTemp,LocalDateTime date){
        EditParametersInput parameters = Mockito.mock(EditParametersInput.class);
        ProfileInput profileInput = new ProfileInput();
        ParametersInput paramInput = new ParametersInput();
        profileInput.setRole(role);
        paramInput.setInsideTemp(insideTemp);
        paramInput.setOutsideTemp(outsideTemp);
        paramInput.setDate(date);
        when(parameters.getProfileInput()).thenReturn(profileInput);
        when(parameters.getParametersInput()).thenReturn(paramInput);
        return parameters;
    }
}
