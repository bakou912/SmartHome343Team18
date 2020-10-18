import React from "react";
import Container from "react-bootstrap/Container";
import { Button } from "react-bootstrap";
import httpLayoutService from "../../service/HouseLayoutService";

export default class HouseLayoutUpload extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            file: null
        };

        this.fileChangedHandler = this.fileChangedHandler.bind(this);
        this.fileUploadHandler = this.fileUploadHandler.bind(this);

    }

    async fileChangedHandler(event) {
        this.setState({
            file: event.target.files[0]
        });
    }

    async fileUploadHandler() {
        await httpLayoutService.createLayout(this.state.file)
        .catch(() =>{
          alert("Invalid File");
        });
    }

    render() {
        return (
            <Container>
                <p>Please enter a valid House Layout initialization file:</p>
                <input
                    type="file"
                    name="file"
                    onChange={this.fileChangedHandler}
                />
                <br/><br/>
                <Button onClick={this.fileUploadHandler}>
                    Create Layout
                </Button>
            </Container>
        )
    }
}
