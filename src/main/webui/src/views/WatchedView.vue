<template>
  <Watched @showDetailDialog="showMovieDetails"/>

  <MovieDetailsDialog
      :detailDialog="detailDialog"
      :movieDetails="movieDetails"
      @update:detailDialog="detailDialog = $event"
  />

</template>

<script setup>
import MovieDetailsDialog from "@/components/MovieDetailsDialog.vue";
import {defineEmits, ref} from "vue";
import {getMovieDetails} from "@/services/tmdbService.js";
import Watched from "@/components/lists/Watched.vue";

const detailDialog = ref(false);
const movieDetails = ref({});

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