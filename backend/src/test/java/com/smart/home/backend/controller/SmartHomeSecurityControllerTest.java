package com.smart.home.backend.controller;

import com.smart.home.backend.input.AuthoritiesTimerInput;
import com.smart.home.backend.input.AwayModeInput;
import com.smart.home.backend.model.security.AwayModeHours;
import com.smart.home.backend.model.security.SecurityModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Test for Use Case 3.5.1: Away mode functionality
 */
@RunWith(PowerMockRunner.class)
class SmartHomeSecurityControllerTest {

    SecurityModel securityModel;

    SmartHomeSecurityController controller;

    @BeforeEach
    void BeforeEach(){
        securityModel = Mockito.spy(new SecurityModel());
        controller = new SmartHomeSecurityController(securityModel);
    }

    /**
     * Test that the default behavior of the away mode is set to false
     */
    @Test
    void defaultAwayMode(){
        ResponseEntity<Boolean> awayModeBool = controller.getAwayMode();
        assertFalse(awayModeBool.getBody());
    }

    /**
     * Test that once activated the away mode is set true
     */
    @Test
    void ActivateAwayMode() {
        ResponseEntity<Boolean> awayModeBool = controller.setAwayMode(instantiateAwayModeInput(true));
        assertTrue(awayModeBool.getBody());
        verify(securityModel).setAwayMode(true);
    }

    /**
     * Test that when deactivated the away mode is set to false
     */
    @Test
    void DeactivateAwayMode(){
        ResponseEntity<Boolean> awayModeBool = controller.setAwayMode(instantiateAwayModeInput(false));
        assertFalse(awayModeBool.getBody());
        verify(securityModel).setAwayMode(false);
    }

    /**
     * Tests the time set before the authorities are notified
     */
    @Test
    void setAuthoritiesTimer(){
        ResponseEntity<Integer> authoritiesTimer = controller.setAwayModeHours(instantiateAuthoritiesTimerInput(4));
        assertEquals(4, authoritiesTimer.getBody().intValue());

        authoritiesTimer = controller.setAwayModeHours(instantiateAuthoritiesTimerInput(10000));
        assertEquals(10000, controller.getAuthorityTimerDuration().getBody().getSeconds());
    }

    /**
     * Test setting the awayModeHours
     */
    @Test
    void setAwayModeHoursTest(){
        ResponseEntity<AwayModeHours> setAwayModeHours = controller.setAwayModeHours(new AwayModeHours(LocalTime.NOON,LocalTime.MIDNIGHT));
        assertEquals(LocalTime.NOON, setAwayModeHours.getBody().getFrom());
        assertEquals(LocalTime.MIDNIGHT, setAwayModeHours.getBody().getTo());

        assertEquals(LocalTime.NOON, controller.getAwayModeHours().getBody().getFrom());
        assertEquals(LocalTime.MIDNIGHT, controller.getAwayModeHours().getBody().getTo());
    }
    /**
     * Helper Method to instantiate AwayModeInput
     * @param awayState
     * @return
     */
    AwayModeInput instantiateAwayModeInput(boolean awayState){
        AwayModeInput awayModeInput = new AwayModeInput();
        awayModeInput.setState(awayState);
        return awayModeInput;
    }

    /**
     * Helper Method to instantiate AuthoritiesTimerInput
     * @param duration
     * @return
     */
    AuthoritiesTimerInput instantiateAuthoritiesTimerInput(Integer duration){
        AuthoritiesTimerInput authoritiesTimerInput = new AuthoritiesTimerInput();
        authoritiesTimerInput.setDuration(duration);
        return authoritiesTimerInput;
    }
}
