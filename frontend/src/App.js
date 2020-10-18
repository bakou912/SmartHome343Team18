import "./style/App.css";
import React, { Suspense } from "react";
import Container from "react-bootstrap/Container";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Dashboard from "./view/Dashboard";
import SimulationParameters from "./view/SimulationParameters";

export default function App() {
  return (
    <Container fluid className="MainContainer">
        <Router>
            <Suspense fallback={<div>Loading...</div>}>
                <Switch>
                    <Route exact path="/dashboard" component={Dashboard} />
                    <Route exact path="/parameters" component={SimulationParameters}/>
                </Switch>
            </Suspense>
        </Router>
    </Container>
  );
}
