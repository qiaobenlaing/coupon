<template>
    <div id="indexcoupondetails" >
        <div class="album">
            <header class="header">
              <div class="header-item icon-back" @click='goBack'></div>
              <div class="header-item">
                <span v-show="isCollect === false" class="icon-collect" @click="collectCoupon(couponAllInfo.batchCouponCode)"></span>
                <span v-show="isCollect === true" class="icon-collect-solid" @click="collectCoupon(couponAllInfo.batchCouponCode)"></span>
            </div>
        </header>
        <swiper :imgSrcArr="couponAllInfo.logoUrl"></swiper>
    </div>
        <div class="rule">
            <div class="rule-wrapper">
                <div class="shopName">{{shopDecoration.shopName}} <span v-show="shopDecoration.shopName!==undefined && couponAllInfo.remark !==''">|</span> <span v-show="couponAllInfo.remark != ''">{{couponAllInfo.remark}}</span><span class="popularity">人气：{{shopDecoration.popularity
                }}</span></div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='1'">
                    <!-- N元购 -->
                    <p>{{couponAllInfo.function}}</p>
                </div>
                <div class="ruleText" v-if="couponAllInfo.couponType==='2'">
                    
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
                    <p>￥{{couponAllInfo.payPrice}}元{{couponAllInfo.function}}</p>
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
            
        </div>
        <div class="activeInfo" v-if="this.appType === 1 && activityList.length > 0 && activityList !== undefined">
            <p class="activeInfoName"><img class="discountImg" :src="this.global.staticSrc+'/huiquan/static/img/discountActive.png'"></p>
            <p class="active">有效期：</p>
            <p class="content">{{activityList[0].startTime
                    }}至{{activityList[0].endTime}}</p>
            <p class="active">活动内容：</p>
            <p class="content">{{activityList[0].txtContent}}</p>
        </div>
        <div class="shopInfo">
            <div class="left">
                <div class="clock">
                    <div class="icon icon-clock"></div>
                    <div class="clocktext"><p>有效期：</p><p>{{couponAllInfo.startUsingTime
                    }}-{{couponAllInfo.expireTime}}</p></div>
                </div>
                <div class="location">
                    <div class="icon icon-location"></div>
                    <div class="locationtext"><p><span>{{shopDecoration.city}}</span><span>{{shopDecoration.street}}</span></p>
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="content"><a :href="'tel:' + shopDecoration.tel"><p class="icon-phone"></p></a></div>
            </div>
        </div>
        
        <div class="shopInfoName" @click="toShopDetails(couponAllInfo.shopCode)">
            <div class="name">商户信息：{{shopDecoration.shopName}} <span class="icon-back"></span></div>
        </div>
        <codegroup :couponAllInfo="couponAllInfo" ref="codegroup"></codegroup>
        <div class="cart">
            <div v-show="(couponAllInfo.payPrice===0 || couponAllInfo.payPrice==='0')&& isCanGetCoupon" class="btn" @click="getCoupon(couponAllInfo)">立即领取</div>
            <div v-show="couponAllInfo.payPrice>0 && isCanGetCoupon" class="btn price">
                <span :class="activityList.length > 0 && activityList !== undefined? 'pricespan':''">￥{{couponAllInfo.payPrice}}</span>
                <span v-if="activityList.length > 0 && activityList !== undefined">￥{{discountPrice}}</span>
            </div>
            <div v-show="couponAllInfo.payPrice>0 && isCanGetCoupon" class="btn paybtn" @click="getCoupon(couponAllInfo)">立即购买</div>
            <div class="btn cantget" v-show="!isCanGetCoupon">已领完</div>
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
import {mapState, mapActions} from 'vuex';
import codegroup from '@/components/coupondetails/codegroup';
import swiper from '@/components/swiper/swiper';
export default {
    name: 'indexCouponDetails',
    data() {
        return {
            ok: true,
            // albumImg: '本店暂无图片',
            couponAllInfo: {},
            shopDecoration: {},
            countMyReceived: -1, // 用户已经领取的券数量
            isCanGetCoupon: false, // 是否还能领取优惠券
            isCollect: false, // 本页的收藏按钮显示与否
            isShowBarCode: false,
            userCouponNbrList: [],
            currenUserCouponNbr: '',
            activityList: [],
            discountPrice: 0
        };
    },
    computed: {
        ...mapState(['global', 'CouponListInfo', 'isBLR', 'isLogin', 'userCode', 'appType', 'zoneId'])
    },
    watch: {
        countMyReceived: function() {
            if (this.countMyReceived === -1 || this.countMyReceived === undefined) {
                this.isCanGetCoupon = true;
            } else {
                if ((parseInt(this.couponAllInfo.nbrPerPerson) - this.countMyReceived) === 0) {
                    this.isCanGetCoupon = false;
                } else {
                    this.isCanGetCoupon = true;
                }
            }
        }
    },
    beforeCreate() {
    },
    methods: {
        ...mapActions(['getCollectInfo']),
        top() {
            window.scrollTo(0, 0);
        },
        goBack () {
            this.$router.go(-1);
        },
        showIndexCouponDetails: function() {
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"getBachCouponInfo","params":{"userCode":"' + this.userCode + '","batchCouponCode":"' + this.$route.params.nowBatchCouponCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                console.log(result);
                this.$data.couponAllInfo = result.data.result.BatchCouponInfo;
                this.$data.shopDecoration = result.data.result.ShopDecoration;
                this.$data.couponAllInfo.logoUrl = result.data.result.ShopDecoration.imgUrl;
                this.countMyReceived = result.data.result.countMyReceived;
                if (this.isLogin) {
                    this.getCollectInfo().then(() => {
                        this.filterIsCollect();
                    }); // 收藏信息
                    this.$refs.codegroup.getUserCouponNbr(this.couponAllInfo.batchCouponCode);
                };
            });
        },
        showActiveDetails: function() {
            this.axios({
                method: 'post',
                url: '/Api/Activity',
                data: '{"id":19,"jsonrpc":"2.0","method":"getCouponActivity","params":{"batchCouponCode":"' + this.$route.params.nowBatchCouponCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (result.data.result !== '50220') {
                    this.$data.activityList = result.data.result;
                    this.getDiscountPrice();
                }
            });
        },
        getDiscountPrice: function() {
            this.axios({
                method: 'post',
                url: '/Api/Activity',
                data: '{"id":19,"jsonrpc":"2.0","method":"getPrice","params":{"activityCode":"' + this.$data.activityList[0].activityCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                this.$data.discountPrice = result.data.result;
            });
        },
        filterIsCollect: function() { // 用于筛选收藏按钮的显示与否
            this.CouponListInfo.filter((element, index, self) => {
                if (this.couponAllInfo.batchCouponCode ===
 element.collectCouponCode) {
                    if (element.isCollect === '1') {
                        this.isCollect = true;
                    }
                }
            });
        },
        grabCoupon: function(index) { // 非兑换券及非代金券且价格为0的券的领取方式
            let that = this;
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id": 67,"jsonrpc": "2.0","method": "grabCoupon","params": {"batchCouponCode": "' + index.batchCouponCode + '","userCode": "' + this.$store.state.userCode + '","sharedLvl": "0"}}',
                dataType: 'json'
            }).then((result) => {
                that.isGeting = false;
                if (result.data.result.code === '80222') {
                    that.$vux.toast.text('领取数量已经达到上限', 'bottom');
                    that.index.countMyReceived = that.index.nbrPerPerson;
                } else if (result.data.result.code === 50000) {
                    that.$vux.toast.text('已领取', 'bottom');
                    this.$refs.codegroup.getUserCouponNbr(this.couponAllInfo.batchCouponCode);
                    if (result.data.result.isCount === 0) {
                        that.index.countMyReceived = that.index.nbrPerPerson;
                    } else {
                        that.isCanGet = true;
                    }
                } else {
                    that.$vux.toast.text(result.data.result, 'bottom');
                }
            });
        },
        addANDbank: function(index) { // 代金券兑换券价格为0的领取方式
            let that = this;
            this.axios({
                method: 'post',
                url: that.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"addCouponOrder","params":{"userCode":"' + that.userCode + '","shopCode":"' + index.shopCode + '","batchCouponCode":"' + index.batchCouponCode + '","couponNbr":1,"platBonus":0,"shopBonus":0,"activityCode":""}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((order) => {
                if (order.data.result.code === 50000) {
                    this.axios({
                        method: 'post',
                        url: that.global.hftcomClient,
                        data: '{"id":19,"jsonrpc":"2.0","method":"bankcardPayConfirm","params":{"consumeCode":"' + order.data.result.consumeCode + '","bankAccountCode":"","valCode":""}}',
                        contentType: 'application/json',
                        dataType: 'json'
                    }).then((result) => {
                        that.isGeting = false;
                        if (result.data.result.code === 50000) {
                            that.$vux.toast.text('领取成功', 'bottom');
                            this.$refs.codegroup.getUserCouponNbr(this.couponAllInfo.batchCouponCode);
                        } else {
                            switch (result.data.result.code) {
                            case '80221':
                                that.$vux.toast.text('抢完了', 'bottom');
                                that.index.countMyReceived = that.index.nbrPerPerson;
                                break;
                            default:
                                that.$vux.toast.text(order.data.result.code, 'bottom');
                            }
                        }
                    });
                } else {
                    that.isGeting = false;
                    switch (order.data.result.code) {
                    case '80221':
                        that.$vux.toast.text('抢完了', 'bottom');
                        that.index.countMyReceived = that.index.nbrPerPerson;
                        break;
                    case '80238':
                        that.$vux.toast.text('个人购买数量超上限', 'bottom');
                        that.index.countMyReceived = that.index.nbrPerPerson;
                        break;
                    case '80220':
                        that.$vux.toast.text('券过期了', 'bottom');
                        break;
                    case 9527:
                        that.$vux.toast.text('请在个人中心完成手机号的绑定', 'bottom');
                        break;
                    case 9377:
                        this.$vux.toast.text('未到优惠券领用时间', 'bottom');
                        break;
                    default:
                        that.$vux.toast.text(order.data.result, 'bottom');
                    }
                }
            });
        },
        getCoupon: function(index) {
            let that = this;
            if (!this.isLogin) {
                this.$store.state.isBLR = 1;
                this.$store.state.showLoginBox = true;
                this.$vux.toast.text('请登录', 'bottom');
            } else {
                that.isGeting = true;
                if ((index.payPrice === 0 || index.payPrice === '0') && index.couponType !== '7' && index.couponType !== '8') {
                  // 非兑换券及非代金券且价格为0的券的领取方式
                    this.grabCoupon(index);
                    return;
                } else {
                    if (index.payPrice === 0) {
                    // 代金券兑换券价格为0的领取方式
                        this.addANDbank(index);
                    } else {
                        // 代金券兑换券的支付
                        if (this.appType === 1 && this.$data.activityList.length > 0 && this.$data.activityList !== undefined) {
                            this.$router.push({path: `/submitorder/${index.batchCouponCode}`, query: {activeCode: this.$data.activityList[0].activityCode}});
                        } else {
                            this.$router.push({path: `/submitorder/${index.batchCouponCode}`});
                        }
                    }
                }
            }
        },
        collectCoupon: function(code) {
            var dataStr = '';
            if (this.appType === 1) {
                dataStr = '{"id":19,"jsonrpc":"2.0","method":"collectCoupon","params":{"batchCouponCode":"' + code + '","userCode":"' + this.$store.state.userCode + '","zoneId":"' + this.zoneId + '"}}';
            } else {
                dataStr = '{"id":19,"jsonrpc":"2.0","method":"collectCoupon","params":{"batchCouponCode":"' + code + '","userCode":"' + this.$store.state.userCode + '"}}';
            }
            let that = this;
            if (!this.isLogin) {
                this.$store.state.isBLR = 1;
                this.$store.state.showLoginBox = true;
                this.$vux.toast.text('请登录', 'bottom');
            } else {
                this.axios({
                    method: 'post',
                    url: this.global.hftcomShop,
                    data: dataStr,
                    contentType: 'application/json',
                    dataType: 'json'
                }).then((result) => {
                    console.log(result);
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
        }
    },
    created() {
        this.showIndexCouponDetails();
        if (this.appType === 1) {
            this.showActiveDetails();
        }
        this.top(); // 在进入页面时 将其定位在顶端
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
#indexcoupondetails
    background:#f0f0f0
    width:100%
    height:100%
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
    z-index:0
    .header
        position:absolute
        top:0
        left:0
        z-index:1
        height:1.28rem
        width:10.8rem
        line-height:1.28rem
        color:#fff
        :nth-child(1)
          float:left
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
    margin-top:0.1rem
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
  .barcode
      margin-top:0.1rem
      width:100%
      background:#fff
    .barcontainer
        width:8rem  
        margin:0 auto
        .vue-barcode-element
          width:8rem
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
    margin-top:0.1rem
    padding:0.3rem 0.4rem
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
  .barcode
    margin-top:0.1rem
    width:100%
    background:#fff
    .barwrapper
        margin:0 0.4rem
        border-bottom:1px solid #cecece
        .barcontainer
            width:7rem  
            margin:0 auto
            padding:0.5rem 0
            .vue-barcode-element
              width:7rem
              height:4rem
              border:1px solid #3e3e3e75
              border-radius:0.1rem
    .barcodeName
        margin:0.1rem 0.4rem
        padding:0.4rem 0.4rem
        font-size:0.5rem
        // line-height:0.3rem
        .icon-barcode
            color:#000
            font-size:0.5rem
            padding-right:0.4rem
        .nouse
            float:right
            color:#149f14
        .used
            float:right
            color:#f64f48
        .disuse
            float:right
            color:#f64f48
    .isActive
        background:rgba(232, 232, 232, 0.522)
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
  .cart
    position:fixed
    bottom:0
    left:0
    height:1.48rem
    width:100%
    background:#fff
    .btn
      width:100%
      font-size:0.44rem
      color:#fff
      background:#f64f48
      text-align:center
      line-height:1.48rem
    .price
        color:#fff
        background:#525252
        width:30%
        float:left
    .pricespan
        text-decoration:line-through
        position: absolute
        left: 2.2rem
        font-size: 0.35rem
        bottom: 0.4rem
    .paybtn
        width:70%
        float:right
    .cantget
        background:#b5b5b5
  .ruleInfo
      background:#fff
      width:100%
      margin-top:0.1rem
      line-height:1rem
      padding-bottom:2rem
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

    .activeInfo
        background:#fff
        width:100%
        margin-top:0.1rem
        line-height:1rem
        .activeInfoName
            margin:0 auto
            font-size:0.6rem
            width:5em
            text-align:center
            .discountImg
              width: 3rem
              margin-top: 0.3rem
        .active
            padding:0 0.5rem
            font-size:0.44rem
            font-weight:bold
            color:#F14E45
        .content
            padding: 0 0.9rem
            font-size:0.4rem
            &:before
                content:'• '
                font-weight:bold
</style>
