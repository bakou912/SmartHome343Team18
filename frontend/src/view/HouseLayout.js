import React from "react";
import Room from "../component/houselayout/Room";
import DoorsFactory from "../service/factory/DoorsFactory";
import houseLayoutService from "../service/HouseLayoutService";
import WindowsFactory from "../service/factory/WindowsFactory";
import LightsFactory from "../service/factory/LightsFactory";

export default class HouseLayout extends React.Component {

    layoutModel = undefined;

    constructor(props) {
        super(props);
        this.state = {
            layoutModel: null,
            rooms: []
        };
    }

    async componentDidMount() {
        this.layoutModel = (await houseLayoutService.getLayout()).data;

        if (this.layoutModel && Array.isArray(this.layoutModel.rows)) {
            this.setState(this.createRooms());
        }
    }

    createRooms() {
        const rows = this.layoutModel.rows;
        const rooms = [];
        let doors = [];
        let lights = [];
        let windows = [];

        for(let i = 0; i < rows.length; i++) {
            const rowRooms = rows[i].rooms;

            for(let j = 0; j < rowRooms.length; j++) {
                const room = rowRooms[j];
                const roomDimensions = { width: 150, height: 75 };
                const startPosition = { x: j * roomDimensions.width + 50, y: i * roomDimensions.height + 50 };

                rooms.push(
                    <Room
                        key={`${i}${j}`}
                        x={startPosition.x}
                        y={startPosition.y}
                        room={room}
                        width={roomDimensions.width}
                        height={roomDimensions.height}
                    />
                );
                doors = doors.concat(DoorsFactory.create(room, startPosition, roomDimensions, i));
                windows = windows.concat(WindowsFactory.create(room, startPosition, roomDimensions, i));
                lights = lights.concat(LightsFactory.create(room, startPosition, roomDimensions, i));

            }
        }

        return {
            rooms: rooms,
            doors: doors,
            lights: lights,
            windows: windows
        };
    }

    render() {
        return (
            <svg width="500" height="500">
                {this.state.rooms}
                {this.state.doors}
                {this.state.windows}
                {this.state.lights}
            </svg>
        );
    }

}
