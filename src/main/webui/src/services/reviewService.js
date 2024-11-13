import apiService from '@/services/apiService.js';

export async function getReview(userId, movieId) {
    try {
        const response = await apiService.get(`/reviews/user=${userId}/movie=${movieId}`);
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

export const saveOrUpdateReview = async (uid, movieId, rating, reviewText) => {
    try {
        const response = await apiService.post('/reviews/saveOrUpdate', { uid, movieId, rating, reviewText });
        console.log("Rezension gespeichert oder aktualisiert:", response.data);
    } catch (error) {
        console.error("Fehler beim Speichern oder Aktualisieren der Rezension:", error);
        throw error;
    }
};

export async function getAllReviews(movieId) {
    try {
        const response = await apiService.get(`/reviews/movie=${movieId}`);
        if (response.status === 200) {
            return response.data;
        }
    } catch (error) {
        console.error("Fehler bei der API-Abfrage:", error);
    }
    return null;
}
