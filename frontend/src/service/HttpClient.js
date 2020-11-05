import axios from "axios";

class HttpClient {

    client;

    constructor() {
        this.client = axios.create({
            baseURL: "http://localhost:8080/"
        });
    }

    async get(path) {
        return this.client.get(path);
    }

    async post(path, body) {
        return this.client.post(path, body);
    }

    async put(path, body) {
        return this.client.put(path, body);
    }

    async delete(path) {
        return this.client.delete(path);
    }

}

export default new HttpClient();
