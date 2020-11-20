<template>
	<div id="index">
		<v-header></v-header>
		<swiperclick :swiperinfo="swiperinfo"></swiperclick>
		<column :shopTypeList="shopTypeList"></column>
        <container class="container" :adList="adList"></container>
        <div class="line-wrapper">
          <div class="line">
             <span class="linetext">推荐优惠券</span>
         </div>
     </div>
     <div class="indexcoupon" v-infinite-scroll="loadMore" infinite-scroll-disabled="busy" infinite-scroll-distance="10">
        <indexcoupon v-for="each in couponList" :index="each" :key="each.batchCouponCode"></indexcoupon>
        <loadGif v-show="showLoadGif"></loadGif>
    </div>
    <v-nav></v-nav>
</div>
</template>
<script>
import nav from '@/components/nav/nav.vue';
import header from '@/components/index/header.vue';
import swiperclick from '@/components/swiper/swiperclick.vue';
import column from '@/components/index/column.vue';
import indexcoupon from '@/components/coupon/indexcoupon.vue';
import { mapState, mapActions } from 'vuex';
import loadGif from '@/components/loadGif/loadGif.vue';
import container from '@/components/index/container.vue';

export default {
    name: 'index',
    head() {
        return {
            title: '惠付券'
        };
    },
    beforeCreate() {
    },
    data() {
        return {
            adList: [], // 新闻区
            shopTypeList: [], // 栏目列表
            couponList: [], // 优惠券列表
            currentPage: 1, // 优惠券列表当前页码
            nextPage: 1, // 下一次应该请求的页码
            totalPage: Number,
            position: '',
            busy: true, // 触发无限加载插件的禁用true-禁止触发，false-启用触发
            showLoadGif: true, // 动态图的加载
            swiperinfo: [
                {
                    url: this.$store.state.global.hftcom + '/huiquan/static/img/a316d77207e3466e811d099d19e9a05886d442e14d086-qQwXw7_fw658.jpg'
                    // activityWeb: ''
                }
            ]
        };
    },
    computed: {
        ...mapState(['CouponListInfo', 'localCity', 'isChooseCity', 'global', 'weixin', 'zoneId'])
    },
    watch: {
        // 监听localCity的变化
        localCity: function(newval, oldval) {
            this.currentPage = 1;
            this.couponList = [];
            this.getshopTypeList();  // 分类信息
            this.getCouponList();    // 优惠券列表
            this.getcouponSwiper();  // 优惠券轮播图
            this.getCollectInfo();  // 收藏信息
        }
    },
    methods: {
        ...mapActions(['getCollectInfo']),
        getcouponSwiper() { // 轮播图
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"couponSwiper","params":{"city":"' + this.localCity + '","zoneId":"' + this.zoneId + '"}}',
                dataType: 'json'
            }).then((result) => {
                if (result.data.result.length >= 1) {
                    this.swiperinfo = result.data.result;
                    for (let index = 0; index < this.swiperinfo.length; index++) {
                        this.swiperinfo[index].url = this.swiperinfo[index].url;
                    }
                }
            });
        },
        getshopTypeList() {  // 获得栏目列表
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"getClientHomePage","params":{"city":"' + this.$store.state.localCity + '","zoneId":"' + this.zoneId + '"}}',
                dataType: 'json'
            }).then((result) => {
                this.$data.shopTypeList = result.data.result.shopTypeList;
            });
        },
        getCouponList() {  // 获得优惠券列表的方法
            this.showLoadGif = true;
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"listCoupon","params":{"couponType":"0","searchWord":"","longitude":"' + this.$store.state.longitude + '","latitude":"' + this.$store.state.latitude + '","page":"' + this.nextPage + '","city":"' + this.$store.state.localCity + '","userCode":"' + this.$store.state.userCode + '","zoneId":"' + this.zoneId + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (result.data.result.couponList.CouponListInfo) {
                    this.couponList = [...this.couponList, ...result.data.result.couponList.CouponListInfo];

                    this.currentPage = result.data.result.page;
                    this.nextPage = result.data.result.nextPage;
                    this.busy = false;
                }
                this.showLoadGif = false;
            });
        },
        getNewsList() {
            this.axios({
                method: 'post',
                url: '/Api/News',
                data: '{"id":19,"jsonrpc":"2.0","method":"newsList","params":{"city":"' + this.$store.state.localCity + '","zoneId":"' + this.zoneId + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                // this.adList = result.data.result;
                for (let i in result.data.result) {
                    this.adList.push(result.data.result[i]); // 属性
                }
            });
        },
        loadMore() {
            this.busy = true;
            if (this.nextPage > this.currentPage) {
                this.getCouponList();
            } else {
                this.busy = true;
                this.showLoadGif = false;
            }
        }
    },
    created () {
        this.getcouponSwiper(); // 获得轮播图
        this.getshopTypeList();  // 分类信息
        this.getCouponList();    // 优惠券列表
        this.getNewsList();
    },
    mounted() {
        this.getCollectInfo(); // 更新获得优惠券的收藏信息，用于筛选收藏按钮
    },
    components: {
        'v-nav': nav,
        'v-header': header,
        swiperclick,
        column,
        indexcoupon,
        loadGif,
        container
    }};
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
#index
  position:absolute
  height:100%
  width:10.8rem
  background:#f0f0f0
  overflow-x:hidden
  .line-wrapper
      width:100%
      height:1.1rem
      background:#fff
      margin-top:0.12rem
      .line
        position:relative
        width:14em
        height:0.55rem
        border-bottom:1px solid #cecece
        margin:0 auto
        & > .linetext
          position: absolute
          width: 8em
          top: 0.39rem
          left: calc((100% - 8em) / 2)
          background: #fff
          height: 2em
          font-size:0.32rem
          text-align: center
.indexcoupon
    margin-bottom 1.5rem
.container
    margin-top 10px
</style>

