<template>
	<h3>LoginUser</h3>
	<div class="login">
		<form v-on:submit.prevent="login">
			<p>User: <input type="text" v-model="username" /></p>
			<p>Passwort: <input type="password" v-model="password" /></p>
			<p><button type="submit">Login</button></p>
		</form>
		<p>Result: {{ text }}</p>
	</div>
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
