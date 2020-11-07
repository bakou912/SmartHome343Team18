import React from "react";
import SmartHomeSecurityService from "../../service/SmartHomeSecurityService";
import "../../style/Modules.css";
import Switch from "react-switch";
import { Container } from "react-bootstrap";
export default class SHPModule extends React.Component {
    constructor() {
        super();
        this.state = { checked: SmartHomeSecurityService.getAwayModeState };
        this.changeHandler = this.changeHandler.bind(this);
    }

    changeHandler(checked) {
        this.setState({ checked });
        SmartHomeSecurityService.toggleAwayMode(checked);
    }

    render() {
        return (
            <Container>
                <div className="Module"
                    style={{
                    display: "flex",
                    justifyContent: "left",
                    alignItems: "left",
                }}
                >
                <label>
                    <span>Away Mode</span>
                        <Switch
                            onChange={this.changeHandler}
                            checked={this.state.checked}
                        />
                </label>
                </div>
            </Container>
        );
    }
}
