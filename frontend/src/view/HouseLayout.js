import "../style/HouseLayoutView.css";
import React, {useCallback, useEffect, useRef, useState} from "react";
import Room from "../component/houselayout/Room";
import DoorsFactory from "../service/factory/DoorsFactory";
import HouseLayoutService from "../service/HouseLayoutService";
import WindowsFactory from "../service/factory/WindowsFactory";
import LightsFactory from "../service/factory/LightsFactory";
import { TransformWrapper, TransformComponent } from "react-zoom-pan-pinch";
import { Container } from "react-bootstrap";
import PersonsFactory from "../service/factory/PersonsFactory";

export default function HouseLayout() {

    const roomDimensions = useRef({ width: 150, height: 75 });
    const layoutModel = useRef(null);
    const [rooms, setRooms] = useState([]);
    const [doors, setDoors] = useState([]);
    const [lights, setLights] = useState([]);
    const [windows, setWindows] = useState([]);
    const [persons, setPersons] = useState([]);
    const [layoutWidth, setLayoutWidth] = useState(1);
    const [layoutHeight, setLayoutHeight] = useState(150);

    const createLocations = useCallback(() => {
        console.log("creating layout")
        const rows = layoutModel.current.rows;
        const newRooms = [];
        let newDoors = [];
        let newLights = [];
        let newWindows = [];
        let newPersons = [];
        let newLayoutHeight = layoutModel.current.rows.length * roomDimensions.current.height;
        let nbRoomsMax = 0;

        for(let i = 0; i < rows.length; i++) {
            const rowRooms = rows[i].rooms;

            for(let j = 0; j < rowRooms.length; j++) {
                const room = rowRooms[j];
                const startPosition = { x: j * roomDimensions.current.width, y: i * roomDimensions.current.height};

                newRooms.push(
                    <Room
                        key={`${i}${j}`}
                        x={startPosition.x}
                        y={startPosition.y}
                        room={room}
                        width={roomDimensions.current.width}
                        height={roomDimensions.current.height}
                    />
                );

                newDoors = newDoors.concat(DoorsFactory.create(room, startPosition, roomDimensions.current, i));
                newWindows = newWindows.concat(WindowsFactory.create(room, startPosition, roomDimensions.current, i));
                newLights = newLights.concat(LightsFactory.create(room, startPosition, roomDimensions.current, i));
                newPersons = newPersons.concat(PersonsFactory.create(room, startPosition, roomDimensions.current, i));

                if (j >= nbRoomsMax) {
                    nbRoomsMax++;
                }
            }
        }

        const backyardPosition = { x: 0, y: -layoutHeight / 2 };
        const entrancePosition = { x: 0, y: layoutHeight};
        const outsideDimensions = { width: nbRoomsMax * roomDimensions.current.width, height: layoutHeight / 2};

        newLights = newLights.concat(LightsFactory.create(layoutModel.current.backyard, backyardPosition, outsideDimensions, "-1"));
        newPersons = newPersons.concat(PersonsFactory.create(layoutModel.current.backyard, backyardPosition, outsideDimensions, "-1"));

        newLights = newLights.concat(LightsFactory.create(layoutModel.current.entrance, entrancePosition, outsideDimensions, "-2"));
        newPersons = newPersons.concat(PersonsFactory.create(layoutModel.current.entrance, entrancePosition, outsideDimensions, "-2"));

        setRooms(newRooms);
        setDoors(newDoors);
        setWindows(newWindows);
        setLights(newLights);
        setPersons(newPersons);
        setLayoutWidth(nbRoomsMax * roomDimensions.current.width);
        setLayoutHeight(newLayoutHeight);
    }, [layoutHeight]);

    const layoutInit = useCallback(async () => {
        layoutModel.current = (await HouseLayoutService.getLayout()).data;

        if (layoutModel.current && Array.isArray(layoutModel.current.rows)) {
            createLocations();
        }
    }, [createLocations]);

    useEffect(
        () => {
            window.addEventListener("updateLayout", async () => {
                await layoutInit();
            });

            layoutInit();
        }, [layoutInit]
    );

    return (
        <Container className="HouseLayoutRepresentation">
            <TransformWrapper>
                <TransformComponent>
                    <svg width="600px" height="400px">
                        <g transform={`translate(${300 - layoutWidth / 2}, ${200 - layoutHeight / 2})`}>
                            {rooms}
                            {doors}
                            {windows}
                            {lights}
                            {persons}
                        </g>
                    </svg>
                </TransformComponent>
            </TransformWrapper>
        </Container>
    );
}
