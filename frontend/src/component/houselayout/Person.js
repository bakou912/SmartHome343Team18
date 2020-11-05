import React from "react";

export default class Person extends React.Component {

    static dimension = 10;

    constructor(props) {
        super(props);

        this.state = {
            x: props.x,
            y: props.y,
            state: props.state
        };
    }
    render() {
        return (
            <image
                x={this.state.x}
                y={this.state.y}
                width={Person.dimension}
                height={Person.dimension}
                href="/person.png"
            />
        );
    }

}
