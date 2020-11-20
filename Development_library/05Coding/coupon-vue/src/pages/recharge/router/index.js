import Vue from 'vue';
import VueRouter from 'vue-router';
// vuex
import store from '../../../store';

Vue.use(VueRouter);
const routes = [
];
const router = new VueRouter({
    // base: '/huiquan/',
    mode: 'history',
    linkActiveClass: 'active',
    routes
});
router.beforeEach((to, from, next) => {  // 这是导航守卫
    if (to.meta.title) {
        document.title = to.meta.title;
    };
    var query = {};
    query.to = to.query;
    query.from = from.query;
    // 初始化，若有cookie则免登录
    store.dispatch('initLogin').then(() => {

    }).catch(() => {
        store.dispatch('loginApp', query).then(() => {
        }).catch(() => {
        });
    });
    next();
});
export default router;
