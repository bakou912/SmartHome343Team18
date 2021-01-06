import React from "react";
import Light from "../../component/houselayout/Light";

export default class LightsFactory {

    static dimension = 15;

    static create(room, startPosition, roomDimensions, rowIndex) {
        return (
            <svg
                key={`${rowIndex}${room.id}`}
                x={startPosition.x + roomDimensions.width / 2 - LightsFactory.dimension / 2}
                y={startPosition.y + roomDimensions.height / 2}
                width={LightsFactory.dimension}
                height={roomDimensions.height}
            >
                <Light
                    x="0"
                    y="0"
                    state={room.light.state}
                    dimension={LightsFactory.dimension}
                />
            </svg>
        );
    }

}
