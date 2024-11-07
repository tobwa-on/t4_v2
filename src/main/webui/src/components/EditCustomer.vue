<template>
	<h3>Kunde editieren</h3>
	<div class="editcustomer">
		<form v-on:submit.prevent="change">
			<p>Vorname: <input type="text" v-model="givenName" /></p>
			<p>Name: <input type="text" v-model="familyName" /></p>
			<p>Mail: <input type="text" v-model="email" /></p>
			<p><button type="submit">Ändern</button></p>
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
		change: function() {
			let customer = {givenName:this.givenName,familyName:this.familyName,email:this.email};
			let state = this;
			let id = this.$route.query.id;
			console.log(customer);
			this.rest.put(`/customer/${id}`, customer).then( (response) => {
				console.log(response);
				state.text = response.status;
			}).catch( (error) => {
				console.log(error);
				state.text = error;
			});
		},
		fetchData: function() {
			let id = this.$route.query.id;
			this.rest.get(`/customer/${id}`).then( (response) => {
				this.givenName = response.data.givenName;
				this.familyName = response.data.familyName;
				this.email = response.data.email;
			}).catch( (error) => {
				console.log(error);
			});
		}
	},
	beforeMount() {
		this.fetchData();
	},
}
</script>
