<template>
    <div id="myCoupon">
        <ul class="couponTabs">
            <li v-for="(item) in tabs" :key="item.id" :class="{active: item.isActive}" @click="showPage(item.id)">{{item.name}} <span v-if="item.isActive===true" class="rec"></span></li>
        </ul>
        <transition name="usercoupon">
            <div class="userCouponList" v-show="isLogin" v-infinite-scroll="loadMore" infinite-scroll-disabled="busy" infinite-scroll-distance="10">
               <largecoupon v-for="each in userCouponList" :index="each" :key="each.userCouponCode" :currentTab="currentId" :currentBtnText="currentBtnText">
               </largecoupon>
               <loadGif v-show="showLoadGif"></loadGif>
           </div>
       </transition>
           <div class="coupon-empty" v-show="userCouponList.length===0">
               暂无优惠券
           </div>
     <div class="background-text" v-show="!isLogin">
        <div class="login-back">
            <button class="login" v-show="!isLogin" @click="login">登录</button>&nbsp;&nbsp;<button class="login" v-show="!isLogin" @click="register">注册</button>
        </div>
        <center>{{tips}}</center>
    </div>
    <div class="barcode" v-show="barcodeDisplay">
        <div class="mask" @click = "closeBarcode"></div>
        <div class="code">
            <center class="text">{{currentBarcodeContent.shopName}}</center>
            <center class="tips" v-show="currentBarcodeContent.couponType==='4'">本优惠券直接使用即可，无需扫码</center>
            <barcode v-show="currentBarcodeContent.couponType!=='4'" v-bind:value="currentBarcodeContent.userCouponNbr" :options="{ lineColor: '#0275d8',width:2 }" tag="svg"></barcode>
            <div class="btn"><button @click = "closeBarcode">确定</button></div>
        </div>
    </div>
    <v-nav></v-nav>
</div>
</template>
<script>
import nav from '@/components/nav/nav.vue';
import largecoupon from '../../components/coupon/largecoupon';
import VueBarcode from 'vue-barcode';
import loadGif from '@/components/loadGif/loadGif.vue';
import { mapState, mapGetters } from 'vuex';
// import Services from '@/store/services';
export default{
    name: 'myCoupon',
    data() {
        return {
            tabs: [
                {
                    id: 0,
                    name: '未使用',
                    btnText: '立即使用',
                    status: '1',
                    isActive: true,
                    currentPage: 1,
                    totalCount: Number
                }, {
                    id: 1,
                    name: '已过期',
                    btnText: '已过期',
                    status: '3',
                    isActive: false,
                    currentPage: 1,
                    totalCount: Number
                }, {
                    id: 2,
                    name: '已使用',
                    btnText: '已使用',
                    status: '2',
                    isActive: false,
                    currentPage: 1,
                    totalCount: Number
                }],
            busy: true,
            currentId: 0,
            currentBtnText: '立即使用',
            barcodeDisplay: false,
            currentBarcodeContent: {},
            tips: '您还未登录，请登录',
            showLoadGif: true, // 动态图的加载
            isBottom: false // 是否加载完
        };
    },
    computed: {
        ...mapState(['userCode', 'userCouponList', 'latitude', 'isLogin']),
        ...mapGetters([
            // 'filterUserCouponList'
        ])
    },
    beforeCreate() {
    },
    created () {
        // if (Services.getLocal('myCouponCurrentId') !== undefined && Services.getLocal('myCouponCurrentId') !== null) {
        //     this.currentId = Services.getLocal('myCouponCurrentId');
        // }
        if (this.isLogin) {
            this.showPage(this.currentId);
        } else {

        }
    },
    methods: {
        login: function() {
            this.$store.state.isBLR = 1;
            this.$store.state.showLoginBox = true;
        },
        register: function() {
            this.$store.state.isBLR = 2;
            this.$store.state.showLoginBox = true;
        },
        showPage(id) {
            this.showLoadGif = true;
            this.currentId = id;
            console.log(this.currentId);
            // Services.setLocal('myCouponCurrentId', this.currentId);
            this.currentBtnText = this.tabs[this.currentId].btnText;
            this.$store.dispatch('fetchUserCouponList', {
                status: this.tabs[this.currentId].status,
                page: 1
            }).then((totalCount) => {
                this.tabs[this.currentId].totalCount = totalCount;
                this.tabs[this.currentId].currentPage ++;
                this.busy = false;
                this.showLoadGif = false;
            });
            // tab栏的样式变化控制
            for (var i = 0; i < 3; i++) {
                this.tabs[i].isActive = false;
            };
            this.tabs[this.currentId].isActive = true;
        },
        showBarcode(index) {
            this.currentBarcodeContent = index;
            this.barcodeDisplay = true;
        },
        closeBarcode() {
            this.barcodeDisplay = false;
        },
        loadMore() {
            this.busy = true;
            // console.log('我的优惠券滚动加载');
            // 一共页数
            // console.log(Math.ceil(this.tabs[this.currentId].totalCount / 10));
            // 目前第几页
            // console.log(this.tabs[this.currentId].currentPage);
            if (Math.ceil(this.tabs[this.currentId].totalCount / 10) >= this.tabs[this.currentId].currentPage && this.tabs[this.currentId].currentPage > 1) {
                this.$store.dispatch('fetchUserCouponList', {
                    status: this.tabs[this.currentId].status,
                    page: this.tabs[this.currentId].currentPage
                }).then((totalCount) => {
                    this.tabs[this.currentId].totalCount = totalCount;
                    this.tabs[this.currentId].currentPage++;
                    this.busy = false;
                });
            } else {
                this.busy = true;
                this.isBottom = true;
                this.showLoadGif = false;
            }
        }
    },
    components: {
        'v-nav': nav,
        largecoupon,
        'barcode': VueBarcode,
        loadGif
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#myCoupon
    position:absolute
    height:100%
    width:10.8rem
    background:rgb(240,240,240)
    .couponTabs
        display:flex
        height:1.68rem
        line-height:1.68rem
        text-align:center
        font-size:0.4rem
        background:rgb(246,79,72)
        color:#fff
        li
            flex:1
            &.active
                position:relative
                font-size:0.48rem
                font-weight:bold
                .rec
                    position:absolute
                    bottom:-0.01rem
                    left:calc((100% - 0.46rem) / 2)
                    width:0
                    height:0
                    border:0.23rem solid transparent
                    border-bottom:0.23rem solid rgb(240,240,240)
    .usercoupon-enter-active,.usercoupon-leave-active
        transition:opacity 2s
    .usercoupon-enter,.usercoupon-leave-to
        opacity:0
    .userCouponList
        margin-bottom:1.5rem
    .coupon-empty
        position:absolute
        top:50%
        left:calc((100% - 5.1em) / 2)
        color:#a7a7a7
        font-size:0.45rem
    .background-text
        position:absolute
        width:100%
        height:100%
        font-size:0.45rem
        background:#f3f3f3
        color:#a7a7a7
        .login-back
            margin:1rem auto
            margin-top:calc((100%-1em) / 2)
            margin-left:calc((100% - 3.8rem)/2)
            .login
                color:#FFF
                padding:0.1rem 0.5rem
                background:#f64f48
                border:#f64f48 1px solid
                border-radius:0.05rem
    .barcode
        .mask
            position:fixed
            top:0
            left:0
            width:100%
            height:100%
            background:rgba(91, 91, 91, 0.55)
            z-index:1
        .code
            position: fixed
            top: 2rem
            left: calc((100% - 9rem) / 2)
            padding: 0.7rem 1rem
            background: #fff
            border-radius: 0.1rem
            z-index:2
            .vue-barcode-element
                width:7rem
                height:4.5rem
            .text
                font-size:0.44rem
            .tips
                width:7rem
                margin:1rem 0
                font-size:0.34rem
            .btn
                button
                    width:100%
                    height:1rem
                    background:#f64f48
                    border:#f64f48
                    border-radius:0.1rem
                    color:#FFF
</style>
