import React, {useEffect, useRef, useState} from "react";
import SmartHomeHeaterService from "../../service/SmartHomeHeaterService";
import "../../style/Modules.css";
import {Button, Col, Container, Row} from "react-bootstrap";
import Select from "react-select";
import {AddRoomToZone} from "../AddRoomToZone";
import Switch from "react-switch";
import Command from "./Command";
import ZoneRooms from "../ZoneRooms";
import TempLimitsUtil from "../../service/util/TempLimitsUtil";

export default function SHHModule() {

    const [summerDefaultTemp, setSummerDefaultTemp] = useState(0);
    const [winterDefaultTemp, setWinterDefaultTemp] = useState(0);
    const [selectedZoneUpdate, setSelectedZoneUpdate] = useState(null); // Used to update the components that need the selected zone
    const selectedZone = useRef(null); // Actual value for the selected zone
    const [zones, setZones] = useState([]);
    const [addingZone, setAddingZone] = useState(false);
    const [zoneName, setZoneName] = useState("");
    const [systemOn, setSystemOn] = useState(false);

    const init = async () => {
        const defaultTemps = (await SmartHomeHeaterService.getDefaultTemperatures()).data;

        setSummerDefaultTemp(defaultTemps.summerTemp);
        setWinterDefaultTemp(defaultTemps.winterTemp);
        setZones(await SmartHomeHeaterService.getZones().catch(() => []));
        setSystemOn((await SmartHomeHeaterService.getSystemOn()).data);
    };

    const updateSelectedZone = async (event) => {
        if ((!event || !event.value) && !selectedZone.current) {
            return;
        }

        const zoneValue = event.value ?? selectedZone.current.value;

        const newZone = await SmartHomeHeaterService.getZone(zoneValue.id);

        selectedZone.current = newZone;

        setSelectedZoneUpdate(newZone);

        window.dispatchEvent(new CustomEvent("updateZoneRooms", { detail: { rooms: zoneValue.rooms}}));
    };

    const defaultTempChange = async (evt, name, limits) => {
        let value = evt.target.value

        value = TempLimitsUtil.adjustTempWithLimits(value, limits);

        if (name === "summer") {
            setSummerDefaultTemp(value);
            await SmartHomeHeaterService.setDefaultSummerTemp({ temperature: value });
        } else {
            setWinterDefaultTemp(value);
            await SmartHomeHeaterService.setDefaultWinterTemp({ temperature: value });
        }
    };

    const periodTempChange = async (evt, period) => {
        let value = evt.target.value

        value = TempLimitsUtil.adjustTempWithLimits(value, { min: 15, max: 30 });

        await SmartHomeHeaterService.setPeriodTemp(selectedZone.current.value.id, period, value);
        selectedZone.current.value.periods[period] = value
        setSelectedZoneUpdate({...selectedZone.current});
    };

    const addZone = async () => {
        await SmartHomeHeaterService.addZone({ name: zoneName }).then(async response => {
            const zone = {
                value: {
                    ...response.data,
                },
                label: zoneName
            };

            const updatedZones = zones;
            updatedZones.push(zone);

            await updateSelectedZone(zone);

            setZones([...updatedZones]);
            setAddingZone(false);
            setZoneName("");
        }).catch(err => {
            if (err.response.status === 409) {
                alert("A zone with this name is already present.");
            }
        })
    };

    const removeZone = async() => {
        await SmartHomeHeaterService.removeZone(selectedZone.current.value.id).then(async () => {
            const updatedZones = zones.filter(z => z.label !== selectedZone.current.label);

            setZones([...updatedZones]);
            setSelectedZoneUpdate(null);
            selectedZone.current = null;
        });
    };

    const handleZoneNameChange = (evt) => {
        const targetValue = evt.target.value;

        if (targetValue.length > 20) {
            return;
        }

        setZoneName(evt.target.value);
    };

    const onSystemStatusChange = async (checked) => {
        await SmartHomeHeaterService.setSystemOn(checked).then(async () => {
            setSystemOn(checked);
        });
    };

    // Adding event subscription on mount
    useEffect( () => {
        window.addEventListener("updateSelectedZone", updateSelectedZone);
        init();
    }, []);

    // Removing event subscription on unmount
    useEffect(() => {
        return () => {
            window.removeEventListener("updateZoneRooms", updateSelectedZone);
        }
    }, []);

    return (
        <Container>
            <div className="Module">
                <Container>
                    <br/>
                    <Command name="Setting default season temperatures">
                        <Row>
                            <Col md={5}>
                                Away Default Summer (&deg;C):
                            </Col>
                            <Col md={2}>
                                <input
                                    style={{ width: "50px" }}
                                    name="SummerTemp"
                                    type="number"
                                    value={summerDefaultTemp}
                                    onChange={async evt => await defaultTempChange(evt, "summer", { min: 0, max: 50})}
                                    min={0} max={50}
                                />
                            </Col>
                            <Col md={5}>
                                <div style={{float: "right"}}>
                                    <Command  name="Setting HAVC status">
                                        <span>HAVC</span>
                                        &nbsp;
                                        <Switch
                                            onChange={async (evt) => await onSystemStatusChange(evt)}
                                            checked={systemOn}
                                        />
                                    </Command>
                                </div>
                            </Col>
                        </Row>
                        <br/>
                        <Row>
                            <Col md={5}>
                                Away Default Winter (&deg;C):
                            </Col>
                            <Col md={2}>
                                <input
                                    style={{ width: "50px" }}
                                    name="WinterTemp"
                                    type="number"
                                    value={winterDefaultTemp}
                                    onChange={async (evt) => await defaultTempChange(evt, "winter", { min: -40, max: 20})}
                                    min={-40} max={20}
                                />
                            </Col>
                        </Row>
                    </Command>
                    <br/>
                    <Row>
                        <Col md={4}>
                            Zones
                            <Select
                                key={`zones${zones.length}`}
                                styles={{
                                    option: provided => ({...provided, width: "100%"}),
                                    menu: provided => ({...provided, width: "100%"}),
                                    control: provided => ({...provided, width: "100%"}),
                                    singleValue: provided => provided
                                }}
                                options={zones}
                                onChange={async (evt) => await updateSelectedZone(evt)}
                                value={selectedZoneUpdate ? selectedZone.current : null}
                            />
                            <Row>
                                <Col>
                                    {
                                        addingZone ?
                                            <div>
                                                <input
                                                    type="text" placeholder="Name" maxLength="20"
                                                    value={zoneName}
                                                    onChange={handleZoneNameChange}
                                                />
                                                <Button onClick={() => setAddingZone(false)} variant="dark" size="sm">
                                                    X
                                                </Button>
                                                <Button onClick={async () => await addZone()} variant="secondary" size="sm">
                                                    Save
                                                </Button>
                                            </div>
                                            :
                                            <div>
                                                <Command name="Adding heating zone">
                                                    <Button onClick={() => setAddingZone(true)} variant="secondary" size="sm">Add</Button>
                                                </Command>
                                                {
                                                    (selectedZoneUpdate && selectedZone.current.value.rooms.length === 0) &&
                                                    <Command name="Removing heating zone">
                                                        <Button onClick={async () => await removeZone()} variant="secondary" size="sm">Remove</Button>
                                                    </Command>
                                                }
                                            </div>
                                    }
                                </Col>
                            </Row>
                            <br/>
                        </Col>
                        <Col md={8}>
                            {
                                (selectedZoneUpdate && selectedZone.current) &&
                                    <div>
                                        Rooms&nbsp;
                                        <div style={{float: "right", color: "orange", fontSize: "small"}}>
                                            *Overridden
                                        </div>
                                        <Command name="Adding room to zone">
                                            <AddRoomToZone key={selectedZoneUpdate.label} zone={selectedZone.current.value}/>
                                        </Command>
                                        {
                                            selectedZone.current.value.rooms.length > 0 &&
                                            <ZoneRooms key={selectedZoneUpdate.label} zoneLabel={selectedZone.current.label} rooms={[...selectedZone.current.value.rooms]}/>
                                        }
                                    </div>
                            }
                        </Col>
                    </Row>
                    <br/>
                    <Row>
                        {
                            (selectedZoneUpdate && selectedZone.current) &&
                            <Container>
                                <Command name="Setting period temperatures">
                                    <Row>
                                        <Col md={7}>
                                            [6AM-12PM] Morning Target (&deg;C):
                                        </Col>
                                        <Col md={1}>
                                            <input
                                                style={{ width: "50px" }}
                                                name="MorningTargetTemp"
                                                type="number"
                                                value={selectedZone.current.value.periods.MORNING}
                                                onChange={evt => periodTempChange(evt, "MORNING")}
                                                min={0} max={50}
                                            />
                                        </Col>
                                    </Row>
                                    <br/>
                                    <Row>
                                        <Col md={7}>
                                            [12PM-10PM] Afternoon Target (&deg;C):
                                        </Col>
                                        <Col md={1}>
                                            <input
                                                style={{ width: "50px" }}
                                                name="AfternoonTargetTemp"
                                                type="number"
                                                value={selectedZone.current.value.periods.AFTERNOON}
                                                onChange={evt => periodTempChange(evt, "AFTERNOON")}
                                                min={15} max={30}
                                            />
                                        </Col>
                                    </Row>
                                    <br/>
                                    <Row>
                                        <Col md={7}>
                                            [10PM-6AM] Night Target (&deg;C):
                                        </Col>
                                        <Col md={1}>
                                            <input
                                                style={{ width: "50px" }}
                                                name="NightTargetTemp"
                                                type="number"
                                                value={selectedZone.current.value.periods.NIGHT}
                                                onChange={ evt => periodTempChange(evt, "NIGHT")}
                                                min={15} max={30}
                                            />
                                        </Col>
                                    </Row>
                                </Command>
                            </Container>
                        }
                    </Row>
                </Container>
            </div>
        </Container>
    );
}
