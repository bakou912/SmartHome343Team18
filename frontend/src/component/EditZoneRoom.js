import React, {useEffect, useState} from "react";
import {Button, Col, Image, ListGroup, Modal, Row} from "react-bootstrap";
import SmartHomeHeaterService from "../service/SmartHomeHeaterService";
import Command from "./modules/Command";
import TempLimitsUtil from "../service/util/TempLimitsUtil";

export function EditZoneRoom(props) {

    const [room, setRoom] = useState(props.room);
    const [currentZoneLabel, setCurrentZoneLabel] = useState(props.zone);
    const [editingZoneRoom, setEditingZoneRoom] = useState(false);
    const [zones, setZones] = useState([]);

    const hideReset = async () => {
        setEditingZoneRoom(false);
        window.dispatchEvent(new Event("updateSelectedZone"));
    }

    const handleClick = async (item) => {
        if (item.label === currentZoneLabel) {
            return;
        }

        await SmartHomeHeaterService.addRoomToZone({ rowId: room.rowId, roomId: room.id }, item.value.id).then(async() => {
            setCurrentZoneLabel(item.label)
        });

        window.dispatchEvent(new Event("updateSelectedZone"));
    }

    const overrideTempChange = async (evt, limits) => {
        if (!evt.target.value || isNaN(evt.target.value)) {
            return;
        }

        const limitedTemp = TempLimitsUtil.adjustTempWithLimits(evt.target.value, limits);

        await SmartHomeHeaterService.overrideRoomTemp(room.rowId, room.id, limitedTemp).then(async() => {
            setRoom({
                ...room,
                heatingMode: "OVERRIDDEN",
                temperature: limitedTemp
            });
        })
    }

    const removeOverride = async () => {
        await SmartHomeHeaterService.removeRoomOverride(room.rowId, room.id).then(async response => {

            setRoom({
                ...room,
                heatingMode: response.data
            });
        });
    }

    useEffect( () => {
        (async () => setZones(await SmartHomeHeaterService.getZones()))();
    }, []);

    return (
        <div className="EditProfilesDiv">
            <div className="EditProfilesButton DarkButton" onClick={() => setEditingZoneRoom(true)}>
                <Image className="EditIcon" style={{display: "block"}} width="15px" alt="Edit Zone Room" src={"/edit.png"}/>
            </div>
            <Modal show={editingZoneRoom} onHide={hideReset}>
                <Modal.Header closeButton>
                    <Modal.Title>{`Edit ${room.name}`}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Row>
                        <Col md={4}>
                            <ListGroup>
                                Choose zone
                                {
                                    zones.map((item) =>
                                        <ListGroup.Item
                                            style={{textAlign: "center"}}
                                            key={zones.indexOf(item)}
                                            className="ItemsTable"
                                            bsPrefix="list-group-item py-1" action
                                            variant="dark"
                                            active={item.label === currentZoneLabel}
                                            disabled={item.label === currentZoneLabel}
                                            onClick={async () => handleClick(item)}
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
                                    value={room.temperature}
                                    onChange={async evt => await overrideTempChange(evt, { min: -50, max: 50})}
                                    min={-50} max={50}
                                />
                                &nbsp;
                                {
                                    room.heatingMode === "OVERRIDDEN" &&
                                    <Button onClick={removeOverride} variant="dark" size="sm">
                                        X
                                    </Button>
                                }
                            </Command>
                        </Col>
                    </Row>
                </Modal.Body>
            </Modal>
        </div>
    );
}
