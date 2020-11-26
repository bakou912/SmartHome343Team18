package com.smart.home.backend.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractBaseModel implements PropertyChangeListener {
	
	protected PropertyChangeSupport support;
	
	protected AbstractBaseModel() {
		this.support = new PropertyChangeSupport(this);
	}
	
	/**
	 * Resets the model's values.
	 */
	public abstract void reset();
}
