<template>
  <FavoriteMovies @showDetailDialog="showMovieDetails"/>

  <!-- Dialog für Filmdetails -->
  <MovieDetailsDialog
      :detailDialog="detailDialog"
      :movieDetails="movieDetails"
      :isSavedForLater="isSavedForLater"
      @update:dialog="dialog = $event"
  />
</template>

<script setup>
import FavoriteMovies from "@/components/FavoriteMovies.vue";
import MovieDetailsDialog from "@/components/MovieDetailsDialog.vue";
import {defineEmits, ref} from "vue";
import {getMovieDetails} from "@/services/tmdbService.js";

const detailDialog = ref(false);
const movieDetails = ref({});
const isSavedForLater = ref(false);

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

</script>

<style scoped>

</style>