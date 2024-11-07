<template>
	<h3>Produkt editieren</h3>
	<div class="editindprod">
		<form v-on:submit.prevent="change">
			<p>Seriennummer: <input type="text" v-model="indprod.serialNumber" /></p>
			<p>Ext. Id.: <input type="text" v-model="indprod.externalId" /></p>
			
			<ReadingsList :indprod="indprod"/>
			
			<p><button type="submit">Ändern</button></p>
		</form>
		<p>Result: {{ text }}</p>
	</div>
</template>

<script>
import ReadingsList from './ReadingsList.vue';

export default {
	components: {
		ReadingsList,
	},
	data() {
		return {
			indprod: {},
			text : ""
		};
	},
	methods: {
		change: function() {
			let indprod = this.indprod;
			let state = this;
			let id = this.$route.query.id;
			console.log(indprod);
			this.rest.put(`/indprod/${id}`, indprod).then( (response) => {
				console.log(response);
				state.text = response.status;
			}).catch( (error) => {
				console.log(error);
				state.text = error;
			});
		},
		fetchData: function() {
			let id = this.$route.query.id;
			this.rest.get(`/indprod/${id}`).then( (response) => {
				this.indprod = response.data;
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
