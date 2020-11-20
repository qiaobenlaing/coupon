<template>
	<div id="shopdetail">
       <div class="album">
        <header class="header">
          <div class="header-item icon-back" @click='goBack'></div>
          <div class="header-item">
            <span v-show="isCollect ===false" class="icon-collect" @click="collectShop(shopAllInfo.shopInfo.shopCode)"></span>
            <span v-show="isCollect === true" class="icon-collect-solid theme-red" @click="collectShop(shopAllInfo.shopInfo.shopCode)"></span>
        </div>
    </header>
    <swiper v-if="shopImgList.length !== 0" :imgSrcArr="shopImgList"></swiper>
        </div>
        <div class="shopInfoWrapper">
            <p class="shopName">{{shopAllInfo.shopInfo.shopName}} <span class="popularity">人气：{{shopAllInfo.shopInfo.popularity}}</span></p>
            <div class="shopInfo">
            <div class="left">
                <div class="clock" v-show="shopAllInfo.shopInfo.businessHours!==null">
                    <div class="icon icon-clock"></div>
                    <div class="clocktext"><p v-for="time in shopAllInfo.shopInfo.businessHours">{{time.open}}-{{time.close}}</p></div>
                </div>
                <div class="location">
                    <div class="icon icon-location"></div>
                    <div class="locationtext"><p><span>{{shopAllInfo.shopInfo.city}}</span><span>{{shopAllInfo.shopInfo.district}}</span><span>{{shopAllInfo.shopInfo.street}}</span></p>
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="content"><a :href="'tel:' + shopAllInfo.shopInfo.tel"><p class="icon-phone"></p></a></div>
            </div>
            </div>
        </div>
        <!-- <div class="vipcard" v-show="viptype!==1">
            <div class="left">
                <div class="logo">
                    <span v-show="viptype===0">您有一张会员卡未领取!</span>
                    <span v-show="viptype===2">会员卡可添加到微信卡包!</span>
                </div>
                <div class="vipinfo">会员卡专享:九折优惠</div>
            </div>
            <div class="right" @click="getvipcard">
                <span v-show="viptype===0">领取会员卡</span>
                <span v-show="viptype===2 && isadding!==shopAllInfo.shopInfo.shopCode">添加会员卡</span>
                <transition name="rotate">
                    <span class="icon-spinner8" v-show="isadding===shopAllInfo.shopInfo.shopCode"></span>
                </transition>
            </div>
        </div> -->
        <div class="mask" v-show="showmask">
          <!-- <div class="toCard">
          </div> -->
        </div>
        <div class="shopCoupon">
            <p class="name">优惠券</p>
            <p v-if="shopAllInfo.couponList.shopCoupon.length === 0" class="text">本店暂无可领优惠券</p>
            <indexcoupon v-if="shopAllInfoFlag" v-for="each in shopAllInfo.couponList.shopCoupon" :index="each" :key="each.batchCouponCode"></indexcoupon>
        </div>

        <div class="shopdetail">
            <p class="name">商家详情</p>
            <p v-if="shopAllInfo.shopInfo.shortDes === ''" class="text">本店暂无商家详情</p>
            <p class="content">{{shopAllInfo.shopInfo.shortDes}}</p>
        </div>
	</div>
</template>
<script>
import indexcoupon from '../../components/coupon/indexcoupon.vue';
import swiper from '@/components/swiper/swiper';
import {mapState, mapActions} from 'vuex';
export default {
    name: 'shopdetail',
    data() {
        return {
            ok: true,
            albumImg: '本店暂无图片',
            shopImgList: [],
            shopAllInfo: {
                couponList: {
                    shopCoupon: []
                },
                shopInfo: {
                    'shopName': ''
                }
            },
            shopDecoList: [],
            shopAllInfoFlag: false, // 优惠券显示与否
            isCollect: false, // 是否收藏
            viptype: 1, // false(0)-未领取 1-已领取已添加 2-已领取未添加
            isadding: '',
            showmask: false
        };
    },
    computed: {
        ...mapState(['collectShopList', 'global', 'userCode', 'isLogin', 'zoneId'])
    },
    beforeCreate() {
    },
    methods: {
        ...mapActions(['getCollectShop', 'getCollectInfo']),
        goBack () {
            this.$router.go(-1);
        },
        getShopDetail: function() { // 获取商户详情
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"getShopInfo","params":{"shopCode":"' + this.$route.params.shopCode + '","userCode":"' + this.$store.state.userCode + '","longitude":' + this.$store.state.longitude + ',"latitude":' + this.$store.state.latitude + '}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                this.shopAllInfo = result.data.result;
                this.shopAllInfoFlag = true;
                    // 收藏按钮的筛选
                if (this.collectShopList.length !== 0) {
                    this.collectShopList.filter((element, index, self) => {
                        if (this.shopAllInfo.shopInfo.shopCode === element.shopCode) {
                            this.isCollect = true;
                        }
                    });
                }
                this.showvipcard();
            });
        },
        getShopDecoration: function() { // 获取商户装饰信息
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"cGetShopDecoration","params":{"shopCode":"' + this.$route.params.shopCode + '","page":1}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                this.shopDecoList = result.data.result.shopDecoList;
                for (let item of this.shopDecoList) {
                    this.shopImgList.push(item.imgUrl);
                }
            });
        },
        collectShop: function(shopCode) { // 收藏商户
            if (!this.isLogin) {
                this.$store.state.isBLR = 1;
                this.$store.state.showLoginBox = true;
                this.$vux.toast.text('请登录', 'bottom');
            } else {
                this.axios({
                    method: 'post',
                    url: this.global.hftcomShop,
                    data: '{"id":19,"jsonrpc":"2.0","method":"showCollect","params":{"shopCode":"' + shopCode + '","userCode":"' + this.$store.state.userCode + '","zoneId":"' + this.zoneId + '"}}',
                    contentType: 'application/json',
                    dataType: 'json'
                }).then((result) => {
                    if (result.data.result === 1) {
                        this.isCollect = true;
                        this.$vux.toast.text('已收藏', 'bottom');
                    } else if (result.data.result === 0) {
                        this.isCollect = false;
                        this.$vux.toast.text('已取消收藏', 'bottom');
                    } else {
                        this.$vux.toast.text('数据错误，请稍后重试', 'bottom');
                    }
                });
            }
        },
        showvipcard: function() { // 查询用户在本店的会员卡状态
            this.axios({
                method: 'post',
                url: this.global.hftcomWeixinCard,
                data: '{"id":29,"jsonrpc":"2.0","method":"hasCard","params":{"userCode":"' + this.userCode + '","shopCode":"' + this.shopAllInfo.shopInfo.shopCode + '"}}'
            }).then((result) => {
                if (result.data.result.code === 50000) {
                    if (result.data.result.state === false) {
                        this.viptype = 0;
                    } else {
                        this.viptype = result.data.result.state;
                    }
                } else {
                    this.$vux.toast.text(result.data.result.code, 'bottom');
                }
            });
        },
        getvipcard: function() { // 领取会员卡
            if (!this.isLogin) {
                this.$store.state.isBLR = 1;
                this.$store.state.showLoginBox = true;
                this.$vux.toast.text('请登录', 'bottom');
            } else {
                if (this.viptype === 0) {
                    this.axios({
                        method: 'post',
                        url: this.global.hftcomWeixinCard,
                        data: '{"id":29,"jsonrpc":"2.0","method":"addCardUser","params":{"userCode":"' + this.userCode + '","shopCode":"' + this.shopAllInfo.shopInfo.shopCode + '"}}'
                    }).then((result) => {
                        console.log(result.data);
                        if (result.data.result.code === 50000) {
                            switch (result.data.result.state) {
                            case true:
                                this.viptype = 2;
                                this.$vux.toast.text('领取成功', 'bottom');
                                this.addvipcard(this.shopAllInfo.shopInfo.shopCode);
                                break;
                            case false:
                                this.$vux.toast.text('领取失败，请重试', 'bottom');
                                break;
                            default:
                                this.$vux.toast.text(result.data.result.state, 'bottom');
                            }
                        } else {
                            this.$vux.toast.text(result.data.result.code, 'bottom');
                        }
                    });
                } else {
                    this.addvipcard(this.shopAllInfo.shopInfo.shopCode);
                }
            }
        },
        addvipcard: function(shopCode) { // 添加会员卡到微信卡包
            this.isadding = shopCode;
            this.axios({
                method: 'post',
                url: this.global.hftcomWeixinCard,
                data: '{"id":29,"jsonrpc":"2.0","method":"addWeixinCard","params":{"userCode":"' + this.userCode + '","shopCode":"' + shopCode + '"}}'
            }).then((result) => {
                console.log(result.data);
                if (result.data.result.signPakage === '' || result.data.result.signPakage === undefined) {
                    this.$vux.toast.text('数据请求错误，请稍后重试', 'bottom');
                } else {
                    this.cardInfo = result.data.result.cardInfo;
                    this.signPakage = result.data.result.signPakage;
                    this.userCardCode = result.data.result.userCardCode;
                    this.addcardtoweixin();
                }
                this.isadding = '';
            });
        },
        updateUserWeixinCard: function() { // 告知后端会员卡添加至卡包成功
            this.axios({
                method: 'post',
                url: this.global.hftcomWeixinCard,
                data: '{"id":29,"jsonrpc":"2.0","method":"updateUserWeixinCard","params":{"userCardCode":"' + this.userCardCode + '"}}'
            }).then((result) => {
                this.viptype = 1;
                this.$vux.toast.text('已添加', 'bottom');
            });
        },
        addcardtoweixin: function() {
          /* eslint-disable */
          wx.config({
            debug: false,
            appId: this.signPakage.appid,
            timestamp: this.signPakage.timestamp,
            nonceStr: this.signPakage.noncestr,
            signature: this.signPakage.signature,
            jsApiList: [
            'addCard'
            ]
          });
          wx.ready(() => {
            wx.addCard({
              cardList:  [
                    {
                      code:this.cardInfo.code,
                        cardId: this.cardInfo.card_id,
                        cardExt: '{"code":"' + this.cardInfo.code + '","timestamp":"' + this.cardInfo.timestamp + '","signature":"' + this.cardInfo.signature + '","nonce_str":"' + this.cardInfo.nonce_str + '"}'
                    }
                ],
              success: (res) => {
                this.updateUserWeixinCard();
                  },
              cancel:  (res) => {
                    this.$vux.toast.text('取消添加','bottom')
                  }
              });
          });
          wx.error(function(res){
            console.log('err', res)
          });
            /* eslint-enable */
        }
    },
    created() {
        this.getShopDetail();
        this.getShopDecoration();
        this.getCollectShop();
        this.getCollectInfo();
    },
    components: {
        swiper,
        indexcoupon
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#shopdetail
  background:rgb(240,240,240)
  width:10.8rem
  overflow-x:hidden
  .album
    position:relative
    width:100%
    height:5rem
    background-color:#f0f0f0
    background-size:contain
    text-align:center
    line-height:200px
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
  .vipcard
      margin-top:0.1rem
      line-height:0.8rem
      height:2rem
      position:relative
      background:#fcf5e3
      .left
          position:absolute
          left:0.8rem
          top:0.2rem
          .logo
              color:#da9424
              font-size:0.46rem
              font-weight:bold
          .vipinfo
              color:#9c9b98
      .right
          position:absolute
          top:0.5rem
          right:0.7rem
          height:1rem
          width:2.8rem
          text-align:center
          line-height:1rem
          background:#ff621a
          border-radius:0.5rem
          color:#fff
          .icon-spinner8
              position:absolute
              top:0.28rem
              left:1.2rem
              font-size:0.46rem
          .rotate-enter-active
            transition: transform 4s linear
          .rotate-enter
            transform: rotate(-1440deg)
  .mask
      position:fixed
      height:100%
      width:100%
      overflow:hidden
      top:0
      left:0
      background:rgba(93,93,93,0.3)
      z-index:1
      .toCard
        display:inline-block
        margin-top:calc((100% - 6rem) / 2)
        margin-left:2.5rem
        text-align:center
        height:6rem
        width:5.8rem
        background:#fff
        border-radius:0.2rem
  .shopCoupon
      margin-top:0.1rem
      line-height:1rem
      background:#fff
    .name
        margin: 0rem auto
        height:1rem
        width:4em
        color:#525252
        font-size:0.5rem
    .text
        height:1.3rem
        width:10em
        margin:0 auto
        font-size:0.28rem
  .shopInfoWrapper
    padding:0.3rem 0.5rem
    margin-top:0.1rem
    background:#fff                                    
    .shopName
        font-size:0.66rem
        padding:0.3rem 0.1rem 0.5rem 0.3rem
        margin-bottom:0.5rem
        border-bottom:1px solid #dededede
        .popularity
            line-height:0.8rem
            float:right
            font-size:0.32rem
            color:#f64f48
    .shopInfo
        padding:0.3rem 0.3rem
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
                width:5.5rem
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
                width:5.8rem
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
        width:5em
        color:#525252
        font-size:0.5rem
    .text
        height:1.3rem
        width:9em
        margin:0 auto
        font-size:0.28rem
    .content
        font-size:0.44rem
        line-height:0.63rem
        padding:0.2rem 0.69rem
        text-indent:2em
</style>

