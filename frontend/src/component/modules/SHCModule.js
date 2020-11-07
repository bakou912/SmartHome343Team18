import React from "react";
import "../../style/Modules.css";
import HouseLayoutService from "../../service/HouseLayoutService";
import SimulationContextService from "../../service/SimulationContextService";
import Select from "react-select";
import {Container, Button, Col, Row, ListGroup} from "react-bootstrap";
import ParametersService from "../../service/ParametersService";
import Command from "./Command";

const ITEMS = ["Window", "Light", "Door", "Person"];
const OUTSIDE = ["Backyard", "Entrance"];

export default class SHCModule extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            locations: [],
            loaded: false,
            addingPerson: false,
            personUpdateKey: 0
        };

        this.onSelectedLocation = this.onSelectedLocation.bind(this);
        this.blockWindow = this.blockWindow.bind(this);
        this.setEditing = this.setEditing.bind(this);
        this.addPerson = this.addPerson.bind(this);
        this.handleNameChange = this.handleNameChange.bind(this);
        this.itemSelected = this.itemSelected.bind(this);
        this.openCloseWindow = this.openCloseWindow.bind(this);
        this.modifyLightState = this.modifyLightState.bind(this);
        this.openCloseDoor = this.openCloseDoor.bind(this);
        this.autoMode = this.autoMode.bind(this);
		this.onSelectedItem = this.onSelectedItem.bind(this);
		this.removePerson = this.removePerson.bind(this);
    }

    async componentDidMount() {
        await this.setState({
            locations: await HouseLayoutService.getAllLocations(),
            user: await ParametersService.getUser(),
            selectedLocation: null,
            selectedWindow: null,
			selectedDoor: null,
            loaded: true
        });
    }

    async onSelectedLocation(evt) {
        await this.setState({
            locations: await HouseLayoutService.getAllLocations(),
            selectedLocation: null,
            selectedWindow: null,
            selectedDoor: null,
            loaded: true,
            selectedWindowItem: false,
            selectedLightItem: false,
            selectedDoorItem: false,
            selectedPersonItem: false,
        });

        const windows = OUTSIDE.includes(evt.label) ? [] : evt.value.windows.map(w => {
            return {
                value: w,
                label: w.direction
            };
        });

        const doors = OUTSIDE.includes(evt.label) ? [] : evt.value.doors.map(d => {
            return {
                value: d,
                label: d.direction
            };
        })

        await this.setState({
            selectedLocation: {
                light: evt.value.light,
                rowId: evt.value.rowId,
                roomId: evt.value.roomId,
                label: evt.label
            },
            persons: evt.value.persons.map(p => {
                return {
                    value: p,
                    label: p.name
                };
            }),
            windows: windows,
            doors: doors,
            selectedWindow: null,
            selectedPerson: null,
			selectedDoor: null,
            personName: ""
        });
    }

    async onSelectedItem(item, evt) {
        await this.setState({
            [`selected${item}`]: evt
        });
    }

    async setEditing(value) {
        await this.setState({
            addingPerson: value
        });
    }

    async addPerson() {
        const action = OUTSIDE.includes(this.state.selectedLocation.label) ?
            async () => SimulationContextService.addPersonOutside({ location: this.state.selectedLocation.label, name: this.state.personName})
            :
            async () => SimulationContextService.addPersonToRoom(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId,  {name: this.state.personName});

        await action().then(async response => {
            const person = {
                value: {
                    id: response.data,
                    name: this.state.personName
                },
                label: this.state.personName
            };

            const updatedPersons = this.state.persons;
            updatedPersons.push(person);

            await this.setState({
                persons: updatedPersons,
                personUpdateKey: this.state.personUpdateKey + 1,
                addingPerson: false,
                personName: ""
            });

            window.dispatchEvent(new Event("updateLayout"));
        }).catch(err => {
            if (err.response.status === 409) {
                alert("A person with this name is already present.");
            }
        })

    }

	async removePerson() {
        const action = OUTSIDE.includes(this.state.selectedLocation.label) ?
            async () => SimulationContextService.removePersonFromOutside(this.state.selectedPerson.value.id)
            :
            async () => SimulationContextService.removePersonFromRoom(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, this.state.selectedPerson.value.id);

            await action().then(async () =>{
			await this.setState({
				persons: this.state.persons.filter(person => person.value.id !== this.state.selectedPerson.value.id),
                personUpdateKey: this.state.personUpdateKey + 1,
                selectedPerson: null
			});
		});
		window.dispatchEvent(new Event("updateLayout"));
	}

    async handleNameChange(evt) {
        const targetValue = evt.target.value;

        if (targetValue.length > 20) {
            return;
        }

        await this.setState({
            personName: evt.target.value
        });
    }

    async openCloseWindow(windowState) {
        if (windowState === "OPEN") {
            await HouseLayoutService.openWindow(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, this.state.selectedWindow.value.id)
        } else {
            await HouseLayoutService.unblockWindow(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, this.state.selectedWindow.value.id)
        }
        this.setState({
            selectedWindow: {
                ...this.state.selectedWindow,
                value: {
                    ...this.state.selectedWindow.value,
                    state: windowState
                }
            }
        });
        window.dispatchEvent(new Event("updateLayout"));
    }

    async modifyLightState(lightState) {

        const action = OUTSIDE.includes(this.state.selectedLocation.label) ?
            async () => HouseLayoutService.modifyOutsideLightState({ location: this.state.selectedLocation.label, state: lightState })
            :
            async () => HouseLayoutService.modifyRoomLightState(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, { state: lightState })

        await action().then(async () =>{
            await this.setState({
                selectedLocation: {
                    ...this.state.selectedLocation,
                    light: {
                        state: lightState
                    }
                }
            });
        });

        window.dispatchEvent(new Event("updateLayout"));
    }

    async openCloseDoor(doorState) {
        await HouseLayoutService.changeDoorState(
            this.state.selectedLocation.rowId,
            this.state.selectedLocation.roomId,
            this.state.selectedDoor.value.id,
            { state: doorState }
        )
		.then(async () => {
			await this.setState({
	            selectedDoor: {
	                ...this.state.selectedDoor,
	                value: {
	                    ...this.state.selectedDoor.value,
	                    state: doorState
	                }
	            }
	        });
		});

		window.dispatchEvent(new Event("updateLayout"));
    }

    async autoMode(mode) {
        if (mode) {
            console.log("automode set");
        } else {
            console.log("automode removed");
        }
    }

    async blockWindow(block) {
        if (block === true) {
            this.setState({
                selectedWindow: {
                    ...this.state.selectedWindow,
                    value: {
                        ...this.state.selectedWindow.value,
                        state: "BLOCKED"
                    }
                }
            });
            await HouseLayoutService.blockWindow(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, this.state.selectedWindow.value.id)
        } else {
            this.setState({
                selectedWindow: {
                    ...this.state.selectedWindow,
                    value: {
                        ...this.state.selectedWindow.value,
                        state: "CLOSED"
                    }
                }
            });
            await HouseLayoutService.unblockWindow(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, this.state.selectedWindow.value.id)
        }

        window.dispatchEvent(new Event("updateLayout"));
    }

    async itemSelected(item) {
        const state = {
            selectedWindowItem: false,
            selectedLightItem: false,
            selectedDoorItem: false,
            selectedPersonItem: false,
        }

        state[`selected${item}Item`] = true;

        await this.setState(state)
    }

    render() {
        return (
            <Container>
                {
                    this.state.loaded ?
                        <div className="Module">
                            <br/>
                            Locations
                            <Select
                                styles={{
                                    option: provided => ({...provided, width: "100%"}),
                                    menu: provided => ({...provided, width: "50%"}),
                                    control: provided => ({...provided, width: "50%"}),
                                    singleValue: provided => provided
                                }}
                                options={this.state.locations}
                                onChange={this.onSelectedLocation}
                            />
                            <br/>
                            {
                                this.state.selectedLocation !== null ?
									<Container>
										<Row>
											<Col>
												<ListGroup>
													<h5 style={{textAlign: "center", color: "blue"}}>Item</h5>
													{
                                                        !OUTSIDE.includes(this.state.selectedLocation.label) ?
															ITEMS.map((item) =>
																<ListGroup.Item
                                                                    key={ITEMS.indexOf(item)} className="ItemsTable"
                                                                    bsPrefix="list-group-item py-1" action
                                                                    onClick={() => this.itemSelected(item)}
                                                                    variant="dark">{item}</ListGroup.Item
                                                                >
															)
															:
															[
																<ListGroup.Item
                                                                    key={ITEMS.indexOf("Light")}
                                                                    className="ItemsTable"
                                                                    bsPrefix="list-group-item py-1" action
                                                                    onClick={() => this.itemSelected("Light")}
                                                                    variant="dark">Light</ListGroup.Item
                                                                >,
																<ListGroup.Item
                                                                    key={ITEMS.indexOf("Person")}
                                                                    className="ItemsTable"
                                                                    bsPrefix="list-group-item py-1" action
                                                                    onClick={() => this.itemSelected("Person")}
                                                                    variant="dark">Person</ListGroup.Item
                                                                >
															]
													}
												</ListGroup>
											</Col>
										</Row>
										{
											this.state.selectedWindowItem ?
												<Row>
													{
														!OUTSIDE.includes(this.state.selectedLocation.label) ?
															<Col>
																Windows
																<Select
																	styles={{
																		option: provided => ({...provided, width: "50%"}),
																		menu: provided => ({...provided, width: "50%"}),
																		control: provided => ({...provided, width: "50%"}),
																		singleValue: provided => provided
																	}}
																	options={this.state.windows}
																	value={this.state.selectedWindow}
																	onChange={(evt) => this.onSelectedItem("Window", evt)}
																/>
																{
																	this.state.selectedWindow !== null ?
																		<div>
																			<Command
																				name="Window obstruction"
																				user={this.state.user}
																				location={this.state.selectedLocation}
																			>
																				{
																					this.state.selectedWindow.value.state === "BLOCKED" ?
																						<Button onClick={() => this.blockWindow(false)}
																								variant="secondary"
																								size="sm">Unobstruct</Button>
																						:
																						<Button onClick={() => this.blockWindow(true)}
																								variant="secondary"
																								size="sm">Obstruct</Button>
																				}
																			</Command>
																			<Command
																				name="Window management"
																				user={this.state.user}
																				location={this.state.selectedLocation}
																			>
																				{
																					this.state.selectedWindow.value.state !== "BLOCKED" ?
																						this.state.selectedWindow.value.state === "CLOSED" ?
																							<Button
																								onClick={() => this.openCloseWindow("OPEN")}
																								variant="secondary"
																								size="sm">Open</Button>
																							:
																							<Button
																								onClick={() => this.openCloseWindow("CLOSED")}
																								variant="secondary"
																								size="sm">Close</Button>
																						: null
																				}
																			</Command>
																		</div>
																		: null
																}
															</Col>
															: null
													}
												</Row>
												: null
										}
										{
											this.state.selectedPersonItem ?
												<Row>
													<Col>
														Persons
														<Select
															key={this.state.personUpdateKey}
															styles={{
																option: provided => ({...provided, width: "50%"}),
																menu: provided => ({...provided, width: "50%"}),
																control: provided => ({...provided, width: "50%"}),
																singleValue: provided => provided
															}}
															options={this.state.persons}
															onChange={(evt) => this.onSelectedItem("Person", evt)}
														/>
														{
															this.state.addingPerson ?
																<div>
																	<input type="text" placeholder="Name" maxLength="20"
																		   value={this.state.personName}
																		   onChange={this.handleNameChange}/>
																	<Button onClick={this.addPerson} variant="secondary"
																			size="sm">Save</Button>
																</div>
																:
																<Command
																	name="Person management"
																	user={this.state.user}
																	location={this.state.selectedLocation}
																>
																	<Button onClick={() => this.setEditing(true)} variant="secondary"
																			  size="sm">Add</Button>
                                                                    {
                                                                        this.state.selectedPerson !== null ?
                                                                            <Button onClick={this.removePerson} variant="secondary"
                                                                                    size="sm">Remove</Button>
                                                                            : null
                                                                    }
														  		</Command>

														}
													</Col>
												</Row>
												: null
										}
										{
											this.state.selectedLightItem ?
												<Command
													name="Light Management"
													user={this.state.user}
													location={this.state.selectedLocation}
												>
													<Row>
														<Col>
															Light
															{
																this.state.selectedLocation.light.state === "OFF" ?
																	<div>
																		<Button onClick={() => this.modifyLightState("ON")}
																				variant="secondary" size="md">On</Button>
																	</div>
																	:
																	<div>
																		<Button onClick={() => this.modifyLightState("OFF")}
																				variant="secondary" size="md">Off</Button>
																	</div>
															}
														</Col>
														<Col>
															<div style={{margin: "25px"}}>
																<input type="checkbox" id="autoMode" name="autoMode" value="true"/>
																<label>Enable Auto Mode</label>
															</div>
														</Col>
													</Row>
												</Command>
												: null
										}
                                        {
                                            this.state.selectedDoorItem ?
                                                <Row>
                                                    {
                                                        !OUTSIDE.includes(this.state.selectedLocation.label) ?
                                                            <Col>
                                                                Doors
                                                                <Select
                                                                    styles={{
                                                                        option: provided => ({...provided, width: "50%"}),
                                                                        menu: provided => ({...provided, width: "50%"}),
                                                                        control: provided => ({...provided, width: "50%"}),
                                                                        singleValue: provided => provided
                                                                    }}
                                                                    options={this.state.doors}
                                                                    value={this.state.selectedDoor}
                                                                    onChange={(evt) => this.onSelectedItem("Door", evt)}
                                                                />
                                                                {
                                                                    this.state.selectedDoor !== null ?
																		<div>
																			<Command
																				name="Door Lock Management"
																				user={this.state.user}
																				location={this.state.selectedLocation}
																			>
																				{
																					this.state.selectedDoor.value.state !== "LOCKED" ?
																						<Button
																							onClick={() => this.openCloseDoor("LOCKED")}
																							variant="secondary"
																							size="sm">Lock</Button>
																						:
																						<Button
																							onClick={() => this.openCloseDoor(this.state.selectedDoor.value.state === "CLOSED"? "OPEN":"CLOSED")}
																							variant="secondary"
																							size="sm">unblock</Button>
																				}
																			</Command>
	                                                                        <Command
	                                                                            name="Door management"
	                                                                            user={this.state.user}
	                                                                            location={this.state.selectedLocation}
	                                                                        >
	                                                                            {
	                                                                                this.state.selectedDoor.value.state !== "LOCKED" ?
	                                                                                    this.state.selectedDoor.value.state === "CLOSED" ?
	                                                                                        <Button
	                                                                                            onClick={() => this.openCloseDoor("OPEN")}
	                                                                                            variant="secondary"
	                                                                                            size="sm">Open</Button>
	                                                                                        :
	                                                                                        <Button
	                                                                                            onClick={() => this.openCloseDoor("CLOSED")}
	                                                                                            variant="secondary"
	                                                                                            size="sm">Close</Button>
	                                                                                    : null
	                                                                            }
	                                                                        </Command>
																		</div>
                                                                        : null
                                                                }
                                                            </Col>
                                                            : null
                                                    }
                                                </Row>
                                                : null
                                        }
									</Container>
                                    : null
                            }
                        </div>
						: null
                }
            </Container>
        );
    }

}
