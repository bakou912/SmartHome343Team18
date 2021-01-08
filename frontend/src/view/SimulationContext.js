import React, {useEffect, useRef, useState} from "react";
import "../style/SimulationContextView.css";
import {Container, Modal, Button, Col, Row} from "react-bootstrap";
import SimulationContextService from "../service/SimulationContextService";
import HouseLayoutService from "../service/HouseLayoutService";
import Select from "react-select";
import ParametersService from "../service/ParametersService";
import {EditUserProfiles} from "../component/EditUserProfiles";
import SmartHomeSecurityService from "../service/SmartHomeSecurityService";

const OUTSIDE = ["Backyard", "Entrance"];

export default function SimulationContext() {

    const contextModel = useRef({});
    const dateTime = useRef(new Date());
    const profiles = useRef({});
    const awayModeHours = useRef(null);
    const hoursUpdate = useRef({
        from: false,
        to: false
    });
    const simulatorTimeHandler = useRef(null);
    const [loaded, setLoaded] = useState(false);
    const [editingParameters, setEditingParameters] = useState(false);
    const [editingContext, setEditingContext] = useState(false);
    const [addingPerson, setAddingPerson] = useState(false);
    const [selectedContextLocation, setSelectedContextLocation] = useState(null);
    const [selectedWindow, setSelectedWindow] = useState(null);
    const [selectedPerson, setSelectedPerson] = useState(null);
    const [timeSpeed, setTimeSpeed] = useState(1);
    const [rooms, setRooms] = useState([]);
    const [windows, setWindows] = useState([]);
    const [persons, setPersons] = useState([]);
    const [user, setUser] = useState(null);
    const [personName, setPersonName] = useState(null);
    const [outsideTemp, setOutsideTemp] = useState(null);
    const [date, setDate] = useState(null);
    const [time, setTime] = useState(null);
    const [summerStart, setSummerStart] = useState(null);
    const [winterStart, setWinterStart] = useState(null);
    const [simulationState, setSimulationState] = useState(null);
    const [presentationInfo, setPresentationInfo] = useState(null);

    const handleNameChange = (evt) => {
        const targetValue = evt.target.value;

        if (targetValue.length > 20) {
            return;
        }

       setUser({
           ...user,
           name: evt.target.value
       });
    };

    const handlePersonNameChange = (evt) => {
        const targetValue = evt.target.value;

        if (targetValue.length > 20) {
            return;
        }

        setPersonName(evt.target.value);
    };

    const onSelectedLocation = (evt) => {
        setUser({
            ...user,
            location: evt.value
        });
    };

    const onSelectedContextLocation = async (evt) => {
        setRooms(await HouseLayoutService.getAllLocations());
        setSelectedWindow(null);
        setSelectedPerson(null);
        setSelectedContextLocation({
            value: evt.value,
            label: evt.label
        });
        setWindows(OUTSIDE.includes(evt.label) ? [] : evt.value.windows.map(w => {
            return {
                value: w,
                label: w.direction
            };
        }));
        setPersons(evt.value.persons.map(p => {
            return {
                value: p,
                label: p.name
            };
        }));
        setPersonName("")
    };

    const onSelectedProfile = (evt) => {
        setUser({
            ...user,
            profile: evt.value
        });
    };

    const setModal = (modalName, value) => {
        if (modalName === "Parameters") {
            setEditingParameters(value);
        } else {
            setEditingContext(value);
        }

        setSelectedWindow(null);
        setSelectedPerson(null);
        setSelectedContextLocation(null);
    };

    const onTimeSpeedSelected = async (evt) => {
        setTimeSpeed(evt.target.value);
        await clearInterval(simulatorTimeHandler.current);
        simulatorTimeHandler.current = setInterval(async () => await simulatorTime(), 1000 / timeSpeed);
    };

    const saveEdit = async () => {
        const userInput = {
            ...user,
            profile: user.profile.name
        }

        await SimulationContextService.modifyUser(userInput);
        await SimulationContextService.modifySysParams({
            dateTime: date + "T" + time + (time.length === 5 ? ":00" : ""),
            timeSpeed: timeSpeed,
            outsideTemp: outsideTemp,
			seasonDates: {
				summerStart: summerStart + "T00:00:00",
				winterStart: winterStart + "T00:00:00"
			}
        });
        await window.location.reload();
    };

    const toggleSimulationState = async () => {
        await SimulationContextService.toggleState()
            .then(async response => {
                setSimulationState(response.data);
            })
            .catch(() => {
                alert("Simulation state could not be changed");
            });
    };

    const blockWindow = async (block) => {
        let windowState;
        if (block === true) {
            windowState = "BLOCKED";
            await SimulationContextService.blockWindow(selectedContextLocation.value.rowId, selectedContextLocation.value.roomId, selectedWindow.value.id)
        } else {
            windowState = "CLOSED";
            await SimulationContextService.unblockWindow(selectedContextLocation.value.rowId, selectedContextLocation.value.roomId, selectedWindow.value.id)
        }

        setSelectedWindow({
            ...selectedWindow,
            value: {
                ...selectedWindow.value,
                state: windowState
            }
        });

        await window.dispatchEvent(new Event("updateLayout"));
        await window.dispatchEvent(new Event("updatePermissions"));
    };

    const addPerson = async () => {
        const action = OUTSIDE.includes(selectedContextLocation.label) ?
            async () => SimulationContextService.addPersonOutside({ location: selectedContextLocation.label, name: personName})
            :
            async () => SimulationContextService.addPersonToRoom(selectedContextLocation.value.rowId, selectedContextLocation.value.roomId,  {name: personName});

        await action().then(async response => {
            const person = {
                value: {
                    id: response.data,
                    name: personName
                },
                label: personName
            };

            const updatedPersons = persons;
            updatedPersons.push(person);

            setPersons([...updatedPersons]);
            setAddingPerson(false);
            setPersonName("");

            window.dispatchEvent(new Event("updateLayout"));
        }).catch(err => {
            if (err.response.status === 409) {
                alert("A person with this name is already present.");
            }
        })
    };

    const removePerson = async () => {
        const action = OUTSIDE.includes(selectedContextLocation.label) ?
            async () => SimulationContextService.removePersonFromOutside(selectedContextLocation.label, selectedPerson.value.id)
            :
            async () => SimulationContextService.removePersonFromRoom(selectedContextLocation.value.rowId, selectedContextLocation.value.roomId, selectedPerson.value.id);

        await action().then(async () =>{
            setPersons([...persons.filter(person => person.value.name !== selectedPerson.value.name)]);
            setSelectedPerson(null)
        });
        window.dispatchEvent(new Event("updateLayout"));
    };

    const setAwayModeTimes = async () => {
        awayModeHours.current = (await SmartHomeSecurityService.getAwayModeHours()).data;
    };

    const simulatorTime = async () => {
        if (simulationState === "OFF" || editingParameters === true) {
            return;
        }
        dateTime.current.setSeconds(dateTime.current.getSeconds() + 1);

        setDate(dateTime.current.toISOString().substring(0, 10));
        setTime(dateTime.current.toISOString().substring(11, 19));

        await checkAwayModeHours();

        await window.dispatchEvent(new CustomEvent("updateSelectedZone", { detail: true}));
        await window.dispatchEvent(new Event("updateConsole"));
    };

    const checkAwayModeHours = async () => {
        const newTime = dateTime.current.toISOString().substring(11, 19);

        if (newTime >= awayModeHours.current.from) {
            if (newTime <= awayModeHours.current.to && hoursUpdate.current.from === false && hoursUpdate.current.to === false) {
                hoursUpdate.current.to = true;
                await window.dispatchEvent(new Event("updateLayout"));
            } else {
                hoursUpdate.current.to = false;
            }
            hoursUpdate.current.from = true;
        } else {
            hoursUpdate.current.from = false;
        }
    };

    useEffect( () => {
        (async () => {
            window.addEventListener("awayModeOn", async () => {
                await setAwayModeTimes();
            });

            await setAwayModeTimes();

            contextModel.current = (await SimulationContextService.getContext()).data;
            profiles.current = await ParametersService.getProfiles(contextModel.current.parameters.userProfiles.profiles);

            const newDate = new Date(contextModel.current.parameters.sysParams.date);
            const newSummerStart = new Date(contextModel.current.parameters.sysParams.seasonDates.summerStart);
            const newWinterStart = new Date(contextModel.current.parameters.sysParams.seasonDates.winterStart);
            dateTime.current.current = new Date(newDate.getTime() - newDate.getTimezoneOffset() * 60000);

            setUser(contextModel.current.parameters.user);
            setOutsideTemp(contextModel.current.parameters.sysParams.outsideTemp);
            setTimeSpeed(contextModel.current.parameters.sysParams.timeSpeed);
            setSimulationState(contextModel.current.state);
            setDate(dateTime.current.toISOString().substring(0, 10));
            setTime(dateTime.current.toISOString().substring(11, 19));
            setSummerStart(newSummerStart.toISOString().substring(0, 10));
            setWinterStart(newWinterStart.toISOString().substring(0, 10));
            setRooms([...await HouseLayoutService.getAllLocations(contextModel.current.layout)]);
            setPresentationInfo({
                userName: contextModel.current.parameters.user.name,
                profileName: contextModel.current.parameters.user.profile.name,
                location: contextModel.current.parameters.user.location.name,
                outsideTemp: contextModel.current.parameters.sysParams.outsideTemp,
                timeSpeed: contextModel.current.parameters.sysParams.timeSpeed
            });
            setLoaded(true);

            simulatorTimeHandler.current = setInterval(async () => await simulatorTime(),1000 / timeSpeed);
        })();
    }, []);

    return (
        <Container>
            {
                loaded &&
                    <div className="SimulationContext">
                        <Button className="StartButton" size="sm" variant={simulationState === "ON" ? "danger" : "primary"} onClick={toggleSimulationState}>
                            {simulationState === "ON" ? "Stop" : "Start"}
                        </Button>
                        <Button className="EditButton" size="sm" variant="secondary" onClick={() => setModal("Parameters", true)}>Parameters</Button>
                        <br/>
                        <br/>
                        <Button className="EditButton" size="sm" variant="secondary" onClick={() => setModal("Context", true)}>Context</Button>
                        <br/>
                        <br/>
                        <img src="/user.png" alt="profile pic" width="100px"/>
                        <br/>
                        {presentationInfo.userName}
                        <br/>
                        Profile: {`${presentationInfo.profileName} `}
                        <EditUserProfiles profiles={profiles.current}/>
                        <br/>
                        <br/>
                        Location: {presentationInfo.location}
                        <br/>
                        <br/>
                        Outside temperature: {presentationInfo.outsideTemp}&deg;C
                        <br/>
                        <br/>
                        {dateTime.current.toDateString()}
                        <br/>
                        {`${time} (x${presentationInfo.timeSpeed})`}
                        <br/>
                        <br/>
                        <Modal show={editingParameters} onHide={() => setModal("Parameters", false)}>
                            <Modal.Header closeButton>
                                <Modal.Title>Parameters Editing</Modal.Title>
                            </Modal.Header>
                            <Modal.Body style={{display: "flex", justifyContent: "center"}}>
                                <div>
                                    Name
                                    <br/>
                                    <input type="text" placeholder="User" maxLength="20" value={user.name} onChange={handleNameChange}/>
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
                                            options={profiles.current}
                                            onChange={onSelectedProfile}
                                            defaultValue={profiles.current.filter(profile => profile.value.name === user.profile.name)}
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
                                            options={rooms}
                                            onChange={onSelectedLocation}
                                            defaultValue={rooms.filter(room => room.label === user.location.name)}
                                        />
                                    </div>
                                    <br/>
                                    <label>Outside Temperature</label>
                                    <br/>
                                    <input name="outsideTemp" type="number" value={outsideTemp} onChange={(evt) => setOutsideTemp(evt.target.value)} style={{width: "120px"}}/>
                                    <br/>
                                    <br/>
                                    <Row>
                                        <Col>
                                            <label>Date</label>
                                            <br/>
                                            <input type="date" name="date" value={date} onChange={(evt) => setDate(evt.target.value)}/>
                                        </Col>
                                        <Col>
                                            <label>Time</label>
                                            <br/>
                                            <input type="time" name="time" value={time} onChange={(evt) => setTime(evt.target.value)}/>
                                        </Col>
                                        <Col>
                                            <label>Time Speed</label>
                                            <br/>
                                            <input type="number" name="timeSpeed" min={1} max={500} value={timeSpeed} onChange={onTimeSpeedSelected}/>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col>
                                            <br/>
                                            <label>Summer Start Date</label>
                                            <br/>
                                            <input type="date" name="date" value={summerStart} onChange={(evt) => setSummerStart(evt.target.value)}/>
                                        </Col>
                                        <Col>
                                            <br/>
                                            <label>Winter Start Date</label>
                                            <br/>
                                            <input type="date" name="date" value={winterStart} onChange={(evt) => setWinterStart(evt.target.value)}/>
                                        </Col>
                                    </Row>
                                </div>
                            </Modal.Body>
                            <Modal.Footer>
                                <Button onClick={saveEdit}>Save</Button>
                            </Modal.Footer>
                        </Modal>
                        <Modal show={editingContext} onHide={() => setModal("Context", false)}>
                            <Modal.Header closeButton>
                                <Modal.Title>Context Editing</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <Container style={{display: "flex", justifyContent: "center"}}>
                                    <div className="SelectDiv">
                                        Locations
                                        <Select
                                            styles={{
                                                option: provided => ({...provided, width: "100%"}),
                                                menu: provided => ({...provided, width: "100%"}),
                                                control: provided => ({...provided, width: "100%"}),
                                                singleValue: provided => provided
                                            }}
                                            options={rooms}
                                            onChange={onSelectedContextLocation}
                                        />
                                        {
                                            selectedContextLocation &&
                                                <Container>
                                                    <Row>
                                                        <Col>
                                                            <br/>
                                                            {
                                                                !OUTSIDE.includes(selectedContextLocation.label) &&
                                                                    <div>
                                                                        Windows
                                                                        <Select
                                                                            styles={{
                                                                                option: provided => ({...provided, width: "100%"}),
                                                                                menu: provided => ({...provided, width: "100%"}),
                                                                                control: provided => ({...provided, width: "100%"}),
                                                                                singleValue: provided => provided
                                                                            }}
                                                                            options={windows}
                                                                            value={selectedWindow}
                                                                            onChange={(evt) => setSelectedWindow(evt)}
                                                                        />
                                                                        {
                                                                            selectedWindow !== null &&
                                                                                <div>
                                                                                    {
                                                                                        selectedWindow.value.state === "BLOCKED" ?
                                                                                            <Button onClick={() => blockWindow(false)}
                                                                                                    variant="secondary"
                                                                                                    size="sm">Unobstruct</Button>
                                                                                            :
                                                                                            <Button onClick={() => blockWindow(true)}
                                                                                                    variant="secondary"
                                                                                                    size="sm">Obstruct</Button>
                                                                                    }
                                                                                </div>
                                                                        }
                                                                    </div>
                                                            }
                                                            <br/>
                                                            Persons
                                                            <Select
                                                                styles={{
                                                                    option: provided => ({...provided, width: "100%"}),
                                                                    menu: provided => ({...provided, width: "100%"}),
                                                                    control: provided => ({...provided, width: "100%"}),
                                                                    singleValue: provided => provided
                                                                }}
                                                                options={persons}
                                                                onChange={(evt) => setSelectedPerson(evt)}
                                                            />
                                                            <Row>
                                                                <Col>
                                                                    {
                                                                        addingPerson ?
                                                                            <div>
                                                                                <input
                                                                                    type="text" placeholder="Name" maxLength="20"
                                                                                    value={personName}
                                                                                    onChange={handlePersonNameChange}
                                                                                />
                                                                                <Button onClick={() => setAddingPerson(false)} variant="dark" size="sm">
                                                                                    X
                                                                                </Button>
                                                                                <Button onClick={addPerson} variant="secondary" size="sm">
                                                                                    Save
                                                                                </Button>
                                                                            </div>
                                                                            :
                                                                            <div>
                                                                                <Button onClick={() => setAddingPerson(true)} variant="secondary"
                                                                                        size="sm">
                                                                                    Add
                                                                                </Button>
                                                                                {
                                                                                    selectedPerson !== null &&
                                                                                        <Button onClick={removePerson} variant="secondary"
                                                                                                size="sm">
                                                                                            Remove
                                                                                        </Button>
                                                                                }
                                                                            </div>
                                                                    }
                                                                </Col>
                                                            </Row>
                                                        </Col>
                                                    </Row>
                                                </Container>
                                        }
                                    </div>
                                </Container>
                            </Modal.Body>
                        </Modal>
                    </div>
            }
        </Container>
    );
}
