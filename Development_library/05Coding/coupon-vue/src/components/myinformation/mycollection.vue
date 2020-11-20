<template>
    <div id="mycollection">
      <header2fix :title="'我的收藏'"></header2fix>    
      <ul class="couponTabs">
        <li v-for="(item) in tabs" :key="item.id" :class="{active: item.isActive}" @click="changePage(item.id)">{{item.name}}<span v-if="item.isActive===true" class="rec"></span></li>
    </ul>
    <transition name="fade">
        <div class="shoplist" v-show="tabs[0].isActive">
          <shoplist v-show="shopList!=={}"  v-for="(index) in shopList" :key = "index.shopCode"
          :eachShopList='index'></shoplist>
          <div class="shoplist-empty" v-show="shopList.length===0">您还没有收藏任何商家</div>
      </div>
      </transition>
  <transition name="fade">
  <div class="couponlist" v-show="tabs[1].isActive">
      <indexcoupon v-show="couponList!=={}" v-for="each in couponList" :index="each" :key="each.batchCouponCode ":CouponListInfo="CouponListInfo"></indexcoupon>
      <div class="shoplist-empty" v-show="couponList.length===0">您还没有收藏任何优惠券</div>
  </div>
  </transition>
</div>
</template>
<script>
import shoplist from '@/components/shoplist/shoplist';
import indexcoupon from '@/components/coupon/indexcoupon';
import nav from '../../components/nav/nav.vue';
import header2fix from '@/components/header/header2fix';
import { mapState, mapGetters, mapActions } from 'vuex';
export default{
    name: 'mycollection',
    data() {
        return {
            tabs: [
                {
                    id: 0,
                    name: '商家',
                    isActive: true
                }, {
                    id: 1,
                    name: '优惠券',
                    isActive: false
                }],
            currentTab: 0,
            shopList: [],
            couponList: []
        };
    },
    computed: {
        ...mapState(['CouponListInfo', 'global', 'zoneId', 'collectShopList', 'userCode']),
        ...mapGetters([
            // 'filterUserCouponList'
        ])
    },
    beforeCreate() {

    },
    methods: {
        ...mapActions(['getCollectShop', 'getCollectInfo']),
        loadShopPage() { // 商家列表
            this.getCollectShop().then(() => {
                this.shopList = this.collectShopList;
            });
        },
        loadCouponPage() { // 获取收藏的优惠券列表
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"showCoupon","params":{"userCode":"' + this.userCode + '", "zoneId":"' + this.zoneId + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (result.data.result === 'null' || result.data.result === null) {
                    this.couponList = [];
                } else {
                    var newlist = [];
                    for (var i = 0; i < result.data.result.length; i++) {
                        if (result.data.result[i][0] === undefined) {
                            result.data.result[i][0] = {'collectCouponCode': ''};
                        }
                        if (result.data.result[i][0]['collectCouponCode'] === '' || result.data.result[i][0]['collectCouponCode'] === undefined) {
                        } else {
                            result.data.result[i][0]['batchCouponCode'] = result.data.result[i][0]['collectCouponCode'];
                            newlist.push(result.data.result[i][0]);
                        }
                    }
                    this.couponList = newlist;
                }
            });
            this.getCollectInfo(); // 更新获得优惠券的收藏信息，用于筛选收藏按钮
        },
        changePage(id) {
            if (id === 0) {
                this.loadShopPage();
            } else {
                this.loadCouponPage();
            }
            for (var i = 0; i < 2; i++) {
                this.$data.tabs[i].isActive = false;
            };
            this.$data.tabs[id].isActive = true;
        },
        goBack () {
            this.$router.go(-1);
        }
    },
    created () {
        this.loadCouponPage();
        this.loadShopPage();
    },
    components: {
        'v-nav': nav,
        shoplist,
        indexcoupon,
        header2fix
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#mycollection
    position:absolute
    width:100%
    height:100%
    background:#f0f0f0
    padding-bottom:0.1rem
    .couponTabs
        position:fixed
        top:1.2rem
        left:0rem
        width:100%
        height:1.2rem
        line-height:1.2rem
        text-align:center
        font-size:0.4rem
        background:rgb(246,79,72)
        color:#fff
        z-index:1
        li
            width:5.38rem
            float:left
            &.active
                position:relative
                font-size:0.48rem
                font-weight:bold
                .rec
                    position:absolute
                    bottom:-0.04rem
                    left:calc((100% - 0.46rem) / 2)
                    width:0
                    height:0
                    border:0.23rem solid transparent
                    border-bottom:0.23rem solid rgb(240,240,240)
    .fade-enter-active
        transition: opacity 0.5s
    .fade-enter
        opacity: 0
    .shoplist
        margin-top:2.5rem
        .shoplist-empty
            position:absolute
            top:50%
            left:calc((100% - 11em) / 2)
            color:#a7a7a7
            font-size:0.45rem
    .couponlist
        margin-top:2.5rem
        .shoplist-empty
            position:absolute
            top:50%
            left:calc((100% - 11em) / 2)
            color:#a7a7a7
            font-size:0.45rem
</style>