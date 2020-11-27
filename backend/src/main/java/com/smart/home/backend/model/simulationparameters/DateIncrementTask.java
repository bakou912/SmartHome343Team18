package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.model.AbstractNotifier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Timed task to alert authorities.
 */
@Component
public class DateIncrementTask extends AbstractNotifier implements Runnable {
	
	@Getter
	@Setter
	private SystemParameters systemParameters;
	
	@Override
	public void run() {
		if (this.systemParameters.isIncrementing()) {
			this.systemParameters.setDate(this.systemParameters.getDate().plusSeconds(1));
			this.support.firePropertyChange("timeIncrement", null, this.systemParameters.getDate());
			this.support.firePropertyChange("date", null, this.systemParameters.getDate());
		}
	}
	
}
