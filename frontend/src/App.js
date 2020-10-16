import './style/App.css';
import React, { Suspense } from "react";
import Container from "react-bootstrap/Container";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Dashboard from "./view/Dashboard";
import HouseLayout from "./view/HouseLayout";
import SimulationParameters from "./view/SimulationParameters";
import HouseLayoutUpload from "./component/houselayout/HouseLayoutUpload";

function App() {
  return (
    <div className="App">

        <Container fluid >
            <br />
            <Router>
                <Suspense fallback={<div>Loading...</div>}>
                    <Switch>
                        <Route exact path="/dashboard" component={Dashboard} />
                        <Route exact path="/layout" component={HouseLayout} />
                        <Route exact path="/layoutUpload" component={HouseLayoutUpload} />
                        <Route path="/parameters" component={SimulationParameters}/>
                    </Switch>
                </Suspense>
            </Router>
        </Container>
    </div>
  );
}

export default App;
