import React, {useEffect, useState} from "react";
import "../../style/Modules.css";
import { Container } from "react-bootstrap";
import OutputConsoleService from "../../service/OutputConsoleService";

export default function Console() {

    const consoleLinesRef = React.createRef();

    const [lines, setLines] = useState([]);

    const getLines = async () => {
        const rawLines = (await OutputConsoleService.getLines()).data;
        const componentLines = [];

        for (let i = 0; i < rawLines.length; i++) {
            if (rawLines[i].includes("window", "door", "light")) {
                window.dispatchEvent(new Event("updateLayout"));
            }
            componentLines.push(<p key={i}>{rawLines[i]}</p>);
        }

        setLines(componentLines);
    };

    useEffect( () => {
        window.addEventListener("updateConsole", async () => {
            await getLines();
        });
        getLines();
    }, []);

    useEffect( () => {
        if (consoleLinesRef && consoleLinesRef.current) {
            consoleLinesRef.current.scrollTop = consoleLinesRef.current.scrollHeight;
        }
    }, [lines, consoleLinesRef]);

    return (
        <Container className="Console">
            <div ref={consoleLinesRef} className="ConsoleLines">
                {lines}
            </div>
        </Container>
    );
}
