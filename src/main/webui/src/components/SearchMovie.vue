<template>
  <v-container fluid>
    <v-row class="d-flex justify-center align-center py-4">
      <v-icon color="green" size="36" class="mr-2">mdi-magnify</v-icon>
      <h2 class="font-weight-bold mb-0">Filmsuche</h2>
    </v-row>

    <v-row align="center" justify="center" class="my-4">
      <v-col cols="7">
        <v-text-field
            v-model="searchQuery"
            label="Suche..."
            prepend-inner-icon="mdi-magnify"
            class="mx-4 py-1 text-center"
            variant="outlined"
            @keyup.enter="searchMovies"
            hide-details
        ></v-text-field>
      </v-col>

      <v-col cols="auto">
        <v-btn color="black" @click="searchMovies">
          Suchen
        </v-btn>
      </v-col>
    </v-row>

    <!-- Nachricht bei keinen Ergebnissen, nur nach Klick auf Suchen -->
    <v-row v-if="isSearchPerformed && !movies.length" class="text-center">
      <v-col cols="12">
        <p>Keine Filme gefunden für "{{ searchQuery }}"</p>
      </v-col>
    </v-row>

    <!-- Ergebnisse -->
    <v-row v-if="movies.length" class="mt-4" dense align-content="start">
      <v-col
          v-for="movie in movies"
          :key="movie.id"
          cols="4"
      >
        <v-row>
          <v-card class="d-flex align-center pa-4" variant="flat" @click="$emit('showDetailDialog', movie.id);">
            <v-col>
              <v-img
                  :src="getImageUrl(movie.poster_path)"
                  height="150px"
                  width="100px"
              />
            </v-col>

            <v-col>
              <v-card-title class="font-weight-bold">{{ movie.title }}</v-card-title>
              <v-card-subtitle>{{ movie.release_date }}</v-card-subtitle>
            </v-col>
          </v-card>
        </v-row>
      </v-col>
    </v-row>

    <!-- Snackbar für leere Sucheingabe -->
    <v-snackbar v-model="showSnackbar" color="error" top right>
      Bitte geben Sie einen Suchbegriff ein.
    </v-snackbar>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { getImageUrl, performSearch } from "@/services/tmdbService.js";

const searchQuery = ref('');
const movies = ref([]);
const showSnackbar = ref(false);
const isSearchPerformed = ref(false); // Neue Variable zum Verfolgen des Suchstatus

const searchMovies = async () => {
  if (!searchQuery.value) {
    showSnackbar.value = true;
    return;
  }

  isSearchPerformed.value = true; // Suche wird durchgeführt

  try {
    const response = await performSearch(searchQuery.value);
    movies.value = response.data.results.slice(0, 20);
  } catch (error) {
    console.error("Fehler beim Abrufen der Filme:", error);
  }
};
</script>

