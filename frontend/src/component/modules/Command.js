import React, {useCallback, useEffect, useState} from "react";
import "../../style/Modules.css";
import ParametersService from "../../service/ParametersService";
import OutputConsoleService from "../../service/OutputConsoleService";

const OUTSIDE = ["Backyard", "Entrance"];

export default function Command(props) {

    const [commandAuthorized, setCommandAuthorized] = useState();

    const isCommandAuthorized = useCallback(async () => {
        let authorized = false;
        let logString = "";

        const user = await ParametersService.getUser();

        const commandPermission = user.profile.commandPermissions.find(cp => cp.name === props.name);

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
                    authorized = props.location.rowId === user.location.rowId && props.location.roomId === user.location.roomId;
                    logString = " if not in the same room";
                    break;
                default:
            }
        }

        if (!authorized) {
            await OutputConsoleService.logLine(
                `Profile ${user.profile.name} does not have permission to use the ${props.name.toLowerCase()} command${logString}`
            );
        }

        return authorized;
    }, [props]);

    useEffect( () => {
        (async () => setCommandAuthorized(await isCommandAuthorized()))();
    },[isCommandAuthorized]);

    return (
        <span>
            {
                commandAuthorized ?
                    props.children
                    :
                    null
            }
        </span>
    );
}
