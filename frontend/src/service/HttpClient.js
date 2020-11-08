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
        return this.client.post(path, body).then(async response => {
            await this.notifyConsole();
            return response;
        });
    }

    async put(path, body) {
        return this.client.put(path, body).then(async response => {
            await this.notifyConsole();
            return response;
        });
    }

    async delete(path) {
        return this.client.delete(path).then(async response => {
            await this.notifyConsole();
            return response;
        });
    }

    async notifyConsole() {
        await window.dispatchEvent(new Event("updateConsole"));
    }

}

export default new HttpClient();
