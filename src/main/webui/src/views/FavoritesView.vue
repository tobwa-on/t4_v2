<template>
  <FavoriteMovies @showDetailDialog="showMovieDetails"/>

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
import FavoriteMovies from "@/components/lists/FavoriteMovies.vue";
import MovieDetailsDialog from "@/components/MovieDetailsDialog.vue";
import {defineEmits, ref} from "vue";
import {getMovieDetails} from "@/services/tmdbService.js";

const detailDialog = ref(false);
const movieDetails = ref({});
const showSnackbar = ref(false);
const snackbarMessage = ref('');
const snackbarColor = ref('');

defineEmits(['showDetailDialog']);

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
  snackbarMessage.value = message;
  snackbarColor.value = color;
  showSnackbar.value = true;
};
</script>

<style scoped>

</style>