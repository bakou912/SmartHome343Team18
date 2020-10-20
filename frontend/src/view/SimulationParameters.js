import React from "react";
import ParametersService from '../service/ParametersService';
import "../style/SimulationParametersView.css";
import HouseLayout from "./HouseLayout";
import {Button, Container, Row, Col} from 'react-bootstrap';
import HouseLayoutService from "../service/HouseLayoutService";
import Select from 'react-select'

export default class SimulationParameters extends React.Component {

    time = undefined;
    date = undefined;
    parametersInput = {};
    userInput = {
        profile: "PARENT",
        name: null
    };
    layoutKey = 0;

    static profiles = [
        { value: "PARENT", label: "Parent"},
        { value: "CHILD", label: "Child"},
        { value: "VISITOR", label: "Visitor"},
        { value: "STRANGER", label: "Stranger"}
    ]

    constructor(props) {
        super(props);
        this.state = {
            parametersInput: {
                insideTemp: null,
                outsideTemp: null,
                dateTime: null
            },
            userInput: {
                profile: "PARENT",
                name: null
            },
            file: null,
            uploadedFile: localStorage.getItem("uploadedFile") === "true",
            rooms: [],
            userLocation: null
        };

        this.tempChangeHandler = this.tempChangeHandler.bind(this);
        this.userNameHandler = this.userNameHandler.bind(this);
        this.saveParametersChanges = this.saveParametersChanges.bind(this);
        this.onSelectedProfile = this.onSelectedProfile.bind(this);
        this.onSelectedLocation = this.onSelectedLocation.bind(this);
        this.onDateSelected = this.onDateSelected.bind(this);
        this.onTimeSelected = this.onTimeSelected.bind(this);
        this.fileChangedHandler = this.fileChangedHandler.bind(this);
        this.fileUploadHandler = this.fileUploadHandler.bind(this);
        this.getHouseLayout = this.getHouseLayout.bind(this);
    }

    async componentDidMount() {
        if (localStorage.getItem("parametersSet") === "true") {
            this.redirectToDashboard();
        }

        await this.getHouseLayout();
    }

    async getHouseLayout() {
        let selectRooms = [];

        if (this.state.uploadedFile === true) {
            selectRooms = await HouseLayoutService.getAllLocations();
        }

        await this.setState({
            houseLayout: <HouseLayout key={this.layoutKey++}/>,
            rooms: selectRooms
        })
    }

    async onSelectedProfile(evt) {
        await this.setState({
            userInput: {
                ...this.state.userInput,
                profile: evt.value
            }
        });
    }

    async onSelectedLocation(evt) {
        if (evt.label === "Outside") {
            evt.value.name = "Outside";
        }

        await this.setState({
            userLocation: evt.value,
            userInput: {
                ...this.state.userInput,
                location: evt.value
            }
        });
    }

    tempChangeHandler(evt) {
        this.parametersInput[evt.target.name] = evt.target.value;
    }

    userNameHandler(evt) {
        this.setState({
            userInput: {
                ...this.state.userInput,
                name: evt.target.value
            }
        });
    }

    async saveParametersChanges() {
        this.parametersInput.dateTime = this.date + "T" + this.time + (this.time && this.time.length === 5 ? ":00" : "");
        await this.setState({
            parametersInput: this.parametersInput
        });

        if (this.state.userInput.name === null) {
            alert("A name for the user must be set");
            return;
        }

        if (this.state.uploadedFile === false) {
            alert("A layout must be uploaded");
            return;
        }

        if (this.state.userLocation === null) {
            alert("A location for the user must be chosen");
            return;
        }

        await ParametersService.saveParams(this.state)
            .then(async () => {
                localStorage.setItem("parametersSet", "true");
                await this.setState({
                    parametersSet: true
                });
                this.redirectToDashboard();
            })
            .catch(() => {
                alert("One or more system parameters were inappropriate");
            })
    }

    redirectToDashboard() {
        window.location = "http://localhost:3000/dashboard";
    }

    onDateSelected(evt) {
        this.date = evt.target.value;
    }

    onTimeSelected(evt) {
        this.time = evt.target.value;
    }

    async fileChangedHandler(event) {
        await this.setState({
            file: event.target.files[0]
        });
    }

    async fileUploadHandler() {
        await HouseLayoutService.createLayout(this.state.file)
            .then(async () => {
                localStorage.setItem("uploadedFile", "true");
                this.setState({
                    uploadedFile: true
                });
                await this.getHouseLayout();
            })
            .catch(() => {
                alert("Invalid File");
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
                                    <Container fluid className="SimulationParameters_Parameters_Container">
                                        <Row>
                                            <Col lg={6} className="SimulationParameters_Parameters_Col_One">
                                                <h2>User</h2>
                                                <br/>
                                                <img src="/user.png" alt="profile pic" width="150"/>
                                                <br/>
                                                <br/>
                                                <Row>
                                                    <Col lg={2}>
                                                        <label>Name</label>
                                                    </Col>
                                                    <Col>
                                                        <input name="name" type="text" onChange={this.userNameHandler} style={{width: "120px"}}/>
                                                    </Col>
                                                </Row>
                                                <br/>
                                                Profile
                                                <Container className="SimulationParameters_Parameters_Select">
                                                    <Select
                                                            styles={{
                                                                option: provided => ({...provided, width: 200}),
                                                                menu: provided => ({...provided, width: 200}),
                                                                control: provided => ({...provided, width: 200}),
                                                                singleValue: provided => provided
                                                            }}
                                                            options={SimulationParameters.profiles}
                                                            onChange={this.onSelectedProfile}
                                                            defaultValue={SimulationParameters.profiles[0]}
                                                    />
                                                </Container>
                                                <br/>
                                                Location
                                                <Container className="SimulationParameters_Parameters_Select">
                                                    <Select
                                                        styles={{
                                                            option: provided => ({...provided, width: "200px"}),
                                                            menu: provided => ({...provided, width: "200px"}),
                                                            control: provided => ({...provided, width: "200px"}),
                                                            singleValue: provided => provided
                                                        }}
                                                        options={this.state.rooms}
                                                        onChange={this.onSelectedLocation}
                                                    />
                                                </Container>
                                            </Col>
                                            <Col lg={6} className="SimulationParameters_Parameters_Col_Two">
                                                <h2>Parameters</h2>
                                                <br/>
                                                <Container>
                                                    <Row>
                                                        <Col>
                                                            <label>Outside Temperature</label>
                                                        </Col>
                                                        <Col>
                                                            <input name="outsideTemp" type="number" onChange={this.tempChangeHandler} style={{width: "120px"}}/>
                                                        </Col>
                                                    </Row>
                                                    <Row>
                                                        <Col>
                                                            <label>Inside Temperature</label>
                                                        </Col>
                                                        <Col>
                                                            <input name="insideTemp" type="number" onChange={this.tempChangeHandler} style={{width: "120px"}}/>
                                                        </Col>
                                                    </Row>
                                                    <Row>
                                                        <Col>
                                                            <label>Date</label>
                                                        </Col>
                                                        <Col>
                                                            <input type="date" name="date" onChange={this.onDateSelected}/>
                                                        </Col>
                                                    </Row>
                                                    <Row>
                                                        <Col>
                                                            <label>Time</label>
                                                        </Col>
                                                        <Col>
                                                            <input type="time" name="time" onChange={this.onTimeSelected}/>
                                                        </Col>
                                                    </Row>
                                                </Container>
                                            </Col>
                                        </Row>
                                    </Container>
                                </Col>
                            </Row>
                        </Container>
                    </Col>
                    <Col>
                        <Container fluid className="SimulationParameters_HouseLayout_Container">
                            <Row className="SimulationParameters_HouseLayout_Image">
                                {this.state.houseLayout}
                            </Row>
                            <Row>
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
                            </Row>
                        </Container>
                    </Col>
                </Row>
                <Row>
                    <Col className="SimulationParameters_Buttons_Row">
                        <Button
                            className="SimulationParameters_Buttons"
                            onClick={this.saveParametersChanges}
                            variant="primary" size="lg"
                        >
                            Apply
                        </Button>
                    </Col>
                </Row>
            </Container>
        );
    }

}
