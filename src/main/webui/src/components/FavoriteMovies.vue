<template>
  <v-container fluid>
    <h2 class="text-center mb-2">Meine Favoriten</h2>
    <!-- Ergebnisse -->
    <v-row class="mt-4" dense align-content="start">
      <v-col
          v-for="movie in favouriteMovies"
          :key="movie.id"
          cols="4"
      >
        <v-row>
          <v-card class="d-flex align-center pa-4" variant="flat">
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
                <v-card-actions>
                  <v-btn @click="$emit('showDetailDialog', movie.id)">Details ansehen</v-btn>
                </v-card-actions>
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
import { getFavouriteMovies } from "@/services/favoriteService.js";
import UserService from "@/services/userService.js";

const favouriteMovies = ref([]);

const fetchMovieDetails = async () => {
  try {
    const user = UserService.getUser(); // User abrufen
    const uid = user?.upn; // UID dynamisch aus User abrufen

    if (!uid) {
      console.error("Kein Benutzer angemeldet oder keine UID verfügbar.");
      return;
    }

    const favorites = await getFavouriteMovies(uid); // Ruft die Favoriten ab
    if (favorites && favorites.length > 0) { // Prüft, ob favorites nicht leer ist
      console.log("Test", favorites)
      const movieDetailsPromises = favorites.map(favorite => getMovieDetails(favorite.movieId));
      const movies = await Promise.all(movieDetailsPromises); // Holt alle Details parallel
      favouriteMovies.value = movies.map(response => response.data); // Speichert die Daten in favouriteMovies
    }
  } catch (error) {
    console.error("Fehler beim Abrufen der Filmdetails:", error);
  }
};

onMounted(fetchMovieDetails); // Ruft die Funktion beim Laden der Komponente auf
</script>

<style scoped>
</style>
