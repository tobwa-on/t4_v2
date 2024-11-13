<template>
  <v-dialog v-model="detailDialog" max-width="1200px" persistent>
    <v-card>
      <v-snackbar v-model="showError" timeout="3000" color="red" top>
        {{ errorMessage }}
      </v-snackbar>
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
              <v-btn flat icon @click="handleToggleStatus('isWatched')">
                <v-icon>{{ isWatched ? 'mdi-eye' : 'mdi-eye-outline' }}</v-icon>
              </v-btn>
              <div>{{ isWatched ? 'Gesehen' : 'Gesehen' }}</div>
            </v-col>

            <v-col cols="auto" class="text-center">
              <v-btn flat icon @click="handleToggleStatus('isFavorite')">
                <v-icon>{{ isFavorite ? 'mdi-heart' : 'mdi-heart-outline' }}</v-icon>
              </v-btn>
              <div>{{ isFavorite ? 'Favorit' : 'Favorit' }}</div>
            </v-col>

            <v-col cols="auto" class="text-center">
              <v-btn flat icon @click="handleToggleStatus('isInWatchlist')">
                <v-icon>{{ isInWatchlist ? 'mdi-clock-minus' : 'mdi-clock-plus-outline' }}</v-icon>
              </v-btn>
              <div>{{ isInWatchlist ? 'Gemerkt' : 'Merken' }}</div>
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
                  clearable
              ></v-rating>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
      <v-row no-gutters justify="center" align="center">
        <v-col>
          <v-textarea variant="outlined" rows="4" v-model="reviewText" label="Deine Rezension" outlined class="pa-4"></v-textarea>
        </v-col>
      </v-row>
      <v-divider></v-divider>
      <v-row no-gutters justify="center" align="center" class="px-4">
        <v-col>
          <v-card @click="showReviews = !showReviews" variant="flat">
            <v-card-actions>
              <v-card-text class="pa-0">Alle Rezensionen</v-card-text>
              <v-spacer></v-spacer>
              <v-btn
                  :icon="showReviews ? 'mdi-chevron-up' : 'mdi-chevron-down'"
              ></v-btn>
            </v-card-actions>

            <v-expand-transition>
              <div v-show="showReviews">
                <v-divider></v-divider>
                <v-list lines="one">
                  <v-list-item
                      v-if="reviews === null || reviews.length === 0"
                  >
                    <v-row  no-gutters align="center" justify="space-between">
                      <v-col cols="auto">
                        <v-list-item-subtitle>Keine Rezensionen vorhanden</v-list-item-subtitle>
                      </v-col>
                    </v-row>
                  </v-list-item>
                  <v-list-item
                      v-else
                      v-for="(review, index) in reviews" :key="index"
                  >
                    <v-row no-gutters align="center" justify="space-between">
                      <v-col cols="auto">
                        <v-list-item-title>{{ review.uid.uid }}</v-list-item-title>
                        <v-list-item-subtitle>{{ review.reviewText }}</v-list-item-subtitle>
                      </v-col>
                      <v-col cols="auto" class="text-right">
                        <v-rating
                            v-model="review.rating"
                            color="amber"
                            readonly
                            dense
                            :length="5"
                            half-increments
                        ></v-rating>
                      </v-col>
                    </v-row>
                  </v-list-item>
                </v-list>
              </div>

            </v-expand-transition>
          </v-card>
        </v-col>
      </v-row>
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn color="black" prepend-icon="mdi-content-save-outline" variant="flat" @click="saveAndExit">Speichern</v-btn>
        <v-btn color="black" prepend-icon="mdi-close" variant="outlined" @click="closeDialog">Abbrechen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import {defineEmits, ref, watch} from 'vue';
import {getImageUrl} from '@/services/tmdbService.js';
import {deleteMovieStatus, getMovieStatus, updateMovieStatus} from '@/services/movieStatusService.js';
import UserService from "@/services/userService.js";
import {deleteReview, getAllReviews, getReview, saveOrUpdateReview} from "@/services/reviewService.js";

const props = defineProps({
  detailDialog: Boolean,
  movieDetails: Object,
});

const emit = defineEmits(['update:detailDialog', 'showSnackbar']);
const detailDialog = ref(props.detailDialog);
const uid = UserService.getUser().upn;
const showError = ref(false);

const starRating = ref(0);
const reviewText = ref(null);
const reviews = ref([]);
const showReviews = ref(false)
const errorMessage = ref('');

const isFavorite = ref(false);
const isWatched = ref(false);
const isInWatchlist = ref(false);


function throwErrorMessage(message) {
  showError.value = true;
  errorMessage.value = message;
}

function checkUserLoggedIn() {
  if(!uid) {
    throwErrorMessage("Für diese Aktion musst du angemeldet sein");
    throw new Error(errorMessage.value);
  }
}

const handleToggleStatus = async (statusField) => {
  checkUserLoggedIn();

  if (statusField === "isFavorite") {
    isFavorite.value = !isFavorite.value;
  } else if (statusField === "isWatched") {
    isWatched.value = !isWatched.value;
  } else {
    isInWatchlist.value = !isInWatchlist.value;
  }

  try {
    if (!isInWatchlist.value && !isFavorite.value && !isWatched.value) {
        await deleteMovieStatus(uid, props.movieDetails.id);
        return;
    }

    await updateMovieStatus(uid, props.movieDetails.id, {
      favorite: isFavorite.value,
      watchlist: isInWatchlist.value,
      watched: isWatched.value
    });
  } catch (error) {
    console.error("Fehler bei der Status-Aktualisierung:", error);
    statusField.value = !statusField.value;
  }
}

const getExistingReview = async () => {
  try {
    const existingReview = await getReview(uid, props.movieDetails.id);
    if (existingReview) {
      starRating.value = existingReview.rating;
      reviewText.value = existingReview.reviewText;
    }
  } catch (error) {
    console.error("Fehler beim Laden der Rezension:", error);
    starRating.value = 0;
  }
};
const loadReviews = async () => {
  try {
    const allReviews = await getAllReviews(props.movieDetails.id);
    reviews.value = allReviews.filter(review => review.rating !== 0);
  } catch (error) {

  }
};

const handleSaveReview = async () => {
    checkUserLoggedIn();

    if ((reviewText.value !== "" && reviewText.value !== null) && starRating.value === 0) {
      throwErrorMessage("Um eine Rezension zu speichern, ist eine Sternebewertung notwendig.");
      throw new Error(errorMessage.value);
    }

    if (starRating.value !== 0 || reviewText.value !== null){
      // Review speichern oder aus DB loeschen
      if (starRating.value !== 0 && reviewText.value !== null) {
        await saveOrUpdateReview(uid, props.movieDetails.id, starRating.value, reviewText.value);
        emit('showSnackbar', { message: 'Die Rezension wurde erfolgreich gespeichert!', color: 'green' });
      } else {
        await deleteReview(uid, props.movieDetails.id);
      }

    } else {
      emit('showSnackbar', { message: 'Keine Rezension gespeichert', color: 'grey' });
    }
};

const getExistingMovieStatus = async () => {
  const status = await getMovieStatus(uid, props.movieDetails.id);
  isFavorite.value = status.favorite;
  isWatched.value = status.watched;
  isInWatchlist.value = status.watchlist;
};

const closeDialog = () => {
  emit('update:detailDialog', false);
  setFieldsAtExit();
};

const saveAndExit = async () => {
  try {
    await handleSaveReview();
    emit('update:detailDialog', false);
    setFieldsAtExit();
  } catch (error){
    console.error(error)
  }
};

function setFieldsAtExit() {
  isFavorite.value = false;
  isWatched.value = false;
  isInWatchlist.value = false;
  reviewText.value = "";
  showError.value = false;
  showReviews.value = false;
  starRating.value = 0;
  reviews.value = [];
}

watch(
    () => props.detailDialog,
    (newValue) => {
      detailDialog.value = !!newValue;
      if (newValue) {
        getExistingMovieStatus();
        getExistingReview();
        loadReviews();
      }
    }
);
</script>
