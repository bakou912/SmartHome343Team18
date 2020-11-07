package com.smart.home.backend.controller;

import java.time.Duration;

import com.smart.home.backend.model.simulationcontext.SimulationContextModel;
import com.smart.home.backend.model.smarthomesecurity.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	 * Activating or deactivating AwayMode
	 * @param state away mode state
	 * @return New away mode value
	 */
	@PutMapping("context/awayMode/{state}")
	public ResponseEntity<Boolean> setAwayMode(@PathVariable(value = "state") boolean state){
		this.houseLayoutController.getHouseLayoutModel().updateAwayMode(state);
		this.getSecurity().setAwayMode(state);
		return new ResponseEntity<>(this.getSecurity().getAwayMode(), HttpStatus.OK);
	}

	/**
	 * Setting duration for authorityTimer while on away mode
	 * @param duration new duration
	 * @return Updated duration
	 */
	@PutMapping("context/authoritiyTimer/{duration}")
	public ResponseEntity<Duration> setAuthorityTimerDuration(@PathVariable(value="duration") Duration duration){
		this.houseLayoutController.getHouseLayoutModel().updateAuthoritiesTimer(duration);
		return new ResponseEntity<>(this.getSecurity().getAlertAuthoritiesTime(), HttpStatus.OK);
	}
	
	/**
	 * Retrieving the away mode state.
	 * @return Wether the away mode is on or not
	 */
	@GetMapping("context/awayMode/state")
	public ResponseEntity<Boolean> getAwayMode(){
		return new ResponseEntity<>(this.getSecurity().getAwayMode(), HttpStatus.OK);
	}
	
}
