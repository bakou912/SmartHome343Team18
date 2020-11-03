package com.smart.home.backend.model.simulationcontext;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.home.backend.constant.SimulationState;
import com.smart.home.backend.model.BaseModel;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Person;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.houselayout.RoomRow;
import com.smart.home.backend.model.simulationparameters.SimulationParametersModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import ch.qos.logback.core.util.Duration;

@Getter
@Setter
@Component
public class SimulationContextModel implements BaseModel {
	
	
	private PropertyChangeSupport support; 
	private Boolean awayMode; 
	private Boolean personDetected;
    private Duration alertAuthoritiesTime;

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
		support = new PropertyChangeSupport(this);
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

	/**
	 * add a PropertyChangeListener, essentially an observable due to deprecation
	 * @param pcl
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
	}
	
	/**
	 * remove a propertyChangeListner, essentially an observable due to deprecation
	 * 
	 * @param pcl
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
	
	/**
	 * update all propteryChangeListeners of change in awayMode only if no one is home.
	 * @param value
	 */
	public void updateAwayMode(Boolean value){

		int numberOfPeopleHome = 0;

		for (RoomRow row: this.getHouseLayoutModel().getRows()) {
			for (Room room : row.getRooms()) {
				if(room.getPersons().size()>0){
					numberOfPeopleHome += room.getPersons().size();
				}
			}
		}

		if(numberOfPeopleHome>0){
			System.out.println("Cannot activate Away mode because there are still people home. Please remove them to activate AwayMode.");
		}else if( this.getAwayMode() == false){
			System.out.println("Activating Away mode!");
			this.support.firePropertyChange("awayMode", this.awayMode, value);
			this.setAwayMode(value);
		}else{
			System.out.println("Away mode is already actived.");
		}
	}

	/**
	 * update all propteryChangeListeners of change in DetectedPerson
	 * @param value
	 */
	public void updateDetectedPerson(Boolean value){
		this.support.firePropertyChange("detectedPerson", this.personDetected, value);
		this.setPersonDetected(value);
	}

	/**
	 * update duration of auhtoritiesTimer
	 * @param duration
	 */
	public void updateAuthoritiesTimer(java.time.Duration duration){
		this.support.firePropertyChange("alertAuthoritiesTime", this.getAlertAuthoritiesTime(), duration);
		this.setAlertAuthoritiesTime(alertAuthoritiesTime);
	}
}
