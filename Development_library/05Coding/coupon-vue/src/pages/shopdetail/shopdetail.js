// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
// 引用的核心文件
import Vue from 'vue';
import shopdetail from './shopdetail';
// vux的toastvux的Confirm
import { ToastPlugin, ConfirmPlugin, LoadingPlugin } from 'vux';
Vue.use(ToastPlugin);
Vue.use(ConfirmPlugin);
Vue.use(LoadingPlugin);

import axios from 'axios';
axios.defaults.headers.post['Content-Type'] = 'application/json'; // 设置默认Content-Type
Vue.prototype.axios = axios;

// icon样式
import '../../common/stylus/index.styl';
// vuex
import store from '../../store';
// 引用路由
import router from './router/index.js';
// 使用全局自定义组件
// 使用懒加载插件
import VueLazyload from 'vue-lazyload';
Vue.use(VueLazyload, {
    error: store.state.global.hftcom + '/huiquan/static/img/null-page-draw.png', // 这个是请求失败后显示的图片
    loading: store.state.global.hftcom + '/huiquan/static/img/loading.gif', // 这个是加载的loading过渡效果
    try: 3 // 这个是加载图片数量
});

// 使用vue无限加载插件
import infiniteScroll from 'vue-infinite-scroll';
Vue.use(infiniteScroll);

// 导入轮播图插件
// import VueAwesomeSwiper from 'vue-awesome-swiper';
// Vue.use(VueAwesomeSwiper);
require('swiper/dist/css/swiper.css');

new Vue({
    template: '<App/>',
    components: { shopdetail },
    router,
    store
}).$mount('#shopdetail');
