import React from "react";
import Room from "../component/houselayout/Room";
import httpLayoutService from "../service/HouseLayoutService";

export default class HouseLayout extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            layoutModel: null,
            rooms: []
        };
    }

    async componentDidMount() {
        this.setState({
            layoutModel: (await httpLayoutService.getLayout()).data
        });

        this.createRooms();
    }

    createRooms(){
        const rows = this.state.layoutModel.rows;
        const rooms = [];

        for(let i = 0; i < rows.length; i++) {
            const rowRooms = rows[i].rooms;

            for(let j = 0; j < rowRooms.length; j++) {

                rooms.push(
                    <Room key={`${i}${j}`} x={j * 100} y={i * 50} name={rowRooms[j].name}/>
                );
            }
        }

        this.setState({
            rooms: rooms
        })
    }

    render() {
        return (
            <svg width="500" height="500">
                {this.state.rooms}
            </svg>
        );
    }
}
