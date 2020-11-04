import React from "react";
import "../style/SimulationContextView.css";
import {Container, Modal, Button, Col, Row} from "react-bootstrap";
import SimulationContextService from "../service/SimulationContextService";
import HouseLayoutService from "../service/HouseLayoutService";
import Select from "react-select";
import ParametersService from "../service/ParametersService";
import {EditUserProfiles} from "../component/EditUserProfiles";

export default class SimulationContext extends React.Component {

    contextModel = {};
    dateTime = new Date();
    profiles = {}

    constructor(props) {
        super(props);

        this.state = {
            loaded: false,
            editingParameters: false
        };

        this.handleNameChange = this.handleNameChange.bind(this);
        this.onSelectedLocation = this.onSelectedLocation.bind(this);
        this.onSelectedProfile = this.onSelectedProfile.bind(this);
        this.tempChangeHandler = this.tempChangeHandler.bind(this);
        this.onDateSelected = this.onDateSelected.bind(this);
        this.onTimeSelected = this.onTimeSelected.bind(this);
        this.saveEdit = this.saveEdit.bind(this);
        this.toggleSimulationState = this.toggleSimulationState.bind(this);
    }

    async componentDidMount() {
        this.contextModel = (await SimulationContextService.getContext()).data;
        this.profiles = await ParametersService.getProfiles(this.contextModel.parameters.userProfiles.profiles);

        const date = new Date(this.contextModel.parameters.sysParams.date);
        this.dateTime = new Date(date.getTime() - date.getTimezoneOffset() * 60000);

        await this.setState({
            user: this.contextModel.parameters.user,
            outsideTemp: this.contextModel.parameters.sysParams.outsideTemp,
            simulationState: this.contextModel.state,
            date: this.dateTime.toISOString().substring(0, 10),
            time: this.dateTime.toISOString().substring(11, 19),
            rooms: await HouseLayoutService.getAllLocations(this.contextModel.layout)
        });

        await this.setState({
            firstState: this.state,
            loaded: true
        });
    }

    async handleNameChange(evt) {
        const targetValue = evt.target.value;

        if (targetValue.length > 20) {
            return;
        }

        await this.setState( {
            user: {
                ...this.state.user,
                name: evt.target.value
            }
        });
    }

    async onSelectedLocation(evt) {
        if (evt.label === "Outside") {
            evt.value.name = "Outside";
        }

        await this.setState({
            user: {
                ...this.state.user,
                location: evt.value
            }
        });
    }

    async onSelectedProfile(evt) {
        await this.setState({
            user: {
                ...this.state.user,
                profile: evt.value
            }
        });
    }

    async setModal(value) {
        await this.setState({
            editingParameters: value
        });
    }

    async tempChangeHandler(evt) {
        await this.setState({
            outsideTemp: evt.target.value
        });
    }

    async onDateSelected(evt) {
        await this.setState({
            date: evt.target.value
        });
    }

    async onTimeSelected(evt) {
        await this.setState({
            time: evt.target.value
        });
    }

    async saveEdit() {
        const userInput = {
            ...this.state.user,
            profile: this.state.user.profile.name
        }

        await SimulationContextService.modifyUser(userInput);
        await SimulationContextService.modifySysParams({
            dateTime: this.state.date + "T" + this.state.time + (this.state.time.length === 5 ? ":00" : ""),
            outsideTemp: this.state.outsideTemp
        });

        await window.location.reload();
    }

    async toggleSimulationState() {
        await SimulationContextService.toggleState()
            .then(async response => {
                await this.setState({
                    simulationState: response.data
                })
            })
            .catch(() => {
                alert("Simulation state could not be changed");
            })
    }

    render() {
        return (
            <Container>
                {
                    this.state.loaded ?
                        <div className="SimulationContext">
                            <Button className="StartButton" size="sm" variant={this.state.simulationState === "ON" ? "danger" : "primary"} onClick={this.toggleSimulationState}>
                                {this.state.simulationState === "ON" ? "Stop" : "Start"}
                            </Button>
                            <Button className="EditButton" size="sm" variant="secondary" onClick={() => this.setModal(true)}>Edit</Button>
                            <br/>
                            <br/>
                            <img src="/user.png" alt="profile pic" width="100px"/>
                            <br/>
                            {this.state.firstState.user.name}
                            <br/>
                            Profile: {`${this.state.firstState.user.profile.name} `}
                            <EditUserProfiles profiles={this.profiles}/>
                            <br/>
                            <br/>
                            Location: {this.state.firstState.user.location.name}
                            <br/>
                            <br/>
                            Outside temperature: {this.state.firstState.outsideTemp}&deg;C
                            <br/>
                            <br/>
                            {this.dateTime.toDateString()}
                            <br/>
                            {this.state.firstState.time}
                            <br/>
                            <br/>
                            <Modal show={this.state.editingParameters} onHide={() => this.setModal(false)}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Simulation Context Editing</Modal.Title>
                                </Modal.Header>
                                <Modal.Body style={{display: "flex", justifyContent: "center"}}>
                                    <div>
                                        Name
                                        <br/>
                                        <input type="text" placeholder="User" maxLength="20" value={this.state.user.name} onChange={this.handleNameChange}/>
                                        <br/>
                                        <br/>
                                        Profile
                                        <div className="SelectDiv">
                                            <Select
                                                styles={{
                                                    option: provided => ({...provided, width: "100%"}),
                                                    menu: provided => ({...provided, width: "100%"}),
                                                    control: provided => ({...provided, width: "100%"}),
                                                    singleValue: provided => provided
                                                }}
                                                options={this.profiles}
                                                onChange={this.onSelectedProfile}
                                                defaultValue={this.profiles.filter(profile => profile.value.name === this.state.user.profile.name)}
                                            />
                                        </div>
                                        <br/>
                                        Location
                                        <div className="SelectDiv">
                                            <Select
                                                styles={{
                                                    option: provided => ({...provided, width: "100%"}),
                                                    menu: provided => ({...provided, width: "100%"}),
                                                    control: provided => ({...provided, width: "100%"}),
                                                    singleValue: provided => provided
                                                }}
                                                options={this.state.rooms}
                                                onChange={this.onSelectedLocation}
                                                defaultValue={this.state.rooms.filter(room => room.label === this.state.user.location.name)}
                                            />
                                        </div>
                                        <br/>
                                        <label>Outside Temperature</label>
                                        <br/>
                                        <input name="outsideTemp" type="number" value={this.state.outsideTemp} onChange={this.tempChangeHandler} style={{width: "120px"}}/>
                                        <br/>
                                        <br/>
                                        <Row>
                                            <Col>
                                                <label>Date</label>
                                                <br/>
                                                <input type="date" name="date" value={this.state.date} onChange={this.onDateSelected}/>
                                            </Col>
                                            <Col>
                                                <label>Time</label>
                                                <br/>
                                                <input type="time" name="time" value={this.state.time} onChange={this.onTimeSelected}/>
                                            </Col>
                                        </Row>
                                    </div>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button onClick={this.saveEdit}>Save</Button>
                                </Modal.Footer>
                            </Modal>
                        </div>
                        : null
                }
            </Container>
        )
    }

}
