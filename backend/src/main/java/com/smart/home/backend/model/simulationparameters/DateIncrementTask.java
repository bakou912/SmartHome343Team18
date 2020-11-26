package com.smart.home.backend.model.simulationparameters;

import com.smart.home.backend.config.SpringUtils;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.TimerTask;

/**
 * Timed task to alert authorities.
 */
public class DateIncrementTask extends TimerTask {
	
	private LocalDateTime date;
	private final PropertyChangeSupport support;
	
	/**
	 * 1-parameter constructor.
	 * @param date date
	 */
	public DateIncrementTask(LocalDateTime date) {
		this.date = date;
		this.support = new PropertyChangeSupport(this);
		support.addPropertyChangeListener(SpringUtils.getBean(HeatingModel.class));
	}
	
	@Override
	public void run() {
		this.date = this.date.plusSeconds(1);
		this.support.firePropertyChange("timeIncrement", null, this.date);
	}
	
}
