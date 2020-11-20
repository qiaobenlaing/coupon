<template>
  <div id="indexCouponList">
    <div class="couponItem">
      <div class="toCouponDetails" @click="toIndexCouponDetails(index.batchCouponCode)">
        <div class="img">
          <img v-lazy="global.hftcomImgSrc + index.url" alt="">
          <!-- 单商户由于图片图库问题，临时使用logoUrl -->
        </div>
        <div class="text">
          <img class="discountImg" v-if="appType === 1 && index.activityCode !== '' && index.activityCode !== undefined" :src="this.global.staticSrc+'/huiquan/static/img/discount.png'">
          <p class="shopName" :class="appType === 1 && index.activityCode !== '' && index.activityCode !== undefined? 'marginLeft':''">{{computedShopName}}</p>
          <p class="couponType">
        <span v-if="index.couponType === '1'"><span  class="experience">N</span>{{index.function}}</span>
        <span class="" v-if="index.couponType === '2'">2</span>
        <span v-if="index.couponType === '3'">
          <span class="instead">抵</span>
          <span class="price">
            <span class="renmin">￥</span>
            {{index.payPrice}}
          </span>
          <span class="oldPrice" v-if="appType === 1 && index.activityCode !== '' && index.activityCode !== undefined">{{index.oldPrice}}</span>
          抵{{index.insteadPrice}}元
        </span>
        <span  v-if="index.couponType === '4'">
          <span class="discount">折</span>
          <span class="price">
            <span class="renmin">￥</span>
            {{index.payPrice}}
          </span>
          <span class="oldPrice" v-if="appType === 1 && index.activityCode !== '' && index.activityCode !== undefined">{{index.oldPrice}}</span>
          {{index.discountPercent}}折
        </span>
        <span v-if="index.couponType === '5'">
          <span class="experience">实</span>
          <span class="price">
            <span class="renmin">￥</span>
            {{index.payPrice}}
          </span>
          <span class="oldPrice" v-if="appType === 1 && index.activityCode !== '' && index.activityCode !== undefined">{{index.oldPrice}}</span>
          {{index.function}}
        </span>
        <span v-if="index.couponType === '6'">
          <span class="experience">体</span>
          <span class="price">
            <span class="renmin">￥</span>
            {{index.payPrice}}
          </span>
          <span class="oldPrice" v-if="appType === 1 && index.activityCode !== '' && index.activityCode !== undefined">{{index.oldPrice}}</span>
          {{index.function}}
        </span>
        <span v-if="index.couponType === '7'">
          <span class="function">兑</span>
          <span class="price" v-show="index.payPrice>0">
            <span class="renmin">￥</span>
            {{index.payPrice}}
          </span>
          <span class="oldPrice" v-if="appType === 1 && index.activityCode !== '' && index.activityCode !== undefined">{{index.oldPrice}}</span>
          {{index.function}}
        </span>
        <span class="item" v-if="index.couponType === '8'">
          <span class="instead">代</span>
          <span class="price">
            <span class="renmin">￥</span>
            {{index.payPrice}}
          </span>
          <span class="oldPrice" v-if="appType === 1 && index.activityCode !== '' && index.activityCode !== undefined">{{index.oldPrice}}</span>
          代{{index.insteadPrice}}元
        </span>
        <!-- <span class="experience" v-if="index.couponType === '9'">体</span> -->
      </p>
          <p class="street"><span class="street">{{computedStreet}}</span><span class="distance" v-if="index.distance < 100"><100m</span><span class="distance" v-if="100 < index.distance && index.distance < 500">>100m</span><span class="distance" v-if="index.distance > 500">>500m</span></p>
        </div>
      </div>
      <div class="collect" @click="collectCoupon(index.batchCouponCode)">
        <span class="icon-collect" v-show="!isCollect && !isCollecting"></span>
<transition name="rotate">
                <span class="icon-spinner8" v-show="isCollecting"></span>
            </transition>
        <span class="icon-collect-solid" v-show="isCollect && !isCollecting"></span>
      </div>
      <button class="btn" v-show="isCanGet && !isGeting" :disabled="iswaiting"  @click="getCoupon(index)">
        <!-- <p class="btnName">立即领取</p> -->
        <p class="btnName" v-show="index.payPrice===0 || index.payPrice==='0'">免费领取</p>
        <p class="btnName" v-show="index.payPrice>0">立即购买</p>
      </button>
      <button class="btn" v-show="isGeting">
      <transition name="slide-fade">
        <p class="btnName">领取中...</p>
        </transition>
      </button>
      <button class="btn btnDisabled" v-show="!isCanGet && !isGeting">
      <p class="btnName" >已领完</p>
      </button>
    </div>
  </div>
</template>
<script>
import {mapState} from 'vuex';
export default{
    props: ['index'],
    data() {
        return {
            isGeting: false,  // 正在领取，等待数据返回的时候显示
            iswaiting: false, // 正在等待，等待的时候屏蔽领取事件
            isCollect: false, // 是否收藏
            isCollecting: false  // 正在收藏，等待数据返回的时候显示
        };
    },
    computed: {
        ...mapState(['global', 'CouponListInfo', 'isBLR', 'isLogin', 'userCode', 'currentOrderInfo', 'zoneId', 'appType']),
      // 控制显示的内容
        computedStreet() {
            if (this.index.street !== '' && this.index.street !== undefined) {
                return this.methodGetByteLen(this.index.street, 25);
            } else {
                return '';
            }
        },
        computedShopName() {
            if (this.index.shopName !== '' && this.index.shopName !== undefined) {
                return this.methodGetByteLen(this.index.shopName, 22);
            } else {
                return this.index.shopName;
            }
        },
        isCanGet: function() { // 是否还能领取
            if (this.index.countMyReceived === undefined || this.index.nbrPerPerson === '0') {
                return true;
            } else {
                if ((this.index.nbrPerPerson - this.index.countMyReceived) > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    },
    watch: {
        CouponListInfo: function() {
            this.filterIsCollect();
        }
    },
    methods: {
        timer: function(t) {
            var time = 1000;
            if (t === '' || t === undefined) {
            } else {
                time = t;
            }
            this.iswaiting = true;
            setTimeout(() => {
                this.iswaiting = false;
            }, time);
        },
        toIndexCouponDetails: function(nowBatchCouponCode) {
            this.$router.push({path: `/indexCouponDetails/${nowBatchCouponCode}`});
        },
        grabCoupon: function(index) { // 非兑换券及非代金券且价格为0的券的领取方式
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id": 67,"jsonrpc": "2.0","method": "grabCoupon","params": {"batchCouponCode": "' + index.batchCouponCode + '","userCode": "' + this.$store.state.userCode + '","sharedLvl": "0"}}',
                dataType: 'json'
            }).then((result) => {
                this.isGeting = false;
                if (result.data.result.code === '80222') {
                    this.$vux.toast.text('领取数量已经达到上限', 'bottom');
                    this.index.countMyReceived = this.index.nbrPerPerson;
                } else if (result.data.result.code === 50000) {
                    this.$vux.toast.text('已领取', 'bottom');
                    if (result.data.result.isCount === 0) {
                        this.index.countMyReceived = this.index.nbrPerPerson;
                    } else {
                        this.isCanGet = true;
                    }
                } else {
                    this.$vux.toast.text(result.data.result, 'bottom');
                }
                // 禁用按钮延时
                this.timer();
            });
        },
        addANDbank: function(index) { // 代金券兑换券价格为0的领取方式
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"addCouponOrder","params":{"userCode":"' + this.userCode + '","shopCode":"' + index.shopCode + '","batchCouponCode":"' + index.batchCouponCode + '","couponNbr":1,"platBonus":0,"shopBonus":0}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((order) => {
                if (order.data.result.code === 50000) {
                    this.axios({
                        method: 'post',
                        url: this.global.hftcomClient,
                        data: '{"id":19,"jsonrpc":"2.0","method":"bankcardPayConfirm","params":{"consumeCode":"' + order.data.result.consumeCode + '","bankAccountCode":"","valCode":""}}',
                        contentType: 'application/json',
                        dataType: 'json'
                    }).then((result) => {
                        this.isGeting = false;
                        if (result.data.result.code === 50000) {
                            this.$vux.toast.text('领取成功', 'bottom');
                        } else {
                            switch (result.data.result.code) {
                            case 80221:
                                this.$vux.toast.text('抢完了', 'bottom');
                                this.index.countMyReceived = this.index.nbrPerPerson;
                                break;
                            default:
                                this.$vux.toast.text(order.data.result.code, 'bottom');
                            }
                        }
                        // 禁用按钮延时
                        this.timer();
                    });
                } else {
                    this.isGeting = false;
                    switch (order.data.result.code) {
                    case '80221':
                        this.$vux.toast.text('抢完了', 'bottom');
                        this.index.countMyReceived = this.index.nbrPerPerson;
                        break;
                    case '80238':
                        this.$vux.toast.text('个人购买数量超上限', 'bottom');
                        this.index.countMyReceived = this.index.nbrPerPerson;
                        break;
                    case 9527:
                        this.$vux.toast.text('请在个人中心完成手机号的绑定', 'bottom');
                        break;
                    case 9377:
                        this.$vux.toast.text('未到优惠券领用时间', 'bottom');
                        break;
                    case '80220':
                        this.$vux.toast.text('券过期了', 'bottom');
                        break;
                    default:
                        this.$vux.toast.text(order.data.result, 'bottom');
                    }
                }
            });
        },
        getCoupon: function(index) {
            if (!this.isLogin) {
                this.$store.state.isBLR = 1;
                this.$store.state.showLoginBox = true;
                this.$vux.toast.text('请登录', 'bottom');
            } else {
                this.isGeting = true;
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
                        if (index.activityCode !== '' && index.activityCode !== undefined && this.appType === 1) {
                            this.$router.push({path: `/submitorder/${index.batchCouponCode}`, query: {activeCode: index.activityCode}});
                        } else {
                            this.$router.push({path: `/submitorder/${index.batchCouponCode}`});
                        }
                    }
                }
            }
        },
        collectCoupon: function(code) {
            if (!this.isLogin) {
                this.$store.state.isBLR = 1;
                this.$store.state.showLoginBox = true;
                this.$vux.toast.text('请登录', 'bottom');
            } else {
                this.isCollecting = true;
                this.axios({
                    method: 'post',
                    url: this.global.hftcomShop,
                    data: '{"id":19,"jsonrpc":"2.0","method":"collectCoupon","params":{"batchCouponCode":"' + code + '","userCode":"' + this.$store.state.userCode + '","zoneId":"' + this.zoneId + '"}}',
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
                    this.isCollecting = false;
                });
            }
        },
        filterIsCollect: function() {
            if (this.CouponListInfo.length !== 0) {
                this.CouponListInfo.filter((element, index, self) => {
                    if (this.index.batchCouponCode === element.collectCouponCode) {
                        if (element.isCollect === '1') {
                            this.isCollect = true;
                        }
                    }
                });
            }
        },
        methodGetByteLen: function(str, len) { // 控制字符长度，超过则削减添加省略号
            if (str.replace(/[^\x00-\xff]/g, '01').length <= len) {
                return str;
            }
            for (let i = Math.floor(len / 2); i < str.length; i++) {
                if (str.substr(0, i).replace(/[^\x00-\xff]/g, '01').length >= len) {
                    return str.substr(0, Math.floor(i / 2) * 2) + '...';
                }
            }
        }
    },
    created () {
        this.filterIsCollect();
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#indexCouponList
  border-bottom:2px solid #f0f0f0
  .couponItem
    position:relative
    background:#fff
    height:2.8rem
    width:100%
    padding-left:0.24rem
    .img
      position:absolute
      top:0.1rem
      left:0.2rem
      height:2.55rem
      width:2.55rem
      overflow:hidden
      border-radius: 6%
      img
        height:100%
        background-attachment:fixed
        background-position:center
        background-repeat: no-repeat;
        background-origin:content-box
    .text
      position:absolute
      top:0.2rem
      left:3.03rem
      display:inline-block
      margin-left:0.1rem
      height:2.75rem
      width:7rem
      font-size:0.5rem
      .discountImg
        position: absolute
        margin-top: 0.2rem
        height: 0.5rem
      .shopName
        margin-top:0.1rem
        font-weight:500
        font-size:0.5rem
        color:#212121
        line-height:0.6667rem
        span
          font-size:0.6rem
      .marginLeft
        margin-left:1.5rem
      .couponType
        margin-top:0.35rem
        height:0.4rem
        color:rgb(82,82,82)
        span
          display:inline-block
          font-size:0.3rem
          color: #F14C45
          span
            margin-right:0.1rem
            display:inline-block
            width:0.4rem
            height:0.4rem
            line-height:0.4rem
            text-align:center
            font-size:0.3rem
            // padding:0.04rem 0.1rem
            background:#cecece
            color:#fff
            border-radius:0.08rem
          .experience
            background:#38e237
          .instead
            background:#f64f48
          .discount
            background:#ff9c00
          .function
            background:#be61b2
          .price
            border:none
            width:auto
            background:#fff
            color:#f64f48
            font-size:0.7rem
            .renmin
              border:none
              width:auto
              background:#fff
              color:#f64f48
              font-size:0.3rem
          .oldPrice
            border:none
            width:auto
            background:#fff
            text-decoration:line-through
            color:#A6A6A6
            font-size:0.5rem
      .street
          margin-top:0.21rem
          line-height:0.5rem
          height:0.5rem
          font-size:0.28rem
          color:rgb(95,95,95)
          .street
              display:inline-block
          .distance
              float:right
              line-height:0.9rem
              font-size:0.28rem
              color:rgb(95,95,95)
      .usingTime
        margin-top:0.2rem
        height:0.38rem
        font-size:0.30rem
        line-height:0.3rem
        border:1px solid rgb(246,79,72)
        border-radius:0.13rem
        padding:0.08rem
        width:11em
        color:rgb(246,79,72)
    .collect
      position:absolute
      top:0.35rem
      right:0.77rem
      .icon-collect
        font-size:0.66rem
        position:absolute
        top:0
        right:0
      .icon-collect-solid
        color:#ff9c00
        font-size:0.66rem
        position:absolute
        top:0
        right:0
      .icon-spinner8
        position:absolute
        top:0
        right:0
        color:#cecece
        font-size:0.56rem
    .rotate-enter-active
      transition: transform 3s linear
    .rotate-enter
      transform: rotate(-1080deg)
    .btn
      position:absolute
      top:1.245rem
      right:0.7rem
      display:inline-block
      height:0.66rem
      line-height:0.66rem
      width:2.37rem
      border-radius:1rem
      background:rgb(108,204,229)
      color:#fff
      border:none
      outline : none
      .btnName
        margin:0 auto
        width:4.5em
        height:100%
        font-size:0.36rem
        color:#fff
      .slide-fade-enter-active
        transition: all 2s ease
      .slide-fade-enter
        transform: translateX(15px)
    .btnDisabled
      background:rgb(181,181,181)
      .btnName
        margin:0 auto
        width:4em
        height:100%
        font-size:0.36rem
        color:#fff
</style>
