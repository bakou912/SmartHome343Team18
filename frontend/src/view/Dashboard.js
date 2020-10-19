import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import HouseLayout from "./HouseLayout";
import "../style/App.css";
import "../style/Dashboard.css";

export default class Dashboard extends React.Component {

    layoutModel = undefined;

    constructor(props) {
        super(props);
        this.state = {

        };
    }

    async componentDidMount() {

    }

    render() {
        return (
            <Container fluid className="Dashboard">
                <header>
                    <h1 className="Header">
                        Smart Home Simulator
                    </h1>
                </header>
                <br/><br/><br/>
                <Row>
                    <Col className="SimulationContextContainer">

                    </Col>
                    <Col className="ModulesContainer">

                    </Col>
                    <Col className="HouseLayoutContainer">
                        <Container className="HouseLayoutImage">
                            <h2 className="SectionHeader">
                                House Layout
                            </h2>
                            <HouseLayout/>
                        </Container>
                    </Col>
                </Row>
            </Container>
        );
    }

}
