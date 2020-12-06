import React from "react";
import {ListGroup, Modal} from "react-bootstrap";
import SmartHomeHeaterService from "../service/SmartHomeHeaterService";
import HouseLayoutService from "../service/HouseLayoutService";

export class AddRoomToZone extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            zone: props.zone,
            rooms: [],
            currentRoomLabel: "",
            addingRoom: false,
            zones: []
        };

        this.hideReset = this.hideReset.bind(this);
        this.addRoom = this.addRoom.bind(this);
    }

    async componentDidMount() {
        await this.setState({
            rooms: await HouseLayoutService.getAllRooms()
        });
    }

    async hideReset() {
        await this.setState({
            addingRoom: false
        });

        window.dispatchEvent(new Event("updateSelectedZone"));
    }

    async addRoom(room) {
        await SmartHomeHeaterService.addRoomToZone({ rowId: room.rowId, roomId: room.id }, this.state.zone.id).then(async response => {
            const updatedRooms = this.state.zone.rooms;
            updatedRooms.push({
                value: response.data,
                label: response.data.name
            });

            await this.setState({
                zone: {
                    ...this.state.zone,
                    rooms: updatedRooms
                }
            })
        });
    }

    render() {
        return (
            <div className="EditProfilesDiv">
                <div className="AddRoomToZoneButton DarkButton" onClick={() => this.setState({ addingRoom: true })}>
                    <div className="AddRoomToZoneButtonInside">+</div>
                </div>
                <Modal show={this.state.addingRoom} onHide={this.hideReset}>
                    <Modal.Header closeButton>
                        <Modal.Title>{`Adding Rooms to ${this.state.zone.name}`}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <ListGroup>
                            {
                                this.state.rooms.map((item) =>
                                    <ListGroup.Item
                                        key={this.state.rooms.indexOf(item)} className="ItemsTable"
                                        bsPrefix="list-group-item py-1" action
                                        variant="dark"
                                        disabled={this.state.zone.rooms.some(r => item.label === r.label)}
                                        onClick={async () => await this.addRoom(item.value)}
                                    >
                                        <div style={{float: "left"}}>
                                            {item.label}
                                        </div>
                                    </ListGroup.Item>
                                )
                            }
                        </ListGroup>
                    </Modal.Body>
                </Modal>
            </div>
        )
    }

}
