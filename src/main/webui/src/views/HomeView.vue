<template>
  <v-container fluid>
    <PopularMovies @showDetailDialog="showMovieDetails" />
    <SearchMovie @showDetailDialog="showMovieDetails"/>

    <MovieDetailsDialog
        :detailDialog="detailDialog"
        :movieDetails="movieDetails"
        @update:detailDialog="detailDialog = $event"
        @reviewDialog="reviewDialog = $event"
    />

    <ReviewDialog
        :reviewDialog="reviewDialog"
        :movieDetails="movieDetails"
        @update:reviewDialog="reviewDialog = $event"
    />
  </v-container>
</template>

<script setup>
import { ref, onMounted, defineEmits  } from 'vue';

import { getPopularMovies, getMovieDetails } from '@/services/tmdbService.js';
import PopularMovies from "@/components/PopularMovies.vue";
import SearchMovie from "@/components/SearchMovie.vue";
import MovieDetailsDialog from "@/components/MovieDetailsDialog.vue";
import ReviewDialog from "@/components/ReviewDialog.vue";

const popularMovies = ref([]);
const detailDialog = ref(false);
const reviewDialog = ref(false);
const movieDetails = ref({});

defineEmits(['showDetailDialog', 'showReviewDialog']);
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
