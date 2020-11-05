package com.smart.home.backend.model.simulationcontext;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.SimulationState;
import com.smart.home.backend.model.BaseModel;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Person;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class SimulationContextModel implements BaseModel {
	
	@JsonProperty("layout")
	private HouseLayoutModel houseLayoutModel;
	@JsonProperty("parameters")
	private SimulationParametersModel simulationParametersModel;
	@JsonProperty("state")
	private SimulationState state;
	
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
	  * @param rowId Row id
	  * @param roomId Room id
	  * @param personId Searched person's id
	  * @return Found person
	  */
	 @Nullable
	 public Person findPerson(int rowId, int roomId, int personId) {
		 Room foundRoom = this.getHouseLayoutModel().findRoom(rowId, roomId);
		 Person foundPerson = null;
		
		 if (foundRoom != null) {
			 foundPerson = foundRoom.findPerson(personId);
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
	
	@Override
	public void reset() {
	 	this.setState(SimulationState.OFF);
		this.getHouseLayoutModel().reset();
		this.getSimulationParametersModel().reset();
	}
}
