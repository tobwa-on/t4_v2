<template>
  <v-container fluid class="d-flex justify-center align-center" style="height: 50vh;">
    <v-card class="pa-4" width="600" rounded title="Passwort ändern" border variant="flat">
      <v-card-item>
        <v-form @submit.prevent="handleChangePassword">
          <!-- Verstecktes Benutzername-Feld für Barrierefreiheit und Passwort-Manager -->
          <v-text-field
              label="Benutzername"
              v-model="username"
              autocomplete="username"
              style="position: absolute; opacity: 0; height: 0; width: 0; overflow: hidden;"
          ></v-text-field>

          <v-text-field
              class="mb-1"
              label="Aktuelles Passwort"
              v-model="currentPassword"
              :type="'password'"
              required
              variant="outlined"
              autocomplete="current-password"
          ></v-text-field>

          <v-text-field
              class="mb-1"
              label="Neues Passwort"
              v-model="newPassword"
              :type="'password'"
              required
              variant="outlined"
              autocomplete="new-password"
          ></v-text-field>

          <v-text-field
              class="mb-3"
              label="Neues Passwort wiederholen"
              v-model="confirmPassword"
              :type="'password'"
              required
              variant="outlined"
              autocomplete="new-password"
          ></v-text-field>

          <v-btn type="submit" color="black" variant="elevated" block>
            Passwort ändern
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
import { changePassword } from "@/services/userDataService.js";
import UserService from "@/services/userService.js";

const currentPassword = ref("");
const newPassword = ref("");
const confirmPassword = ref("");
const resultMessage = ref("");
const showSnackbar = ref(false);
const snackbarColor = ref("success");
const username = ref(UserService.getUser().upn);

const router = useRouter();

const handleChangePassword = async () => {
  if (newPassword.value === currentPassword.value) {
    throwError("Das neue Passwort darf nicht mit dem aktuellen Passwort übereinstimmen.")
    return;
  }

  if (newPassword.value !== confirmPassword.value) {
    throwError("Die neuen Passwörter stimmen nicht überein.")
    return;
  }

  try {
    const response = await changePassword(username.value, currentPassword.value, newPassword.value);
    if (!response.success) {
      throwError(response.data.message);
      return;
    }

    resultMessage.value = "Passwort erfolgreich geändert.";
    snackbarColor.value = "success";
    showSnackbar.value = true;
    await router.push({path: '/'});
  } catch (error) {
    throwError(error.response);
  }
};

function throwError(errorMessage) {
  resultMessage.value = errorMessage || "Fehler beim Ändern des Passworts.";
  snackbarColor.value = "error";
  showSnackbar.value = true;
}
</script>
