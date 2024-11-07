<template>
	<div class="home">
		<AddOrder />
	</div>
</template>

<script>
// @ is an alias to /src
import AddOrder from '@/components/AddOrder.vue'

export default {
	name: 'AddOrderView',
	components: {
		AddOrder
	}
}
</script>
