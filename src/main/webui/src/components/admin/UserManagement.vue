<template>
  <v-container fluid>
    <v-row class="d-flex justify-center align-center py-4">
      <v-icon color="grey" size="36" class="mr-2">mdi-cog</v-icon>
      <h2 class="font-weight-bold mb-0">Nutzerverwaltung</h2>
    </v-row>

    <!-- Button für neuen Benutzer -->
    <v-row class="d-flex justify-center mb-4 pt-4">
      <v-btn color="black" @click="goToRegisterView" prepend-icon="mdi-account-plus">
        Neuen Benutzer hinzufügen
      </v-btn>
    </v-row>

    <v-row class="d-flex justify-center">
      <v-col cols="12" md="4">
        <v-data-table
            :headers="headers"
            :items="users"
            :items-per-page="10"
        >
          <template v-slot:item.uid="{ item }">
            <span class="font-weight-bold">{{ item.uid }}</span>
          </template>
          <template v-slot:item.role="{ item }">
            <span>{{ formatRole(item.roles) }}</span>
          </template>
          <template v-slot:item.actions="{ item }">
            <div class="text-right">
              <v-btn
                  prepend-icon="mdi-delete"
                  color="black"
                  :disabled="item.roles.includes('admin')"
                  @click="confirmDelete(item.uid)"
              >
                Entfernen
              </v-btn>
            </div>
          </template>
        </v-data-table>
      </v-col>
    </v-row>

    <v-dialog v-model="showDialog" max-width="400">
      <v-row>
        <v-card title="Bestätigung" text="Sind Sie sicher, dass Sie diesen Benutzer löschen möchten?">
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="grey" prepend-icon="mdi-cancel" @click="showDialog = false">Abbrechen</v-btn>
            <v-btn color="red" prepend-icon="mdi-check" @click="handleDeleteUser(selectedUser)">Löschen</v-btn>
          </v-card-actions>
        </v-card>
      </v-row>
    </v-dialog>
  </v-container>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { deleteUser, fetchAllUsers } from "@/services/userDataService.js";
import {toast} from "vue3-toastify";

const users = ref([]);
const showDialog = ref(false);
const selectedUser = ref(null);

const router = useRouter();

const headers = [
  { text: "Benutzername", value: "uid" },
  { text: "Rolle", value: "role" },
  { text: "Aktionen", value: "actions", sortable: false }
];

const loadUsers = async () => {
  try {
    const userNames = await fetchAllUsers();
    users.value = userNames.map(user => ({
      uid: user.uid,
      roles: user.roles
    }));
  } catch (error) {
    toast.error("Fehler beim Abrufen der Benutzer.", {
      position: 'top-right',
      autoClose: 3000,
    });
    throw new Error(error);
  }
};

const formatRole = (roles) => {
  if (roles.includes("admin")) return "admin";
  return roles.length ? roles.join(", ") : "user";
};

const confirmDelete = (uid) => {
  selectedUser.value = uid;
  showDialog.value = true;
};

const handleDeleteUser = async (uid) => {
  showDialog.value = false;
  try {
    await deleteUser(uid);
    toast.success("Benutzer erfolgreich gelöscht.", {
      position: 'top-right',
      autoClose: 3000,
    });
    await loadUsers();
  } catch (error) {
    toast.error("Fehler beim Löschen des Benutzers.", {
      position: 'top-right',
      autoClose: 3000,
    });

  }
};

const goToRegisterView = () => {
  router.push({ path: "/register" });
};

onMounted(loadUsers);
</script>
