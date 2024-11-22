<template>
  <v-container fluid>
    <v-row class="d-flex justify-center align-center py-4">
      <v-icon color="blue" size="36" class="mr-2">mdi-eye</v-icon>
      <h2 class="font-weight-bold mb-0">Zuletzt gesehene Filme</h2>
    </v-row>

    <v-row v-if="sortedMovies.length === 0" class="mt-4">
      <v-col cols="12" class="text-center">
        <p>Bisher wurden keine Filme als Gesehen gespeichert.</p>
      </v-col>
    </v-row>

    <!-- Ergebnisse -->
    <v-row v-else class="mt-4" dense align-content="start">
      <v-col
          v-for="movie in sortedMovies"
          :key="movie.id"
          cols="4"
      >
        <v-row>
          <v-card class="d-flex align-center pa-4" variant="flat" @click="$emit('showDetailDialog', movie.id)">
            <v-col>
              <v-img
                  :src="movie.poster_path ? getImageUrl(movie.poster_path) : 'https://via.placeholder.com/100x150?text=No+Image'"
                  height="150px"
                  width="100px"
              />
            </v-col>

            <v-col>
              <div class="movie-info">
                <v-card-title class="font-weight-bold">{{ movie.title }}</v-card-title>
                <v-card-subtitle>{{ movie.release_date }}</v-card-subtitle>
              </div>
            </v-col>
          </v-card>
        </v-row>
      </v-col>
    </v-row>

  </v-container>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import UserService from "@/services/userService.js";
import {getMoviesByStatus} from "@/services/movieStatusService.js";
import {getImageUrl, getMovieDetails} from "@/services/tmdbService.js";

const watchedMovies = ref([]);
const resultMessage = ref("");
const showSnackbar = ref(false);
const snackbarColor = ref("success");

const fetchMovieDetails = async () => {
  try {
    const user = UserService.getUser();
    const uid = user?.upn;

    if (!uid) {
      throwError("Kein Benutzer angemeldet oder keine UID verfügbar.");
      return;
    }

    // Abrufen der Daten mit watchedAt
    const movieData = await getMoviesByStatus(uid, "watched"); // movieData enthält movieId und watchedAt
    const movieDetailsPromises = movieData.map(async (movie) => {
      const response = await getMovieDetails(movie.movieId);
      return {
        // KI (sortieren)
        ...response.data, // Details aus TMDB
        watchedAt: new Date(movie.watchedAt), // Konvertierung in ein Date-Objekt für Sortierung
      };
    });

    watchedMovies.value = await Promise.all(movieDetailsPromises);
  } catch (error) {
    throwError("Fehler beim Abrufen der Filmdetails.");
    console.error("Fehler beim Abrufen der Filmdetails:", error);
  }
};

// Computed Property für die Sortierung nach watchedAt
const sortedMovies = computed(() => {
  return watchedMovies.value.slice().sort((a, b) => b.watchedAt - a.watchedAt);
});

onMounted(fetchMovieDetails);

function throwError(errorMessage) {
  resultMessage.value = errorMessage || "Fehler beim Abrufen der Filmdetails";
  snackbarColor.value = "error";
  showSnackbar.value = true;
}
</script>

<style scoped>
</style>
