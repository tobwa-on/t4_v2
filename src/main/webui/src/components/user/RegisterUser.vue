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
  </v-container>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import {registerNewUser} from "@/services/userDataService.js";
import UserService from "@/services/userService.js";
import {toast} from "vue3-toastify";

const username = ref("");
const password = ref("");
const confirmPassword = ref("");

const router = useRouter();

onMounted(async () => {
  try {
    const currentUser = UserService.getUser();
    let userRoles = null;

    if (Object.keys(currentUser).length !== 0) {
      userRoles = await UserService.getUserRole(currentUser?.upn || "");
    }

    if (Object.keys(currentUser).length !== 0 && (!userRoles || !userRoles.includes("admin"))) {
      await router.push({ path: '/' });
    }
  } catch (error) {
    console.error("Fehler beim Überprüfen der Benutzerberechtigung:", error);
    await router.push({path: '/'});
  }
});

const register = async () => {
  if (password.value !== confirmPassword.value) {
    throwError("Die Passwörter stimmen nicht überein.");
    return;
  }

  try {
    const response = await registerNewUser(username.value, password.value);
    console.log(response)
    if (response.success) {
      toast.success("Registrierung erfolgreich.", {
        position: 'top-right',
        autoClose: 3000,
      });

      let currentUser = UserService.getUser();
      let userRoles = null;

      if (Object.keys(currentUser).length !== 0) {
        userRoles = await UserService.getUserRole(currentUser.upn || "");
      }

      if (Object.keys(currentUser).length !== 0 || ( userRoles !== null && userRoles.includes("admin"))) {
        await router.push({ path: '/usermanagement' });
      } else {
        await router.push({ path: '/login' });
      }
    } else {
      throwError("Fehler bei der Registrierung.");
    }
  } catch (error) {
    throwError(error.message);
  }
};

function throwError(message) {
  toast.error(message, {
    position: 'top-right',
    autoClose: 3000,
  });
}
</script>
