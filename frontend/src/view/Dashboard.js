import React from "react";
import {Container} from "react-bootstrap";
import HouseLayout from "./HouseLayout";
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
            <Container>
                <header className="header">
                    <h1>
                        Smart Home Simulator
                    </h1>
                </header>
                <HouseLayout/>
            </Container>
        );
    }

}
