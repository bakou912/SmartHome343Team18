package com.smart.home.backend.model;

import java.beans.PropertyChangeListener;

public interface BaseModel extends PropertyChangeListener {
	
	/**
	 * Resets the model's values.
	 */
	void reset();
}
