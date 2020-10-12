import React from "react";
import Window from "../../component/houselayout/Window";

export default class WindowsFactory {

    static create(room, startPosition, roomDimensions, rowIndex) {
        const windowModels = room.windows;
        const windowComponents = [];

        for(let i = 0; i < windowModels.length; i++) {

            windowComponents.push(
                <Window
                    key={`${i}`}
                    roomWidth={roomDimensions.width}
                    roomHeight={roomDimensions.height}
                    direction={windowModels[i].direction}
                    state={windowModels[i].state}
                />
            );
        }

        return (
            <svg
                key={`${rowIndex}${room.id}`}
                x={startPosition.x - Window.dimension / 2}
                y={startPosition.y - Window.dimension / 2}
                width={roomDimensions.width + Window.dimension}
                height={roomDimensions.height + Window.dimension}
            >
                {windowComponents}
            </svg>
        );
    }

}
