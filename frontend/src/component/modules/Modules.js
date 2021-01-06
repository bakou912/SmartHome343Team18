import React, {useState} from "react";
import "../../style/Modules.css";
import SHCModule from "./SHCModule";
import SHPModule from "./SHPModule";
import SHHModule from "./SHHModule";

export default function Modules() {

    const modules = new Map();
    modules.set("SHC", <SHCModule/>);
    modules.set("SHP", <SHPModule/>);
    modules.set("SHH", <SHHModule/>);
    const [selectedTab, setSelectedTab] = useState(Array.from(modules.keys())[0]);

    const createTabs = () => {
        return Array.from(modules.keys()).map(tab => {
            return <div key={tab} className={`Tab ${selectedTab === tab ? "ActiveTab" : ""}`} onClick={() => changeSelectedTab(tab)}>
                {tab}
            </div>;
        })
    };

    const changeSelectedTab = (tab) => {
        setSelectedTab(tab);
    };

    return (
        <div className="Modules">
            <div className="Tabs">
                {createTabs()}
            </div>
            {modules.get(selectedTab)}
        </div>
    );

}
