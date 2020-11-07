import httpClient from "./HttpClient";

class SmartHomeSecurityService {
    async toggleAwayMode(state) {
        const path = `context/awayMode/${state}`;
        return httpClient.put(path);
     }

    async getAwayModeState() {
        const path = "context/awayMode/state"
        return httpClient.get(path);
    }     
}

export default new SmartHomeSecurityService();
