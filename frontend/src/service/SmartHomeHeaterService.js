import httpClient from "./HttpClient";

class SmartHomeHeaterService {
    async setDefaultSummerTemp(temp) {
        const path = "/heating/awaymode/summer/temperature";
        return httpClient.put(path, temp);
    }

    async setDefaultWinterTemp(temp) {
        const path = "/heating/awaymode/winter/temperature";
        return httpClient.put(path, temp);
    }

    async getDefaultTemperatures() {
        const path = "/heating/awaymode";
        return httpClient.get(path);
    }

    async addZone(zone) {
        const path = "/heating/zone";
        return httpClient.post(path, zone);
    }

    async getZones() {
        const path = "/heating/zone";
        return (await httpClient.get(path)).data.map(z => ({
            value: {
                ...z
            },
            label: z.name
        }));
    }

}

export default new SmartHomeHeaterService();
