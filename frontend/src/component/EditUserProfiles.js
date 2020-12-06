import React from "react";
import {Image, Modal} from "react-bootstrap";
import ParametersService from "../service/ParametersService";
import Select from "react-select";

export class EditUserProfiles extends React.Component {

    foundPermission = false
    modificationsMade = false

    constructor(props) {
        super(props);

        this.state = {
            editingProfiles: props.editing,
            profiles: props.profiles,
            modules: [],
            selectedProfile: null,
            selectedModule: null,
            selectedCommand: null,
            selectedProfileUpdateKey: 0,
            selectedModuleUpdateKey: 0,
            selectedCommandUpdateKey: 0
        };

        this.hideReset = this.hideReset.bind(this);
        this.onSelected = this.onSelected.bind(this);
        this.createModules = this.createModules.bind(this);
        this.permissionDefaultValue = this.permissionDefaultValue.bind(this);
        this.onChangePermission = this.onChangePermission.bind(this);
    }

    async componentDidMount() {
        this.setState({
            modules: await this.createModules()
        })
    }

    async createModules() {
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
        })
    }

    async onSelected(component, evt) {
        this.foundPermission = false;

        let newState = {
            [component]: evt.value,
            [`${component}UpdateKey`]: this.state[`${component}UpdateKey`] + 1
        };

        if (component !== "selectedCommand") {
            newState.selectedCommand = null;
        }

        await this.setState(newState);
    }

    permissionDefaultValue(restriction) {
        const permissionMatching = this.state.selectedProfile.commandPermissions.some(cp => cp.name === this.state.selectedCommand.name && cp.locationRestriction === restriction);

        if (permissionMatching === true) {
            this.foundPermission = permissionMatching;
        }

        return permissionMatching;
    }

    hideReset() {
        if (this.modificationsMade) {
            return window.location.reload();
        }

        this.setState({
            selectedProfile: null,
            selectedModule: null,
            selectedCommand: null,
            editingProfiles: false
        });
    }

    async onChangePermission(evt) {
        this.modificationsMade = true;

        const restriction = evt.target.value;

        if (restriction === "REMOVE") {
            this.foundPermission = false;
            return await ParametersService.removePermissionFromProfile(this.state.selectedProfile.name, this.state.selectedCommand.name);
        }

        const permissionInput = {
            name: this.state.selectedCommand.name,
            locationRestriction: restriction
        };

        if (this.foundPermission) {
            return await ParametersService.modifyPermissionFromProfile(this.state.selectedProfile.name, permissionInput);
        }

        this.foundPermission = true;
        return await ParametersService.addPermissionToProfile(this.state.selectedProfile.name, permissionInput);

    }

    render() {
        return (
            <div className="EditProfilesDiv">
                <div className="EditProfilesButton DarkButton" onClick={() => this.setState({ editingProfiles: true })}>
                    <Image className="EditIcon" style={{display: "block"}} width="15px" alt="Edit Profiles" src={"/edit.png"}/>
                </div>
                <Modal show={this.state.editingProfiles} onHide={this.hideReset}>
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
                                    options={this.state.profiles}
                                    onChange={async (evt) => await this.onSelected("selectedProfile", evt)}
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
                                    options={this.state.modules}
                                    onChange={async (evt) => await this.onSelected("selectedModule", evt)}
                                />
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div >
                            {this.state.selectedModule?.name}
                        </div>
                        <div style={{display: "flex", justifyContent: "center"}}>
                            <div className="SelectDivLarger">
                                <Select
                                    key={`${this.state.selectedCommandUpdateKey}${this.state.selectedModuleUpdateKey}${this.state.selectedProfileUpdateKey}`}
                                    styles={{
                                        option: provided => ({...provided, width: "100%"}),
                                        menu: provided => ({...provided, width: "100%"}),
                                        control: provided => ({...provided, width: "100%"}),
                                        singleValue: provided => provided
                                    }}
                                    options={this.state.selectedModule?.commands}
                                    onChange={async (evt) => await this.onSelected("selectedCommand", evt)}
                                />
                            </div>
                        </div>
                        <div key={this.state.selectedProfile?.name + this.state.selectedModule?.name}>
                            {
                                this.state.selectedProfile && this.state.selectedModule && this.state.selectedCommand &&
                                    <div key={this.state.selectedCommand.name} onChange={this.onChangePermission}>
                                        <input type="radio" value="NONE" name="permission" defaultChecked={this.permissionDefaultValue("NONE")}/> Authorized
                                        <br/>
                                        {
                                            this.state.selectedCommand.locationDependent === true ? <div>
                                                <input type="radio" value="INSIDE" name="permission" defaultChecked={this.permissionDefaultValue("INSIDE")}/> Authorized Inside
                                                <br/>
                                                <input type="radio" value="OUTSIDE" name="permission" defaultChecked={this.permissionDefaultValue("OUTSIDE")}/> Authorized Outside
                                                <br/>
                                                <input type="radio" value="ROOM" name="permission" defaultChecked={this.permissionDefaultValue("ROOM")}/> Authorized in Same Room
                                            </div>
                                                : null
                                        }
                                        <input type="radio" value="REMOVE" name="permission" defaultChecked={this.permissionDefaultValue("REMOVE") || this.foundPermission === false}/> Unauthorized
                                    </div>
                            }
                        </div>
                    </Modal.Body>
                </Modal>
            </div>
        )
    }

}
