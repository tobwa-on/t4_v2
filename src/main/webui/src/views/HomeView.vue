<template>
    <PopularMovies @showDetailDialog="showMovieDetails" />
    <SearchMovie @showDetailDialog="showMovieDetails" />

    <MovieDetailsDialog
        :detailDialog="detailDialog"
        :movieDetails="movieDetails"
        @update:detailDialog="detailDialog = $event"
    />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getPopularMovies, getMovieDetails } from '@/services/tmdbService.js';
import PopularMovies from "@/components/home/PopularMovies.vue";
import SearchMovie from "@/components/home/SearchMovie.vue";
import MovieDetailsDialog from "@/components/MovieDetailsDialog.vue";

const popularMovies = ref([]);
const detailDialog = ref(false);
const movieDetails = ref({});

const fetchPopularMovies = async () => {
  try {
    const response = await getPopularMovies();
    popularMovies.value = response.data.results;
  } catch (error) {
    console.error("Fehler beim Abrufen der beliebten Filme:", error);
  }
};

onMounted(fetchPopularMovies);

const showMovieDetails = async (movieId) => {
  try {
    const response = await getMovieDetails(movieId);
    movieDetails.value = response.data;
    detailDialog.value = true;
  } catch (error) {
    console.error("Fehler beim Laden der Filmdetails:", error);
  }
};



</script>
