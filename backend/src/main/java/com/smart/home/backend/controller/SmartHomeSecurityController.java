package com.smart.home.backend.controller;

import java.time.Duration;

import com.smart.home.backend.input.AuthoritiesTimerInput;
import com.smart.home.backend.input.AwayModeInput;
import com.smart.home.backend.model.simulationparameters.module.command.shp.AuthorityTimerManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shp.AwayModeHoursManagementCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shp.AwayModeManagementCommand;
import com.smart.home.backend.model.smarthomesecurity.AwayModeHours;
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
	 * Retrieving duration for authoritiesTimer while on away mode
	 * @return Duration for authoritiesTimer while on away mod
	 */
	@GetMapping("security/authoritiestime")
	public ResponseEntity<Duration> getAuthorityTimerDuration() {
		return new ResponseEntity<>(this.getSecurityModel().getAlertAuthoritiesTime(), HttpStatus.OK);
	}

	/**
	 * Setting duration for authorityTimer while on away mode
	 * @param authoritiesTimerInput new duration input
	 * @return Updated duration
	 */
	@PutMapping("security/authoritiestime")
	public ResponseEntity<Integer> setAwayModeHours(@RequestBody AuthoritiesTimerInput authoritiesTimerInput) {
		return new AuthorityTimerManagementCommand().execute(this.getSecurityModel(), authoritiesTimerInput.getDuration());
	}
	
	/**
	 * Retrieving away mode light hours
	 * @return Away mode light hours
	 */
	@GetMapping("security/hours")
	public ResponseEntity<AwayModeHours> getAwayModeHours() {
		return new ResponseEntity<>(this.getSecurityModel().getAwayModeHours(), HttpStatus.OK);
	}
	
	/**
	 * Setting away mode light hours.
	 * @param awayModeHours new awayModeHours input
	 * @return Updated awayModeHours
	 */
	@PutMapping("security/hours")
	public ResponseEntity<AwayModeHours> setAwayModeHours(@RequestBody AwayModeHours awayModeHours) {
		return new AwayModeHoursManagementCommand().execute(this.getSecurityModel(), awayModeHours);
	}
	
}
