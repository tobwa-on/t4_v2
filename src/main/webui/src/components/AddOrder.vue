<template>
	<h3>Bestellung anlegen</h3>
	<div class="addorder">
		<form v-on:submit.prevent="create">
			<p>Kunde:
				<select v-model="customer">
					<option v-for="(c, index) in customers" :value="c" :key="index">{{ c.givenName + " " + c.familyName }}</option>
				</select>
			</p>
			<p>Produkt:
				<select v-model="product">
					<option v-for="(p, index) in products" :value="p" :key="index">{{ p.name }}</option>
				</select>
			</p>
			<p>Seriennummer: <input type="text" v-model="serialNumber" /></p>
			<p><button type="submit">Anlegen</button></p>
		</form>
		<p>Result: {{ text }}</p>
	</div>
</template>

<script>
export default {
	data() {
		return {
			customers: [],
			products: [],
			customer: {},
			product: {},
			serialNumber: "",
			text : "",
		};
	},
	methods: {
		create: function() {
			let order = {products:[{serialNumber:this.serialNumber, product:this.product}], customer:this.customer};
			let state = this;
			console.log(order);
			this.rest.post('/order', order).then(function (response) {
				console.log(response);
				state.text = response.status;
			}).catch(function (error) {
				console.log(error);
			});
		},
		fetchData: function() {
			let customers = this.customers;
			this.rest.get('/customer/all').then(function (response) {
				response.data.forEach(item => {
					customers.push(item);
				})
			}).catch(function (error) {
				console.log(error);
			});

			let products = this.products;
			this.rest.get('/products/all').then(function (response) {
				response.data.forEach(item => {
					products.push(item);
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
