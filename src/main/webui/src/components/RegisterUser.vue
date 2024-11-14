<template>
  <v-container fluid class="d-flex justify-center align-center" style="height: 50vh;">
    <v-card class="pa-4" width="600" rounded title="Neuen Nutzer registrieren" border variant="flat">
      <v-card-item>
        <v-form @submit.prevent="register">
          <v-text-field
              class="mt-2 mb-1"
              label="Benutzername"
              v-model="username"
              required
              variant="outlined"
              autocomplete="username"
          ></v-text-field>

          <v-text-field
              class="mb-1"
              label="Passwort"
              v-model="password"
              :type="'password'"
              required
              variant="outlined"
              autocomplete="new-password"
          ></v-text-field>

          <v-text-field
              class="mb-3"
              label="Passwort wiederholen"
              v-model="confirmPassword"
              :type="'password'"
              required
              variant="outlined"
              autocomplete="new-password"
          ></v-text-field>

          <v-btn type="submit" color="black" variant="elevated" block>
            Registrieren
          </v-btn>
        </v-form>
      </v-card-item>
    </v-card>

    <!-- Snackbar für Ergebnisanzeige -->
    <v-snackbar v-model="showSnackbar" :color="snackbarColor" top right>
      {{ resultMessage }}
    </v-snackbar>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import {registerNewUser} from "@/services/userDataService.js";

const username = ref("");
const password = ref("");
const confirmPassword = ref("");
const resultMessage = ref("");
const showSnackbar = ref(false);
const snackbarColor = ref("success");

const router = useRouter();

const register = async () => {
  if (password.value !== confirmPassword.value) {
    throwError("Die Passwörter stimmen nicht überein.")
    return;
  }

  try {
    const response = await registerNewUser(username.value, password.value);

    if (response.success) {
      resultMessage.value = "Registrierung erfolgreich.";
      snackbarColor.value = "success";
      showSnackbar.value = true;
      await router.push({ path: '/login' });
    } else {
      throw new Error(response.message || "Fehler bei der Registrierung.");
    }
  } catch (error) {
    throwError(error.message)
  }
};

function throwError(errorMessage) {
  resultMessage.value = errorMessage || "Fehler beim Ändern des Passworts.";
  snackbarColor.value = "error";
  showSnackbar.value = true;
}
</script>
