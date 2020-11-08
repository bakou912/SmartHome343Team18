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

    async getAuthoritiesTimer() {
        const path = "security/authoritiestime"
        return httpClient.get(path);
    }

    async modifyAuthoritiesTimer(duration) {
        const path = "security/authoritiestime"
        return httpClient.put(path, { duration: duration });
    }

}

export default new SmartHomeSecurityService();
