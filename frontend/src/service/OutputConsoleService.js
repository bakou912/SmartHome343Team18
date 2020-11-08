import httpClient from "./HttpClient";

class OutputConsoleService {

    async logLine(line) {
        const path = `console/lines`;
        return httpClient.post(path, line);
     }

    async getLines() {
        const path = "console/lines"
        return httpClient.get(path);
    }     
}

export default new OutputConsoleService();
