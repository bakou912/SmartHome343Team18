import React from "react";
import Light from "../../component/houselayout/Light";

export default class LightsFactory {

    static create(room, startPosition, roomDimensions, rowIndex) {
        const lightModels = room.lights;
        const lightComponents = [];

        for(let i = 0; i < lightModels.length; i++) {

            lightComponents.push(
                <Light
                    key={`${i}`}
                    x={i * Light.dimension}
                    y="0"
                    state={lightModels[i].state}
                />
            );
        }

        console.log("nb lights:" + lightComponents.length);

        const svgWidth = lightComponents.length * Light.dimension;

        return (
            <svg
                key={`${rowIndex}${room.id}`}
                x={startPosition.x + roomDimensions.width / 2 - svgWidth / 2}
                y={startPosition.y + roomDimensions.height / 2}
                width={svgWidth}
                height={roomDimensions.height}
            >
                {lightComponents}
            </svg>
        );
    }

}
