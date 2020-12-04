import React from "react";
import SmartHomeHeaterService from "../../service/SmartHomeHeaterService";
import "../../style/Modules.css";
import { Col, Container, Row } from "react-bootstrap";

export default class SHHModule extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            summerDefaultTemp: null,
            winterDefaultTemp: null,
        };
        this.defaultTempChange = this.winterDefaultTemp.bind(this);
    }
    async onSelectedItem(item, evt) {
        await this.setState({
            [`selected${item}`]: evt,
        });
    }

    async defaultTempChange(evt, name) {
        await this.setState({
            [`${name}DefaultTemp`]: evt.target.value,
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
                                onChange={async evt => await this.defaultTempChange(evt, "summer")}
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
                                onChange={async evt => await this.defaultTempChange(evt, "winter")}
                            />
                            &nbsp;
                        </label>
                    </Col>
                </Row>
            </Container>
        );
    }
}
