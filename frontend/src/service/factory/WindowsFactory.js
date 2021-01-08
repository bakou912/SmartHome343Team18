import React from "react";
import RoomItem from "../../component/houselayout/RoomItem";

export default class WindowsFactory {

    static dimension = 15;

    static getImagePath(state) {
        let path = "/window";

        if (state === "CLOSED") {
            path += "-closed"
        }

        if (state === "BLOCKED") {
            path += "-blocked"
        }

        return path + ".png";
    }

    static create(room, startPosition, roomDimensions, rowIndex) {
        const windowModels = room.windows;
        const windowComponents = [];

        for(let i = 0; i < windowModels.length; i++) {

            windowComponents.push(
                <RoomItem
                    key={`${i}`}
                    roomWidth={roomDimensions.width}
                    roomHeight={roomDimensions.height}
                    direction={windowModels[i].direction}
                    state={windowModels[i].state}
                    dimension={WindowsFactory.dimension}
                    imagePath={this.getImagePath(windowModels[i].state)}
                />
            );
        }

        return (
            <svg
                key={`${rowIndex}${room.id}`}
                x={startPosition.x - WindowsFactory.dimension / 2}
                y={startPosition.y - WindowsFactory.dimension / 2}
                width={roomDimensions.width + WindowsFactory.dimension}
                height={roomDimensions.height + WindowsFactory.dimension}
            >
                {windowComponents}
            </svg>
        );
    }

}
