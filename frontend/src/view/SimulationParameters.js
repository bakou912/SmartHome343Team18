import React, {useCallback, useEffect, useRef, useState} from "react";
import ParametersService from '../service/ParametersService';
import "../style/SimulationParametersView.css";
import HouseLayout from "./HouseLayout";
import { Button, Container, Row, Col } from 'react-bootstrap';
import HouseLayoutService from "../service/HouseLayoutService";
import Select from 'react-select'
import SmartHomeHeaterService from "../service/SmartHomeHeaterService";

export default function SimulationParameters() {

    const time = useRef(undefined);
    const date = useRef(undefined);
    const profiles = useRef([]);
    const [insideTemp, setInsideTemp] = useState(null);
    const [outsideTemp, setOutsideTemp] = useState(null);
    const [seasonDates, setSeasonDates] = useState({
        winterStart: "",
        summerStart: ""
    });
    const [profile, setProfile] = useState({
        profile: "",
        name: ""
    });
    const [name, setName] = useState("");
    const [file, setFile] = useState(null);
    const [uploadedFile, setUploadedFile] = useState(localStorage.getItem("uploadedFile") === "true");
    const [rooms, setRooms] = useState([]);
    const [userLocation, setUserLocation] = useState([]);
    const [houseLayout, setHouseLayout] = useState(null);
    const layoutKey = useRef(0);

    const getHouseLayout = useCallback(async () => {
        let selectRooms = [];

        if (uploadedFile === true) {
            selectRooms = await HouseLayoutService.getAllLocations();
        }

        layoutKey.current = layoutKey.current + 1;
        setHouseLayout(<HouseLayout key={layoutKey.current} />);
        setRooms(selectRooms);
    }, [uploadedFile]);

    const onSelectedLocation = (evt) => {
        if (evt.label === "Outside") {
            evt.value.name = "Outside";
        }

        setUserLocation(evt.value);
    }

    const redirectToDashboard = () => {
        window.location = "http://localhost:3000/dashboard";
    }

    const saveParametersChanges = async () => {
        const parametersInput = {
            outsideTemp: outsideTemp,
            insideTemp: insideTemp,
            dateTime: date.current + "T" + time.current + (time.current && time.current.length === 5 ? ":00" : "")
        };

        const userInput = {
            profile: profile,
            name: name,
            location: userLocation
        };

        if (!name) {
            alert("A name for the user must be set");
            return;
        }

        if (!uploadedFile) {
            alert("A layout must be uploaded");
            return;
        }

        if (!userLocation) {
            alert("A location for the user must be chosen");
            return;
        }

        await ParametersService.saveParams({
            parametersInput: parametersInput,
            userInput: userInput
        })
            .then(async () => {
                localStorage.setItem("parametersSet", "true");
                redirectToDashboard();
            })
            .catch(() => {
                alert("One or more system parameters were inappropriate");
            })
		await SmartHomeHeaterService.initTemp();
    }

    const fileChangedHandler = (evt) => {
        setFile(evt.target.files[0]);
    }

	const onSummerStartSelected = (evt) => {
        setSeasonDates({
            winterStart: seasonDates.winterStart,
            summerStart: evt.target.value + "T00:00:00"
        });
    }

	const onWinterStartSelected = (evt) => {
        setSeasonDates({
            winterStart: evt.target.value + "T00:00:00",
            summerStart: seasonDates.winterStart
        });
    }

    const fileUploadHandler = async () => {
        await HouseLayoutService.createLayout(file)
            .then(async () => {
                localStorage.setItem("uploadedFile", "true");
                setUploadedFile(true);
                await getHouseLayout();
            })
            .catch(() => {
                alert("Invalid File");
            });
    }

    useEffect( () => {
        (async () => {
            if (localStorage.getItem("parametersSet") === "true") {
                redirectToDashboard();
            }

            profiles.current = await ParametersService.getProfiles();
            await getHouseLayout();
        })();
    }, [getHouseLayout]);

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
                                                    <input name="name" type="text" onChange={(evt) => setName(evt.target.value)} style={{width: "120px"}}/>
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
                                                        options={profiles.current}
                                                        onChange={(evt => setProfile(evt.value.name))}
                                                        defaultValue={profiles.current[0]}
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
                                                    options={rooms}
                                                    onChange={onSelectedLocation}
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
                                                        <input name="outsideTemp" type="number" onChange={(evt) => setOutsideTemp(evt.target.value)} style={{width: "120px"}}/>
                                                    </Col>
                                                </Row>
                                                <Row>
                                                    <Col>
                                                        <label>Inside Temperature</label>
                                                    </Col>
                                                    <Col>
                                                        <input name="insideTemp" type="number" onChange={(evt) => setInsideTemp(evt.target.value)} style={{width: "120px"}}/>
                                                    </Col>
                                                </Row>
                                                <Row>
                                                    <Col>
                                                        <label>Date</label>
                                                    </Col>
                                                    <Col>
                                                        <input type="date" name="date" onChange={(evt) => date.current = evt.target.value}/>
                                                    </Col>
                                                </Row>
                                                <Row>
                                                    <Col>
                                                        <label>Time</label>
                                                    </Col>
                                                    <Col>
                                                        <input type="time" name="time" onChange={(evt) => time.current = evt.target.value}/>
                                                    </Col>
                                                </Row>
                                                <Row>
                                                    <Col>
                                                        <br/>
                                                        <label>Summer Start Date</label>
                                                        <br/>
                                                        <input type="date" name="date" value={seasonDates.summerStart} onChange={onSummerStartSelected}/>
                                                    </Col>
                                                    <Col>
                                                        <br/>
                                                        <label>Winter Start Date</label>
                                                        <br/>
                                                        <input type="date" name="date" value={seasonDates.winterStart} onChange={onWinterStartSelected}/>
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
                            {houseLayout}
                        </Row>
                        <Row>
                            <Container>
                                <p>Please enter a valid House Layout initialization file:</p>
                                <input
                                    type="file"
                                    name="file"
                                    onChange={fileChangedHandler}
                                />
                                <br/><br/>
                                <Button onClick={async () => await fileUploadHandler()}>
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
                        onClick={saveParametersChanges}
                        variant="primary" size="lg"
                    >
                        Apply
                    </Button>
                </Col>
            </Row>
        </Container>
    );
}
