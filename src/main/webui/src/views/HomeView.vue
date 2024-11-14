<template>
    <PopularMovies @showDetailDialog="showMovieDetails" />
    <SearchMovie @showDetailDialog="showMovieDetails" />

    <MovieDetailsDialog
        :detailDialog="detailDialog"
        :movieDetails="movieDetails"
        @update:detailDialog="detailDialog = $event"
        @showSnackbar="handleSnackbar"
    />

    <v-snackbar v-model="showSnackbar" :color="snackbarColor" timeout="3000" top>
      {{ snackbarMessage }}
    </v-snackbar>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getPopularMovies, getMovieDetails } from '@/services/tmdbService.js';
import PopularMovies from "@/components/PopularMovies.vue";
import SearchMovie from "@/components/SearchMovie.vue";
import MovieDetailsDialog from "@/components/MovieDetailsDialog.vue";

const popularMovies = ref([]);
const detailDialog = ref(false);
const movieDetails = ref({});

const showSnackbar = ref(false);      // Zeigt die Snackbar an
const snackbarMessage = ref('');      // Speichert die Nachricht der Snackbar
const snackbarColor = ref('');       // Speichert die Farbe der Snackbar

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

const handleSnackbar = ({ message, color }) => {
  snackbarMessage.value = message;    // Setzt die Nachricht
  snackbarColor.value = color;        // Setzt die Farbe
  showSnackbar.value = true;          // Zeigt die Snackbar an
};

</script>
