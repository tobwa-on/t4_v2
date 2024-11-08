import axios from 'axios';

// Erstellt unter Nutzung von ChatGPT
class apiService {
    constructor() {
        this.requestPreset = axios.create({
            baseURL: '/api',
        });

        // Add an interceptor to attach token
        this.requestPreset.interceptors.request.use((config) => {
            const token = localStorage.getItem("user");
            if (token) {
                config.headers["Authorization"] = 'Bearer ' + token;
            }
            return config;
        });
    }

    get(url, config = {}) {
        return this.requestPreset.get(url, config);
    }

    post(url, data, config = {}) {
        return this.requestPreset.post(url, data, config);
    }

    put(url, data, config = {}) {
        return this.requestPreset.put(url, data, config);
    }

    delete(url, config = {}) {
        return this.requestPreset.delete(url, config);
    }
}

export default new apiService();