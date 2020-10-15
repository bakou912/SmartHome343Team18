import React from "react";
import ParametersService from '../service/ParametersService';
import "../component/css/SimulationParametersView.css";
import {Redirect} from 'react-router-dom';

export default class HouseLayout extends React.Component {

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
            profileInput:{
                role:"PARENT"
            },
            redirect:false,
        };
        this.tempChangeHandler = this.tempChangeHandler.bind(this);
        this.saveParametersChanges = this.saveParametersChanges.bind(this);
        this.onSelectedUser = this.onSelectedUser.bind(this);
        this.onDateSelected = this.onDateSelected.bind(this);
        this.onTimeSelected = this.onTimeSelected.bind(this);
    }

//    async componentDidMount() {
//        await this.setState({
//            parametersModel:(await ParametersService.getParams()).data,
//        });
//    }
    async onSelectedUser(evt){
        this.setState({
            profileInput:{
                role:evt.target.value
            }
        })
        console.log("changed profile");
    }
    async tempChangeHandler(evt){
        const value  =  evt.target.value;
        this.parametersInput[evt.target.name] = value;
        console.log(this.state.insideTemp)
    }
    async saveParametersChanges(){
        this.parametersInput.dateTime  =  this.date+"T"+this.time+":00";
        this.state.parametersInput = this.parametersInput;
        await ParametersService.saveParams(this.state)
        .then((_)=>{
            this.setState({redirect:true});
        })
        .catch((error)=>{
            alert("One or more system parameters were inappropriate");
        })
    }
    async onDateSelected(evt){
        this.date  =  evt.target.value;
        console.log(this.date);
    }
    async onTimeSelected(evt){
        this.time = evt.target.value;
        console.log(this.time);
    }

    render() {
        return (
                <div className = "simulationParametersView">
                      {this.state.redirect ? <Redirect path="http://localhost:3000//dashboard"/> :null}
                      <div className = "simulationParametersView_center">
                        <div className = "simulationParametersView_profile">
                          <h2>Profile Information</h2>
                          <div className = "profile">
                            <img src = "https://cdn.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png" alt = "profile pic"
                            width = "150"/>
                            <br/>
                            <select onChange = {this.onSelectedUser}>
                              <option selected = {this.state?.profile  ===  "PARENT" ? "selected":""}>PARENT</option>
                              <option selected = {this.state?.profile  ===  "CHILD" ? "selected":""}>CHILD</option>
                              <option selected = {this.state?.profile  ===  "VISITOR" ? "selected":""}>VISITOR</option>
                              <option selected = {this.state?.profile  ===  "STRANGER" ? "selected":""}>STRANGER</option>
                            </select>
                          </div>
                        </div>
                        <div className = "simulationParametersView_simulationParameters">
                          <h2>System Parameters</h2>
                          <div className = "systemParameters">
                            <label>Outside Temperature
                                <input type = "number" name = "insideTemp" onChange = {this.tempChangeHandler} />&deg;C</label>
                            <br/>
                            <label>Inside Temperature<input type = "number" name = "outsideTemp" onChange = {this.tempChangeHandler} />&deg;C</label>
                            <div className = "systemParameters_date">
                                <input type = "date" onChange = {this.onDateSelected}/>
                            </div>
                            <div className = "systemParameters_time">
                              <input type = "time" onChange = {this.onTimeSelected}/>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className = "simulationParametersView_buttons">
                        <button className = "simulationParametersView_buttons_skip">Cancel</button>
                        <button className = "simulationParametersView_buttons_apply" onClick = {this.saveParametersChanges}>Apply</button>
                      </div>
                </div>
        );
    }

}
