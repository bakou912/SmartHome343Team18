import React, {useEffect, useState} from "react";
import "../../style/Modules.css";
import HouseLayoutService from "../../service/HouseLayoutService";
import Select from "react-select";
import {Container, Button, Col, Row, ListGroup} from "react-bootstrap";
import Command from "./Command";
import Switch from "react-switch";

const ITEMS = ["Window", "Light", "Door"];
const OUTSIDE = ["Backyard", "Entrance"];

export default function SHCModule() {
    const [locations, setLocations] = useState([]);
    const [windows, setWindows] = useState([]);
    const [doors, setDoors] = useState([]);
    const [selectedLocation, setSelectedLocation] = useState(null);
    const [selectedWindow, setSelectedWindow] = useState(null);
    const [selectedDoor, setSelectedDoor] = useState(null);
    const [selectedWindowItem, setSelectedWindowItem] = useState(false);
    const [selectedDoorItem, setSelectedDoorItem] = useState(false);
    const [selectedLightItem, setSelectedLightItem] = useState(false);
    const [loaded, setLoaded] = useState(false);

    const setup = async () => {
        setLocations(await HouseLayoutService.getAllLocations());
        setSelectedLocation(null)
        setSelectedWindow(null)
        setSelectedDoor(null)
        setLoaded(true);
    };

    const onSelectedLocation = async (evt) =>  {
        setLocations(await HouseLayoutService.getAllLocations());

        const newWindows = OUTSIDE.includes(evt.label) ? [] : evt.value.windows.map(w => {
            return {
                value: w,
                label: w.direction
            };
        });

        const newDoors = OUTSIDE.includes(evt.label) ? [] : evt.value.doors.map(d => {
            return {
                value: d,
                label: d.direction
            };
        })

        setSelectedLocation({
            light: evt.value.light,
            rowId: evt.value.rowId,
            roomId: evt.value.roomId,
            label: evt.label
        });
        setWindows(newWindows);
        setDoors(newDoors);
        setSelectedWindow(null);
        setSelectedDoor(null);
    };

    const onSelectedItem = (item, evt) => {
        if (item === "Window") {
            setSelectedWindow(evt)
        } else {
            setSelectedDoor(evt)
        }
    };

    const openCloseWindow = async (windowState) => {
        await HouseLayoutService.changeWindowState(selectedLocation.rowId, selectedLocation.roomId, selectedWindow.value.id, windowState);

        setSelectedWindow({
            ...selectedWindow,
            value: {
                ...selectedWindow.value,
                state: windowState
            }
        });

        window.dispatchEvent(new Event("updateLayout"));
    };

    const modifyLightState = async (lightState) => {
        const action = OUTSIDE.includes(selectedLocation.label) ?
            async () => HouseLayoutService.modifyOutsideLightState({ location: selectedLocation.label, state: lightState })
            :
            async () => HouseLayoutService.modifyRoomLightState(selectedLocation.rowId, selectedLocation.roomId, { state: lightState })

        await action().then(() => {
            setSelectedLocation({
                ...selectedLocation,
                light: {
                    ...selectedLocation.light,
                    state: lightState
                }
            });
        });

        window.dispatchEvent(new Event("updateLayout"));
    };

    const openCloseDoor = async (doorState) => {
        await HouseLayoutService.changeDoorState(
            selectedLocation.rowId,
            selectedLocation.roomId,
            selectedDoor.value.id,
            { state: doorState }
        )
		.then(async () => {
            setSelectedDoor({
                ...selectedDoor,
                value: {
                    ...selectedDoor.value,
                    state: doorState
                }
            });
		});

		window.dispatchEvent(new Event("updateLayout"));
    };

    const setAutoMode = async (setOn) => {
        const action = OUTSIDE.includes(selectedLocation.label) ?
            async () => HouseLayoutService.modifyOutsideLightState({ location: selectedLocation.label, autoMode: setOn })
            :
            async () => HouseLayoutService.modifyRoomLightState(selectedLocation.rowId, selectedLocation.roomId, { autoMode: setOn })

        await action().then(response =>{
            setSelectedLocation({
                ...selectedLocation,
                light: {
                    ...selectedLocation.light,
                    autoMode: setOn,
                    state: response.data.state
                }
            });
        });

        window.dispatchEvent(new Event("updateLayout"));
    };

    const itemSelected = (item) => {
        setSelectedWindowItem(item === "Window");
        setSelectedLightItem(item === "Light");
        setSelectedDoorItem(item === "Door");
    };

    // Adding event subscription on mount
    useEffect( () => {
        window.addEventListener("updatePermissions", setup);
        setup();
    }, []);

    // Removing event subscription on unmount
    useEffect(() => {
        return () => {
            window.removeEventListener("updateZoneRooms", setup);
        }
    }, []);

    useEffect( () => {
        if (selectedDoor)
            console.log("update: " + selectedDoor.value.state)
    }, [selectedDoor]);


    return (
        <Container>
            {
                loaded &&
                    <div className="Module">
                        <br/>
                        Locations
                        <Select
                            styles={{
                                option: provided => ({...provided, width: "100%"}),
                                menu: provided => ({...provided, width: "50%"}),
                                control: provided => ({...provided, width: "50%"}),
                                singleValue: provided => provided
                            }}
                            options={locations}
                            onChange={onSelectedLocation}
                        />
                        <br/>
                        {
                            selectedLocation !== null &&
                                <Container>
                                    <Row>
                                        <Col>
                                            <ListGroup>
                                                {
                                                    !OUTSIDE.includes(selectedLocation.label) ?
                                                        ITEMS.map((item) =>
                                                            <ListGroup.Item
                                                                key={ITEMS.indexOf(item)} className="ItemsTable"
                                                                bsPrefix="list-group-item py-1" action
                                                                onClick={() => itemSelected(item)}
                                                                variant="dark">{item}</ListGroup.Item
                                                            >
                                                        )
                                                        :
                                                        [
                                                            <ListGroup.Item
                                                                key={ITEMS.indexOf("Light")}
                                                                className="ItemsTable"
                                                                bsPrefix="list-group-item py-1" action
                                                                onClick={() => itemSelected("Light")}
                                                                variant="dark">Light</ListGroup.Item
                                                            >
                                                        ]
                                                }
                                            </ListGroup>
                                        </Col>
                                    </Row>
                                    {
                                        selectedWindowItem &&
                                            <Row>
                                                {
                                                    !OUTSIDE.includes(selectedLocation.label) &&
                                                        <Col>
                                                            <Command
                                                                name="Window management"
                                                                location={selectedLocation}
                                                            >
                                                                Windows
                                                                <Select
                                                                    styles={{
                                                                        option: provided => ({...provided, width: "50%"}),
                                                                        menu: provided => ({...provided, width: "50%"}),
                                                                        control: provided => ({...provided, width: "50%"}),
                                                                        singleValue: provided => provided
                                                                    }}
                                                                    options={windows}
                                                                    value={selectedWindow}
                                                                    onChange={(evt) => onSelectedItem("Window", evt)}
                                                                />
                                                                {
                                                                    selectedWindow !== null &&
                                                                        <div>
                                                                                {
                                                                                    selectedWindow.value.state !== "BLOCKED" &&
                                                                                        <span>
                                                                                            {
                                                                                                selectedWindow.value.state === "CLOSED" ?
                                                                                                <Button
                                                                                                    onClick={() => openCloseWindow("OPEN")}
                                                                                                    variant="secondary"
                                                                                                    size="sm">
                                                                                                    Open
                                                                                                </Button>
                                                                                                :
                                                                                                <Button
                                                                                                    onClick={() => openCloseWindow("CLOSED")}
                                                                                                    variant="secondary"
                                                                                                    size="sm">
                                                                                                    Close
                                                                                                </Button>
                                                                                            }
                                                                                        </span>
                                                                                }
                                                                        </div>
                                                                }
                                                            </Command>
                                                        </Col>
                                                }
                                            </Row>
                                    }
                                    {
                                        selectedLightItem &&
                                            <Command
                                                name="Light management"
                                                location={selectedLocation}
                                            >
                                                <Row>
                                                    <Col>
                                                        Light
                                                        {
                                                            selectedLocation.light.state === "OFF" ?
                                                                <div>
                                                                    <Button
                                                                        onClick={() => modifyLightState("ON")}
                                                                        variant="secondary" size="md"
                                                                        disabled={selectedLocation.light.autoMode}
                                                                    >
                                                                        On
                                                                    </Button>
                                                                </div>
                                                                :
                                                                <div>
                                                                    <Button
                                                                        onClick={() => modifyLightState("OFF")}
                                                                        variant="secondary" size="md"
                                                                        disabled={selectedLocation.light.autoMode}
                                                                    >
                                                                        Off
                                                                    </Button>
                                                                </div>
                                                        }
                                                    </Col>
                                                    <Col>
                                                        <div style={{margin: "25px"}}>
                                                            <label>Enable Auto Mode</label>
                                                            &nbsp;
                                                            <Switch
                                                                height={20}
                                                                width={48}
                                                                onChange={setAutoMode}
                                                                checked={selectedLocation.light.autoMode}
                                                            />
                                                        </div>
                                                    </Col>
                                                </Row>
                                            </Command>
                                    }
                                    {
                                        selectedDoorItem &&
                                            <Row>
                                                {
                                                    !OUTSIDE.includes(selectedLocation.label) &&
                                                        <Col>
                                                            <Command
                                                                name="Door management"
                                                                location={selectedLocation}
                                                            >
                                                                Doors
                                                                <Select
                                                                    styles={{
                                                                        option: provided => ({...provided, width: "50%"}),
                                                                        menu: provided => ({...provided, width: "50%"}),
                                                                        control: provided => ({...provided, width: "50%"}),
                                                                        singleValue: provided => provided
                                                                    }}
                                                                    options={doors}
                                                                    value={selectedDoor}
                                                                    onChange={(evt) => onSelectedItem("Door", evt)}
                                                                />
                                                                {
                                                                    selectedDoor !== null &&
                                                                        <div>
                                                                                {
                                                                                    selectedDoor.value.state === "LOCKED" ?
                                                                                        <Button
                                                                                            onClick={() => openCloseDoor(selectedDoor.value.state === "CLOSED" ? "OPEN" : "CLOSED")}
                                                                                            variant="secondary"
                                                                                            size="sm">
                                                                                            Unlock
                                                                                        </Button> :
                                                                                        <Button
                                                                                            onClick={() => openCloseDoor("LOCKED")}
                                                                                            variant="secondary"
                                                                                            size="sm">
                                                                                            Lock
                                                                                        </Button>
                                                                                }
                                                                                {
                                                                                    selectedDoor.value.state !== "LOCKED" &&
                                                                                    <span>
                                                                                        {
                                                                                            selectedDoor.value.state === "CLOSED" ?
                                                                                                <Button
                                                                                                    onClick={() => openCloseDoor("OPEN")}
                                                                                                    variant="secondary"
                                                                                                    size="sm">
                                                                                                    Open
                                                                                                </Button>
                                                                                                :
                                                                                                <Button
                                                                                                    onClick={() => openCloseDoor("CLOSED")}
                                                                                                    variant="secondary"
                                                                                                    size="sm">
                                                                                                    Close
                                                                                                </Button>
                                                                                        }
                                                                                    </span>
                                                                                }
                                                                        </div>
                                                                }
                                                            </Command>
                                                        </Col>
                                                }
                                            </Row>
                                    }
                                </Container>
                        }
                    </div>
            }
        </Container>
    );
}
