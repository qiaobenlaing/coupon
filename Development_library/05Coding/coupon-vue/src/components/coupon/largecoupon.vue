<template>
  <div class="couponList">
  	<div class="couponItem">
      <div class="toCouponDetails" @click="toCouponDetails(index.userCouponCode)">
        <div class="sawtooth"></div>
      <div class="img">
        <img :src="global.hftcomImgSrc + index.url" alt="">
      </div>
      <div class="text">
      <p class="shopName">
        <span class="name">{{computedTxt}}</span>
        <span class="distance" v-if="index.distance < 100"><100m</span>
        <span class="distance" v-if="100 < index.distance && index.distance < 500">>100m</span>
        <span class="distance" v-if="index.distance > 500">>500m</span>
      </p>
      <p class="couponType">
        <span v-if="index.couponType === '1'"><span  class="experience">N</span>{{computedFunction}}</span>
        <span class="" v-if="index.couponType === '2'">2</span>
        <span v-if="index.couponType === '3'"><span class="instead">抵</span>满{{index.availablePrice}}抵{{index.insteadPrice}}元</span>
        <span  v-if="index.couponType === '4'"><span class="discount">折</span>满{{index.availablePrice}}打{{index.discountPercent}}折</span>
        <span v-if="index.couponType === '5'"><span class="experience">实</span>{{computedFunction}}</span>
        <span v-if="index.couponType === '6'"><span class="experience">体</span>{{computedFunction}}</span>
        <span v-if="index.couponType === '7'"><span class="function">兑</span>{{computedFunction}}</span>
        <span class="item" v-if="index.couponType === '8'">
        <span class="instead">代</span>满{{index.availablePrice}}代{{index.insteadPrice}}元</span>
        <!-- <span class="experience" v-if="index.couponType === '9'">体</span> -->
      </p>
      <p class="couponName">{{index.couponName}}</p>
      <p class="createTime">{{index.createTime}}</p>
      <p class="usingTime">使用时间：{{index.dayStartUsingTime}}-{{index.dayEndUsingTime}}</p>
      </div>
      </div>
  		<button class="btn" v-bind:disabled="!!currentTab" @click="useCoupon(index)" :class="{'btnDisabled':!!currentTab}">
      <p class="btnName">{{currentBtnText}}</p>
      <div class="btn-circle-right"></div>
      </button>
  	</div>
    
  </div>
</template>
<script>
import {mapState} from 'vuex';
export default{
    props: ['index', 'currentTab', 'currentBtnText'],
    data() {
        return {

        };
    },
    computed: {
        ...mapState(['global']),
      // 控制显示的内容
        computedTxt() {
            return this.methodGetByteLen(this.index.shopName, 18);
        },
        computedFunction() {
            return this.methodGetByteLen(this.index.function, 14);
        }
    },
    created () {
    },
    methods: {
        useCoupon: function(index) {
            // console.log('dianjileanniu1');
            // this.$parent.showBarcode(index);
            var startOK = true;
            var endOK = true;
            var myDate = new Date();
            var hours = myDate.getHours();
            var min = myDate.getMinutes();
            // console.log('m=' + min);
            // console.log(index.dayEndUsingTime);
            // console.log(index.couponType);
            // console.log(index.dayStartUsingTime);
            var daystart1 = index.dayStartUsingTime.split(':')[0]; // 这是 每天最早使用的时间
            var daystart2 = index.dayStartUsingTime.split(':')[1];
            var dayend1 = index.dayEndUsingTime.split(':')[0]; // 这是 每天最晚使用的时间
            var dayend2 = index.dayEndUsingTime.split(':')[1];
            // console.log('s1=' + daystart1);
            // console.log('s2=' + daystart2);
            // console.log('e1=' + dayend1);
            // console.log('e2=' + dayend2);
            if (hours < daystart1) {
                startOK = false;
            }
            if (hours.toString() === daystart1.toString() && min < daystart2) {
                startOK = false;
            }
            if (hours > dayend1) {
                endOK = false;
            }
            if (hours.toString() === dayend1.toString() && min > dayend2) {
                endOK = false;
            }
            if (hours === dayend1) {
                console.log('xx');
            }
            if (startOK === true && endOK === true) {
                if (index.couponType === '7' || index.couponType === '8') {
                    this.$parent.showBarcode(index);
                }
                if (index.couponType === '3' || index.couponType === '4') {
                    this.$router.push({ path: `/ksubmit/${index.userCouponCode}` });
                }
            } else {
                this.$vux.toast.text('未到该优惠券的使用时间', 'bottom');
            }
        },
        toCouponDetails: function(nowUserCouponCode) {
            this.$router.push({path: `/coupondetails/${nowUserCouponCode}`});
        },
        methodGetByteLen: function(str, len) { // 控制字符长度，超过则削减并添加省略号
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
    components: {

    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
.couponList
  width:10.6rem
  margin:0 auto
  overflow-x:hidden
  background:rgb(240,240,240)
  .couponItem
    position:relative
    background:#fff
    margin:0.1rem 0rem
    height:2.75rem
    width:10.8rem
    padding-left:0.24rem
    .sawtooth
      position:absolute
      top:0
      left:0rem
      height:2.75rem
      width:0.25rem
      background: radial-gradient(rgb(240,240,240) 0, rgb(240,240,240) 0.19rem, #fff 0.19rem);
      background-size: 0.55rem 0.55rem;
      background-position: 0.29rem  0.55rem;
    .img
      position:absolute
      top:0
      left:0
      margin:0.3rem 0.3rem
      height:2.15rem
      width:2.63rem
      overflow:hidden
      img
        height:100%
        background-attachment:fixed
        background-position:center
        background-repeat: no-repeat;
        background-origin:content-box
    .text
      position:absolute
      top:0
      left:3.13rem
      display:inline-block
      margin-left:0.1rem
      height:2.75rem
      width:5rem
      font-size:0.5rem
      .shopName
        margin-top:0.24rem
        font-weight:bold
        font-size:0.5rem
        line-height:0.6667rem
        .points
          height:0.6rem
          line-height:0.6rem
          // vertical-align:30%
        .distance
          float:right
          font-size:0.28rem
          color:rgb(95,95,95)
      .couponType
        margin-top:0.1rem
        height:0.4rem
        color:rgb(82,82,82)
        span
          display:inline-block
          font-size:0.3rem
          span
            margin-right:0.1rem
            display:inline-block
            width:0.4rem
            height:0.4rem
            line-height:0.4rem
            text-align:center
            font-size:0.24rem
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
      .usingTime
        margin-top:0.3rem
        height:0.48rem
        font-size:0.30rem
        line-height:0.48rem
        text-align:center
        border:1px solid rgb(246,79,72)
        border-radius:0.13rem
        width:11em
        color:rgb(246,79,72)
    .btn
      position:absolute
      top:0
      right:0.25rem
      display:inline-block
      height:2.75rem
      line-height:2.75rem
      width:2.29rem
      background:rgb(108,204,229)
      color:#fff
      border:none
      outline:none
      .btnName
        margin:0 auto
        width:4.5em
        text-align:left
        height:100%
        font-size:0.36rem
        color:#fff
      .btn-circle-right
        position:absolute
        left:1.82rem
        top:0.935rem
        width:0.88rem
        height:0.88rem
        border-radius:50%
        background:#f0f0f0
    .btnDisabled
      background:rgb(181,181,181)
      .btnName
        margin:0 auto
        width:4em
        height:100%
        font-size:0.36rem
        color:#fff
</style>
