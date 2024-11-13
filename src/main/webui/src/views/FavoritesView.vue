<template>
  <FavoriteMovies @showDetailDialog="showMovieDetails"/>

  <MovieDetailsDialog
      :detailDialog="detailDialog"
      :movieDetails="movieDetails"
      @update:detailDialog="detailDialog = $event"
      @showSnackbar="showSnackbar"
  />

  <v-snackbar v-model="showSuccess" timeout="3000" color="green" top>
    Das Speichern war erfolgreich!
  </v-snackbar>

</template>

<script setup>
import FavoriteMovies from "@/components/FavoriteMovies.vue";
import MovieDetailsDialog from "@/components/MovieDetailsDialog.vue";
import {defineEmits, ref} from "vue";
import {getMovieDetails} from "@/services/tmdbService.js";

const detailDialog = ref(false);
const movieDetails = ref({});
const showSuccess = ref(false);

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

const showSnackbar = () => {
  showSuccess.value = true;
};

</script>

<style scoped>

</style>