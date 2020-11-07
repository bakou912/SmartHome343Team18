import httpClient from "./HttpClient";

class SmartHomeSecurityService {

  async toggleAwayMode(state) {
    const path = `context/awayMode/${state}`;
    return httpClient.put(path);
  }

}

export default new SmartHomeSecurityService();
