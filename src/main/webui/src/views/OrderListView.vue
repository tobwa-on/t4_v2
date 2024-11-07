<template>
	<div class="orderList">
		<OrderList />
	</div>
</template>

<script>
// @ is an alias to /src
import OrderList from '@/components/OrderList.vue'

export default {
	name: 'OrderListView',
	components: {
		OrderList
	}
}
</script>
