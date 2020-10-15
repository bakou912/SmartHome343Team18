import httpClient from './HttpClient';

class ParametersService {

    async saveParams(parametersInput) {
        console.log("posting");
        console.log(parametersInput);
        return httpClient.post("parameters", parametersInput);
    }

    async getParams() {
        return httpClient.get("parameters");
    }

}

export default new ParametersService();
