import React from "react";
import SmartHomeSecurityService from "../../service/SmartHomeSecurityService";
import "../../style/Modules.css";
import Switch from "react-switch";
import {Button, Col, Container, Row} from "react-bootstrap";
import Command from "./Command";

export default class SHPModule extends React.Component {

    interval = null

    constructor(props) {
        super(props);
        this.state = {
            awayMode: false,
            authoritiesTimer: 0
        };
        this.onAwayModeChange = this.onAwayModeChange.bind(this);
        this.onAuthoritiesTimerChange = this.onAuthoritiesTimerChange.bind(this);
        this.saveAuthoritiesTimer = this.saveAuthoritiesTimer.bind(this);
    }

    async componentDidMount() {
        let authoritiesTimer = (await SmartHomeSecurityService.getAuthoritiesTimer()).data;
        authoritiesTimer = authoritiesTimer.substr(2);
        authoritiesTimer = authoritiesTimer.substr(0, authoritiesTimer.length - 1);
        this.interval = setInterval(() => window.dispatchEvent(new Event("updateConsole")), authoritiesTimer * 1000);
        await this.setState({
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
    }

    async onAuthoritiesTimerChange(evt) {
        await this.setState({
            authoritiesTimer: evt.target.value
        });
    }

    async saveAuthoritiesTimer() {
        await SmartHomeSecurityService.modifyAuthoritiesTimer(this.state.authoritiesTimer);
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
            </Container>
        );
    }
}
