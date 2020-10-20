import httpClient from './HttpClient';

class ParametersService {

    async saveParams(parametersInput) {
        return httpClient.post("parameters", parametersInput);
    }

    async getParams() {
        return httpClient.get("parameters");
    }

    async resetParams() {
        return httpClient.delete("parameters");
    }

}

export default new ParametersService();
