package com.smart.home.backend.model.security;

import com.smart.home.backend.service.OutputConsole;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timed task to alert authorities.
 */
public class AuthoritiesCallTask extends TimerTask {
	
	private final SecurityModel securityModel;
	private final Timer timer;
	
	/**
	 * 1-parameter constructor.
	 * @param securityModel security model
	 */
	public AuthoritiesCallTask(SecurityModel securityModel, Timer timer) {
		this.securityModel = securityModel;
		this.timer = timer;
	}
	
	@Override
	public void run() {
		if (!securityModel.isUpdatingTime()) {
			return;
		}
		
		if (securityModel.getNbPersonsInside() > 0 && securityModel.getAwayMode().equals(true)) {
			OutputConsole.log("SHP | Alerting authorities");
		} else {
			OutputConsole.log("SHP | The house is now empty. Alert dismissed");
		}
		
		securityModel.setAlertDetected(false);
		timer.cancel();
	}
}
