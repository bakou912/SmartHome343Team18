package com.smart.home.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract notifier class.
 */
public abstract class AbstractNotifier {
	
	@JsonIgnore
	protected final PropertyChangeSupport support;
	
	/**
	 * Default constructor.
	 */
	protected AbstractNotifier() {
		this.support = new PropertyChangeSupport(this);
	}
	
	/**
	 * Adds a listener to the list of listeners for this model.
	 * @param listener listener to add
	 */
	public void addListener(PropertyChangeListener listener) {
		this.support.addPropertyChangeListener(listener);
	}
	
}
