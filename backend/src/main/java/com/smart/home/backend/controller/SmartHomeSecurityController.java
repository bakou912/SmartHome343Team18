package com.smart.home.backend.controller;

import java.time.Duration;

import com.smart.home.backend.input.AwayModeInput;
import com.smart.home.backend.model.simulationparameters.module.command.shp.AuthorityTimerManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shp.AwayModeManagementCommand;
import com.smart.home.backend.model.smarthomesecurity.SecurityModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Smart Home Security Controller
 */
@Getter
@Setter
@RestController
public class SmartHomeSecurityController {
    
	private SecurityModel securityModel;
	
	/**
	 * 1-parameter constructor
	 * @param securityModel security model
	 */
	@Autowired
	public SmartHomeSecurityController(SecurityModel securityModel) {
		this.securityModel = securityModel;
	}
	
	
	/**
	 * Retrieving the away mode state.
	 * @return Wether the away mode is on or not
	 */
	@GetMapping("security/awaymode")
	public ResponseEntity<Boolean> getAwayMode(){
		return new ResponseEntity<>(this.getSecurityModel().getAwayMode(), HttpStatus.OK);
	}

    /**
	 * Activating or deactivating AwayMode
	 * @param awayModeInput away mode input containing the new state
	 * @return New away mode state
	 */
	@PutMapping("security/awaymode")
	public ResponseEntity<Boolean> setAwayMode(@RequestBody AwayModeInput awayModeInput) {
		return new AwayModeManagementCommand().execute(this.getSecurityModel(), awayModeInput);
	}

	/**
	 * Setting duration for authorityTimer while on away mode
	 * @param duration new duration
	 * @return Updated duration
	 */
	@PutMapping("security/authoritytime/{duration}")
	public ResponseEntity<Duration> setAuthorityTimerDuration(@PathVariable(value="duration") Duration duration) {
		return new AuthorityTimerManagementCommand().execute(this.getSecurityModel(), duration);
	}
	
}
