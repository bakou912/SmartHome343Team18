import React from "react";
import SmartHomeSecurityService from "../../service/SmartHomeSecurityService";
import "../../style/Modules.css";
import Switch from "react-switch";
import {Button, Col, Container, Row} from "react-bootstrap";
import Command from "./Command";
import Select from "react-select";
import HouseLayoutService from "../../service/HouseLayoutService";

const OUTSIDE = ["Backyard", "Entrance"];

export default class SHPModule extends React.Component {

    interval = null

    constructor(props) {
        super(props);
        this.state = {
            awayMode: false,
            authoritiesTimer: 0,
            selectedLocation: null
        };
        this.onAwayModeChange = this.onAwayModeChange.bind(this);
        this.onAuthoritiesTimerChange = this.onAuthoritiesTimerChange.bind(this);
        this.saveAuthoritiesTimer = this.saveAuthoritiesTimer.bind(this);
        this.setLightAwayMode = this.setLightAwayMode.bind(this);
        this.onSelectedLocation = this.onSelectedLocation.bind(this);
    }

    async componentDidMount() {
        let authoritiesTimer = (await SmartHomeSecurityService.getAuthoritiesTimer()).data;
        authoritiesTimer = authoritiesTimer.substr(2);
        authoritiesTimer = authoritiesTimer.substr(0, authoritiesTimer.length - 1);
        this.interval = setInterval(() => window.dispatchEvent(new Event("updateConsole")), authoritiesTimer * 1000);
        await this.setState({
            locations: await HouseLayoutService.getAllLocations(),
            awayMode: (await SmartHomeSecurityService.getAwayModeState()).data,
            authoritiesTimer: authoritiesTimer
        })
    }

    async onAwayModeChange(checked) {
        await SmartHomeSecurityService.toggleAwayMode(checked).then(async () => {
            await this.setState({
                awayMode: checked
            });
        });

        window.dispatchEvent(new Event("updateLayout"));
    }

    async onAuthoritiesTimerChange(evt) {
        await this.setState({
            authoritiesTimer: evt.target.value
        });
    }

    async saveAuthoritiesTimer() {
        await SmartHomeSecurityService.modifyAuthoritiesTimer(this.state.authoritiesTimer);
    }

    async setLightAwayMode(setOn) {
        const light = { location: this.state.selectedLocation.name, awayMode: setOn };

        const action = OUTSIDE.includes(this.state.selectedLocation.name) ?
            async () => HouseLayoutService.modifyOutsideLightState(light)
            :
            async () => HouseLayoutService.modifyRoomLightState(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, light)

        await action().then(async () => {
            await this.setState({
                selectedLocation: {
                    ...this.state.selectedLocation,
                    light: {
                        ...this.state.selectedLocation.light,
                        awayMode: setOn
                    }
                }
            });
        });

        window.dispatchEvent(new Event("updateLayout"));
    }

    async onSelectedLocation(evt) {
        await this.setState({
            locations: await HouseLayoutService.getAllLocations(),
            selectedLocation: null,
        });

        await this.setState({
            selectedLocation: evt.value
        });
    }

    render() {
        return (
            <Container className="Module">
                <Row>
                    <Col>
                        <Command name="Away mode management">
                            <span>Away Mode</span>
                            <Switch
                                onChange={this.onAwayModeChange}
                                checked={this.state.awayMode}
                            />
                        </Command>
                        <br/>
                        <Command name="Authorities alert time management">
                            <label>
                                Authorities alert delay (seconds):&nbsp;
                                <input
                                    key={this.state.loaded}
                                    style={{width: "50px"}}
                                    min={0}
                                    disabled={this.state.awayMode}
                                    name="authoritiesTimer"
                                    type="number"
                                    value={this.state.authoritiesTimer}
                                    onChange={this.onAuthoritiesTimerChange}
                                />
                                &nbsp;
                                <Button disabled={this.state.awayMode} onClick={this.saveAuthoritiesTimer} variant="secondary" size="sm">
                                    Save
                                </Button>
                            </label>

                        </Command>
                    </Col>
                </Row>
                <Row disabled={this.state.awayMode}>
                    <div className="Module">
                        <br/>
                        Locations
                        <Select
                            styles={{
                                option: provided => ({...provided, width: "200px"}),
                                menu: provided => ({...provided, width: "200px"}),
                                control: provided => ({...provided, width: "200px"}),
                                singleValue: provided => provided
                            }}
                            options={this.state.locations}
                            onChange={this.onSelectedLocation}
                        />
                        {
                            this.state.selectedLocation !== null ?
                                <Container>
                                        <Command name="Light away mode management" location={this.state.selectedLocation}>
                                            <Row>
                                                <Col>
                                                    <div style={{margin: "25px"}}>
                                                        <label>Enable Light Away Mode</label>
                                                        &nbsp;
                                                        <Switch
                                                            disabled={this.state.awayMode}
                                                            height={20}
                                                            width={48}
                                                            onChange={this.setLightAwayMode}
                                                            checked={this.state.selectedLocation.light.awayMode}
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
}
