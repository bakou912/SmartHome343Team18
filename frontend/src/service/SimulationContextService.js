import httpClient from "./HttpClient";

class SimulationContextService {

    async addPersonToRoom(rowId, roomId, person) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/persons`
        return httpClient.post(path, { name: person.name });
    }

    async removePerson(rowId, roomId, personId) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/persons/${personId}`
        return httpClient.delete(path);
    }

    async resetContext() {
        return httpClient.delete("context");
    }

}

export default new SimulationContextService();
