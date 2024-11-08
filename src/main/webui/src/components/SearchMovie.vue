<template>
  <v-container fluid>
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
        <v-btn color="primary" @click="searchMovies">
          Suchen
        </v-btn>
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
          <v-card class="d-flex align-center pa-4" variant="flat">
            <v-col>
              <v-img
                  :src="getImageUrl(movie.poster_path)"
                  height="150px"
                  width="100px"
              />
            </v-col>

            <v-col>
              <div class="movie-info">
                <v-card-title class="font-weight-bold">{{ movie.title }}</v-card-title>
                <v-card-subtitle>{{ movie.release_date }}</v-card-subtitle>
                <v-card-actions>
                  <v-btn @click="$emit('showDetailDialog', movie.id);">Details ansehen</v-btn>
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
import { ref } from 'vue';
import {getImageUrl, performSearch} from "@/services/tmdbService.js";

const searchQuery = ref('');
const movies = ref([]);

const searchMovies = async () => {
  try {
    const response = await performSearch(searchQuery.value);
    movies.value = response.data.results.slice(0, 20);
  } catch (error) {
    console.error("Fehler beim Abrufen der Filme:", error);
  }
};


</script>

<style scoped>
.mx-4 {
  margin-left: 16px;
  margin-right: 16px;
}
.py-1 {
  padding-top: 4px;
  padding-bottom: 4px;
}
.movie-info {
  flex: 1;
}
</style>
