import React from "react";
import {Image, ListGroup, Modal} from "react-bootstrap";
import SmartHomeHeaterService from "../service/SmartHomeHeaterService";

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
    }

    render() {
        return (
            <div className="EditProfilesDiv">
                <div className="EditProfilesButton DarkButton" onClick={() => this.setState({ editingZoneRoom: true })}>
                    <Image className="EditIcon" style={{display: "block"}} width="15px" alt="Edit Zone Room" src={"/edit.png"}/>
                </div>
                <Modal show={this.state.editingZoneRoom} onHide={this.hideReset}>
                    <Modal.Header closeButton>
                        <Modal.Title>Room Zone Editing</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <ListGroup>
                            {
                                this.state.zones.map((item) =>
                                    <ListGroup.Item
                                        key={this.state.zones.indexOf(item)} className="ItemsTable"
                                        bsPrefix="list-group-item py-1" action
                                        variant="dark"
                                        active={item.label === this.state.currentZoneLabel}
                                        onClick={async () => this.handleClick(item)}
                                    >
                                        <div>
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
