<template>
	<h4>Werte</h4>
	<div class="readingsList">
		<table class="table table-striped" v-if="indprod.readings && indprod.readings.length">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Wert</th>
					<th scope="col">Einheit</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="(r, index) in indprod.readings" v-bind:key="index">
					<td><input type="text" v-model="r.name" /></td>
					<td><input type="text" v-model="r.value" /></td>
					<td><input type="text" v-model="r.unit" /></td>
				</tr>
			</tbody>
		</table>
		<p v-else>Keine Daten.</p>
		<button @click="addLine">+</button>
	</div>
</template>

<script>
export default {
	data() {
		return {
		};
	},
	props: ["indprod"],
	methods: {
		addLine: function() {
			// eslint-disable-next-line
			this.indprod.readings.push({name:"", value:"", unit:""});
		},
	},
}
</script>
