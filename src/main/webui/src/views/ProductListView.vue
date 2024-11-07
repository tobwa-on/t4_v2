<template>
	<div class="productList">
		<ProductList />
		<AddProduct />
	</div>
</template>

<script>
// @ is an alias to /src
import ProductList from '@/components/ProductList.vue'
import AddProduct from '@/components/AddProduct.vue'

export default {
	name: 'ProductListView',
	components: {
		ProductList,
		AddProduct
	}
}
</script>
