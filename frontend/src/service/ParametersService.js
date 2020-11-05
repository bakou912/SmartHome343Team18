import httpClient from './HttpClient';

class ParametersService {

    async saveParams(parametersInput) {
        return httpClient.post("parameters", parametersInput);
    }

    async getParams() {
        return httpClient.get("parameters");
    }

    async getUser() {
        return (await httpClient.get("parameters")).data.user;
    }

    async getModules() {
        return httpClient.get("parameters/modules");
    }

    async resetParams() {
        return httpClient.delete("parameters");
    }

    async addPermissionToProfile(profileName, permissionInput) {
        return httpClient.post(`parameters/profiles/${profileName}/permissions`, permissionInput);
    }

    async modifyPermissionFromProfile(profileName, permissionInput) {
        return httpClient.put(`parameters/profiles/${profileName}/permissions/${permissionInput.name}`, permissionInput);
    }

    async removePermissionFromProfile(profileName, commandName) {
        return httpClient.delete(`parameters/profiles/${profileName}/permissions/${commandName}`);
    }

    async getProfiles(profiles) {
        let processedProfiles = profiles;

        if (!processedProfiles) {
            processedProfiles = (await httpClient.get("/parameters/profiles")).data;
        }

        return processedProfiles.map(p => {
            return {
                value: p,
                label: p.name
            }
        });
    }

}

export default new ParametersService();
