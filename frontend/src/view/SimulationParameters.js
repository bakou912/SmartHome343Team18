import React from "react";
import ParametersService from '../service/ParametersService';
import "../style/SimulationParametersView.css";
import HouseLayoutUpload from "../component/houselayout/HouseLayoutUpload.js";
import HouseLayout from "./HouseLayout";
import { Container, Row, Col} from 'reactstrap';
import {Button} from 'react-bootstrap';

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
            profileInput: {
                role:"PARENT",
            }
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
            <Container className="SimulationParameters_Container">
              <Row>
                <h1 className="SimulationParameters_Title">Simulation Parameters</h1>
              </Row>
              <Row className="SimulationParameters_Container_Row_Two">
                <Col>
                  <Container className="SimulationParameters_Profile_System_Container">
                    <Row>
                      <Container className="SimulationsParameters_Parameters_Container">
                        <Row>
                          <Col className="SimulationsParameters_Parameters_Container_Profile_Container">
                            <Row>
                              <img src="/user.png" alt="profile pic" width="150"/>
                            </Row>
                            <Row>
                              <select className="SimulationParameters_User_Select" onChange={this.onSelectedUser} defaultValue="PARENT">
                                <option value="PARENT">Parent</option>
                                <option value="CHILD">Child</option>
                                <option value="VISITOR">Visitor</option>
                                <option value="STRANGER">Stranger</option>
                              </select>
                            </Row>
                          </Col>
                          <Col>
                            <Container className="SimulationParameters_System_Parameters_Container">
                              <Row>
                                <label>Outside Temperature
                                  <input id="outsideTemp" name="outsideTemp" type="number"/>
                                </label>
                              </Row>
                              <Row>
                                <label>Inside Temperature
                                  <input id="insideTemp" name="insideTemp" type="number"/>
                                </label>
                              </Row>
                              <Row>
                                <span>Date</span>
                                  <input type="date" name="date" onChange={this.onDateSelected}/>
                              </Row>
                              <Row>
                                <span>Time</span>
                                  <input type="time" name="time" onChange={this.onTimeSelected}/>
                              </Row>
                            </Container>
                          </Col>
                        </Row>
                      </Container>
                    </Row>
                  </Container>
                </Col>
                <Col>
                  <Container className="SimulationParameters_HouseLayout_Container">
                    <Row>
                      <HouseLayout/>
                    </Row>
                    <Row>
                      <HouseLayoutUpload/>
                    </Row>
                  </Container>
                </Col>
              </Row>
              <Row className="SimulationParameters_Buttons_Row">
                <Button variant="secondary" size="sm">Cancel</Button>
                <Button onClick={this.saveParametersChanges} variant="primary" size="lg">Apply</Button>
              </Row>
            </Container>
        );
    }

}
