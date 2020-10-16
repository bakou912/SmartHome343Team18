import React from "react";
import RoomItem from "./RoomItem";

export default class Door extends RoomItem {

    roomWidth = undefined;
    roomHeight = undefined;

    static dimension = 15;

    getImagePath() {
        let path = "/door";

        if (this.state.state === "LOCKED") {
            path += "-locked";
        } else if (this.state.state === "OPEN") {
            path += "-open";
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
