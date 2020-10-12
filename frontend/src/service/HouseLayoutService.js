import httpClient from "./HttpClient";

class HouseLayoutService {

    async createLayout(layoutInput) {
        return httpClient.post("layout", layoutInput);
    }

    async getLayout() {
        return httpClient.get("layout");
    }

}

export default new HouseLayoutService();
