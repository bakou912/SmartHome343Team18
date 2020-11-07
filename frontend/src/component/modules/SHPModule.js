import React from "react";
import "../../style/Modules.css";
import Switch from "react-switch";
import { Container, Button, Col, Row } from "react-bootstrap";
import HttpClient from "../../service/HttpClient";
export default class SHPModule extends React.Component {
  constructor() {
    super();
    this.state = { checked: false };
    this.changeHandler = this.changeHandler.bind(this);
  }

  changeHandler(checked) {
    this.setState({ checked });
    HttpClient.put("context/awayMode/" + checked);
  }

  render() {
    return (
      <Container>
        <div
          className="Module"
          style={{
            display: "flex",
            justifyContent: "left",
            alignItems: "left",
          }}
        >
          <label>
            <span>Away Mode</span>
            <Switch
              onChange={this.changeHandler}
              checked={this.state.checked}
            />
          </label>
        </div>
      </Container>
    );
  }
}
