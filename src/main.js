import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import VueNumberInput from '@chenfengyuan/vue-number-input';

Vue.use(VueNumberInput);

Vue.config.productionTip = false;
Vue.prototype.$BaseURL = 'http://localhost:23700/';
Vue.prototype.$axios = axios;

new Vue({
    router,
    render: h => h(App)
}).$mount('#app');