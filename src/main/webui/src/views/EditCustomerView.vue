<template>
	<div class="editcustomer">
		<EditCustomer />
	</div>
</template>

<script>
// @ is an alias to /src
import EditCustomer from '@/components/EditCustomer.vue'

export default {
	name: 'EditCustomerView',
	components: {
		EditCustomer
	}
}
</script>
