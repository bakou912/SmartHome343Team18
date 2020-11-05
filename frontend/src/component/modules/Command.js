import React from "react";
import "../../style/Modules.css";

export default class Command extends React.Component {

    constructor(props) {
        super(props);

        this.isCommandAuthorized = this.isCommandAuthorized.bind(this);

        this.state = {
            commandAuthorized: this.isCommandAuthorized()
        };
    }

    isCommandAuthorized() {
        let authorized = false;

        const commandPermission = this.props.user.profile.commandPermissions.find(cp => cp.name === this.props.name);
        const { rowId, roomId } = this.props.user.location;

        if (commandPermission) {
            switch(commandPermission.locationRestriction) {
                case "NONE":
                    authorized = true;
                    break;
                case "INSIDE":
                    authorized = this.props.user.location.outside === false
                    break;
                case "OUTSIDE":
                    authorized = this.props.user.location.outside === true
                    break;
                case "ROOM":
                    authorized = this.props.location.rowId === rowId && this.props.location.roomId === roomId;
                    break;
                default:
            }
        }

        return authorized;
    }

    render() {
        return (
            <div>
                {
                    this.state.commandAuthorized ?
                        this.props.children
                        :
                        null
                }
            </div>
        );
    }

}
