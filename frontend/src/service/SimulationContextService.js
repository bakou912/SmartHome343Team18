import httpClient from "./HttpClient";

class SimulationContextService {

    async addPersonToRoom(personId,roomId) {
        let url = "simulation/rows/rooms/" + roomId + "/addPerson"
        return httpClient.post(url, {personId:personId});
    }

}

export default new SimulationContextService();
