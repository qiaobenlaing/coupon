import Vue from 'vue';
import Vuex from 'vuex';
import actions from './actions';
import getters from './getters';
import mutations from './mutations';

Vue.use(Vuex);
const store = new Vuex.Store({
    state: {
        appType: process.env.APP_TYPE, // 版本0-单机版，1-运营版
        isLogin: false, // 判断用户是否登录的标识
        isBLR: 0, // 用于判断用户注册登录绑定行为,0-bind,1-login,2-register
        showLoginBox: false, // 用于全局登陆框的显示与否
        mobileNbr: '',
        password: '',
        userCode: '',
        tokenCode: '',
        localCity: '',
        latitude: '',
        longitude: '',
        phoneNum: '',
        isChooseCity: false, // 用于判别用户是否自己选择城市
        zoneId: '2', // 商圈id
        from: '', // 判别跳转来源，为空时，是正常公众号入口进入，weixin是优惠券卡券使用入口进入
        weixin: {
            openId: '',
            code: '',
            htmldata: ''
        },
        global: {
            staticSrc: process.env.BASE_URL, // 静态资源目录
            hftcom: process.env.BASE_URL, // 域名
            hftcomImgSrc: process.env.IMG_SRC, // 图片前缀，一般与域名相同
            hftcomApi: process.env.API_ROOT_DICT, // 接口前缀，一般情况下与域名相同
            hftcomClient: '/Api/Client',
            hftcomShop: '/Api/Shop',
            hftcomWeixinCard: '/Api/WeiXinCard',
            hftcomOpenID: '/Admin/OpenId/getUserOpenId',
            hftcomGetUserInfo: '/Api/Shop',
            Daipay: '/Admin/Lcy/Daipay',
            Dipay: '/Admin/Lcy/DiPay',
            getAT: '/Admin/OpenId/getAccessToken'
        },
        CouponListInfo: [], // 已收藏的优惠券信息
        collectShopList: [], // 收藏的商户列表（也用来筛选收藏按钮）
        userCouponList: [], // 我的优惠券列表
        isbool: true, // 首页使用的判断
        searchIsBool: true,
        nearbyIsBool: true, // 附近页面使用的判断
        nearbyShopList: [],  // 附近页面使用的shop集合
        searchConponIsBool: true, // 优惠券查询页使用的判断
        searchConponList: [], // 优惠券查询页面使用的shop集合
        currentOrderInfo: [], // 当前正在提交订单的优惠券信息
        isCancelOrder: false
    },
    getters,
    mutations,
    actions
});
export default store;
