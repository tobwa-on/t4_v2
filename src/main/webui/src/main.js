import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import router from './router'

import './scss/styles.scss'
import { createVuetify } from 'vuetify';
import 'vuetify/styles';

import "@mdi/font/css/materialdesignicons.css"
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import Vue3Toastify from 'vue3-toastify';
import 'vue3-toastify/dist/index.css'; // Importiere das Stylesheet für Toastify

const vuetify = createVuetify({
	components,
	directives,
});

const rest = axios.create({
	baseURL: '/api',
});

rest.interceptors.request.use(
	function(config) {
		// automatically create Auth header
		let token = localStorage.getItem("user");
		if(token)
		{
			config.headers["Authorization"] = 'Bearer ' + token;
		}
		return config;
	}
);

const app = createApp(App)
	.use(router)
	.use(vuetify)
	.use(Vue3Toastify, {
		position: 'top-right',  // Beispiel für eine Position
		autoClose: 5000,        // Zeit, nach der Toasts automatisch geschlossen werden (in ms)
		hideProgressBar: false, // Anzeige des Fortschrittsbalkens
	});
app.use(VueAxios, {axios});

// define this.rest for using the properly configured axios instance
app.config.globalProperties.rest = rest;

app.mount('#vueapp');
