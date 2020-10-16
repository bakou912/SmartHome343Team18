import React from "react";
import {Container} from "react-bootstrap";
import HouseLayout from "./HouseLayout";

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
                <HouseLayout/>
            </Container>
        );
    }

}
