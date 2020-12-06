import React from "react";
import SmartHomeHeaterService from "../../service/SmartHomeHeaterService";
import "../../style/Modules.css";
import {Button, Col, Container, ListGroup, Row} from "react-bootstrap";
import HouseLayoutService from "../../service/HouseLayoutService";
import Select from "react-select";
import {EditZoneRoom} from "../EditZoneRoom";
import {AddRoomToZone} from "../AddRoomToZone";
import Switch from "react-switch";
import Command from "./Command";

export default class SHHModule extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            summerDefaultTemp: 0,
            winterDefaultTemp: 0,
            selectedZone: null,
            addingZone: false,
            zoneUpdateKey: 0,
            zoneName: "",
            systemOn: false
        };
        this.defaultTempChange = this.defaultTempChange.bind(this);
        this.setAddingZone = this.setAddingZone.bind(this);
        this.addZone = this.addZone.bind(this);
        this.removeZone = this.removeZone.bind(this);
        this.handleZoneNameChange = this.handleZoneNameChange.bind(this);
        this.updateSelectedZone = this.updateSelectedZone.bind(this);
        this.periodTempChange = this.periodTempChange.bind(this);
        this.onSystemStatusChange = this.onSystemStatusChange.bind(this);
    }

    async componentDidMount() {
        window.addEventListener("updateSelectedZone", async e => {
            await this.updateSelectedZone(e);
        });

        const defaultTemps = (await SmartHomeHeaterService.getDefaultTemperatures()).data;

        await this.setState({
            summerDefaultTemp: defaultTemps.summerTemp,
            winterDefaultTemp: defaultTemps.winterTemp,
            rooms: await HouseLayoutService.getAllRooms().catch(() => []),
            zones: await SmartHomeHeaterService.getZones().catch(() => []),
            systemOn: (await SmartHomeHeaterService.getSystemOn()).data
        });
    }

    async updateSelectedZone(event) {
        if (this.state.selectedZone === null) {
            return;
        }
        const newState = {
            selectedZone: await SmartHomeHeaterService.getZone(this.state.selectedZone.value.id),
        };

        if (!event || !event.detail || event.detail !== true) {
            newState.zoneUpdateKey = this.state.zoneUpdateKey + 1;
        }

        await this.setState(newState);
    }

    async onSelectedItem(item, evt) {
        await this.setState({
            [`selected${item}`]: evt,
        });

        if (item === "Zone") {
            await this.updateSelectedZone();
        }
    }

    async defaultTempChange(evt, name, limits) {
        let value = evt.target.value

        value = SHHModule.adjustTempWithLimits(value, limits);

        await this.setState({
            [`${name}DefaultTemp`]: value
        });

        if (name === "summer") {
            await SmartHomeHeaterService.setDefaultSummerTemp({ temperature: value });
        } else {
            await SmartHomeHeaterService.setDefaultWinterTemp({ temperature: value });
        }
    }

    async periodTempChange(evt, period) {
        let value = evt.target.value

        value = SHHModule.adjustTempWithLimits(value, { min: 15, max: 30 });

        await SmartHomeHeaterService.setPeriodTemp(this.state.selectedZone.value.id, period, value).then(async() => {
            await this.updateSelectedZone();
        });
    }

    static adjustTempWithLimits(temp, limits) {
        if (temp > limits.max) {
            temp = limits.max;
        } else if (temp < limits.min) {
            temp = limits.min
        }
        return temp;
    }

    async setAddingZone(value) {
        await this.setState({
            addingZone: value
        });
    }

    async addZone() {
        await SmartHomeHeaterService.addZone({ name: this.state.zoneName }).then(async response => {
            const zone = {
                value: {
                    ...response.data,
                },
                label: this.state.zoneName
            };

            const updatedZones = this.state.zones;
            updatedZones.push(zone);

            await this.updateSelectedZone();

            await this.setState({
                zones: updatedZones,
                zoneUpdateKey: this.state.zoneUpdateKey + 1,
                addingZone: false,
                zoneName: ""
            });
        }).catch(err => {
            if (err.response.status === 409) {
                alert("A zone with this name is already present.");
            }
        })
    }

    async removeZone() {
        await SmartHomeHeaterService.removeZone(this.state.selectedZone.value.id).then(async () => {
            const updatedZones = this.state.zones.filter(z => z.label !== this.state.selectedZone.label);

            await this.setState({
                zones: updatedZones,
                selectedZone: null,
                zoneUpdateKey: this.state.zoneUpdateKey + 1
            });
        });
    }

    async handleZoneNameChange(evt) {
        const targetValue = evt.target.value;

        if (targetValue.length > 20) {
            return;
        }

        await this.setState({
            zoneName: evt.target.value
        });
    }

    async onSystemStatusChange(checked) {
        await SmartHomeHeaterService.setSystemOn(checked).then(async () => {
            await this.setState({
                systemOn: checked
            });
        });
    }

    render() {
        return (
            <Container>
                <div className="Module">
                    <Container>
                        <br/>
                        <Row>
                            <Col md={6}>
                                Default Summer Temperature (&deg;C):
                            </Col>
                            <Col md={1}>
                                <input
                                    style={{ width: "50px" }}
                                    name="SummerTemp"
                                    type="number"
                                    value={this.state.summerDefaultTemp}
                                    onChange={async evt => await this.defaultTempChange(evt, "summer", { min: 0, max: 50})}
                                    min={0} max={50}
                                />
                            </Col>
                            <Col md={5}>
                                <div style={{float: "right"}}>
                                    <Command  name="Setting HAVC status">
                                        <span>HAVC</span>
                                        &nbsp;
                                        <Switch
                                            onChange={this.onSystemStatusChange}
                                            checked={this.state.systemOn}
                                        />
                                    </Command>
                                </div>
                            </Col>
                        </Row>
                        <br/>
                        <Row>
                            <Col md={6}>
                                Default Winter Temperature (&deg;C):
                            </Col>
                            <Col md={1}>
                                <input
                                    style={{ width: "50px" }}
                                    name="WinterTemp"
                                    type="number"
                                    value={this.state.winterDefaultTemp}
                                    onChange={async evt => await this.defaultTempChange(evt, "winter", { min: -40, max: 20})}
                                    min={-40} max={20}
                                />
                            </Col>
                        </Row>
                        <br/>
                        <Row>
                            <Col md={4}>
                                Zones
                                <Select
                                    key={this.state.zoneUpdateKey}
                                    styles={{
                                        option: provided => ({...provided, width: "100%"}),
                                        menu: provided => ({...provided, width: "100%"}),
                                        control: provided => ({...provided, width: "100%"}),
                                        singleValue: provided => provided
                                    }}
                                    options={this.state.zones}
                                    onChange={(evt) => this.onSelectedItem("Zone", evt)}
                                    defaultValue={this.state.selectedZone}
                                />
                                <Row>
                                    <Col>
                                        {
                                            this.state.addingZone ?
                                                <div>
                                                    <input
                                                        type="text" placeholder="Name" maxLength="20"
                                                        value={this.state.zoneName}
                                                        onChange={this.handleZoneNameChange}
                                                    />
                                                    <Button onClick={async () => await this.setState({ addingZone: false })} variant="dark" size="sm">
                                                        X
                                                    </Button>
                                                    <Button onClick={this.addZone} variant="secondary" size="sm">
                                                        Save
                                                    </Button>
                                                </div>
                                                :
                                                <div>
                                                    <Command name="Adding heating zone">
                                                        <Button onClick={() => this.setAddingZone(true)} variant="secondary" size="sm">Add</Button>
                                                    </Command>
                                                    {
                                                        (this.state.selectedZone !== null && this.state.selectedZone.value.rooms.length === 0) &&
                                                        <Command name="Removing heating zone">
                                                            <Button onClick={this.removeZone} variant="secondary" size="sm">Remove</Button>
                                                        </Command>
                                                    }
                                                </div>
                                        }
                                    </Col>
                                </Row>
                                <br/>
                            </Col>
                            <Col md={8}>
                                {
                                    this.state.selectedZone !== null &&
                                        <div>
                                            Rooms&nbsp;
                                            <div style={{float: "right", color: "orange", fontSize: "small"}}>
                                                *Overridden
                                            </div>
                                            <Command name="Adding room to zone">
                                                <AddRoomToZone key={this.state.zoneUpdateKey} zone={this.state.selectedZone.value}/>
                                            </Command>
                                            {
                                                this.state.selectedZone.value.rooms.length > 0 &&
                                                <div>
                                                    <ListGroup key={this.state.zoneUpdateKey}>
                                                        {
                                                            this.state.selectedZone.value.rooms.map((item) =>
                                                                <ListGroup.Item
                                                                    key={`${item.label}${this.state.zoneUpdateKey}`} className="ItemsTable"
                                                                    bsPrefix="list-group-item py-1" action
                                                                    variant="dark">
                                                                    <div>
                                                                        <div className="RoomLabelTemp">
                                                                            <span>{item.label}</span>:&nbsp;
                                                                            <span style={{color: item.value.heatingMode === "OVERRIDDEN" ? "orange" : "black"}}>
                                                                                {Math.round(item.value.temperature * 10) / 10}&deg;C
                                                                            </span>
                                                                        </div>
                                                                        <div className="RoomZone">
                                                                            {this.state.selectedZone.label} &nbsp;
                                                                            <EditZoneRoom key={`${item.label}${this.state.zoneUpdateKey}`} className="RoomLabelTemp" zone={this.state.selectedZone} room={item.value}/>
                                                                        </div>
                                                                    </div>
                                                                </ListGroup.Item>
                                                            )
                                                        }
                                                    </ListGroup>
                                                </div>
                                            }
                                        </div>
                                }
                            </Col>
                        </Row>
                        <br/>
                        <Row>
                            {
                                this.state.selectedZone !== null &&
                                <Container>
                                    <Row>
                                        <Col md={7}>
                                            [6AM-12PM] Morning Target (&deg;C):
                                        </Col>
                                        <Col md={1}>
                                            <input
                                                style={{ width: "50px" }}
                                                name="MorningTargetTemp"
                                                type="number"
                                                value={this.state.selectedZone.value.periods.MORNING}
                                                onChange={async evt => await this.periodTempChange(evt, "MORNING")}
                                                min={0} max={50}
                                            />
                                        </Col>
                                    </Row>
                                    <br/>
                                    <Row>
                                        <Col md={7}>
                                            [12PM-10PM] Afternoon Target (&deg;C):
                                        </Col>
                                        <Col md={1}>
                                            <input
                                                style={{ width: "50px" }}
                                                name="AfternoonTargetTemp"
                                                type="number"
                                                value={this.state.selectedZone.value.periods.AFTERNOON}
                                                onChange={async evt => await this.periodTempChange(evt, "AFTERNOON")}
                                                min={15} max={30}
                                            />
                                        </Col>
                                    </Row>
                                    <br/>
                                    <Row>
                                        <Col md={7}>
                                            [10PM-6AM] Night Target (&deg;C):
                                        </Col>
                                        <Col md={1}>
                                            <input
                                                style={{ width: "50px" }}
                                                name="NightTargetTemp"
                                                type="number"
                                                value={this.state.selectedZone.value.periods.NIGHT}
                                                onChange={async evt => await this.periodTempChange(evt, "NIGHT")}
                                                min={15} max={30}
                                            />
                                        </Col>
                                    </Row>
                                </Container>
                            }
                        </Row>
                    </Container>
                </div>
            </Container>
        );
    }
}
