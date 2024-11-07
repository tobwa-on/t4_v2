<template>
	<h3>Produkt anlegen</h3>
	<div class="addproduct">
		<form v-on:submit.prevent="create">
			<p>Name: <input type="text" v-model="name" /></p>
			<p><button type="submit">Anlegen</button></p>
		</form>
		<p>Result: {{ text }}</p>
	</div>
</template>

<script>
export default {
	data() {
		return {
			name: "",
			text : ""
		};
	},
	methods: {
		create: function() {
			let product = {name:this.name};
			let state = this;
			console.log(product);
			this.rest.post('/products', product).then(function (response) {
				console.log(response);
				state.text = response.status;
			}).catch(function (error) {
				console.log(error);
			});
		}
	}
}
</script>
