import React from "react";

export default function Person (props) {
    return (
        <image
            x={props.x}
            y={props.y}
            width={props.dimension}
            height={props.dimension}
            href="/person.png"
        />
    );
}
