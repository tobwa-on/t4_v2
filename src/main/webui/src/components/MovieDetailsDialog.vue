<template>
  <v-dialog v-model="detailDialog" max-width="1200px" persistent>
    <v-card>
      <v-row no-gutters justify="center" align="center" class="pl-4 mt-4">
        <v-col cols="2">
          <v-img :src="getImageUrl(movieDetails.poster_path)" height="300px"></v-img>
        </v-col>
        <v-col class="pl-4 pr-4">
          <v-card-title class="font-weight-bold text-h5">{{ movieDetails.title }}</v-card-title>
          <v-card-subtitle class="text-subtitle-1 grey--text">{{ movieDetails.release_date }}</v-card-subtitle>
          <v-card-text class="mt-4 mb-2">{{ movieDetails.overview }}</v-card-text>
        </v-col>

        <v-col cols="auto" class="pr-4">
          <v-row class="justify-center">
            <v-col cols="4" class="text-center">
              <v-btn flat icon @click="">
                <v-icon>{{ isWatched ? 'mdi-eye' : 'mdi-eye-outline' }}</v-icon>
              </v-btn>
              <div>{{ isWatched ? 'Watched' : 'Watch' }}</div>
            </v-col>

            <v-col cols="auto" class="text-center">
              <v-btn flat icon @click="handleToggleFavorite">
                <v-icon>{{ isFavorite ? 'mdi-heart' : 'mdi-heart-outline' }}</v-icon>
              </v-btn>
              <div>{{ isFavorite ? 'Liked' : 'Like' }}</div>
            </v-col>

            <v-col cols="auto" class="text-center">
              <v-btn flat icon @click="">
                <v-icon>{{ isInWatchlist ? 'mdi-clock-minus-outline' : 'mdi-clock-plus-outline' }}</v-icon>
              </v-btn>
              <div>Watchlist</div>
            </v-col>
          </v-row>
          <v-row class="justify-center">
            <v-col cols="auto" class="text-center">
              <v-rating
                  v-model="starRating"
                  color="amber"
                  dense
                  hover
                  :length="5"
                  half-increments
              ></v-rating>
            </v-col>
          </v-row>
          <v-row class="justify-center">
            <v-col cols="auto" class="text-center">
              <v-btn flat variant="outlined" @click="openReviewDialog">
                Rezension schreiben
              </v-btn>
            </v-col>
          </v-row>

        </v-col>
      </v-row>

      <v-divider></v-divider>
      <v-card-actions>
        <v-btn @click="closeDialog">Schließen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { defineEmits, ref, watch } from 'vue';
import { getImageUrl } from '@/services/tmdbService.js';
import { isFavoriteMovie, removeFavorite, saveFavorite } from '@/services/favoriteService';
import UserService from "@/services/userService.js";

const props = defineProps({
  detailDialog: Boolean,
  movieDetails: Object,
  starRating: Number,
  review: String,
  isSavedForLater: Boolean,
});

const emit = defineEmits(['update:detailDialog', 'reviewDialog']); // `showReviewDialog` Event hinzufügen

const detailDialog = ref(props.detailDialog);
const starRating = ref(props.starRating || 0);
const uid = UserService.getUser().upn; // User abrufen
let isFavorite = ref(false);
let isWatched = ref(false);
let isInWatchlist= ref(false);

const handleToggleFavorite = async () => {
  isFavorite.value = !isFavorite.value;
  try {
    if (isFavorite.value) {
      await saveFavorite(uid, props.movieDetails.id);
      console.log("Film als Favorit gespeichert.");
    } else {
      await removeFavorite(uid, props.movieDetails.id);
      console.log("Film aus Favoriten entfernt.");
    }
  } catch (error) {
    console.error("Fehler bei der Favoritenaktualisierung:", error);
    isFavorite.value = !isFavorite.value;
  }
};
const checkFavoriteStatus = async () => {
  try {
    isFavorite.value = false;
    isFavorite.value = await isFavoriteMovie(uid, props.movieDetails.id);
  } catch (error) {
    console.error("Fehler beim Überprüfen des Favoritenstatus:", error);
  }
};

const openReviewDialog = () => {
  emit('reviewDialog', true);
};

const closeDialog = () => {
  emit('update:detailDialog', false);
  isFavorite.value = false; // Zurücksetzen bevor der Status überprüft wird
};

watch(
    () => props.detailDialog,
    (newValue) => {
      detailDialog.value = !!newValue; // Umwandlung in Boolean
      if (newValue) {
        checkFavoriteStatus();
      }
    }
);



</script>
