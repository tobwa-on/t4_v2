import axios from 'axios';

const API_KEY = '0be5365d010677cecc0e9950aa086202';
const BASE_URL = 'https://api.themoviedb.org/3';

export const performSearch = (query) => {
    return axios.get(`${BASE_URL}/search/movie`, {
        params: { api_key: API_KEY, query },
    });
};

export const getMovieDetails = (movieId) => {
    return axios.get(`${BASE_URL}/movie/${movieId}`, {
        params: { api_key: API_KEY },
    });
};

export const getPopularMovies = () => {
    return axios.get(`${BASE_URL}/movie/popular`, {
        params: { api_key: API_KEY },
    });
};

export function getImageUrl(path) {
    return path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/100x150?text=No+Image';
}

