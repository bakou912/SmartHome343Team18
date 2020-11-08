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

        locations = locations.concat([
            {
                value: layout.entrance,
                label: "Entrance"
            },
            {
                value: layout.backyard,
                label: "Backyard"
            }
        ])

        return locations;
    }

    async resetLayout() {
        return httpClient.delete("layout");
    }

    async changeWindowState(rowId, roomId, windowId, state) {
      const path = `layout/rows/${rowId}/rooms/${roomId}/windows/${windowId}`
      return httpClient.put(path, { state: state });
    }

    async modifyRoomLightState(rowId, roomId, light) {
        const path = `layout/rows/${rowId}/rooms/${roomId}/light`
        return httpClient.put(path, light);
    }

    async modifyOutsideLightState(light) {
        const path = `layout/outside/light`
        return httpClient.put(path, light);
    }

	async changeDoorState(rowId, roomId, doorId, door) {
		const path = `/layout/rows/${rowId}/rooms/${roomId}/doors/${doorId}`
		return httpClient.put(path, door);
	}

}

export default new HouseLayoutService();
