import React from "react";
import "../../style/Modules.css";
import ParametersService from "../../service/ParametersService";
import OutputConsoleService from "../../service/OutputConsoleService";

const OUTSIDE = ["Backyard", "Entrance"];

export default class Command extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loaded: false
        }

        this.isCommandAuthorized = this.isCommandAuthorized.bind(this);
    }

    async componentDidMount() {
        await this.setState({
            commandAuthorized: await this.isCommandAuthorized(),
            loaded: true
        });
    }

    async isCommandAuthorized() {
        let authorized = false;
        let logString = "";

        const user = await ParametersService.getUser();

        const commandPermission = user.profile.commandPermissions.find(cp => cp.name === this.props.name);

        if (commandPermission) {
            switch(commandPermission.locationRestriction) {
                case "NONE":
                    authorized = true;
                    break;
                case "INSIDE":
                    authorized = !OUTSIDE.includes(user.location.name);
                    logString = " when outside";
                    break;
                case "OUTSIDE":
                    authorized = OUTSIDE.includes(user.location.name);
                    logString = " when inside";
                    break;
                case "ROOM":
                    authorized = this.props.location.rowId === user.location.rowId && this.props.location.roomId === user.location.roomId;
                    logString = " if not in the same room";
                    break;
                default:
            }
        }

        if (!authorized) {
            await OutputConsoleService.logLine(
                `Profile ${user.profile.name} does not have permission to use the ${this.props.name.toLowerCase()} command${logString}`
            );
        }

        return authorized;
    }

    render() {
        return this.state.loaded === true ?
            <span>
                {
                    this.state.commandAuthorized ?
                        this.props.children
                        :
                        null
                }
            </span>
            : null;
    }

}
