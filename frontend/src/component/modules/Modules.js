import React from "react";
import "../../style/Modules.css";
import ContextModule from "./ContextModule";

export default class Modules extends React.Component {

    modules = [];

    constructor(props) {
        super(props);

        this.modules.push(<ContextModule/>)

        this.state = {

        };
    }

    render() {
        return (
            <div className="Modules">
                <div className="Tabs">
                </div>
                {this.modules[0]}
            </div>
        )
    }

}
