import React, {useEffect, useState} from "react";
import {ListGroup, Modal} from "react-bootstrap";
import SmartHomeHeaterService from "../service/SmartHomeHeaterService";
import HouseLayoutService from "../service/HouseLayoutService";

export function AddRoomToZone(props) {

    const [zone, setZone] = useState(props.zone);
    const [rooms, setRooms] = useState([]);
    const [addingRoom, setAddingRoom] = useState(false);

    const hideReset = async () => {
        setAddingRoom(false);
        window.dispatchEvent(new Event("updateSelectedZone"));
    }

    const addRoom = async (room) => {
        await SmartHomeHeaterService.addRoomToZone({ rowId: room.rowId, roomId: room.id }, zone.id).then(async response => {
            const updatedRooms = zone.rooms;
            updatedRooms.push({
                value: response.data,
                label: response.data.name
            });

            setZone({
                ...zone,
                rooms: updatedRooms
            });
        });
    }

    useEffect( () => {
        (async () => setRooms(await HouseLayoutService.getAllRooms()))();
    }, []);

    return (
        <div className="EditProfilesDiv">
            <div className="AddRoomToZoneButton DarkButton" onClick={() => setAddingRoom(true)}>
                <div className="AddRoomToZoneButtonInside">+</div>
            </div>
            <Modal show={addingRoom} onHide={hideReset}>
                <Modal.Header closeButton>
                    <Modal.Title>{`Adding Rooms to ${zone.name}`}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <ListGroup>
                        {
                            rooms.map((item) =>
                                <ListGroup.Item
                                    key={rooms.indexOf(item)} className="ItemsTable"
                                    bsPrefix="list-group-item py-1" action
                                    variant="dark"
                                    disabled={zone.rooms.some(r => item.label === r.label)}
                                    onClick={async () => await addRoom(item.value)}
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
