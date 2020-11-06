import "../style/HouseLayoutView.css";
import React from "react";
import Room from "../component/houselayout/Room";
import DoorsFactory from "../service/factory/DoorsFactory";
import HouseLayoutService from "../service/HouseLayoutService";
import WindowsFactory from "../service/factory/WindowsFactory";
import LightsFactory from "../service/factory/LightsFactory";
import { TransformWrapper, TransformComponent } from "react-zoom-pan-pinch";
import { Container } from "react-bootstrap";
import PersonsFactory from "../service/factory/PersonsFactory";

export default class HouseLayout extends React.Component {

    layoutContainerDimensions = { width: 150, height: 75 }
    roomDimensions = { width: 150, height: 75 }

    constructor(props) {
        super(props);

        this.state = {
            rooms: [],
            doors: [],
            lights: [],
            windows: [],
            layoutWidth: 1,
            layoutHeight: 1,
            key: 0
        };

        this.layoutInit = this.layoutInit.bind(this);
    }

    async componentDidMount() {
        window.addEventListener("updateLayout", async () => {
            await this.layoutInit();
        });

        await this.layoutInit();
    }

    async layoutInit() {
        this.layoutModel = (await HouseLayoutService.getLayout()).data;

        if (this.layoutModel && Array.isArray(this.layoutModel.rows)) {
            this.setState(this.createLocations());
        }
    }

    createLocations() {
        const rows = this.layoutModel.rows;
        const rooms = [];
        let doors = [];
        let lights = [];
        let windows = [];
        let persons = [];
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
                persons = persons.concat(PersonsFactory.create(room, startPosition, this.roomDimensions, i));

                if (j >= nbRoomsMax) {
                    nbRoomsMax++;
                }
            }
        }

        const outsidePosition = { x: 0, y: -layoutHeight / 2 };
        const outsideDimensions = { width: nbRoomsMax * this.roomDimensions.width, height: layoutHeight / 2};

        lights = lights.concat(LightsFactory.create(this.layoutModel.outside, outsidePosition, outsideDimensions, "-1"));
        persons = persons.concat(PersonsFactory.create(this.layoutModel.outside, outsidePosition, outsideDimensions, "-1"));

        return {
            rooms: rooms,
            doors: doors,
            lights: lights,
            windows: windows,
            persons: persons,
            layoutWidth: nbRoomsMax * this.roomDimensions.width,
            layoutHeight: layoutHeight,
            key: this.state.key + 1
        };
    }

    render() {
        return (
            <Container className="HouseLayoutRepresentation">
                <TransformWrapper>
                    <TransformComponent>
                        <svg key={this.state.key} width="600px" height="400px">
                            <g transform={`translate(${300 - this.state.layoutWidth / 2}, ${200 - this.state.layoutHeight / 2})`}>
                                {this.state.rooms}
                                {this.state.doors}
                                {this.state.windows}
                                {this.state.lights}
                                {this.state.persons}
                            </g>
                        </svg>
                    </TransformComponent>
                </TransformWrapper>
            </Container>
        );
    }

}
