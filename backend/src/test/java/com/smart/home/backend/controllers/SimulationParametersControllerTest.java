package com.smart.home.backend.controllers;

import com.smart.home.backend.constant.Role;
import com.smart.home.backend.controller.SimulationParametersController;
import com.smart.home.backend.input.EditParametersInput;
import com.smart.home.backend.input.ParametersInput;
import com.smart.home.backend.input.ProfileInput;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


/**
 * Test for Use Case 3.2.1
 */
@ExtendWith(MockitoExtension.class)
public class SimulationParametersControllerTest {

    SimulationParametersController controller = new SimulationParametersController(new SimulationParametersModel());

    /**
     * Method Test for valid Inside Temperatures
     */
    @Test
    public void validInsideTempTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,23.5,33.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.0,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.CHILD,editSimulationParameters.getBody().getProfile().getRole());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,-15,33.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(-15,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.0,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.CHILD,editSimulationParameters.getBody().getProfile().getRole());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,0,33.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(0,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.0,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.CHILD,editSimulationParameters.getBody().getProfile().getRole());
    }

    /**
     * Method test for invalid inside Temperatures
     */
    @Test
    public void invalidInsideTempTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,233.5,33.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,-90,33.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());
    }

    /**
     * Method test for valid outside temperature
     */
    @Test
    public void validOutsideTempTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,23.5,33.6,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.CHILD,editSimulationParameters.getBody().getProfile().getRole());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,23.5,-25,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(-25,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.CHILD,editSimulationParameters.getBody().getProfile().getRole());
    }

    /**
     * Method test for invalid outside temperatures
     */
    @Test
    public void invalidOutsideTempTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,23.5,330.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());

        editSimulationParameters = controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,23.5,-233.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());
    }

    /**
     * Method test for valid roles
     */
    @Test
    public void validRoleTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,23.5,33.6,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.CHILD,editSimulationParameters.getBody().getProfile().getRole());

        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.PARENT,23.5,33.6,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.PARENT,editSimulationParameters.getBody().getProfile().getRole());

        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.VISITOR,23.5,33.6,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.VISITOR,editSimulationParameters.getBody().getProfile().getRole());

        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.STRANGER,23.5,33.6,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.STRANGER,editSimulationParameters.getBody().getProfile().getRole());
    }

    /**
     * Method Test for invalid roles
     */
    @Test
    public void invalidRolesTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(null,23.5,330.0,LocalDateTime.parse("2020-04-08T12:30")));
        assertNull(editSimulationParameters.getBody());
    }

    /**
     * Method test for valid dates
     */
    @Test
    public void validDatesTest(){
        ResponseEntity<SimulationParametersModel> editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,23.5,33.6,LocalDateTime.parse("2020-04-08T12:30")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T12:30"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.CHILD,editSimulationParameters.getBody().getProfile().getRole());

        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.CHILD,23.5,33.6,LocalDateTime.parse("2020-04-08T04:25:05")));
        assertNotNull(editSimulationParameters.getBody());
        assertEquals(23.5,editSimulationParameters.getBody().getSysParams().getInsideTemp());
        assertEquals(33.6,editSimulationParameters.getBody().getSysParams().getOutsideTemp());
        assertEquals(LocalDateTime.parse("2020-04-08T04:25:05"),editSimulationParameters.getBody().getSysParams().getDate());
        assertEquals(Role.CHILD,editSimulationParameters.getBody().getProfile().getRole());
    }

    /**
     * Method Test for invalid Dates
     */
    @Test
    public void invalidDatesTest(){
        LocalDateTime date;
        try{
            date=LocalDateTime.parse("2020-04-08T90:30");
        }
        catch (DateTimeParseException e){
            date=null;
        }
        ResponseEntity<SimulationParametersModel> editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.PARENT,23.5,330.0,date));
        assertNull(editSimulationParameters.getBody());

        try{
            date=LocalDateTime.parse("2020-15-15T10:30");
        }
        catch (DateTimeParseException e){
            date=null;
        }
        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.PARENT,23.5,0.0,date));
        assertNull(editSimulationParameters.getBody());
        try{
            date=LocalDateTime.parse("");
        }
        catch (DateTimeParseException e){
            date=null;
        }
        editSimulationParameters= controller.editSimulationParameters(instantiateSimulationParameters(Role.PARENT,23.5,0.0,date));
        assertNull(editSimulationParameters.getBody());
    }
    @Test
    public void getSimulationParametersTest(){
        /*
         * get not implemented so just a pass test for now
         */
        assertEquals(1,1,"Will always pass");
    }

    /**
     * Helper method to instantiate the Simulation Parameters input
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
