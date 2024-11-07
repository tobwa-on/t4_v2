<template>
  <v-app>
    <MenuBar/>
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import MenuBar from "@/components/MenuBar.vue";
</script>

<style scoped>

</style>
