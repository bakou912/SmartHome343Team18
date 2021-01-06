import React from "react";
import RoomItem from "../../component/houselayout/RoomItem";

export default class DoorsFactory {

    static dimension = 15;

    static getImagePath(state) {
        let path = "/door";

        if (state === "LOCKED") {
            path += "-locked";
        } else if (state === "OPEN") {
            path += "-open";
        }

        return path + ".png";
    }

    static create(room, startPosition, roomDimensions, rowIndex) {
        const doorModels = room.doors;
        const doorComponents = [];

        for(let i = 0; i < doorModels.length; i++) {

            doorComponents.push(
                <RoomItem
                    key={`${i}`}
                    roomWidth={roomDimensions.width}
                    roomHeight={roomDimensions.height}
                    direction={doorModels[i].direction}
                    dimension={DoorsFactory.dimension}
                    imagePath={this.getImagePath(doorModels[i].state)}
                />
            );
        }

        return (
            <svg
                key={`${rowIndex}${room.id}`}
                x={startPosition.x - DoorsFactory.dimension / 2}
                y={startPosition.y - DoorsFactory.dimension / 2}
                width={roomDimensions.width + DoorsFactory.dimension}
                height={roomDimensions.height + DoorsFactory.dimension}
            >
                {doorComponents}
            </svg>
        );
    }

}
