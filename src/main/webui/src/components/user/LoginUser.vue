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
  </v-container>
</template>
<script>
import UserService from "@/services/userService.js";
import {toast} from "vue3-toastify";

export default {
  data() {
    return {
      username: "",
      password: "",
      text: "",
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
            toast.success("Login erfolgreich", {
              position: 'top-right',
              autoClose: 3000,
            });

            this.text = response.status;
            localStorage.setItem("user", response.data);

            this.$router.push({ path: '/' }).then(() => {
              this.$router.go();
            });
          })
          .catch(error => {
            console.error(error);
            toast.error("Fehler beim Login", {
              position: 'top-right',
              autoClose: 3000,
            });
          });
    }
  }
}
</script>

