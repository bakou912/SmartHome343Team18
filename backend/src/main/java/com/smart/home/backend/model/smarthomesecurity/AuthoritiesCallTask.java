package com.smart.home.backend.model.smarthomesecurity;

import com.smart.home.backend.service.OutputConsole;

import java.util.TimerTask;

/**
 * Timed task to alert authorities.
 */
public class AuthoritiesCallTask extends TimerTask {
	
	private SecurityModel securityModel;
	
	/**
	 * 1-parameter constructor.
	 * @param securityModel security model
	 */
	public AuthoritiesCallTask(SecurityModel securityModel) {
		this.securityModel = securityModel;
	}
	
	@Override
	public void run() {
		if (securityModel.getNbPersonsInside() > 0) {
			OutputConsole.log("SHP | Alerting authorities");
		} else {
			OutputConsole.log("SHP | The house is now empty. Alert dismissed");
		}
		securityModel.setAlertDetected(false);
	}
}
