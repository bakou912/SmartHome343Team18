package com.smart.home.backend.model.simulationcontext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.SimulationState;
import com.smart.home.backend.model.AbstractBaseModel;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Person;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;
import com.smart.home.backend.model.simulationparameters.location.RoomItemLocationPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;

@Getter
@Setter
@Component
public class SimulationContextModel extends AbstractBaseModel {
	
	@JsonProperty("layout")
	private HouseLayoutModel houseLayoutModel;
	@JsonProperty("parameters")
	private SimulationParametersModel simulationParametersModel;
	@JsonProperty("state")
	private SimulationState state;
	
	/**
	 * 2-parameter constructor.m
	 * @param houseLayoutModel house layout model
	 * @param simulationParametersModel simulation parameters model
	 */
	@Autowired
	public SimulationContextModel(
			HouseLayoutModel houseLayoutModel,
			SimulationParametersModel simulationParametersModel
	) {
		this.state = SimulationState.OFF;
		this.houseLayoutModel = houseLayoutModel;
		this.simulationParametersModel = simulationParametersModel;
	}
 
	 /**
	  * Finds a person with the corresponding row, room and person ids.
	  * @param location person's location
	  * @return Found person
	  */
	 @Nullable
	 public Person findPerson(RoomItemLocationPosition location) {
		 Room foundRoom = this.getHouseLayoutModel().findRoom(location);
		 Person foundPerson = null;
		
		 if (foundRoom != null) {
			 foundPerson = foundRoom.findPerson(location.getItemId());
		 }
		
		 return foundPerson;
	 }
	
	/**
	 * Toggling simulation state.
	 * @return New state
	 */
	public SimulationState toggleState() {
	 	if (this.getState().equals(SimulationState.ON)){
	 		this.setState(SimulationState.OFF);
		} else {
			this.setState(SimulationState.ON);
		}
	 	return this.getState();
	}
	
	public void reset() {
	 	this.setState(SimulationState.OFF);
		this.getHouseLayoutModel().reset();
		this.getSimulationParametersModel().reset();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// Potentially TBD
	}
}
