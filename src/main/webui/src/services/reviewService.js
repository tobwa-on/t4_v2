import apiService from '@/services/apiService.js';

export const saveReview = async (uid, movieId, rating, reviewText) => {
    try {
        const response = await apiService.post('/reviews', { uid, movieId, rating, reviewText });
        console.log(response.data);
    } catch (error) {
        console.error("Fehler beim Speichern der Rezension:", error);
        throw error;
    }
};

export async function getReview(userId, movieId) {
    try {
        const response = await apiService.get(`/reviews/user/${userId}/movie/${movieId}`);
        if (response.status === 200) {
            return response.data;
        }
    } catch (error) {
        if (error.response && error.response.status === 404) {
            return null;
        } else {
            console.error("Fehler bei der API-Abfrage:", error);
        }
    }
    return null;
}

export async function updateReview(uid, movieId, rating, reviewText) {
    try {
        console.log("Rezension wird aktualisiert...");
        const response = await apiService.put(`/reviews/user/${uid}/movie/${movieId}`, { rating, reviewText });
        console.log(response.data);
    } catch (error) {
        console.error("Fehler beim Aktualisieren der Rezension:", error);
        throw error;
    }
}

