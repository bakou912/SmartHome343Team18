import React from "react";
import SmartHomeHeaterService from "../../service/SmartHomeHeaterService";
import "../../style/Modules.css";
import {Button, Col, Container, Row} from "react-bootstrap";
import HouseLayoutService from "../../service/HouseLayoutService";
import Select from "react-select";

export default class SHHModule extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            summerDefaultTemp: 0,
            winterDefaultTemp: 0,
            selectedZone: null,
            addingZone: false,
            zoneUpdateKey: 0,
            zoneName: ""
        };
        this.defaultTempChange = this.defaultTempChange.bind(this);
        this.setAddingZone = this.setAddingZone.bind(this);
        this.addZone = this.addZone.bind(this);
        this.handleZoneNameChange = this.handleZoneNameChange.bind(this);
    }

    async componentDidMount() {
        const defaultTemps = (await SmartHomeHeaterService.getDefaultTemperatures()).data;

        await this.setState({
            summerDefaultTemp: defaultTemps.summerTemp,
            winterDefaultTemp: defaultTemps.winterTemp,
            rooms: await HouseLayoutService.getAllRooms().catch(() => []),
            zones: await SmartHomeHeaterService.getZones().catch(() => [])
        });
    }

    async onSelectedItem(item, evt) {
        await this.setState({
            [`selected${item}`]: evt,
        });

        console.log(this.state.selectedZone)
    }

    async defaultTempChange(evt, name, limits) {
        let value = evt.target.value

        if (value > limits.max) {
            value = limits.max;
        } else if (value < limits.min) {
            value = limits.min
        }

        await this.setState({
            [`${name}DefaultTemp`]: value
        });

        if (name === "summer") {
            await SmartHomeHeaterService.setDefaultSummerTemp({ temperature: value });
        } else {
            await SmartHomeHeaterService.setDefaultWinterTemp({ temperature: value });
        }
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

            await this.setState({
                zones: updatedZones,
                zoneUpdateKey: this.state.zoneUpdateKey + 1,
                addingZone: false,
                zoneName: ""
            });

            //window.dispatchEvent(new Event("updateLayout"));
        }).catch(err => {
            if (err.response.status === 409) {
                alert("A zone with this name is already present.");
            }
        })
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
                            <Col md={6}>
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
                                                    <Button onClick={() => this.setAddingZone(true)} variant="secondary" size="sm">Add</Button>
                                                    {
                                                        this.state.selectedZone !== null &&
                                                        <Button onClick={this.removeZone} variant="secondary" size="sm">Remove</Button>
                                                    }
                                                </div>
                                        }
                                    </Col>
                                </Row>
                            </Col>
                            <Col md={6}>
                                {
                                    this.state.selectedZone !== null &&
                                        <div>
                                            Rooms
                                            <Select
                                                styles={{
                                                    option: provided => ({...provided, width: "100%"}),
                                                    menu: provided => ({...provided, width: "100%"}),
                                                    control: provided => ({...provided, width: "100%"}),
                                                    singleValue: provided => provided
                                                }}
                                                options={this.state.zones}
                                                onChange={(evt) => this.onSelectedItem("Person", evt)}
                                            />
                                        </div>
                                }
                            </Col>
                        </Row>
                    </Container>
                </div>
            </Container>
        );
    }
}
