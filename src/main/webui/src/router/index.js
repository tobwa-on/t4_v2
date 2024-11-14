import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
	{
		path: '/',
		name: 'home',
		component: HomeView
	},
	{
		path: '/favorites',
		name: 'favorites',
		component: () => import(/* webpackChunkName: "login" */ '../views/FavoritesView.vue')
	},
	{
		path: '/login',
		name: 'login',
		component: () => import(/* webpackChunkName: "login" */ '../views/LoginView.vue')
	},
	{
		path: '/register',
		name: 'register',
		component: () => import(/* webpackChunkName: "login" */ '../views/RegisterView.vue')
	},
	{
		path: '/changepassword',
		name: 'changepassword',
		component: () => import(/* webpackChunkName: "login" */ '../views/ChangePasswordView.vue')
	},
	{
		path: '/watched',
		name: 'watched',
		component: () => import(/* webpackChunkName: "login" */ '../views/WatchedView.vue')
	},
	{
		path: '/watchlist',
		name: 'watchlist',
		component: () => import(/* webpackChunkName: "login" */ '../views/WatchlistView.vue')
	},
	{
		path: '/changepassword',
		name: 'changepassword',
		component: () => import(/* webpackChunkName: "login" */ '../views/ChangePasswordView.vue')
	},
	{
		path: '/usermanagement',
		name: 'usermanagement',
		component: () => import(/* webpackChunkName: "login" */ '../views/UserManagementView.vue')
	},

]

const router = createRouter({
	history: createWebHistory(process.env.BASE_URL),
	routes
})

export default router
