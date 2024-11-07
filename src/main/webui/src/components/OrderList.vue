<template>
	<h3>Bestellungen</h3>
	<div class="orderList">
		<table class="table table-striped" v-if="orders.length">
			<thead>
				<tr>
					<th scope="col">Kunde</th>
					<th scope="col">Produkt</th>
					<th scope="col">Seriennummer</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="o in orders" v-bind:key="o.id">
					<td>{{ o.customer.givenName + " " + o.customer.familyName }}</td>
					<td>{{ o.products[0].product.name }}</td>
					<td><router-link class="nav-link" :to="{name: 'editindprod', query: {id: o.products[0].id}}">{{ o.products[0].serialNumber }}</router-link></td>
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
			orders: [],
		};
	},
	methods: {
		fetchData: function() {
			let orders = this.orders;
			this.rest.get('/order/all').then(function (response) {
				response.data.forEach(item => {
					orders.push(item);
				})
			}).catch(function (error) {
				console.log(error);
			});
		}
	},
	beforeMount() {
		this.fetchData();
	},
}
</script>
