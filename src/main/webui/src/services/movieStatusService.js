import apiService from '@/services/apiService.js';

export const getFavoriteMovies = async (uid) => {
    try {
        const response = await apiService.get(`/moviestatus/user=${uid}`);
        return response.data.filter(movieStatus => movieStatus.favorite);
    } catch (error) {
        throw error;
    }
};

export const getMovieStatus = async (uid, movieId) => {
    try {
        const response = await apiService.get(`/moviestatus/user=${uid}/movie=${movieId}`);
        const { favorite, watchlist, watched } = response.data;
        return {
            favorite,
            watchlist,
            watched
        };
    } catch (error) {
        throw error;
    }
};

export const updateMovieStatus = async (uid, movieId, { favorite, watchlist, watched }) => {
    try {
        const response = await apiService.put(`/moviestatus/user/${uid}/movie/${movieId}`, { favorite, watchlist, watched });
        return response.data;
    } catch (error) {
        throw error;
    }
};