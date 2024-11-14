<template>
  <v-container fluid class="d-flex justify-center align-center" style="height: 50vh;">
    <v-card class="pa-4" width="600" rounded title="Login" border variant="flat">
      <v-card-item>
        <v-form @submit.prevent="login">
          <v-text-field
              class="mt-2 mb-1"
              label="Benutzername"
              v-model="username"
              required
              variant="outlined"
              autocomplete="username"
          ></v-text-field>

          <v-text-field
              class="mb-3"
              label="Passwort"
              v-model="password"
              :type="'password'"
              required
              variant="outlined"
              autocomplete="current-password"
          ></v-text-field>

          <v-btn type="submit" color="black" variant="elevated" block>
            Login
          </v-btn>
        </v-form>
      </v-card-item>
    </v-card>
    <v-snackbar v-model="showSnackbar" :color="snackbarColor" top right>
      {{ snackbarMessage }}
    </v-snackbar>
  </v-container>
</template>
<script>
import UserService from "@/services/userService.js"; // Importiere deinen UserService zur Statusprüfung

export default {
  data() {
    return {
      username: "",
      password: "",
      text: "",
      showSnackbar: false,
      snackbarMessage: "",
      snackbarColor: "success"
    };
  },
  async beforeMount() {
    const currentUser = UserService.getUser();
    if (currentUser && currentUser.upn) {
      await this.$router.push({ path: '/' });
    }
  },
  methods: {
    login() {
      let data = { user: this.username, password: this.password };
      this.rest.post('/login', data)
          .then(response => {
            this.snackbarMessage = "Login erfolgreich!";
            this.snackbarColor = "success";
            this.showSnackbar = true;

            this.text = response.status;
            localStorage.setItem("user", response.data);

            this.$router.push({ path: '/' }).then(() => {
              this.$router.go();
            });
          })
          .catch(error => {
            console.error(error);
            this.snackbarMessage = "Fehler beim Login. Bitte überprüfen Sie Ihre Anmeldedaten.";
            this.snackbarColor = "error";
            this.showSnackbar = true;
          });
    }
  }
}
</script>

