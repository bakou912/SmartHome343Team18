import React from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import HouseLayout from "./HouseLayout";
import "../style/App.css";
import "../style/Dashboard.css";
import HouseLayoutService from "../service/HouseLayoutService";
import ParametersService from "../service/ParametersService";
import SimulationContextService from "../service/SimulationContextService";

export default class Dashboard extends React.Component {

    layoutModel = undefined;

    constructor(props) {
        super(props);
        this.state = {

        };
    }

    async componentDidMount() {
        if (localStorage.getItem( "parametersSet") === "false") {
            this.redirectToParameters();
        }
    }

    redirectToParameters() {
        window.location = "http://localhost:3000/parameters";
    }

    async resetSimulator() {
        await SimulationContextService.resetContext();

        localStorage.setItem("uploadedFile", "false");
        localStorage.setItem("parametersSet", "false");

        this.redirectToParameters();
    }

    render() {
        return (
            <Container fluid className="Dashboard">
                <header className="Header">
                    <h1 className="HeaderText">
                        Smart Home Simulator
                    </h1>
                    <Button
                        variant="outline-danger"
                        title="Reset Simulator"
                        className="HeaderResetButton"
                        onClick={async() => await this.resetSimulator()}
                    >
                        Reset
                    </Button>
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
