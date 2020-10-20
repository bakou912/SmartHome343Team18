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

}

export default new HouseLayoutService();
