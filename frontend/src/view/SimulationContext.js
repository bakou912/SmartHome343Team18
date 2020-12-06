import React from "react";
import "../style/SimulationContextView.css";
import {Container, Modal, Button, Col, Row} from "react-bootstrap";
import SimulationContextService from "../service/SimulationContextService";
import HouseLayoutService from "../service/HouseLayoutService";
import Select from "react-select";
import ParametersService from "../service/ParametersService";
import {EditUserProfiles} from "../component/EditUserProfiles";
import SmartHomeSecurityService from "../service/SmartHomeSecurityService";

const OUTSIDE = ["Backyard", "Entrance"];

export default class SimulationContext extends React.Component {

    contextModel = {};
    dateTime = new Date();
    profiles = {}
    awayModeHours = null;
    hoursUpdate = {
        from: false,
        to: false
    };
    simulatorTimeHandler = null;

    constructor(props) {
        super(props);

        this.state = {
            loaded: false,
            editingParameters: false,
            editingContext: false,
            selectedContextLocation: null,
            selectedWindow: null,
            selectedPerson: null,
            addingPerson: false,
            personUpdateKey: 0,
            timeSpeed: 1
        };

        this.handleNameChange = this.handleNameChange.bind(this);
        this.addPerson = this.addPerson.bind(this);
        this.removePerson = this.removePerson.bind(this);
        this.setEditing = this.setEditing.bind(this);
        this.handlePersonNameChange = this.handlePersonNameChange.bind(this);
        this.onSelectedLocation = this.onSelectedLocation.bind(this);
        this.onSelectedContextLocation = this.onSelectedContextLocation.bind(this);
        this.onSelectedProfile = this.onSelectedProfile.bind(this);
        this.tempChangeHandler = this.tempChangeHandler.bind(this);
        this.onDateSelected = this.onDateSelected.bind(this);
        this.onTimeSelected = this.onTimeSelected.bind(this);
        this.onTimeSpeedSelected = this.onTimeSpeedSelected.bind(this);
        this.onSelectedItem = this.onSelectedItem.bind(this);
        this.blockWindow = this.blockWindow.bind(this);
        this.saveEdit = this.saveEdit.bind(this);
        this.toggleSimulationState = this.toggleSimulationState.bind(this);
        this.setAwayModeTimes = this.setAwayModeTimes.bind(this);
        this.simulatorTime = this.simulatorTime.bind(this);
        this.checkAwayModeHours = this.checkAwayModeHours.bind(this);
    }

    async componentDidMount() {
        window.addEventListener("awayModeOn", async () => {
            await this.setAwayModeTimes();
        });

        await this.setAwayModeTimes();

        this.contextModel = (await SimulationContextService.getContext()).data;
        this.profiles = await ParametersService.getProfiles(this.contextModel.parameters.userProfiles.profiles);

        const date = new Date(this.contextModel.parameters.sysParams.date);
        this.dateTime = new Date(date.getTime() - date.getTimezoneOffset() * 60000);

        await this.setState({
            user: this.contextModel.parameters.user,
            outsideTemp: this.contextModel.parameters.sysParams.outsideTemp,
            timeSpeed: this.contextModel.parameters.sysParams.timeSpeed,
            simulationState: this.contextModel.state,
            date: this.dateTime.toISOString().substring(0, 10),
            time: this.dateTime.toISOString().substring(11, 19),
            rooms: await HouseLayoutService.getAllLocations(this.contextModel.layout)
        });

        await this.setState({
            firstState: this.state,
            loaded: true
        });

        this.simulatorTimeHandler = setInterval(async () => await this.simulatorTime(),1000 / this.state.timeSpeed);
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

    async handlePersonNameChange(evt) {
        const targetValue = evt.target.value;

        if (targetValue.length > 20) {
            return;
        }

        await this.setState({
            personName: evt.target.value
        });
    }

    async onSelectedLocation(evt) {
        await this.setState({
            user: {
                ...this.state.user,
                location: evt.value
            }
        });
    }

    async onSelectedContextLocation(evt) {
        await this.setState({
            rooms: await HouseLayoutService.getAllLocations(),
            selectedWindow: null,
            selectedPerson: null,
            selectedContextLocation: {
                value: evt.value,
                label: evt.label
            },
            windows: OUTSIDE.includes(evt.label) ? [] : evt.value.windows.map(w => {
                return {
                    value: w,
                    label: w.direction
                };
            }),
            persons: evt.value.persons.map(p => {
                return {
                    value: p,
                    label: p.name
                };
            }),
            personName: "",
            updateLocationKey: this.state.updateLocationKey + 1
        });
    }

    async onSelectedItem(item, evt) {
        await this.setState({
            [`selected${item}`]: {
                ...evt
            }
        });
    }

    async onSelectedProfile(evt) {
        await this.setState({
            user: {
                ...this.state.user,
                profile: evt.value
            },
            updateProfileKey: this.state.updateProfileKey + 1
        });
    }

    async setModal(modalName, value) {
        await this.setState({
            [`editing${modalName}`]: value,
            selectedWindow: null,
            selectedPerson: null,
            selectedContextLocation: null
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

    async onTimeSpeedSelected(evt) {
        await this.setState({
            timeSpeed: evt.target.value
        });

        await clearInterval(this.simulatorTimeHandler);
        this.simulatorTimeHandler = setInterval(async () => await this.simulatorTime(),1000 / this.state.timeSpeed);
    }

    async saveEdit() {
        const userInput = {
            ...this.state.user,
            profile: this.state.user.profile.name
        }

        await SimulationContextService.modifyUser(userInput);
        await SimulationContextService.modifySysParams({
            dateTime: this.state.date + "T" + this.state.time + (this.state.time.length === 5 ? ":00" : ""),
            timeSpeed: this.state.timeSpeed,
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
            });
    }


    async blockWindow(block) {
        let windowState;
        if (block === true) {
            windowState = "BLOCKED";
            await SimulationContextService.blockWindow(this.state.selectedContextLocation.value.rowId, this.state.selectedContextLocation.value.roomId, this.state.selectedWindow.value.id)
        } else {
            windowState = "CLOSED";
            await SimulationContextService.unblockWindow(this.state.selectedContextLocation.value.rowId, this.state.selectedContextLocation.value.roomId, this.state.selectedWindow.value.id)
        }

        this.setState({
            selectedWindow: {
                ...this.state.selectedWindow,
                value: {
                    ...this.state.selectedWindow.value,
                    state: windowState
                }
            }
        });

        await window.dispatchEvent(new Event("updateLayout"));
        await window.dispatchEvent(new Event("updatePermissions"));
    }

    async addPerson() {
        const action = OUTSIDE.includes(this.state.selectedContextLocation.label) ?
            async () => SimulationContextService.addPersonOutside({ location: this.state.selectedContextLocation.label, name: this.state.personName})
            :
            async () => SimulationContextService.addPersonToRoom(this.state.selectedContextLocation.value.rowId, this.state.selectedContextLocation.value.roomId,  {name: this.state.personName});

        await action().then(async response => {
            const person = {
                value: {
                    id: response.data,
                    name: this.state.personName
                },
                label: this.state.personName
            };

            const updatedPersons = this.state.persons;
            updatedPersons.push(person);

            await this.setState({
                persons: updatedPersons,
                personUpdateKey: this.state.personUpdateKey + 1,
                addingPerson: false,
                personName: ""
            });

            window.dispatchEvent(new Event("updateLayout"));
        }).catch(err => {
            if (err.response.status === 409) {
                alert("A person with this name is already present.");
            }
        })

    }

    async removePerson() {
        const action = OUTSIDE.includes(this.state.selectedContextLocation.label) ?
            async () => SimulationContextService.removePersonFromOutside(this.state.selectedContextLocation.label, this.state.selectedPerson.value.id)
            :
            async () => SimulationContextService.removePersonFromRoom(this.state.selectedContextLocation.value.rowId, this.state.selectedContextLocation.value.roomId, this.state.selectedPerson.value.id);

        await action().then(async () =>{
            await this.setState({
                persons: this.state.persons.filter(person => person.value.name !== this.state.selectedPerson.value.name),
                personUpdateKey: this.state.personUpdateKey + 1,
                selectedPerson: null
            });
        });
        window.dispatchEvent(new Event("updateLayout"));
    }

    async setEditing(value) {
        await this.setState({
            addingPerson: value
        });
    }

    async setAwayModeTimes() {
        this.awayModeHours = (await SmartHomeSecurityService.getAwayModeHours()).data;
    }
    
    async simulatorTime() {
        if (this.state.simulationState === "OFF" || this.state.editingParameters === true) {
            return;
        }
        this.dateTime.setSeconds(this.dateTime.getSeconds() + 1);

        await this.setState({
            date: this.dateTime.toISOString().substring(0, 10),
            time: this.dateTime.toISOString().substring(11, 19)
        });

        await this.checkAwayModeHours();

        await window.dispatchEvent(new CustomEvent("updateSelectedZone", { detail: true}));
        await window.dispatchEvent(new Event("updateConsole"));
    }

    async checkAwayModeHours() {
        const time = this.dateTime.toISOString().substring(11, 19);

        if (time >= this.awayModeHours.from) {
            if (time <= this.awayModeHours.to && this.hoursUpdate.from === false && this.hoursUpdate.to === false) {
                this.hoursUpdate.to = true;
                await window.dispatchEvent(new Event("updateLayout"));
            } else {
                this.hoursUpdate.to = false;
            }
            this.hoursUpdate.from = true;
        } else {
            this.hoursUpdate.from = false;
        }
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
                            <Button className="EditButton" size="sm" variant="secondary" onClick={() => this.setModal("Parameters", true)}>Parameters</Button>
                            <br/>
                            <br/>
                            <Button className="EditButton" size="sm" variant="secondary" onClick={() => this.setModal("Context", true)}>Context</Button>
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
                            {`${this.state.time} (x${this.state.firstState.timeSpeed})`}
                            <br/>
                            <br/>
                            <Modal show={this.state.editingParameters} onHide={() => this.setModal("Parameters", false)}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Parameters Editing</Modal.Title>
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
                                                key={""}
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
                                            <Col>
                                                <label>Time Speed</label>
                                                <br/>
                                                <input type="number" name="timeSpeed" min={1} max={500} value={this.state.timeSpeed} onChange={this.onTimeSpeedSelected}/>
                                            </Col>
                                        </Row>
                                    </div>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button onClick={this.saveEdit}>Save</Button>
                                </Modal.Footer>
                            </Modal>
                            <Modal show={this.state.editingContext} onHide={() => this.setModal("Context", false)}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Context Editing</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container style={{display: "flex", justifyContent: "center"}}>
                                        <div className="SelectDiv">
                                            Locations
                                            <Select
                                                key={this.state.updateLocationKey}
                                                styles={{
                                                    option: provided => ({...provided, width: "100%"}),
                                                    menu: provided => ({...provided, width: "100%"}),
                                                    control: provided => ({...provided, width: "100%"}),
                                                    singleValue: provided => provided
                                                }}
                                                options={this.state.rooms}
                                                onChange={this.onSelectedContextLocation}
                                            />
                                            {
                                                this.state.selectedContextLocation !== null ?
                                                    <Container>
                                                        <Row>
                                                            <Col>
                                                                <br/>
                                                                {
                                                                    !OUTSIDE.includes(this.state.selectedContextLocation.label) ?
                                                                        <div>
                                                                            Windows
                                                                            <Select
                                                                                key={this.state.updateLocationKey}
                                                                                styles={{
                                                                                    option: provided => ({...provided, width: "100%"}),
                                                                                    menu: provided => ({...provided, width: "100%"}),
                                                                                    control: provided => ({...provided, width: "100%"}),
                                                                                    singleValue: provided => provided
                                                                                }}
                                                                                options={this.state.windows}
                                                                                value={this.state.selectedWindow}
                                                                                onChange={(evt) => this.onSelectedItem("Window", evt)}
                                                                            />
                                                                            {
                                                                                this.state.selectedWindow !== null ?
                                                                                    <div>
                                                                                        {
                                                                                            this.state.selectedWindow.value.state === "BLOCKED" ?
                                                                                                <Button onClick={() => this.blockWindow(false)}
                                                                                                        variant="secondary"
                                                                                                        size="sm">Unobstruct</Button>
                                                                                                :
                                                                                                <Button onClick={() => this.blockWindow(true)}
                                                                                                        variant="secondary"
                                                                                                        size="sm">Obstruct</Button>
                                                                                        }
                                                                                    </div>
                                                                                    : null
                                                                            }
                                                                        </div>
                                                                        : null
                                                                }
                                                                <br/>
                                                                Persons
                                                                <Select
                                                                    key={`${this.state.personUpdateKey}${this.state.updateLocationKey}`}
                                                                    styles={{
                                                                        option: provided => ({...provided, width: "100%"}),
                                                                        menu: provided => ({...provided, width: "100%"}),
                                                                        control: provided => ({...provided, width: "100%"}),
                                                                        singleValue: provided => provided
                                                                    }}
                                                                    options={this.state.persons}
                                                                    onChange={(evt) => this.onSelectedItem("Person", evt)}
                                                                />
                                                                <Row>
                                                                    <Col>
                                                                        {
                                                                            this.state.addingPerson ?
                                                                                <div>
                                                                                    <input
                                                                                        type="text" placeholder="Name" maxLength="20"
                                                                                        value={this.state.personName}
                                                                                        onChange={this.handlePersonNameChange}
                                                                                    />
                                                                                    <Button onClick={async () => await this.setState({ addingPerson: false })} variant="dark" size="sm">
                                                                                        X
                                                                                    </Button>
                                                                                    <Button onClick={this.addPerson} variant="secondary" size="sm">
                                                                                        Save
                                                                                    </Button>
                                                                                </div>
                                                                                :
                                                                                <div>
                                                                                    <Button onClick={() => this.setEditing(true)} variant="secondary"
                                                                                            size="sm">Add</Button>
                                                                                    {
                                                                                        this.state.selectedPerson !== null?
                                                                                            <Button onClick={this.removePerson} variant="secondary"
                                                                                                    size="sm">Remove</Button>
                                                                                            : null
                                                                                    }
                                                                                </div>
                                                                        }
                                                                    </Col>
                                                                </Row>
                                                            </Col>
                                                        </Row>
                                                    </Container>
                                                    : null
                                            }
                                        </div>
                                    </Container>
                                </Modal.Body>
                            </Modal>
                        </div>
                        : null
                }
            </Container>
        )
    }

}
