import React from "react";

export default class RoomItem extends React.Component {

    roomWidth = undefined;
    roomHeight = undefined;

    static dimension = 20;

    constructor(props) {
        super(props);

        this.roomWidth = props.roomWidth;
        this.roomHeight = props.roomHeight;

        const { x, y } = this.createPosition(props.direction);

        this.state = {
            x: x,
            y: y,
            state: props.state
        };
    }

    createPosition(direction) {
        let x = "0", y = "0";

        if (direction === "NORTH" || direction === "SOUTH") {
            x = this.roomWidth / 2;

            if (direction === "SOUTH") {
                y = this.roomHeight;
            }
        }

        if (direction === "WEST" || direction === "EAST") {
            y = this.roomHeight / 2;

            if (direction === "EAST") {
                x = this.roomWidth;
            }
        }

        return { x, y };
    }

}
