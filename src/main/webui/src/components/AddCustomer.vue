<template>
	<h3>Kunde anlegen</h3>
	<div class="addcustomer">
		<form v-on:submit.prevent="create">
			<p>Vorname: <input type="text" v-model="givenName" /></p>
			<p>Name: <input type="text" v-model="familyName" /></p>
			<p>Mail: <input type="text" v-model="email" /></p>
			<p><button type="submit">Anlegen</button></p>
		</form>
		<p>Result: {{ text }}</p>
	</div>
</template>

<script>
export default {
	data() {
		return {
			givenName: "",
			familyName: "",
			email : "",
			text : ""
		};
	},
	methods: {
		create: function() {
			let customer = {givenName:this.givenName,familyName:this.familyName,email:this.email};
			let state = this;
			console.log(customer);
			this.rest.post('/customer', customer).then(function (response) {
				console.log(response);
				state.text = response.status;
			}).catch(function (error) {
				console.log(error);
				state.text = error;
			});
		}
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
ul {
	list-style-type: none;
	padding: 0;
}

li {
	display: inline-block;
	margin: 0 10px;
}

a {
	color: #42b983;
}
</style>
