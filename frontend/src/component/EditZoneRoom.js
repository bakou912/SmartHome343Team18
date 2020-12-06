import React from "react";
import {Button, Col, Image, ListGroup, Modal, Row} from "react-bootstrap";
import SmartHomeHeaterService from "../service/SmartHomeHeaterService";
import SHHModule from "./modules/SHHModule";
import Command from "./modules/Command";

export class EditZoneRoom extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            room: props.room,
            currentZoneLabel: props.zone,
            editingZoneRoom: false,
            zones: []
        };

        this.hideReset = this.hideReset.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.overrideTempChange = this.overrideTempChange.bind(this);
        this.removeOverride = this.removeOverride.bind(this);
    }

    async componentDidMount() {
        await this.setState({
            zones: await SmartHomeHeaterService.getZones()
        });
    }

    async hideReset() {
        await this.setState({
            editingZoneRoom: false
        });

        window.dispatchEvent(new Event("updateSelectedZone"));
    }

    async handleClick(item) {
        if (item.label === this.state.currentZoneLabel) {
            return;
        }

        await SmartHomeHeaterService.addRoomToZone({ rowId: this.state.room.rowId, roomId: this.state.room.id }, item.value.id).then(async() => {
            await this.setState({
                currentZoneLabel: item.label
            });
        });
        window.dispatchEvent(new Event("updateSelectedZone"));
    }

    async overrideTempChange(evt, limits) {
        if (!evt.target.value || isNaN(evt.target.value)) {
            return;
        }

        const limitedTemp = SHHModule.adjustTempWithLimits(evt.target.value, limits);

        await SmartHomeHeaterService.overrideRoomTemp(this.state.room.rowId, this.state.room.id, limitedTemp).then(async() => {
            await this.setState({
                room: {
                    ...this.state.room,
                    heatingMode: "OVERRIDDEN",
                    temperature: limitedTemp
                }
            });
        })
    }

    async removeOverride() {
        await SmartHomeHeaterService.removeRoomOverride(this.state.room.rowId, this.state.room.id).then(async response => {
            await this.setState({
                room: {
                    ...this.state.room,
                    heatingMode: response.data
                }
            });
        });
    }

    render() {
        return (
            <div className="EditProfilesDiv">
                <div className="EditProfilesButton DarkButton" onClick={() => this.setState({ editingZoneRoom: true })}>
                    <Image className="EditIcon" style={{display: "block"}} width="15px" alt="Edit Zone Room" src={"/edit.png"}/>
                </div>
                <Modal show={this.state.editingZoneRoom} onHide={this.hideReset}>
                    <Modal.Header closeButton>
                        <Modal.Title>{`Edit ${this.state.room.name}`}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Row>
                            <Col md={4}>
                                <ListGroup>
                                    Choose zone
                                    {
                                        this.state.zones.map((item) =>
                                            <ListGroup.Item
                                                style={{textAlign: "center"}}
                                                key={this.state.zones.indexOf(item)}
                                                className="ItemsTable"
                                                bsPrefix="list-group-item py-1" action
                                                variant="dark"
                                                active={item.label === this.state.currentZoneLabel}
                                                disabled={item.label === this.state.currentZoneLabel}
                                                onClick={async () => this.handleClick(item)}
                                            >
                                                <div>
                                                    {item.label}
                                                </div>
                                            </ListGroup.Item>
                                        )
                                    }
                                </ListGroup>
                            </Col>
                            <Col md={8}>
                                <Command name="Overriding room's temperature">
                                    Override Temperature (&deg;C):&nbsp;
                                    <input
                                        style={{ width: "50px" }}
                                        name="SummerTemp"
                                        type="number"
                                        value={this.state.room.temperature}
                                        onChange={async evt => await this.overrideTempChange(evt, { min: -50, max: 50})}
                                        min={-50} max={50}
                                    />
                                    &nbsp;
                                    {
                                        this.state.room.heatingMode === "OVERRIDDEN" &&
                                        <Button onClick={this.removeOverride} variant="dark" size="sm">
                                            X
                                        </Button>
                                    }
                                </Command>
                            </Col>
                        </Row>
                    </Modal.Body>
                </Modal>
            </div>
        )
    }

}
