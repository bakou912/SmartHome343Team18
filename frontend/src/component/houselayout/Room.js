import React, {useRef} from "react";

export default function Room(props) {

    const roomModel = useRef(props.room);
    const roomWidth = useRef(props.width);
    const roomHeight = useRef(props.height);
    const x = useRef(props.x);
    const y = useRef(props.y);

    return (
        <g>
            <rect x={x.current} y={y.current} width={roomWidth.current} height={roomHeight.current} style={{fill: "rgba(0,0,0,0)", strokeWidth: "2", stroke: "black"}} />
            <svg x={x.current} y={y.current} width={roomWidth.current} height={roomHeight.current} xmlns="http://www.w3.org/2000/svg">
                <text x="50%" y="40%" textAnchor="middle" alignmentBaseline="central" fontFamily="Verdana" fontSize="9" fill="black">{roomModel.current.name}</text>
            </svg>
        </g>
    );
}
