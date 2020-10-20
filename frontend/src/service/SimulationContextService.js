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

    async modifyPerson(rowId, roomId, oldName, person) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/persons/${oldName}`
        return httpClient.put(path, { name: person.name });
    }

    async removePersonFromRoom(rowId, roomId, personId) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/persons/${personId}`
        return httpClient.delete(path);
    }

    async blockWindow(rowId, roomId, windowId) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/windows/${windowId}/block`
        return httpClient.put(path);
    }

    async unblockWindow(rowId, roomId, windowId) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/windows/${windowId}/unblock`
        return httpClient.put(path);
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

}

export default new SimulationContextService();
