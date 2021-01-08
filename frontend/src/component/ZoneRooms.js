import React, {useEffect, useState} from "react";
import {ListGroup} from "react-bootstrap";
import {EditZoneRoom} from "./EditZoneRoom";

export default function ZoneRooms(props) {

    const [rooms, setRooms] = useState(props.rooms);
    const zoneLabel = props.zoneLabel;

    const updateListener = async (evt) => {
        setRooms(evt.detail.rooms);
    };

    // Adding event subscription on mount
    useEffect( () => {
        window.addEventListener("updateZoneRooms", updateListener);
    }, []);

    // Removing event subscription on unmount
    useEffect(() => {
        return () => {
            window.removeEventListener("updateZoneRooms", updateListener);
        }
    }, []);

    return (
        <div>
            <ListGroup>
                {
                    rooms.map((item) =>
                        <ListGroup.Item
                            key={item.label} className="ItemsTable"
                            bsPrefix="list-group-item py-1" action
                            variant="dark">
                            <div>
                                <div className="RoomLabelTemp">
                                    <span>{item.label}</span>:&nbsp;
                                    <span style={{color: item.value.heatingMode === "OVERRIDDEN" ? "orange" : "black"}}>
                                        {Math.round(item.value.temperature * 10) / 10}&deg;C
                                    </span>
                                </div>
                                <div className="RoomZone">
                                    {zoneLabel} &nbsp;
                                    <EditZoneRoom key={item.label} className="RoomLabelTemp" zone={zoneLabel} room={item.value}/>
                                </div>
                            </div>
                        </ListGroup.Item>
                    )
                }
            </ListGroup>
        </div>
    );
}
