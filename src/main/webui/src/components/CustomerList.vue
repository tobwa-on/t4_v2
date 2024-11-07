<template>
	<h3>Kundenliste</h3>
	<div class="customerList">
		<table class="table table-striped" v-if="customers.length">
			<thead>
				<tr>
					<th scope="col">Vorname</th>
					<th scope="col">Nachname</th>
					<th scope="col">eMail</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="c in customers" v-bind:key="c.id">
					<td><router-link class="nav-link" :to="{name: 'editcustomer', query: {id: c.id}}">{{ c.givenName }}</router-link></td>
					<td><router-link class="nav-link" :to="{name: 'editcustomer', query: {id: c.id}}">{{ c.familyName }}</router-link></td>
					<td><router-link class="nav-link" :to="{name: 'editcustomer', query: {id: c.id}}">{{ c.email }}</router-link></td>
					<td><button @click="deleteCustomer(c)">X</button></td>
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
			customers: [],
		};
	},
	methods: {
		deleteCustomer(customer) {
			let state = this;
			this.rest.delete(`/customer/${customer.id}`).then(function (response) {
				console.log(response);
				state.refreshData();
			}).catch(function (error) {
				console.log(error);
			});
		},
		refreshData() {
			this.customers = [];
			this.fetchData();
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
		}
	},
	beforeMount() {
		this.fetchData();
	},
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
	margin: 40px 0 0;
}

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
