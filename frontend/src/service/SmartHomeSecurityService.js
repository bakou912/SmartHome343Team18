import httpClient from "./HttpClient";

class SmartHomeSecurityService {

    async toggleAwayMode(state) {
        const path = `security/awaymode`;
        return httpClient.put(path, { state: state });
     }

    async getAwayModeState() {
        const path = "security/awaymode"
        return httpClient.get(path);
    }     
}

export default new SmartHomeSecurityService();
