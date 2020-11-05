import React from "react";
import "../../style/Modules.css";
import SHCModule from "./SHCModule";

export default class Modules extends React.Component {

    modules = new Map();

    constructor(props) {
        super(props);

        this.modules.set("SHC", <SHCModule/>);
        this.modules.set("SHP", null);

        this.state = {
            selectedTab: Array.from(this.modules.keys())[0]
        };

        this.createTabs = this.createTabs.bind(this);
        this.changeSelectedTab = this.changeSelectedTab.bind(this);
    }

    createTabs() {
        return Array.from(this.modules.keys()).map(tab => {
            return <div key={tab} className={`Tab ${this.state.selectedTab === tab ? "ActiveTab" : ""}`} onClick={() => this.changeSelectedTab(tab)}>
                {tab}
            </div>;
        })
    }

    changeSelectedTab(tab) {
        this.setState({
            selectedTab: tab
        });
    }

    render() {
        return (
            <div className="Modules">
                <div className="Tabs">
                    {this.createTabs()}
                </div>
                {this.modules.get(this.state.selectedTab)}
            </div>
        );
    }

}
