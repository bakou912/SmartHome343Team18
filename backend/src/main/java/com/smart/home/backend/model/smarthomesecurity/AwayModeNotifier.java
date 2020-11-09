package com.smart.home.backend.model.smarthomesecurity;

import com.smart.home.backend.config.SpringUtils;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;

import java.beans.PropertyChangeSupport;

public class AwayModeNotifier {
	
	private PropertyChangeSupport support;
	
	public AwayModeNotifier() {
		this.support = new PropertyChangeSupport(this);
		this.support.addPropertyChangeListener(SpringUtils.getBean(HouseLayoutModel.class));
	}
	
	public void notifyAwayModeOn() {
		this.support.firePropertyChange("awayModeOn", null, true);
	}
	
}
