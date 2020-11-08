import React from "react";
import SmartHomeSecurityService from "../../service/SmartHomeSecurityService";
import "../../style/Modules.css";
import Switch from "react-switch";
import { Container } from "react-bootstrap";
export default class SHPModule extends React.Component {

    constructor(props) {
        super(props);
        this.state = { checked: false };
        this.changeHandler = this.changeHandler.bind(this);
    }

    async componentDidMount() {
        this.setState({
            checked: (await SmartHomeSecurityService.getAwayModeState()).data
        })
    }

    async changeHandler(checked) {
        await SmartHomeSecurityService.toggleAwayMode(checked).then(async () => {
            await this.setState({
                checked: checked
            });
        });
    }

    render() {
        return (
            <Container>
                <Container 
                    className="Module"
                    style={{
                    display: "flex",
                    justifyContent: "left",
                    alignItems: "left",
                    }}
                >
                    <Container>
                        <span>Away Mode</span>
                            <Switch
                                onChange={this.changeHandler}
                                checked={this.state.checked}
                            />  
                    </Container>
                </Container>
            </Container>
        );
    }
}
