import React from "react";
import "../../style/Modules.css";
import { Container } from "react-bootstrap";
import OutputConsoleService from "../../service/OutputConsoleService";

export default class Console extends React.Component {

    consoleLinesRef = React.createRef();

    constructor(props) {
        super(props);

        this.state = {
            lines: []
        }

        this.getLines = this.getLines.bind(this);
    }

    async componentDidMount() {
        window.addEventListener("updateConsole", async () => {
            await this.getLines()
        });

        await this.getLines();
    }

    async getLines() {
        const lines = (await OutputConsoleService.getLines()).data;
        const componentLines = [];

        for (let i = 0; i < lines.length; i++) {
            if (lines[i].includes("window", "door", "light")) {
                window.dispatchEvent(new Event("updateLayout"));
            }
            componentLines.push(<p key={i}>{lines[i]}</p>);
        }

        await this.setState(
            {lines: componentLines},
            () => this.scrollToLastLine()
        );
    }

    scrollToLastLine() {
        this.consoleLinesRef.current.scrollTop = this.consoleLinesRef.current.scrollHeight;
    }

    render() {
        return (    
            <Container className="Console">
                <div ref={this.consoleLinesRef} className="ConsoleLines">
                    {this.state.lines}
                </div>
            </Container>  
        );
    }
}
