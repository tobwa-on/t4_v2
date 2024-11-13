import VueJwtDecode from 'vue-jwt-decode';
import apiService from "@/services/apiService.js";

class UserService {
    constructor() {
        this.user = {};
        this.decodeUserData();
    }

    decodeUserData() {
        const token = localStorage.getItem("user");
        if (token !== null) {
            try {
                const decoded = VueJwtDecode.decode(token);
                if (Date.now() >= decoded.exp * 1000) {
                    this.user = {}; // Setze auf leeres Objekt, wenn das Token abgelaufen ist
                } else {
                    this.user = decoded;
                }
            } catch (error) {
                console.error("Fehler beim Dekodieren des Tokens:", error);
                this.user = {}; // Setze auf leeres Objekt im Fehlerfall
            }
        }
    }

    getUser() {
        if (Object.keys(this.user).length === 0) {
            this.decodeUserData();
        }
        return this.user;
    }

    getUserRole = async (uid) => {
        try {
            if (uid !== null) {
                const response = await apiService.get(`/user/roles`, { params: { uid } });
                return response.data;
            }
        } catch (error) {
            console.error("Fehler beim Abrufen der Benutzerrollen:", error);
            throw error;
        }
    };

    getUserName() {
        return this.user?.upn || null;
    }

    logOut(router) {
        localStorage.removeItem("user");
        this.user = {}; // Setze auf leeres Objekt beim Logout
        router.push({ path: '/' });
        window.location.reload();
    }
}

export default new UserService();
