import httpClient from "./HttpClient";

class SmartHomeHeaterService {
    async addDefaultSummerTemp(temp) {
        const path = "/heating/awaymode/summer/temp";
        return httpClient.put(path, temp);
    }

    async addDefaultWinterTemp(temp) {
        const path = "/heating/awaymode/winter/temp";
        return httpClient.put(path, temp);
    }

    async getDefaultWinterTemp() {
        const path = "/heating/awaymode/winter/temp";
        return httpClient.get(path);
    }

    async getDefaultSummerTemp() {
        const path = "/heating/awaymode/summer/temp";
        return httpClient.get(path);
    }
}

export default new SmartHomeHeaterService();
