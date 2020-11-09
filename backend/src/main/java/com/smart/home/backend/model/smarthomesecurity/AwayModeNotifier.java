package com.smart.home.backend.model.smarthomesecurity;

import com.smart.home.backend.config.SpringUtils;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;

import java.beans.PropertyChangeSupport;

/**
 * Notifying listeners for the away mode event.
 */
public class AwayModeNotifier {
	
	private PropertyChangeSupport support;
	
	/**
	 * Default constructor.
	 */
	public AwayModeNotifier() {
		this.support = new PropertyChangeSupport(this);
		this.support.addPropertyChangeListener(SpringUtils.getBean(HouseLayoutModel.class));
	}
	
	/**
	 * Notifying listeners.
	 */
	public void notifyAwayModeOn() {
		this.support.firePropertyChange("awayModeOn", null, true);
	}
	
}
