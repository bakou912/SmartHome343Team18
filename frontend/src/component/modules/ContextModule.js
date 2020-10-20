import React from "react";
import "../../style/Modules.css";
import HouseLayoutService from "../../service/HouseLayoutService";
import SimulationContextService from "../../service/SimulationContextService";
import Select from "react-select";
import {Container, Button, Col, Row} from "react-bootstrap";

export default class ContextModule extends React.Component {

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
    }

    async componentDidMount() {
        await this.setState({
            locations: await HouseLayoutService.getAllLocations(),
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
            loaded: true
        });

        const windows = evt.label === "Outside" ? [] : evt.value.windows.map(w => {
            return { value: w, label: w.direction };
        });

        await this.setState({
            persons: evt.value.persons.map(p => {
                return { value: p, label: p.name };
            }),
            windows: windows,
            selectedLocation: { rowId: evt.value.rowId, roomId: evt.value.roomId, label: evt.label },
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
        const id = this.state.selectedLocation.label === "Outside" ? (await SimulationContextService.addPersonOutside({ name:this.state.personName })).data
                :
                (await SimulationContextService.addPersonToRoom(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, { name:this.state.personName })).data;

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
    }

    async handleNameChange(evt) {
        const targetValue = evt.target.value;

        if (targetValue.length > 20) {
            return;
        }

        await this.setState( {
            personName: evt.target.value
        });
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
            await SimulationContextService.blockWindow(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, this.state.selectedWindow.value.id)
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
            await SimulationContextService.unblockWindow(this.state.selectedLocation.rowId, this.state.selectedLocation.roomId, this.state.selectedWindow.value.id)
        }

        window.dispatchEvent(new Event("updateLayout"));
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
                            <br/>
                            {
                                this.state.selectedLocation !== null ?
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
                                                            <div>
                                                                {
                                                                    this.state.selectedWindow.value.state === "BLOCKED" ?
                                                                        <Button onClick={() => this.blockWindow(false)} variant="secondary" size="sm">Unobstruct</Button>
                                                                        :
                                                                        <Button onClick={() => this.blockWindow(true)} variant="secondary" size="sm">Obstruct</Button>
                                                                }
                                                            </div>
                                                            : null
                                                    }
                                                </Col>
                                                : null
                                        }
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
                        </div>
                        : null
                }
            </Container>
        )
    }

}
