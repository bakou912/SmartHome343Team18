import React from "react";

export default class Light extends React.Component {

    static dimension = 15;

    constructor(props) {
        super(props);

        this.state = {
            x: props.x,
            y: props.y,
            state: props.state
        };
    }

    getImagePath() {
        let path = "/light";

        if (this.state.state === "ON") {
            path += "-on"
        }

        return path + ".png";
    }

    render() {
        return (
            <image
                x={this.state.x}
                y={this.state.y}
                width={Light.dimension}
                height={Light.dimension}
                href={this.getImagePath()}
            />
        );
    }

}
