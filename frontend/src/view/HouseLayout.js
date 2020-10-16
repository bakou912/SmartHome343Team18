import React from "react";
import Room from "../component/houselayout/Room";
import DoorsFactory from "../service/factory/DoorsFactory";
import houseLayoutService from "../service/HouseLayoutService";
import WindowsFactory from "../service/factory/WindowsFactory";
import LightsFactory from "../service/factory/LightsFactory";
import {Container} from "react-bootstrap";
import "../style/HouseLayoutView.css";
import { TransformWrapper, TransformComponent } from "react-zoom-pan-pinch";


export default class HouseLayout extends React.Component {

    layoutModel = undefined;
    roomDimensions = { width: 120, height: 60 }

    constructor(props) {
        super(props);
        this.state = {
            rooms: [],
            doors: [],
            lights: [],
            windows: [],
            layoutWidth: 1,
            layoutHeight: 1
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
        let layoutHeight = this.layoutModel.rows.length * this.roomDimensions.height;
        let nbRoomsMax = 0;

        for(let i = 0; i < rows.length; i++) {
            const rowRooms = rows[i].rooms;

            for(let j = 0; j < rowRooms.length; j++) {
                const room = rowRooms[j];
                const startPosition = { x: j * this.roomDimensions.width, y: i * this.roomDimensions.height};

                rooms.push(
                    <Room
                        key={`${i}${j}`}
                        x={startPosition.x}
                        y={startPosition.y}
                        room={room}
                        width={this.roomDimensions.width}
                        height={this.roomDimensions.height}
                    />
                );

                doors = doors.concat(DoorsFactory.create(room, startPosition, this.roomDimensions, i));
                windows = windows.concat(WindowsFactory.create(room, startPosition, this.roomDimensions, i));
                lights = lights.concat(LightsFactory.create(room, startPosition, this.roomDimensions, i));

                if (j >= nbRoomsMax) {
                    nbRoomsMax++;
                }
            }
        }

        return {
            rooms: rooms,
            doors: doors,
            lights: lights,
            windows: windows,
            layoutWidth: nbRoomsMax * this.roomDimensions.width,
            layoutHeight: layoutHeight
        };
    }

    render() {
        return (
            <Container className="houseLayoutContainer">
                <TransformWrapper>
                    <TransformComponent>
                        <svg className="houseLayoutRepresentation">
                            <g transform={`translate(${300 - this.state.layoutWidth / 2}, ${200 - this.state.layoutHeight / 2})`}>
                                {this.state.rooms}
                                {this.state.doors}
                                {this.state.windows}
                                {this.state.lights}
                            </g>
                        </svg>
                    </TransformComponent>
                </TransformWrapper>
            </Container>
        );
    }

}
