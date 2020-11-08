import React from "react";
import "../../style/Modules.css";
import { Container } from "react-bootstrap";

export default class SHPModule extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (    
            <Container>
                <textArea
                    className="styleText"
                    disabled
                >{this.props.value}</textArea>
            </Container>  
        );
    }
}
