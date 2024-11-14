<template>
  <v-container fluid>
    <v-row class="d-flex justify-center align-center py-4">
      <v-icon color="purple" size="36" class="mr-2">mdi-bookmark</v-icon>
      <h2 class="font-weight-bold mb-0">Meine Merkliste</h2>
    </v-row>

    <v-row v-if="watchlistMovies.length === 0" class="mt-4">
      <v-col cols="12" class="text-center">
        <p>Bisher wurden keine Filme in der Merkliste gespeichert.</p>
      </v-col>
    </v-row>

    <!-- Ergebnisse -->
    <v-row v-else class="mt-4" dense align-content="start">
      <v-col
          v-for="movie in watchlistMovies"
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
import {defineEmits, onMounted, ref} from "vue";
import UserService from "@/services/userService.js";
import {getMoviesByStatus} from "@/services/movieStatusService.js";
import {getImageUrl, getMovieDetails} from "@/services/tmdbService.js";

const watchlistMovies = ref([]);
const resultMessage = ref("");
const showSnackbar = ref(false);
const snackbarColor = ref("success");
const emit = defineEmits(['showSnackbar']);

const fetchMovieDetails = async () => {
  try {
    const user = UserService.getUser();
    const uid = user?.upn;

    if (!uid) {
      throwError("Kein Benutzer angemeldet oder keine UID verfügbar.")
      return;
    }

    const movieIds = await getMoviesByStatus(uid, 'watchlist');
    const movieDetailsPromises = movieIds.map(id => getMovieDetails(id));
    const movies = await Promise.all(movieDetailsPromises);
    watchlistMovies.value = movies.map(response => response.data);

  } catch (error) {
    throwError("Fehler beim Abrufen der Filmdetails")
    console.error("Fehler beim Abrufen der Filmdetails:", error);
  }
}

onMounted(fetchMovieDetails);

function throwError(errorMessage) {
  resultMessage.value = errorMessage || "Fehler beim Abrufen der Filmdetails";
  snackbarColor.value = "error";
  showSnackbar.value = true;
}
</script>

<style scoped>

</style>