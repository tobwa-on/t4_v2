<template>
  <v-app-bar app color="black" dark>
    <!-- Logo -->
    <v-toolbar-title>
      <router-link to="/" class="text-white text-decoration-none">
        Film-App
      </router-link>
    </v-toolbar-title>

    <v-spacer></v-spacer>

    <!-- Links -->
    <v-btn to="/">Home</v-btn>
    <v-btn to="/favorites" v-if="Object.keys(user).length">Favoriten</v-btn>
    <v-btn to="/watchlist" v-if="Object.keys(user).length">Merkliste</v-btn>
    <v-btn to="/watched" v-if="Object.keys(user).length">Gesehene Filme</v-btn>

    <v-btn to="/adminview" v-if="adminLoggedIn">Nutzerverwaltung</v-btn>
    <v-btn to="/register" v-if="!Object.keys(user).length">Registrieren</v-btn>

    <!-- Benutzer Dropdown oder Login -->
    <v-menu offset-y v-if="Object.keys(user).length">
      <template #activator="{ props }">
        <v-btn v-bind="props">{{ user.upn }}</v-btn>
      </template>
      <v-list>
        <v-list-item @click="router.push({ path: '/changepassword' })">
          <v-list-item-title>Passwort ändern</v-list-item-title>
        </v-list-item>
        <v-list-item @click="logOut">
          <v-list-item-title>Logout</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
    <v-btn v-else to="/login">Login</v-btn>

  </v-app-bar>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import VueJwtDecode from 'vue-jwt-decode';
import { useRouter } from 'vue-router';
import UserService from "@/services/userService.js";

const user = ref({});
const router = useRouter();
const adminLoggedIn = ref(false);

async function decodeUserData() {
  const token = localStorage.getItem("user");
  if (token) {
    try {
      const decoded = VueJwtDecode.decode(token);
      user.value = decoded;

      if (Date.now() >= decoded.exp * 1000) {
        localStorage.removeItem("user");
        await router.push({ path: '/' });
        window.location.reload();
      }

      if (Object.keys(user).length) {
        const userRoleResponse = await UserService.getUserRole(decoded.upn);
        if (userRoleResponse.includes("admin")) {
          adminLoggedIn.value = true;
        }
      }

    } catch (error) {
      console.error(error);
    }
  } else {
    await router.push({path: '/'});
  }
}

function logOut() {
  localStorage.removeItem("user");
  adminLoggedIn.value = false;
  router.push({ path: '/' });
  window.location.reload();
}

onMounted(() => {
  decodeUserData();
});
</script>

<style scoped>
.text-decoration-none {
  text-decoration: none;
}
</style>