import React, {useEffect, useRef, useState} from "react";
import {Image, Modal} from "react-bootstrap";
import ParametersService from "../service/ParametersService";
import Select from "react-select";

export function EditUserProfiles(props) {

    const foundPermission = useRef(false);
    const modificationsMade = useRef(false);
    const profiles = useRef(props.profiles);
    const [editingProfiles, setEditingProfiles] = useState(props.editing);
    const [modules, setModules] = useState([]);
    const [selectedProfile, setSelectedProfile] = useState(null);
    const [selectedModule, setSelectedModule] = useState(null);
    const [selectedCommand, setSelectedCommand] = useState(null);

    const createModules = async () => {
        return (await ParametersService.getModules()).data.map(module => {
            module.commands = module.commands.map(command => {
                return {
                    value: command,
                    label: command.name
                };
            });
            return {
                value: module,
                label: module.name
            };
        });
    }

    const onSelected = async (component, evt) => {
        foundPermission.current = false;

        if (component !== "selectedCommand") {
            setSelectedCommand(null);

            if (component === "selectedModule") {
                setSelectedModule(evt.value);
            } else {
                setSelectedProfile(evt.value);
            }
        } else {
            setSelectedCommand(evt.value);
        }
    }

    const permissionDefaultValue = (restriction) => {
        const permissionMatching = selectedProfile.commandPermissions.some(cp => cp.name === selectedCommand.name && cp.locationRestriction === restriction);

        if (permissionMatching === true) {
            foundPermission.current = permissionMatching;
        }

        return permissionMatching;
    }

    const hideReset = () => {
        if (modificationsMade.current) {
            return window.location.reload();
        }

        setSelectedProfile(null);
        setSelectedModule(null);
        setSelectedCommand(null);
        setEditingProfiles(false);
    }

    const onChangePermission = async (evt) => {
        modificationsMade.current = true;

        const restriction = evt.target.value;

        if (restriction === "REMOVE") {
            foundPermission.current = false;
            return await ParametersService.removePermissionFromProfile(selectedProfile.name, selectedCommand.name);
        }

        const permissionInput = {
            name: selectedCommand.name,
            locationRestriction: restriction
        };

        if (foundPermission.current) {
            return await ParametersService.modifyPermissionFromProfile(selectedProfile.name, permissionInput);
        }

        foundPermission.current = true;
        return await ParametersService.addPermissionToProfile(selectedProfile.name, permissionInput);

    }

    useEffect( () => {
        (async () => setModules(await createModules()))();
    }, []);

    return (
        <div className="EditProfilesDiv">
            <div className="EditProfilesButton DarkButton" onClick={() => setEditingProfiles(true)}>
                <Image className="EditIcon" style={{display: "block"}} width="15px" alt="Edit Profiles" src={"/edit.png"}/>
            </div>
            <Modal show={editingProfiles} onHide={hideReset}>
                <Modal.Header closeButton>
                    <Modal.Title>User Profiles Editing</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Profile
                    <div style={{display: "flex", justifyContent: "center"}}>
                        <div className="SelectDiv">
                            <Select
                                styles={{
                                    option: provided => ({...provided, width: "100%"}),
                                    menu: provided => ({...provided, width: "100%"}),
                                    control: provided => ({...provided, width: "100%"}),
                                    singleValue: provided => provided
                                }}
                                options={profiles.current}
                                onChange={async (evt) => await onSelected("selectedProfile", evt)}
                            />
                        </div>
                    </div>
                    <br/>
                    <br/>
                    Modules
                    <div style={{display: "flex", justifyContent: "center"}}>
                        <div className="SelectDiv">
                            <Select
                                styles={{
                                    option: provided => ({...provided, width: "100%"}),
                                    menu: provided => ({...provided, width: "100%"}),
                                    control: provided => ({...provided, width: "100%"}),
                                    singleValue: provided => provided
                                }}
                                options={modules}
                                onChange={async (evt) => await onSelected("selectedModule", evt)}
                            />
                        </div>
                    </div>
                    <br/>
                    <br/>
                    <div >
                        {selectedModule?.name}
                    </div>
                    <div style={{display: "flex", justifyContent: "center"}}>
                        <div className="SelectDivLarger">
                            <Select
                                styles={{
                                    option: provided => ({...provided, width: "100%"}),
                                    menu: provided => ({...provided, width: "100%"}),
                                    control: provided => ({...provided, width: "100%"}),
                                    singleValue: provided => provided
                                }}
                                options={selectedModule?.commands}
                                onChange={async (evt) => await onSelected("selectedCommand", evt)}
                            />
                        </div>
                    </div>
                    <div key={selectedProfile?.name + selectedModule?.name}>
                        {
                            selectedProfile && selectedModule && selectedCommand &&
                                <div key={selectedCommand.name} onChange={onChangePermission}>
                                    <input type="radio" value="NONE" name="permission" defaultChecked={permissionDefaultValue("NONE")}/> Authorized
                                    <br/>
                                    {
                                        selectedCommand.locationDependent === true ? <div>
                                            <input type="radio" value="INSIDE" name="permission" defaultChecked={permissionDefaultValue("INSIDE")}/> Authorized Inside
                                            <br/>
                                            <input type="radio" value="OUTSIDE" name="permission" defaultChecked={permissionDefaultValue("OUTSIDE")}/> Authorized Outside
                                            <br/>
                                            <input type="radio" value="ROOM" name="permission" defaultChecked={permissionDefaultValue("ROOM")}/> Authorized in Same Room
                                        </div>
                                            : null
                                    }
                                    <input type="radio" value="REMOVE" name="permission" defaultChecked={permissionDefaultValue("REMOVE") || foundPermission.current === false}/> Unauthorized
                                </div>
                        }
                    </div>
                </Modal.Body>
            </Modal>
        </div>
    );
}
