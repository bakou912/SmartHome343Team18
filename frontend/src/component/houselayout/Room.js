import React from "react";

export default class Room extends React.Component {

    roomWidth = undefined;
    roomHeight = undefined;

    roomModel = undefined;

    constructor(props) {
        super(props);

        this.roomModel = props.room;
        this.roomWidth = props.width;
        this.roomHeight = props.height;

        this.state = {
            x: props.x,
            y: props.y
        };
    }

    render() {
        return (
            <g>
                <rect x={this.state.x} y={this.state.y} width={this.roomWidth} height={this.roomHeight} style={{fill: "rgba(0,0,0,0)", strokeWidth: "2", stroke: "black"}} />
                <svg x={this.state.x} y={this.state.y} width={this.roomWidth} height={this.roomHeight} xmlns="http://www.w3.org/2000/svg">
                    <text x="50%" y="40%" textAnchor="middle" alignmentBaseline="central" fontFamily="Verdana" fontSize="9" fill="black">{this.roomModel.name}</text>
                </svg>
            </g>
        );
    }

}
