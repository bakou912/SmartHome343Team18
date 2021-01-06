import React, {useCallback, useEffect, useRef, useState} from "react";
import SmartHomeSecurityService from "../../service/SmartHomeSecurityService";
import "../../style/Modules.css";
import Switch from "react-switch";
import {Button, Col, Container, Row} from "react-bootstrap";
import Command from "./Command";
import Select from "react-select";
import HouseLayoutService from "../../service/HouseLayoutService";
import ParametersService from "../../service/ParametersService";
import SimulationContextService from "../../service/SimulationContextService";

const OUTSIDE = ["Backyard", "Entrance"];

export default function SHPModule () {

    const interval = useRef(null);
    const [awayMode, setAwayMode] = useState(false);
    const [awayModeHours, setAwayModeHours] = useState(null);
    const [authoritiesTimer, setAuthoritiesTimer] = useState(0);
    const [selectedLocation, setSelectedLocation] = useState(null);
    const [locations, setLocations] = useState([]);

    const intervalHandler = useCallback (async () => {
        if ((await SimulationContextService.getContext()).data.state === "ON" && awayMode === true) {
            window.dispatchEvent(new Event("updateConsole"))
        }
    }, [awayMode]);

    const init = useCallback(async () => {
        let initAuthoritiesTimer = (await SmartHomeSecurityService.getAuthoritiesTimer()).data;
        initAuthoritiesTimer = initAuthoritiesTimer.substr(2);
        initAuthoritiesTimer = initAuthoritiesTimer.substr(0, initAuthoritiesTimer.length - 1);
        const timeSpeed = (await ParametersService.getParams()).data.sysParams.timeSpeed;
        interval.current = setInterval(async () => await intervalHandler(), initAuthoritiesTimer * 1000 / timeSpeed);
        setLocations(await HouseLayoutService.getAllLocations());
        setAwayMode((await SmartHomeSecurityService.getAwayModeState()).data);
        setAwayModeHours((await SmartHomeSecurityService.getAwayModeHours()).data);
        setAuthoritiesTimer(initAuthoritiesTimer);
    }, [intervalHandler]);

    const onAwayModeChange = async (checked) => {
        await SmartHomeSecurityService.toggleAwayMode(checked).then(async () => {
            setAwayMode(checked);
        });

        window.dispatchEvent(new Event("updateLayout"));

        const timeSpeed = (await ParametersService.getParams()).data.sysParams.timeSpeed;
        clearInterval(interval.current);
        interval.current = setInterval(async () => await intervalHandler(), authoritiesTimer * 1000 / timeSpeed);
    };

    const onAuthoritiesTimerChange = async (evt) => {
        setAuthoritiesTimer(evt.target.value);
    };

    const saveAuthoritiesTimer = async () => {
        await SmartHomeSecurityService.modifyAuthoritiesTimer(authoritiesTimer);
    };

    const setLightAwayMode = async (setOn) => {
        const action = OUTSIDE.includes(selectedLocation.name) ?
            async () => HouseLayoutService.modifyOutsideLightState({ location: selectedLocation.name, awayMode: setOn })
            :
            async () => HouseLayoutService.modifyRoomLightState(selectedLocation.rowId, selectedLocation.roomId, { awayMode: setOn })

        await action().then(async () => {
            setSelectedLocation({
                ...selectedLocation,
                light: {
                    ...selectedLocation.light,
                    awayMode: setOn
                }
            });
        });

        window.dispatchEvent(new Event("updateLayout"));
        if (setOn === true) {
            window.dispatchEvent(new Event("awayModeOn"));
        }
    };

    const onSelectedLocation = async (evt) => {
        setLocations(await HouseLayoutService.getAllLocations());
        setSelectedLocation({...evt.value});
    };

    const onTimeSelected = async (evt, type) => {
        await SmartHomeSecurityService.modifyAwayModeHours({ [type]: evt.target.value.toString() }).then(async response => {
            setAwayModeHours(response.data);
        });
    };

    useEffect( () => {
        init();
    }, [init]);

    return (
        <Container className="Module">
            <Row>
                <Col>
                    <Command name="Away mode management">
                        <span>Away Mode</span>
                        <Switch
                            onChange={onAwayModeChange}
                            checked={awayMode}
                        />
                    </Command>
                    <br/>
                    <Command name="Authorities alert time management">
                        <label>
                            Authorities alert delay (seconds):&nbsp;
                            <input
                                style={{width: "50px"}}
                                min={0}
                                disabled={awayMode}
                                name="authoritiesTimer"
                                type="number"
                                value={authoritiesTimer}
                                onChange={onAuthoritiesTimerChange}
                            />
                            &nbsp;
                            <Button disabled={awayMode} onClick={saveAuthoritiesTimer} variant="secondary" size="sm">
                                Save
                            </Button>
                        </label>
                    </Command>
                </Col>
            </Row>
            <Row disabled={awayMode}>
                <div className="Module">
                    <br/>
                    <Row>
                        {
                            awayModeHours &&
                            <Command name="Away mode light hours management">
                                <Col>
                                    <label>
                                        Lights on start time&nbsp;
                                        <input disabled={awayMode} type="time" name="fromTime" defaultValue={awayModeHours.from} onChange={async evt => onTimeSelected(evt, "from")}/>
                                    </label>
                                    <br/>
                                    <label>
                                        Lights on stop time&nbsp;
                                        <input disabled={awayMode} type="time" name="toTime" defaultValue={awayModeHours.to} onChange={async evt => onTimeSelected(evt, "to")}/>
                                    </label>
                                </Col>
                            </Command>
                        }
                    </Row>
                    <br/>
                    <Row>
                        <Col>
                            Locations
                            <Select
                                styles={{
                                    option: provided => ({...provided, width: "200px"}),
                                    menu: provided => ({...provided, width: "200px"}),
                                    control: provided => ({...provided, width: "200px"}),
                                    singleValue: provided => provided
                                }}
                                options={locations}
                                onChange={onSelectedLocation}
                            />
                            <br/>
                        </Col>
                    </Row>
                    {
                        selectedLocation !== null ?
                            <Container>
                                    <Command name="Light away mode management" location={selectedLocation}>
                                        <Row>
                                            <Col>
                                                <div style={{margin: "25px"}}>
                                                    <label>Enable Light Away Mode</label>
                                                    &nbsp;
                                                    <Switch
                                                        disabled={awayMode}
                                                        height={20}
                                                        width={48}
                                                        onChange={setLightAwayMode}
                                                        checked={selectedLocation.light.awayMode}
                                                    />
                                                </div>
                                            </Col>
                                        </Row>
                                    </Command>
                            </Container>
                            : null
                    }
                </div>
            </Row>
        </Container>
    );
}
