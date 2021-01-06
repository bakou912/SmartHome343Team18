import React from "react";

export default function Light(props) {

    const getImagePath = () => {
        let path = "/light";

        if (props.state === "ON") {
            path += "-on"
        }

        return path + ".png";
    }

    return (
        <image
            x={props.x}
            y={props.y}
            width={props.dimension}
            height={props.dimension}
            href={getImagePath()}
        />
    );
}
