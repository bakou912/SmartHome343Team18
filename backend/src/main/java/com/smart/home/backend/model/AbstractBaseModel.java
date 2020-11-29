package com.smart.home.backend.model;


import java.beans.PropertyChangeListener;

/**
 * Abstract model class.
 */
public abstract class AbstractBaseModel extends AbstractNotifier implements PropertyChangeListener, BaseModel {
	
	/**
	 * Resets the model's values.
	 */
	public abstract void reset();
}
