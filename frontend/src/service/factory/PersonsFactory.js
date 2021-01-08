import React from "react";
import Person from "../../component/houselayout/Person";

export default class PersonsFactory {

    static dimension = 10;

    static create(room, startPosition, roomDimensions, rowIndex) {
        const personModels = room.persons;
        const personComponents = [];

        for(let i = 0; i < personModels.length; i++) {

            personComponents.push(
                <Person
                    key={`${i}`}
                    x={i * Person.dimension}
                    y="0"
                />
            );
        }

        const svgWidth = personComponents.length * PersonsFactory.dimension;

        return (
            <svg
                key={`${rowIndex}${room.id}`}
                x={startPosition.x + roomDimensions.width / 2 - svgWidth / 2}
                y={startPosition.y + roomDimensions.height / 2 - 25}
                width={svgWidth}
                height={roomDimensions.height}
            >
                {personComponents}
            </svg>
        );
    }

}
