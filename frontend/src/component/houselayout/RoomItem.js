import React, {useCallback, useRef} from "react";

export default function RoomItem(props) {

    const roomWidth = useRef(props.roomWidth);
    const roomHeight = useRef(props.roomHeight);

    const createPosition = useCallback((direction) => {

        let newX = "0", newY = "0";

        if (direction === "NORTH" || direction === "SOUTH") {
            newX = roomWidth.current / 2;

            if (direction === "SOUTH") {
                newY = roomHeight.current;
            }
        }

        if (direction === "WEST" || direction === "EAST") {
            newY = roomHeight.current / 2;

            if (direction === "EAST") {
                newX = roomWidth.current;
            }
        }

        return { x: newX, y: newY };
    }, [roomHeight, roomWidth]);

    const position = useRef(createPosition(props.direction));

    return (
        <image
            x={position.current.x}
            y={position.current.y}
            width={props.dimension}
            height={props.dimension}
            href={props.imagePath}
        />
    );
}
