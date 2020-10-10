import React from "react";

export default class Room extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            name: props.name,
            x: props.x,
            y: props.y
        };
    }

    render() {
        return (
            <g>
                <rect x={this.state.x} y={this.state.y} width="100" height="50" style={{fill: "rgba(0,0,0,0)", strokeWidth: "2", stroke: "black"}} />
                <svg x={this.state.x} y={this.state.y} width="100" height="50">
                    <text x="50%" y="50%" textAnchor="middle" alignmentBaseline="central" fontFamily="Verdana" fontSize="10" fill="black">{this.state.name}</text>
                </svg>
            </g>
        );
    }
}
