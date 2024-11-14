<template>
  <v-container fluid>
    <v-row class="d-flex justify-center align-center py-4">
      <v-icon color="red" size="36" class="mr-2">mdi-heart</v-icon>
      <h2 class="font-weight-bold mb-0">Meine Favoriten</h2>
    </v-row>

    <v-row v-if="favouriteMovies.length === 0" class="mt-4">
      <v-col cols="12" class="text-center">
        <p>Bisher wurden keine Filme als Favorit gespeichert.</p>
      </v-col>
    </v-row>

    <!-- Ergebnisse -->
    <v-row v-else class="mt-4" dense align-content="start">
      <v-col
          v-for="movie in favouriteMovies"
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
import { onMounted, ref } from 'vue';
import { getImageUrl, getMovieDetails } from '@/services/tmdbService.js';
import { getFavoriteMovies } from "@/services/movieStatusService.js";
import UserService from "@/services/userService.js";

const favouriteMovies = ref([]);

const fetchMovieDetails = async () => {
  try {
    const user = UserService.getUser();
    const uid = user?.upn;

    if (!uid) {
      console.error("Kein Benutzer angemeldet oder keine UID verfügbar.");
      return;
    }

    const favorites = await getFavoriteMovies(uid);
    if (favorites && favorites.length > 0) {
      const movieDetailsPromises = favorites.map(favorite => getMovieDetails(favorite.movieId));
      const movies = await Promise.all(movieDetailsPromises);
      favouriteMovies.value = movies.map(response => response.data);
    }
  } catch (error) {
    console.error("Fehler beim Abrufen der Filmdetails:", error);
  }
};

onMounted(fetchMovieDetails);
</script>
