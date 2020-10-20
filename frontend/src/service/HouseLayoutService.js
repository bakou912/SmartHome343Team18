import httpClient from "./HttpClient";

class HouseLayoutService {

    async createLayout(layoutInput) {
        return httpClient.post("layout", layoutInput);
    }

    async getLayout() {
        return httpClient.get("layout");
    }

    async getAllRooms(existingLayout) {
        const layout = existingLayout || (await this.getLayout()).data;
        let rooms = [];

        layout.rows.forEach(row => {
            rooms = rooms.concat(row.rooms.map(room => {
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

        return rooms;
    }

    async resetLayout() {
        return httpClient.delete("layout");
    }

}

export default new HouseLayoutService();
