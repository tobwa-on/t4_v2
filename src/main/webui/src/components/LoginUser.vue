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

          <v-btn type="submit" color="primary" variant="elevated" block>
            Login
          </v-btn>
        </v-form>
        <p>Ergebnis: {{ text }}</p>
      </v-card-item>
    </v-card>
  </v-container>
</template>

<script>
export default {
	data() {
		return {
			username: "",
			password: "",
			text : ""
		};
	},
	methods: {
		login: function() {
			let data = {user:this.username,password:this.password};
			let state = this;
			this.rest.post('/login', data).then(function (response) {
				console.log(response);
				state.text = response.status;
				localStorage.setItem("user", response.data);
				// enforce refresh
				state.$router.push( {path: '/'}).then( () => {state.$router.go();} );
			}).catch(function (error) {
				console.log(error);
				state.text = error;
			});
		}
	}
}
</script>
