import './App.css';
import React, { Suspense, lazy } from "react";
import Container from "react-bootstrap/Container";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

const HouseLayout = lazy(() => import("./view/HouseLayout"));
const HouseLayoutUpload = lazy(() => import("./component/houselayout/HouseLayoutUpload"));

function App() {
  return (
    <div className="App">

        <Container fluid >
            <br />
            <Router>
                <Suspense fallback={<div>Loading...</div>}>
                    <Switch>
                        <Route exact path="/layout" component={HouseLayout} />
                        <Route exact path="/layoutUpload" component={HouseLayoutUpload} />
                    </Switch>
                </Suspense>
            </Router>
        </Container>
    </div>
  );
}

export default App;
