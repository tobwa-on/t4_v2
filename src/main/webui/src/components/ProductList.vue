<template>
	<h3>Produktliste</h3>
	<div class="productList">
		<table class="table table-striped" v-if="products.length">
			<thead>
				<tr>
					<th scope="col">Name</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="p in products" v-bind:key="p.id">
					<td>{{ p.name }}</td>
					<td><button @click="deleteProd(p)">X</button></td>
				</tr>
			</tbody>
		</table>
		<p v-else>Keine Daten.</p>
	</div>
</template>

<script>
export default {
	data() {
		return {
			products: [],
		};
	},
	methods: {
		deleteProd(prod) {
			let state = this;
			this.rest.delete(`/products/${prod.id}`).then(function (response) {
				console.log(response);
				state.refreshData();
			}).catch(function (error) {
				console.log(error);
				state.text = error;
			});
		},
		refreshData() {
			this.products = [];
			this.fetchData();
		},
		fetchData: function() {
			let products = this.products;
			this.rest.get('/products/all').then(function (response) {
				response.data.forEach(item => {
					products.push(item);
				})
			}).catch(function (error) {
				console.log(error);
			});
		},
	},
	beforeMount() {
		this.fetchData();
	},
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
