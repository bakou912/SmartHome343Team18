import httpClient from "./HttpClient";

class HouseLayoutService {

    async createLayout(layoutInput) {
        return httpClient.post("layout", layoutInput);
    }

    async getLayout() {
        return httpClient.get("layout");
    }

    async resetLayout() {
        return httpClient.delete("layout");
    }

}

export default new HouseLayoutService();
