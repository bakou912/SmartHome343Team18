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
        return this.handleMethod(this.client.post(path, body))
    }

    async put(path, body) {
        return this.handleMethod(this.client.put(path, body));
    }

    async delete(path) {
        return this.handleMethod(this.client.delete(path));
    }

    async handleMethod(method) {
        return method.then(async response => {
            await this.notifyConsole();
            return response;
        }).catch(async err => {
            await this.notifyConsole();
            throw(err);
        });
    }

    async notifyConsole() {
        await window.dispatchEvent(new Event("updateConsole"));
    }

}

export default new HttpClient();
