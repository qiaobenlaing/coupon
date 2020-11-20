// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
// 引用的核心文件
import Vue from 'vue';
import recharge from './recharge.vue';

import { ToastPlugin } from 'vux';
Vue.use(ToastPlugin);

import axios from 'axios';
axios.defaults.headers.post['Content-Type'] = 'application/json'; // 设置默认Content-Type
Vue.prototype.axios = axios;

// vuex
import store from '../../store';

// 引用路由
import router from './router/index.js';

new Vue({
    template: '<recharge/>',
    components: { recharge },
    store,
    router
}).$mount('#recharge');
