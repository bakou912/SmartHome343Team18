import React from "react";
import SmartHomeHeaterService from "../../service/SmartHomeHeaterService";
import "../../style/Modules.css";
import { Button, Col, Container, Row } from "react-bootstrap";
import Switch from "react-switch";
import Command from "./Command";

export default class SHHModule extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            summerDefaultTemp: null,
            winterDefaultTemp: null,
        };
        this.summerDefaultTemp = this.summerDefaultTemp.bind(this);
        this.winterDefaultTemp = this.winterDefaultTemp.bind(this);
    }
    async onSelectedItem(item, evt) {
        await this.setState({
            [`selected${item}`]: evt,
        });
    }

    async summerDefaultTemp(evt) {
        await this.setState({
            summerDefaultTemp: evt.target.value,
        });
    }

    async winterDefaultTemp(evt) {
        await this.setState({
            winterDefaultTemp: evt.target.value,
        });
    }

    render() {
        return (
            <Container className="Module">
                <Row>
                    <Col>
                        <br />
                        <label>
                            Default Summer Temperature (celsius):&nbsp;
                            <input
                                key={this.state.loaded}
                                style={{ width: "50px" }}
                                name="SummerTemp"
                                type="number"
                                onChange={this.summerDefaultTemp}
                            />
                            &nbsp;
                        </label>
                        <br />
                        <label>
                            Default Winter Temperature (celsius):&nbsp;
                            <input
                                key={this.state.loaded}
                                style={{ width: "50px" }}
                                name="WinterTemp"
                                type="number"
                                onChange={this.winterDefaultTemp}
                            />
                            &nbsp;
                        </label>
                    </Col>
                </Row>
            </Container>
        );
    }
}
