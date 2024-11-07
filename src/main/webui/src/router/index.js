import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
	{
		path: '/',
		name: 'home',
		component: HomeView
	},
	{
		path: '/about',
		name: 'about',
		// route level code-splitting
		// this generates a separate chunk (about.[hash].js) for this route
		// which is lazy-loaded when the route is visited.
		component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
	},
	{
		path: '/login',
		name: 'login',
		component: () => import(/* webpackChunkName: "login" */ '../views/LoginView.vue')
	},
	{
		path: '/login',
		name: 'login',
		component: () => import(/* webpackChunkName: "login" */ '../views/LoginView.vue')
	},
	{
		path: '/addcustomer',
		name: 'addcustomer',
		component: () => import(/* webpackChunkName: "addcustomer" */ '../views/AddCustomerView.vue')
	},
	{
		path: '/customerlist',
		name: 'customerlist',
		component: () => import(/* webpackChunkName: "customerlist" */ '../views/CustomerListView.vue')
	},
	{
		path: '/editcustomer',
		name: 'editcustomer',
		component: () => import(/* webpackChunkName: "editcustomer" */ '../views/EditCustomerView.vue')
	},
	{
		path: '/productlist',
		name: 'productlist',
		component: () => import(/* webpackChunkName: "productlist" */ '../views/ProductListView.vue')
	},
	{
		path: '/addproduct',
		name: 'addproduct',
		component: () => import(/* webpackChunkName: "addproduct" */ '../views/AddProductView.vue')
	},
	{
		path: '/orderlist',
		name: 'orderlist',
		component: () => import(/* webpackChunkName: "orderlist" */ '../views/OrderListView.vue')
	},
	{
		path: '/addorder',
		name: 'addorder',
		component: () => import(/* webpackChunkName: "addorder" */ '../views/AddOrderView.vue')
	},
	{
		path: '/editindprod',
		name: 'editindprod',
		component: () => import(/* webpackChunkName: "editindprod" */ '../views/EditIndProdView.vue')
	},
]

const router = createRouter({
	history: createWebHistory(process.env.BASE_URL),
	routes
})

export default router
