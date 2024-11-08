import apiService from '@/services/apiService.js';

export const saveFavorite = async (uid, movieId) => {
    try {
        console.log("Request wird abgeschickt")
        const response = await apiService.post('/favorites', { uid, movieId });
        console.log(response.data);
    } catch (error) {
        console.error("Fehler beim Speichern des Favoriten:", error);
        throw error;
    }
};

export async function isFavoriteMovie(userId, movieId) {
    try {
        const response = await fetch(`/api/favorites/user/${userId}/movie/${movieId}/exists`);
        if (!response.ok) {
            console.error('Fehler bei der Überprüfung des Favoritenstatus');
            return false;
        }
        const data = await response.json();
        return data.exists; // Nur den Boolean-Wert zurückgeben
    } catch (error) {
        console.error("Fehler bei der API-Abfrage:", error);
        return false;
    }
}

export const removeFavorite = async (uid, movieId) => {
    try {
        console.log("Favorit wird entfernt");
        await apiService.delete(`/favorites/user/${uid}/movie/${movieId}`);
        console.log("Favorit erfolgreich entfernt");
    } catch (error) {
        console.error("Fehler beim Entfernen des Favoriten:", error);
        throw error;
    }
};

export const getFavouriteMovies = async (uid) => {
    try {
        const response = await apiService.get(`/favorites/user/${uid}`);
        return response.data;
    } catch (error) {
        console.error("Fehler beim Abrufen der Favoriten:", error);
        throw error;
    }
};