import React from "react";
import "../../style/Modules.css";
import HouseLayoutService from "../../service/HouseLayoutService";
import SimulationContextService from "../../service/SimulationContextService";
import Select from "react-select";

import {
	Container,
	Button,
	Col,
	Row,
	ListGroup
} from "react-bootstrap";
import ParametersService from "../../service/ParametersService";
import Command from "./Command";

const ITEMS = ["Windows", "Light", "Doors", "Person"];
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
		this.onSelectedWindow = this.onSelectedWindow.bind(this);
		this.blockWindow = this.blockWindow.bind(this);
		this.setEditing = this.setEditing.bind(this);
		this.addPerson = this.addPerson.bind(this);
		this.handleNameChange = this.handleNameChange.bind(this);
		this.itemSelected = this.itemSelected.bind(this);
		this.openCloseWindow = this.openCloseWindow.bind(this);
		this.modifyLighttate = this.modifyLighttate.bind(this);
		this.openCloseDoor = this.openCloseDoor.bind(this);
		this.autoMode = this.autoMode.bind(this);
	}

	async componentDidMount() {
		await this.setState({
			locations: await HouseLayoutService.getAllLocations(),
			user: await ParametersService.getUser(),
			selectedLocation: null,
			selectedWindow: null,
			loaded: true
		});
	}

	async onSelectedLocation(evt) {
		await this.setState({
			locations: await HouseLayoutService.getAllLocations(),
			selectedLocation: null,
			selectedWindow: null,
			loaded: true,
			selectedDoor: null,
			selectedWindowItem: false,
			selectedLigthItem: false,
			selectedDoorItem: false
		});

		const windows = evt.label === "Outside" ? [] : evt.value.windows.map(w => {
			return {
				value: w,
				label: w.direction
			};
		});

		const doors = evt.label === "Outside" ? [] : evt.value.doors.map(w => {
			return {
				value: w,
				label: w.direction
			};
		})

		await this.setState({
			persons: evt.value.persons.map(p => {
				return {
					value: p,
					label: p.name
				};
			}),
			windows: windows,
			doors: doors,
			selectedLocation: {
				rowId: evt.value.rowId,
				roomId: evt.value.roomId,
				label: evt.label
			},
			selectedWindow: null,
			selectedPerson: null,
			personName: ""
		});
	}

	async onSelectedWindow(evt) {
		await this.setState({
			selectedWindow: evt
		});
	}

	async setEditing(value) {
		await this.setState({
			addingPerson: value
		});
	}

	async addPerson() {
		const id = this.state.selectedLocation.label === "Outside" ? (await SimulationContextService.addPersonOutside({
				name: this.state.personName
			})).data :
			(await SimulationContextService.addPersonToRoom(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, {
				name: this.state.personName
			})).data;

		const person = {
			value: {
				id: id,
				name: this.state.personName
			},
			label: this.state.personName
		};

		const updatedPersons = this.state.persons;
		updatedPersons.push(person);

		await this.setState({
			persons: updatedPersons,
			personUpdateKey: this.state.personUpdateKey + 1,
			addingPerson: false
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
	async openCloseWindow(open) {
		let state = "";
		if (open) {
			console.log("Opening");
			state = "OPEN";
			await HouseLayoutService.openWindow(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, this.state.selectedWindow.value.id)
		} else {
			console.log("Closing");
			state = "CLOSED";
			await HouseLayoutService.unblockWindow(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, this.state.selectedWindow.value.id)
		}
		this.setState({
			selectedWindow: {
				...this.state.selectedWindow,
				value: {
					...this.state.selectedWindow.value,
					state: state
				}
			}
		});
		window.dispatchEvent(new Event("updateLayout"));
	}
	async modifyLighttate(Lighttate) {
		console.log("heeere");
		(await HouseLayoutService.modifyLighttate(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, {
			state: Lighttate
		}))
		.then(async () => {
			await this.setState({
				selectedLocation: {
					...this.state.selectedLocation,
					light: {
						state: Lighttate
					}
				}
			});
		});
		window.dispatchEvent(new Event("updateLayout"));
	}
	async openCloseDoor(open) {
		if (open) {
			console.log("Opening door");
		} else {
			console.log("Closing door");
		}
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
		switch (item) {
			case "Windows":
				console.log("here");
				await this.setState({
					selectedWindowItem: true,
					selectedLigthItem: false,
					selectedDoorItem: false,
					selectedPersonItem: false,
				})
				break;
			case "Light":
				await this.setState({
					selectedWindowItem: false,
					selectedLigthItem: true,
					selectedDoorItem: false,
					selectedPersonItem: false,
				})
				break;
			case "Doors":
				await this.setState({
					selectedWindowItem: false,
					selectedLigthItem: false,
					selectedDoorItem: true,
					selectedPersonItem: false,
				})
				break;
			case "Person":
				await this.setState({
					selectedWindowItem: false,
					selectedLigthItem: false,
					selectedDoorItem: false,
					selectedPersonItem: true,
				})
				break;
			default:

		}
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
                              <Row>
                                <Col>
                                  <ListGroup>
                                    <h5 style={{textAlign:"center",color:"blue"}}>Item</h5>
                                    {
                                      this.state.selectedLocation.label !== "Outside"?
                                      ITEMS.map((item) =>
                                          <ListGroup.Item key="" className="ItemsTable" bsPrefix="list-group-item py-1" action onClick={()=>this.itemSelected(item)} variant="dark">{item}</ListGroup.Item>
                                      )
                                      :
                                      [
                                        <ListGroup.Item className="ItemsTable" bsPrefix="list-group-item py-1" action onClick={()=>this.itemSelected("Light")} variant="dark">Light</ListGroup.Item>,
                                        <ListGroup.Item className="ItemsTable" bsPrefix="list-group-item py-1" action onClick={()=>this.itemSelected("Person")} variant="dark">Person</ListGroup.Item>
                                      ]
                                    }

                                  </ListGroup>
                                </Col>
                              </Row>
                              :null
                            }
                            {
                                this.state.selectedLocation !==null && this.state.selectedWindowItem ?
                                    <Row>
                                        {
                                            this.state.selectedLocation.label !== "Outside" ?
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
                                                        onChange={this.onSelectedWindow}
                                                    />
                                                    {
                                                        this.state.selectedWindow !== null ?
                                                            <Command
                                                                name="Window obstruction"
                                                                user={this.state.user}
                                                                location={this.state.selectedLocation}
                                                            >
                                                                {
                                                                  this.state.selectedWindow.value.state === "BLOCKED" ?
                                                                      <Button onClick={() => this.blockWindow(false)} variant="secondary" size="sm">Unobstruct</Button>
                                                                      :
                                                                      <Button onClick={() => this.blockWindow(true)} variant="secondary" size="sm">Obstruct</Button>
                                                                }
                                                                {
                                                                  this.state.selectedWindow !== null ?
                                                                      <div>
                                                                          {
                                                                              this.state.selectedWindow.value.state !== "BLOCKED" ?
                                                                                this.state.selectedWindow.value.state === "CLOSED"?
                                                                                  <Button onClick={() => this.openCloseWindow(true)} variant="secondary" size="sm">Open</Button>
                                                                                  :
                                                                                  <Button onClick={() => this.openCloseWindow(false)} variant="secondary" size="sm">Close</Button>
                                                                                :null
                                                                          }
                                                                      </div>
                                                                      : null
                                                                }
                                                            </Command>
                                                            : null
                                                    }

                                                </Col>
                                                : null
                                        }
                                    </Row>
                                    : null
                            }
                            {
                              this.state.selectedPersonItem?
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
                                  />
                                  {
                                      this.state.addingPerson ?
                                          <div>
                                              <input type="text" placeholder="Name" maxLength="20" value={this.state.personName} onChange={this.handleNameChange}/>
                                              <Button onClick={this.addPerson} variant="secondary" size="sm">Save</Button>
                                          </div>
                                          : <Button onClick={() => this.setEditing(true)} variant="secondary" size="sm">Add</Button>

                                  }
                                </Col>
                              </Row>
                              : null
                            }
                            {
                              this.state.selectedLocation !==null && this.state.selectedLigthItem ?
                              <Command
                                  name="Window obstruction"
                                  user={this.state.user}
                                  location={this.state.selectedLocation}
                              >
                                <Row>
                                  <Col>
                                    Ligth
                                    {
                                      this.state.selectedLocation.light.state === "OFF"?
                                      <div>
                                        <Button onClick={() => this.modifyLighttate("ON")} variant="secondary" size="md">On</Button>
                                      </div>
                                      :
                                      <div>
                                        <Button onClick={() => this.modifyLighttate("OFF")} variant="secondary" size="md">Off</Button>
                                      </div>
                                    }
                                  </Col>
                                  <Col>
                                    <div style={{margin:"25px"}}>
                                      <input type="checkbox" id="autoMode" name="autoMode" value="true"/>
                                      <label for="autoMode">Enable Auto Mode</label>
                                    </div>
                                  </Col>
                              </Row>
                            </Command>
                            : null
                          }
                          {
                            this.state.selectedLocation !==null && this.state.selectedDoorItem ?
                            <Command
                                name="Window obstruction"
                                user={this.state.user}
                                location={this.state.selectedLocation}
                            >
                              <Row>
                                <Col>
                                  Doors
                                  {
                                    true === true?
                                    <div>
                                    <Select
                                        styles={{
                                            option: provided => ({...provided, width: "50%"}),
                                            menu: provided => ({...provided, width: "50%"}),
                                            control: provided => ({...provided, width: "50%"}),
                                            singleValue: provided => provided
                                        }}
                                        options={this.state.doors}
                                        value={this.state.selectedDoor}
                                        onChange={this.onSelectedWindow}
                                    />
                                      <Button onClick={() => this.openCloseDoor(true)} variant="secondary" size="md">Open</Button>
                                    </div>
                                    :
                                    <div>
                                      <Button onClick={() => this.openCloseDoor(false)} variant="secondary" size="md">Close</Button>
                                    </div>
                                  }
                                </Col>
                            </Row>
                          </Command>
                          : null
                        }
                        </div>
                        : null
                }
            </Container>
        );
    }

}
