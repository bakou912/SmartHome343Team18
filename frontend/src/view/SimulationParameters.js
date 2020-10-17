import React from "react";
import ParametersService from '../service/ParametersService';
import "../style/SimulationParametersView.css";

export default class SimulationParameters extends React.Component {

    time = undefined;
    date = undefined;
    parametersInput = {};

    constructor(props) {
        super(props);
        this.state  =  {
            parametersInput:{
                insideTemp:null,
                outsideTemp:null,
                dateTime:null,
            },
            profileInput: {}
        };
        this.tempChangeHandler = this.tempChangeHandler.bind(this);
        this.saveParametersChanges = this.saveParametersChanges.bind(this);
        this.onSelectedUser = this.onSelectedUser.bind(this);
        this.onDateSelected = this.onDateSelected.bind(this);
        this.onTimeSelected = this.onTimeSelected.bind(this);
    }

    onSelectedUser(evt) {
        this.setState({
            profileInput:{
                role:evt.target.value
            }
        });
    }
    tempChangeHandler(evt) {
        this.parametersInput[evt.target.name] = evt.target.value;
    }

    async saveParametersChanges() {
        this.parametersInput.dateTime = this.date+"T"+this.time+":00";
        this.state.parametersInput = this.parametersInput;

        await ParametersService.saveParams(this.state)
        .then(() => {
            window.location = "http://localhost:3000/dashboard";
        })
        .catch(() => {
            alert("One or more system parameters were inappropriate");
        })
    }

    async onDateSelected(evt){
        this.date = evt.target.value;
    }
    async onTimeSelected(evt){
        this.time = evt.target.value;
    }

    render() {
        return (
                <div className="SimulationParametersView">
                    <div className="SimulationParametersView_center">
                        <div className="SimulationParametersView_profile">
                            <h2>Profile Information</h2>
                            <div className="profile">
                                <img src="/user" alt="profile pic" width="150"/>
                                <br/>
                                <select onChange={this.onSelectedUser}>
                                    <option selected="selected">PARENT</option>
                                    <option>CHILD</option>
                                    <option>VISITOR</option>
                                    <option>STRANGER</option>
                                </select>
                            </div>
                        </div>
                        <div className="SimulationParametersView_simulationParameters">
                            <h2>System Parameters</h2>
                            <div className="systemParameters">
                                <label>Outside Temperature
                                    <input type="number" name="insideTemp" onChange={this.tempChangeHandler} />&deg;C
                                </label>
                                <br/>
                                <label>Inside Temperature<input type ="number" name="outsideTemp" onChange={this.tempChangeHandler} />&deg;C</label>
                                <div className="systemParameters_date">
                                    <input type="date" onChange={this.onDateSelected}/>
                                </div>
                                <div className="systemParameters_time">
                                    <input type="time" onChange={this.onTimeSelected}/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="SimulationParametersView_buttons">
                        <button className="SimulationParametersView_buttons_skip">Cancel</button>
                        <button className="SimulationParametersView_buttons_apply" onClick={this.saveParametersChanges}>Apply</button>
                    </div>
                </div>
        );
    }

}
