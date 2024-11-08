<template>
  <v-dialog v-model="reviewDialog" max-width="1000px" persistent>
    <v-card>

      <v-row no-gutters justify="center" align="center" class="mt-4">
        <v-col cols="3">
          <v-img :src="getImageUrl(movieDetails.poster_path)" height="300px"></v-img>
        </v-col>

        <v-col class="pl-4 pr-4">
          <v-card-title class="font-weight-bold text-h5">{{ movieDetails.title }}</v-card-title>
          <v-textarea variant="outlined" rows="4" v-model="review" label="Deine Rezension" outlined class="mt-4"></v-textarea>
          <v-rating
              v-model="starRating"
              color="amber"
              dense
              hover
              :length="5"
              half-increments
          ></v-rating>
          <v-card-actions>
            <v-btn @click="handleSaveReview">Speichern</v-btn>
            <v-btn @click="closeDialog">Schließen</v-btn>
          </v-card-actions>

        </v-col>
      </v-row>
    </v-card>
  </v-dialog>
</template>

<script setup>

import {ref, watch} from "vue";
import {getReview, saveReview, updateReview} from "@/services/reviewService.js";
import UserService from "@/services/userService.js";
import {getImageUrl} from "@/services/tmdbService.js";

const props = defineProps({
  reviewDialog: Boolean,
  movieDetails: Object,
  starRating: Number,
  review: String,
});

const uid = UserService.getUser().upn; // User abrufen
const starRating = ref(props.starRating || 0);
const review = ref(props.review || "");

const reviewDialog = ref(props.reviewDialog);

watch(
    () => props.reviewDialog,
    (newValue) => {
      reviewDialog.value = newValue;
      if (newValue) {
        loadExistingReview();
      }
    }
);

const loadExistingReview = async () => {
  try {
    const existingReview = await getReview(uid, props.movieDetails.id);
    if (existingReview) {
      starRating.value = existingReview.rating;
      review.value = existingReview.reviewText;
    } else {
      starRating.value = 0;
      review.value = "";
    }
  } catch (error) {
    console.error("Fehler beim Laden der Rezension:", error);
    starRating.value = 0;
    review.value = "";
  }
};

const handleSaveReview = async () => {
  try {
    const existingReview = await getReview(uid, props.movieDetails.id);
    if (existingReview) {
      await updateReview(uid, props.movieDetails.id, starRating.value, review.value);
      console.log("Rezension erfolgreich aktualisiert.");
    } else {
      await saveReview(uid, props.movieDetails.id, starRating.value, review.value);
      console.log("Rezension erfolgreich gespeichert.");
    }
  } catch (error) {
    console.error("Fehler beim Speichern oder Aktualisieren der Rezension:", error);
  }
};

const emit = defineEmits(['update:reviewDialog']);

const closeDialog = () => {
  emit('update:reviewDialog', false);
};

</script>
