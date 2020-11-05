import React from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import HouseLayout from "./HouseLayout";
import "../style/App.css";
import "../style/Dashboard.css";
import SimulationContextService from "../service/SimulationContextService";
import SimulationContext from "./SimulationContext";
import Modules from "../component/modules/Modules";

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
                    <Col md={2} className="SimulationContextContainer">
                        <Row className="ContextRow">
                            <Col className="ContextCol">
                                <Container>
                                    <h2 className="SectionHeader ContextHeader">
                                        SHS
                                    </h2>
                                </Container>
                                <SimulationContext/>
                            </Col>
                        </Row>
                    </Col>
                    <Col md={4} className="ModulesContainer">
                        <Row className="ModulesRow">
                            <Col className="ModulesCol">
                                <Container>
                                    <h2 className="SectionHeader ModulesHeader">
                                        Modules
                                    </h2>
                                    <Modules/>
                                </Container>
                            </Col>
                        </Row>
                    </Col>
                    <Col md={3} className="HouseLayoutContainer">
                        <Container>
                            <Row className="HouseLayoutImage">
                                <h2 className="SectionHeader">
                                    House Layout
                                </h2>
                                <HouseLayout/>
                            </Row>
                        </Container>
                    </Col>
                </Row>
            </Container>
        );
    }

}
