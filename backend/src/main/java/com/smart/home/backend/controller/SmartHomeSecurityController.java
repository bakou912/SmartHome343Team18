package com.smart.home.backend.controller;

import java.time.Duration;

import com.smart.home.backend.model.simulationcontext.SimulationContextModel;
import com.smart.home.backend.model.smarthomesecurity.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.Setter;

/**
 * Smart Home Security Controller
 */
@Getter
@Setter
@RestController
public class SmartHomeSecurityController {
    
    private SimulationContextModel simulationContextModel;
	private HouseLayoutController houseLayoutController;
	private Security security;

	@Autowired
	public SmartHomeSecurityController(SimulationContextModel simulationContextModel, HouseLayoutController houseLayoutController) {
		this.simulationContextModel = simulationContextModel;
		this.houseLayoutController = houseLayoutController;
		this.security = new Security(false, false, null);
		this.houseLayoutController.getHouseLayoutModel().addPropertyChangeListener(security);
	}

    /**
	 * activate or deactivate AwayMode
	 * @param state
	 * @return
	 */
	@PutMapping("context/awayMode/{state}")
	public ResponseEntity<Boolean> setAwayMode(@PathVariable(value = "state") boolean state){
		
		this.houseLayoutController.getHouseLayoutModel().updateAwayMode(state);
		return new ResponseEntity<Boolean>(this.getSecurity().getAwayMode(), HttpStatus.OK);
	}

	/**
	 * Set duration for authorityTimer while on away mode
	 * @param duration
	 * @return
	 */
	@PutMapping("context/authoritiyTimer/{duration}")
	public ResponseEntity<Duration> setAuthorityTimerDuration(@PathVariable(value="duration") Duration duration){
		
		this.houseLayoutController.getHouseLayoutModel().updateAuthoritiesTimer(duration);
		return new ResponseEntity<>(this.getSecurity().getAlertAuthoritiesTime(), HttpStatus.OK);
	}

}
