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

    async setPeriodTemp(zoneId, period, temp) {
        const path = `/heating/zones/${zoneId}/periods/${period}/temperature`;
        return httpClient.put(path, { targetTemperature: temp });
    }

    async getDefaultTemperatures() {
        const path = "/heating/awaymode";
        return httpClient.get(path);
    }

    async addZone(zone) {
        const path = "/heating/zones";
        return httpClient.post(path, zone);
    }

    async removeZone(zoneId) {
        const path = `/heating/zones/${zoneId}`;
        return httpClient.delete(path);
    }

    async addRoomToZone(room, zoneId) {
        const path = `/heating/zones/${zoneId}/rooms`;
        return httpClient.post(path, room);
    }

    async overrideRoomTemp(rowId, roomId, temp) {
        const path = `heating/rows/${rowId}/rooms/${roomId}/temperature`;
        return httpClient.put(path, { temperature: temp });
    }

    async removeRoomOverride(rowId, roomId) {
        const path = `heating/rows/${rowId}/rooms/${roomId}/heatingmode`;
        return httpClient.put(path);
    }

    async getZones() {
        const path = "/heating/zones";
        return (await httpClient.get(path)).data.map(z => this.enrichZone(z));
    }

    async getZone(zoneId) {
        const path = `/heating/zones/${zoneId}`;
        return this.enrichZone((await httpClient.get(path)).data)
    }

    enrichZone(z) {
        return {
            value: {
                ...z,
                rooms: z.rooms.map(r => ({
                    value: r,
                    label: r.name
                }))
            },
            label: z.name
        }
    }

}

export default new SmartHomeHeaterService();
