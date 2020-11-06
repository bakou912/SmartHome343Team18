import httpClient from "./HttpClient";

class HouseLayoutService {

    async createLayout(layoutInput) {
        return httpClient.post("layout", layoutInput);
    }

    async getLayout() {
        return httpClient.get("layout");
    }

    async getAllLocations(existingLayout) {
        const layout = existingLayout || (await this.getLayout()).data;
        let locations = [];

        layout.rows.forEach(row => {
            locations = locations.concat(row.rooms.map(room => {
                return {
                    value: {
                        ...room,
                        rowId: row.id,
                        roomId: room.id
                    },
                    label: room.name
                }
            }));
        })

        locations.push({
            value: layout.outside,
            label: "Outside"
        })

        return locations;
    }

    async resetLayout() {
        return httpClient.delete("layout");
    }

    async blockWindow(rowId, roomId, windowId) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/windows/${windowId}/block`
        return httpClient.put(path);
    }

    async unblockWindow(rowId, roomId, windowId) {
        const path = `context/layout/rows/${rowId}/rooms/${roomId}/windows/${windowId}/unblock`
        return httpClient.put(path);
    }

    async openWindow(rowId, roomId, windowId) {
      const path = `context/layout/rows/${rowId}/rooms/${roomId}/windows/${windowId}/open`
      return httpClient.put(path);
    }

    async modifyLightState(rowId, roomId, light) {
        const path = `/layout/rows/${rowId}/rooms/${roomId}`
        return httpClient.put(path, light);
    }

	async changeDoorState(rowId, roomId, doorId, door) {
        console.log(door)
		const path = `/layout/rows/${rowId}/rooms/${roomId}/doors/${doorId}`
		return httpClient.put(path, door);
	}

}

export default new HouseLayoutService();
