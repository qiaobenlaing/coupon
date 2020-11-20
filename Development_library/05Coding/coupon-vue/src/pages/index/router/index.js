import Vue from 'vue';
import VueRouter from 'vue-router';
// vuex
import store from '../../../store';
// 首页
import index from '@/components/index/index.vue';
const couponSearch = resolve => require(['@/components/search/couponSearch.vue'], resolve);
const location = resolve => require(['@/components/location/cmp-location.vue'], resolve);
// 商户
const shops = resolve => require(['@/components/shops/shops'], resolve);
const shopdetail = resolve => require(['@/components/shopdetail/shopdetail'], resolve);
// 优惠券
const myCoupon = resolve => require(['@/components/mycoupon/mycoupon'], resolve);
const indexcoupondetails = resolve => require(['@/components/coupondetails/indexcoupondetails'], resolve);
const coupondetails = resolve => require(['@/components/coupondetails/coupondetails'], resolve);
// 我的信息页相关页面
const myInformation = resolve => require(['@/components/myinformation/myinformation'], resolve);
const aboutUs = () => import('@/components/myinformation/aboutus');
const changeMyInfo = () => import('@/components/myinformation/changemyinfo');
const changePassword = () => import('@/components/myinformation/changepassword');
const cooperation = () => import('@/components/myinformation/cooperation');
const myCollection = () => import('@/components/myinformation/mycollection');
const myvipcard = () => import('@/components/myinformation/myvipcard');
const sassmyvipcard = () => import('@/components/myinformation/sassmyvipcard');
const shop = () => import('@/components/myinformation/shop');

// 订单
const order = () => import('@/components/myinformation/order');
const submitorder = () => import('@/components/order/submitorder.vue');
const orderDetails = () => import('@/components/order/orderdetails.vue');
const payResult = () => import('@/components/pay/payresult.vue');
const refund = () => import('@/components/pay/refund.vue'); // 这是退款页面
const ksubmit = () => import('@/components/pay/ksubmit.vue');
const gotopay = () => import('@/components/pay/gotopay.vue');

// 买单
const accounts = () => import('@/components/accounts/accounts.vue');

// 结账
const settlement = () => import('@/components/accounts/settlement.vue');

// 支付渠道
const payChannel = () => import('@/components/payChannel/payChannel.vue');

// 卡信息
const cardInfo = () => import('@/components/payChannel/cardInfo.vue');

// 确认授权
const signingAuthorization = () => import('@/components/payChannel/signingAuthorization.vue');

// 签约银行
const contractPage = () => import('@/components/payChannel/contractPage.vue');

// 协议页面
const agreementInfo = () => import('@/components/payChannel/agreementInfo.vue');

const NotFoundComponent = () => import('@/components/other/NotFoundComponent.vue');

Vue.use(VueRouter);
const routes = [
  { path: '*', component: NotFoundComponent },
  {path: '/index', component: index, alias: '/', meta: {title: '惠付券'}},
  {path: '/couponDetails/:nowUserCouponCode', component: coupondetails, meta: {title: '优惠券详情'}},
  {path: '/accounts', component: accounts, meta: {title: '买单'}},
  {path: '/myCoupon', component: myCoupon, meta: {title: '我的优惠券'}},
  {path: '/payChannel', component: payChannel, meta: {title: '支付渠道'}},
  {path: '/agreementInfo', component: agreementInfo, meta: {title: '协议'}},
  {path: '/signingAuthorization/:cardId', component: signingAuthorization, meta: {title: '确认授权'}},
  {path: '/cardInfo/:cardId', component: cardInfo, name: 'cardInfo', meta: {title: '支付渠道'}},
  {path: '/contractPage/:cardId', component: contractPage, name: 'contractPage', meta: {title: '签约银行'}},
  {path: '/myInformation', component: myInformation, meta: {title: '个人信息页'}},
  {path: '/aboutUs', component: aboutUs, meta: {title: '关于我们'}},
  {path: '/changeMyInfo', component: changeMyInfo, meta: {title: '修改个人信息'}},
  {path: '/changePassword', component: changePassword, meta: {title: '修改密码'}},
  {path: '/cooperation', component: cooperation, meta: {title: '合作'}},
  {path: '/myCollection', component: myCollection, meta: {title: '收藏'}},
  {path: '/order', component: order, meta: {title: '订单'}},
  {path: '/shop', component: shop, meta: {title: '微商城'}},
  {path: '/myvipcard', component: myvipcard, meta: { title: '会员卡' }},
  {path: '/sassmyvipcard', component: sassmyvipcard, meta: { title: '会员卡' }},
  {path: '/orderDetails/:consumeCode', component: orderDetails, meta: {title: '订单详情'}},
  {path: '/submitorder/:batchCouponCode', component: submitorder, name: 'submitorder', meta: {title: '提交订单'}},
  {path: '/settlement/:storeCode', name: 'settlement', component: settlement, meta: {title: '买单'}},
  {path: '/gotopay', component: gotopay, name: 'gotopay', meta: {title: '去支付'}},
  {path: '/ksubmit/:userCouponCode', component: ksubmit, meta: {title: '买单'}},
  {path: '/payResult', component: payResult, meta: {title: '支付结果'}},
  {path: '/refund/:batchCouponCode', component: refund, meta: {title: '申请退款'}},
    {
        path: '/shopDetail/:shopCode',
        name: 'shopDetail',
        component: shopdetail,
        meta: {
            keepAlive: false
        }},
    {path: '/shops', component: shops, meta: {title: '附近'}},
    {path: '/couponSearch', component: couponSearch, meta: {title: '优惠券'}},
    {path: '/location', component: location, meta: {title: '开通城市列表'}},
    {path: '/indexCouponDetails/:nowBatchCouponCode', component: indexcoupondetails}
];

const router = new VueRouter({
    base: process.env.APP_ROUTER_BASE,
    mode: 'history',
    linkActiveClass: 'active',
    routes
});
router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title;
    };
    var query = {};
    query.to = to.query;
    query.from = from.query;
    // 初始化，若有cookie则免登录
    store.dispatch('initLogin').then(() => {
    }).catch(() => {
    });
    store.dispatch('loginApp', query).then(() => {
    }).catch(() => {
    });
    next();
});
export default router;
