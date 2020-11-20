<template>
	<div id="coupondetails">
        <div class="album">
            <header class="header">
              <div class="header-item icon-back" @click='goBack' v-show="from==='weixin'?false:true"></div>
              <div class="header-item">
                <span v-show="isCollect === false" class="icon-collect" @click="collectCoupon(couponAllInfo.batchCouponCode)"></span>
                <span v-show="isCollect === true" class="icon-collect-solid" @click="collectCoupon(couponAllInfo.batchCouponCode)"></span>
            </div>
        </header>
        <swiper :imgSrcArr="couponImgList" v-if="couponImgList.length !== 0"></swiper>
             
        </div>
        <div class="rule">
            <div class="rule-wrapper">
                <div class="shopName">{{couponAllInfo.shopName}} <span v-if="couponAllInfo.remark != ''">|{{couponAllInfo.remark}}</span><span class="popularity">人气：{{couponAllInfo.popularity
                }}</span></div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='1'">
                    <!-- N元购 -->
                    <p>{{couponAllInfo.function}}</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='2'">
                    2
                    <p>满{{couponAllInfo.availablePrice}}元抵{{couponAllInfo.insteadPrice}}元</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='3'">
                    <!-- 抵用券 -->
                    <p>抵{{couponAllInfo.insteadPrice}}元</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='4'">
                    <!-- 折扣券 -->
                    <p>{{couponAllInfo.discountPercent}}折券</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='5'">
                    <!-- 实物券 -->
                    <p>{{couponAllInfo.function}}折</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='6'">
                    <!-- 体验券 -->
                    <p>{{couponAllInfo.function}}</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='7'">
                    <!-- 兑换券 -->
                    <p>{{couponAllInfo.function}}</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='8'">
                    <!-- 代金券 -->  
                    <p>￥{{couponAllInfo.payPrice}}元代{{couponAllInfo.insteadPrice}}元券</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='32'">
                    <!-- 注册送的抵扣券 -->
                    {{couponAllInfo.function}}
                    <p>满{{couponAllInfo.availablePrice}}元抵{{couponAllInfo.insteadPrice}}元</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='33'">
                    <!-- 送邀请人的抵扣券 -->
                    <p>抵{{couponAllInfo.insteadPrice}}元</p>
                </div>
                <div class="discription">满{{couponAllInfo.availablePrice}}元可用 | {{couponAllInfo.dayStartUsingTime
                }}-{{couponAllInfo.dayEndUsingTime}}可用</div>

            </div>
            <!-- <button @click="getCoupon">点击领取</button> -->
        </div>
        <codegroup :couponAllInfo="couponAllInfo" ref="codegroup"></codegroup>
        
        <div class="shopInfo">
            <div class="left">
                <div class="clock">
                    <div class="icon icon-clock"></div>
                    <div class="clocktext"><p>有效期：</p><p>{{couponAllInfo.startUsingTime
                    }}-{{couponAllInfo.expireTime}}</p></div>
                </div>
                <div class="location">
                    <div class="icon icon-location"></div>
                    <div class="locationtext"><p><span>{{couponAllInfo.city}}</span><span>{{couponAllInfo.street}}</span></p>
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="content"><a :href="'tel:' + couponAllInfo.tel"><p class="icon-phone"></p></a></div>
            </div>
        </div>
        <div class="shopInfoName" @click="toShopDetails(couponAllInfo.shopCode)">
            <div class="name">商户信息：{{couponAllInfo.shopName}} <span class="icon-back"></span></div>
        </div>
        <div class="ruleInfo">
            <p class="ruleInfoName">使用规则</p>
            <p class="name">有效期：</p>
            <p class="content">{{couponAllInfo.startUsingTime
                    }}至{{couponAllInfo.expireTime}}期间可用（2019-06-29至2019-09-30周六可用）</p>
            <p class="name">使用时间：</p>
            <p class="content">每天{{couponAllInfo.dayStartUsingTime
                }}至{{couponAllInfo.dayEndUsingTime}}</p>
            <p class="name">适用范围：</p>
            <p class="content">满{{couponAllInfo.availablePrice}}元即可使用</p>
        </div>
    </div>
</template>
<script>
import swiper from '@/components/swiper/swiper';
import { mapState, mapActions } from 'vuex';
import codegroup from '@/components/coupondetails/codegroup';
export default {
    name: 'coupondetails',
    data() {
        return {
            ok: true,
            // albumImg: '本店暂无图片',
            couponImgList: [],
            couponAllInfo: [],
            isCollect: false // 本页优惠券的收藏与否
        };
    },
    computed: {
        ...mapState([
            'nowCouponCode', 'CouponListInfo', 'global', 'isLogin', 'isBLR', 'from', 'zoneId'
        ])
    },
    beforeCreate() {
    },
    methods: {
        ...mapActions(['getCollectInfo']),
        goBack () {
            this.$router.go(-1);
        },
        getCouponDetail: function() { // 获取优惠券详情
            let that = this;
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"getUserCouponInfo","params":{"userCouponCode":"' + this.$route.params.nowUserCouponCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                console.log(result);
                this.couponAllInfo = result.data.result[0];
                var len = Object.keys(result.data.result[0].shopDecoration).length; // 获取轮播图数量
                for (var i = 0; i < len; i++) {
                    that.$data.couponImgList[i] = result.data.result[0].shopDecoration[i].imgUrl;
                };
                // 因为只有一个按钮，所以没必要像列表那样，每点击一次重新请求新的收藏列表，从而浪费资源，拿到正确回执之后直接修改按钮，所以这里只是首次进来时请求一次
                this.getCollectInfo().then((CouponListInfo) => {
                    this.filterIsCollect();
                });
                this.$refs.codegroup.getUserCouponNbr(this.couponAllInfo.batchCouponCode);
            });
        },
        collectCoupon: function(code) {
            let that = this;
            if (!this.isLogin) {
                this.$store.state.isBLR = 1;
                this.$store.state.showLoginBox = true;
                this.$vux.toast.text('请登录', 'bottom');
            } else {
                this.axios({
                    method: 'post',
                    url: this.global.hftcomShop,
                    data: '{"id":19,"jsonrpc":"2.0","method":"collectCoupon","params":{"batchCouponCode":"' + code + '","userCode":"' + this.$store.state.userCode + '","zoneId":"' + this.zoneId + '"}}',
                    contentType: 'application/json',
                    dataType: 'json'
                }).then((result) => {
                    if (result.data.result === 1) {
                        that.isCollect = true;
                        that.$vux.toast.text('已收藏', 'bottom');
                    } else if (result.data.result === 0) {
                        that.isCollect = false;
                        that.$vux.toast.text('已取消收藏', 'bottom');
                    } else {
                        that.$vux.toast.text('数据错误，请稍后重试', 'bottom');
                    }
                });
            }
        },
        toShopDetails: function(shopCode) {
            this.$router.push({ path: `/shopDetail/${shopCode}` });
        },
        filterIsCollect: function() { // 用于筛选收藏按钮的显示与否
            this.CouponListInfo.filter((element, index, self) => {
                if (this.couponAllInfo.batchCouponCode ===
 element.collectCouponCode) {
                    if (element.isCollect === '1') {
                        this.isCollect = true;
                    } else {
                        this.isCollect = false;
                    }
                }
            });
        }
    },
    created() {
        this.getCouponDetail();
    },
    updated() {
    },
    mounted() {

    },
    components: {
        swiper,
        codegroup
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#coupondetails
    background:#f0f0f0
    width:100%
    overflow-x:hidden
  .album
    position:relative
    width:100%
    height:3.8rem
    background-color:#f0f0f0
    background-size:contain
    text-align:center
    overflow:hidden
    .header
        position:absolute
        top:0
        left:0
        z-index:1
        height:1.48rem
        width:10.8rem
        line-height:1.48rem
        :nth-child(1)
          float:left
          color:#fff
          margin-left:0.3rem
          margin-top:0.15rem
          width:1.28rem
          height:1.28rem
          line-height:1.35rem
          background:#3e3e3e75
          border-radius:50%
          font-size:0.7rem
          font-weight:bold
        :nth-child(2)
          span
              float:right
              color:#fff
              margin-right:0.3rem
              margin-top:0.15rem
              width:1.28rem
              height:1.28rem
              line-height:1.5rem
              background:#3e3e3e75
              border-radius:50%
              // border:1px solid rgba(255,255,255,0.5)
              text-align:center
          .icon-collect
              font-size:0.7rem
              font-weight:bold
              color:#fff
          .icon-collect-solid
              font-size:0.6rem
              font-weight:bold
              color:#ffda62
  .rule
    width:100%
    // margin-top:0.1rem
    padding:0.4rem 0rem
    line-height:1rem
    background:#fff
    .rule-wrapper
        margin:0 auto
        padding:0.15rem 0.48rem
        width:8.8rem
        // border:1px solid #f64f4882
        // border-radius:3%
        // background:rgba(246,79,72,0.1)
        .shopName
            color:#7c7c7c
            font-size:0.44rem
            .popularity
                float:right
        .ruleText
            color:#f64f48
            font-size:0.72rem
            p
             font-weight:bold
        .discription
            font-size:0.44rem
  .shopInfoName
      // margin-top:0.1rem
      background:#fff
      .name
        margin:0rem 0.5rem 0 0.5rem
        padding:0.5rem 0.4rem
        font-size:0.5rem
        color:#525252
        border-top:1px #f0f0f0 solid
        .icon-back
            float:right
            transform:rotate(180deg)
            &:before
                color:#525252
  .shopInfo
    // margin-top:0.1rem
    padding:0.5rem 0.4rem
    background:#fff
    font-size:0.4rem
    color:#525252
    position:relative
    .left
        // height:1rem
        line-height:0.7rem
        .clock
            position:relative
            .icon
                position:absolute
                display:inline-block
                top:calc((100% - 0.5rem) / 2)
                width:1rem
            .clocktext
                display:inline-block
                width:6rem
                // height:2em
                margin:auto 0
                margin-left:1rem
        .location
            position:relative
            margin-top:0.18rem
            .icon
                position:absolute
                display:inline-block
                top:calc((100% - 0.5rem) / 2)
                width:1rem
            .locationtext
                // position:absolute
                left:1rem
                width:6rem
                // height:2em
                margin:auto 0
                margin-left:1rem
                display:inline-block
    .right
        position:absolute
        right:1.1rem
        top:calc((100% - 0.5rem) / 2)
        .content
            p
                display:inline-block
                padding-left:0.9rem
                border-left:solid 1px rgb(52,52,52)  
  .shopdetail
    margin-top:0.1rem
    background:#fff
    font-size:0.32rem
    color:#525252
    .name
        margin: 0rem auto
        height:1.3rem
        line-height:1.3rem
        width:4em
        color:#525252
        font-size:0.4rem
    .text
        height:1.3rem
        width:8em
        margin:0 auto
        font-size:0.28rem
    .content
        font-size:0.32rem
        line-height:0.63rem
        padding:0.2rem 0.69rem
        text-indent:2em
  .ruleInfo
      background:#fff
      width:100%
      margin-top:0.1rem
      line-height:1rem
      .ruleInfoName
          margin:0 auto
          padding:0.5rem
          font-size:0.5rem
          width:5em
          text-align:center
      .name
          padding:0 0.5rem
          font-size:0.44rem
          font-weight:bold
      .content
          padding: 0 0.9rem
          font-size:0.4rem
          &:before
              content:'• '
              font-weight:bold
</style>
