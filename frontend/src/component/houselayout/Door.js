import React from "react";
import RoomItem from "./RoomItem";

export default class Door extends RoomItem {

    roomWidth = undefined;
    roomHeight = undefined;

    static dimension = 20;

    getImagePath() {
        let path = "/door";

        if (this.state.state === "LOCKED") {
            path += "-locked"
        }

        return path + ".png";
    }

    render() {
        return (
            <image
                x={this.state.x}
                y={this.state.y}
                width={Door.dimension}
                height={Door.dimension}
                href={this.getImagePath()}
            />
        );
    }

}
