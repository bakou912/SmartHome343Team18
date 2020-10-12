import React from "react";
import Door from "../../component/houselayout/Door";

export default class DoorsFactory {

    static create(room, startPosition, roomDimensions, rowIndex) {
        const doorModels = room.doors;
        const doorComponents = [];

        for(let i = 0; i < doorModels.length; i++) {

            doorComponents.push(
                <Door
                    key={`${i}`}
                    roomWidth={roomDimensions.width}
                    roomHeight={roomDimensions.height}
                    direction={doorModels[i].direction}
                    state={doorModels[i].state}
                />
            );
        }

        return (
            <svg
                key={`${rowIndex}${room.id}`}
                x={startPosition.x - Door.dimension / 2}
                y={startPosition.y - Door.dimension / 2}
                width={roomDimensions.width + Door.dimension}
                height={roomDimensions.height + Door.dimension}
            >
                {doorComponents}
            </svg>
        );
    }

}
