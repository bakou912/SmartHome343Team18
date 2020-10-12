import React from "react";
import RoomItem from "./RoomItem";

export default class Window extends RoomItem {

    roomWidth = undefined;
    roomHeight = undefined;

    static dimension = 20;

    getImagePath() {
        let path = "/window";

        if (this.state.state === "CLOSED") {
            path += "-closed"
        }

        if (this.state.state === "BLOCKED") {
            path += "-blocked"
        }

        return path + ".png";
    }

    render() {
        return (
            <image
                x={this.state.x}
                y={this.state.y}
                width={Window.dimension}
                height={Window.dimension}
                href={this.getImagePath()}
            />
        );
    }

}
