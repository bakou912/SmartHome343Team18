import React from "react";
import ParametersService from '../service/ParametersService';
import "../style/SimulationParametersView.css";
import HouseLayoutUpload from "../component/houselayout/HouseLayoutUpload.js";
import HouseLayout from "./HouseLayout";
import { Container, Row, Col} from 'reactstrap';
import {Button} from 'react-bootstrap';
import httpLayoutService from "../service/HouseLayoutService";

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
            },
            file:null,
            uploadedFile:false,
        };
        this.tempChangeHandler = this.tempChangeHandler.bind(this);
        this.saveParametersChanges = this.saveParametersChanges.bind(this);
        this.onSelectedUser = this.onSelectedUser.bind(this);
        this.onDateSelected = this.onDateSelected.bind(this);
        this.onTimeSelected = this.onTimeSelected.bind(this);
        this.fileChangedHandler = this.fileChangedHandler.bind(this);
        this.fileUploadHandler = this.fileUploadHandler.bind(this);
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
    async fileChangedHandler(event) {
        this.setState({
            file: event.target.files[0]
        });
    }
    async fileUploadHandler() {
        await httpLayoutService.createLayout(this.state.file)
        .then(() =>{
          /*For now I'll just refresh the page*/
          window.location.reload(false);
          this.setState({
            uploadedFile:true,
          });
        })
        .catch(() =>{
          alert("Invalid File");
          this.setState({
            uploadedFile:false,
          });
        });
    }
    render() {
        return (
            <Container fluid className="SimulationParameters">
              <Row>
                <Col>
                  <Container fluid>
                    <Row>
                      <Col className="SimulationParameters_Title" sm={12}>
                        <h1>Simulation Parameters</h1>
                      </Col>
                    </Row>
                    <Row>
                      <Col lg={12}>
                        <Container className="SimulationParameters_Parameters_Container">
                          <Row>
                            <Col lg={6}>
                              <img src="/user.png" alt="profile pic" width="150"/>
                              <br/>
                              <select className="SimulationParameters_User_Select" onChange={this.onSelectedUser} defaultValue="PARENT">
                                <option value="PARENT">Parent</option>
                                <option value="CHILD">Child</option>
                                <option value="VISITOR">Visitor</option>
                                <option value="STRANGER">Stranger</option>
                              </select>
                            </Col>
                            <Col lg={6} className="SimulationParameters_Parameters_Col_Two">
                              <Container>
                                <Row>
                                  <Col>
                                    <label>Outside Temperature</label>
                                  </Col>
                                  <Col>
                                    <input name="outsideTemp" type="number" onChange={this.tempChangeHandler} style={{width:"120px"}}/>
                                  </Col>
                                </Row>
                                <Row>
                                  <Col>
                                    <label>Inside Temperature</label>
                                  </Col>
                                  <Col>
                                    <input name="insideTemp" type="number" onChange={this.tempChangeHandler} style={{width:"120px"}}/>
                                  </Col>
                                </Row>
                                <Row>
                                  <Col>
                                    <label>Date</label><br/>
                                    <input type="date" name="date" onChange={this.onDateSelected}/>
                                  </Col>
                                </Row>
                                <Row>
                                  <Col>
                                    <label>Time</label><br/>
                                    <input type="time" name="time" onChange={this.onTimeSelected}/>
                                  </Col>
                                </Row>
                              </Container>
                            </Col>
                          </Row>
                          <Row>
                            <Col sm={2}>
                            </Col>
                          </Row>
                        </Container>
                      </Col>

                    </Row>
                  </Container>
                </Col>
                <Col>
                  <Container className="SimulationParameters_HouseLayout_Container">
                    <Row>
                      <Col lg={10}>
                        <HouseLayout/>
                      </Col>
                    </Row>
                    <Row>
                      <Col>
                        <Container>
                            <p>Please enter a valid House Layout initialization file:</p>
                            <input
                                type="file"
                                name="file"
                                onChange={this.fileChangedHandler}
                            />
                            <br/><br/>
                            <Button onClick={this.fileUploadHandler}>
                                Create Layout
                            </Button>
                        </Container>
                      </Col>
                    </Row>
                  </Container>
                </Col>
              </Row>
              <Row>
                <Col className="SimulationParameters_Buttons_Row">
                  <Button className="SimulationParameters_Buttons" variant="secondary" size="sm">Cancel</Button>
                  <Button className="SimulationParameters_Buttons" onClick={this.saveParametersChanges} variant="primary" size="lg">Apply</Button>
                </Col>
              </Row>
            </Container>
        );
    }

}
