import httpClient from "./HttpClient";

class SimulationContextService {

    async getContext() {
        return httpClient.get("context");
    }

    async toggleState() {
        return httpClient.put("context/state");
    }

    async addPersonToRoom(rowId, roomId, person) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/persons`
        return httpClient.post(path, { name: person.name });
    }

    async addPersonOutside(person) {
        const path = `context/layout/outside/persons`
        return httpClient.post(path, person);
    }

    async removePersonFromRoom(rowId, roomId, personId) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/persons/${personId}`
        return httpClient.delete(path);
    }

    async removePersonFromOutside(location,personId) {
        const path = `context/layout/outside/${location}/persons/${personId}`
        return httpClient.delete(path);
    }

    async modifyUser(userInput) {
        const path = `context/parameters/user`;
        return httpClient.put(path, userInput);
    }

    async modifySysParams(sysParamsInput) {
        const path = `context/parameters/sysparams`;
        return httpClient.put(path, sysParamsInput);
    }

    async resetContext() {
        return httpClient.delete("context");
    }

    async blockWindow(rowId, roomId, windowId) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/windows/${windowId}/block`
        return httpClient.put(path);
    }

    async unblockWindow(rowId, roomId, windowId) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/windows/${windowId}/unblock`
        return httpClient.put(path);
    }

}

export default new SimulationContextService();
