<template>
<div id="submitorder">
    <header2 :title="'提交订单'"></header2>
    <div class="order-item order-item-more">
      <div class="img"><img :src="global.hftcomImgSrc + currentOrderInfo.logoUrl" alt=""></div>
        <div class="orderName">{{currentOrderInfo.couponName}}{{currentOrderInfo.function}}{{currentOrderInfo.remark}}</div>
        <div class="payPrice">
            <span :class="activeCode !== '' && activeCode !== undefined? 'pricespan':''">￥{{currentOrderInfo.payPrice}}元</span>
            <span v-if="activeCode !== '' && activeCode !== undefined && this.appType === 1">￥{{discountPrice}}元</span>
        </div>
    </div>
    <div class="order-item">
        <label for="count">数量:</label>
        <div class="count-inp">
        <span class="nbrPerPerson" v-show="currentOrderInfo.nbrPerPerson!=='0'">每个用户限领{{currentOrderInfo.nbrPerPerson}}张</span>
        <div class="minus" :class="{'minus-disabled':count<2}" @click="minus">-</div>
        <div class="inp"><input v-model.number="count" id="count" value="1"></div>
        <div class="add" @click="add">+</div>
        </div>
    </div>
    <div class="order-item">
        <label for="price">实付款:</label>
      <span class="totalPrice">{{totalPrice}}元</span>
    </div>
    <div class="pay" @click="submitOrder">提交订单</div>
</div>
</template>
<script>
import {mapState} from 'vuex';
import header2 from '@/components/header/header2';
export default {
    data() {
        return {
            count: 1,
            isSucess: Boolean,
            currentOrderInfo: {
                countMyReceived: 0,
                'logoUrl': '',
                payPrice: 0
            },
            batchCouponCode: this.$route.params.batchCouponCode,
            activeCode: this.$route.query.activeCode,
            discountPrice: 0
        };
    },
    computed: {
        ...mapState(['global', 'CouponListInfo', 'isBLR', 'isLogin', 'userCode', 'appType']),

        totalPrice: function() {
            if (this.activeCode !== '' && this.activeCode !== undefined) {
                return (this.count * this.discountPrice).toFixed(2);
            } else {
                return (this.count * this.currentOrderInfo.payPrice).toFixed(2);
            }
        }
    },
    watch: {
        count: function() {
            if (this.currentOrderInfo.countMyReceived === undefined) {
                this.currentOrderInfo.countMyReceived = 0;
            }
            this.count = Math.round(this.count);
            if (this.count > (this.currentOrderInfo.nbrPerPerson - this.currentOrderInfo.countMyReceived) || this.count > this.currentOrderInfo.nbrPerPerson) {
                this.count = this.currentOrderInfo.nbrPerPerson - this.currentOrderInfo.countMyReceived;
                this.$vux.toast.text('您已到达领取限额', 'middle');
            }
        }
    },
    methods: {
        goBack () {
            this.$router.go(-1);
        },
        add: function() {
            this.count++;
        },
        minus: function() {
            if (this.count < 2) {
                return;
            } else {
                this.count--;
            }
        },
        showIndexCouponDetails: function() {
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"getBachCouponInfo","params":{"batchCouponCode":"' + this.batchCouponCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                this.currentOrderInfo = result.data.result.BatchCouponInfo;
                this.shopDecoration = result.data.result.ShopDecoration;
                this.currentOrderInfo.logoUrl = result.data.result.ShopDecoration.imgUrl;
                this.getUserCouponNbr();
                this.getDiscountPrice();
            });
        },
        getDiscountPrice: function() {
            this.axios({
                method: 'post',
                url: '/Api/Activity',
                data: '{"id":19,"jsonrpc":"2.0","method":"getPrice","params":{"activityCode":"' + this.activeCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                console.log(result);
                this.discountPrice = result.data.result;
            });
        },
        getUserCouponNbr: function() { // 拿到同一批券的券码，进行数据更新
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"getUserCouponNbr","params":{"userCode":"' + this.userCode + '","batchCouponCode":"' + this.batchCouponCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (this.currentOrderInfo.countMyReceived === undefined) {
                    this.currentOrderInfo.countMyReceived = 0;
                }
                if (result.data.result === [] || result.data.result === undefined) {
                    result.data.result = [];
                } else {
                    result.data.result.filter((element, index, self) => {
                        if (element.status === '1' || element.status === '2') {
                            this.currentOrderInfo.countMyReceived++;
                        }
                    });
                }
            });
        },
        submitOrder: function() {
            if ((this.currentOrderInfo.nbrPerPerson !== '0') && (this.currentOrderInfo.nbrPerPerson - this.currentOrderInfo.countMyReceived) < 1) {
                this.$vux.toast.text('您已经购买了' + this.currentOrderInfo.countMyReceived + '张，无法再次购买', 'middle');
            } else {
                var dataStr;
                if (this.activeCode !== '' && this.activeCode !== undefined) {
                    dataStr = '{"id":19,"jsonrpc":"2.0","method":"addCouponOrder","params":{"userCode":"' + this.userCode + '","shopCode":"' + this.currentOrderInfo.shopCode + '","batchCouponCode":"' + this.currentOrderInfo.batchCouponCode + '","couponNbr":' + this.count + ',"platBonus":0,"shopBonus":0,"activityCode":"' + this.activeCode + '"}}';
                } else {
                    dataStr = '{"id":19,"jsonrpc":"2.0","method":"addCouponOrder","params":{"userCode":"' + this.userCode + '","shopCode":"' + this.currentOrderInfo.shopCode + '","batchCouponCode":"' + this.currentOrderInfo.batchCouponCode + '","couponNbr":' + this.count + ',"platBonus":0,"shopBonus":0}}';
                }
                this.axios({ // 这是提交订单方法
                    method: 'post',
                    url: this.global.hftcomClient,
                    data: dataStr
                }).then((order) => {
                    switch (order.data.result.code) {
                    case 50000:
                        this.order = order.data.result;
                        this.order.batchCouponCode = this.currentOrderInfo.batchCouponCode;
                        this.order.payType = 'BUYCOUPON';
                        console.log(order.data.result);
                        this.$router.replace({path: '/gotopay', query: {order: this.order}});
                        break;
                    case '80238':
                        this.$vux.toast.text('您已经达到购买上限', 'middle');
                        break;
                    case 9527:
                        this.$vux.toast.text('请在个人中心完成手机号的绑定', 'bottom');
                        break;
                    case 9377:
                        this.$vux.toast.text('未到优惠券领用时间', 'bottom');
                        break;
                    case '80220':
                        this.$vux.toast.text('当前时间无法使用', 'middle');
                        break;
                    case '80221':
                        this.$vux.toast.text('此优惠券已抢光，无法购买', 'middle');
                        break;
                    default:
                        console.log(order.data.result.code);
                    }
                });
            }
        }
    },
    created() {
        this.showIndexCouponDetails();
    },
    components: {
        header2
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#submitorder
  background:rgb(240,240,240)
  .order-item
    width:9.8rem
    height:1.5rem
    line-height:1.5rem
    border-bottom:1px solid #cecece60
    display: inline-block
    padding:0 0.5rem
    background:#fff
    label
      font-size:0.46rem
      color:#3e3e3e
    .inp
      display:inline-block
      input
        width:1.5rem
        text-align:center
    .count-inp
      display:inline
      float:right
      .nbrPerPerson
        border:none
        color:#f64f48
        font-size:0.28rem
        margin-right:0.5rem
      .minus
        display:inline
        margin-left:0rem
        width:0.5rem
        height:0.5rem
        line-height:0.5rem
        font-size:0.4rem
        padding:0.1rem 0.38rem
        border:1px solid #f64f48
        border-radius:10%
      .minus-disabled
        border-color:#cecece
      .add
        display:inline
        width:0.5rem
        height:0.5rem
        font-size:0.4rem
        padding:0.1rem 0.3rem
        border:1px solid #f64f48
        border-radius:10%
    .totalPrice
      float:right
      font-size:0.55rem
      font-weight:bold
      color:#f64f48
      display inline-block
  .order-item-more
    position:relative
    height:3rem
    margin-bottom:0.3rem
    .img
      display:inline-block
      margin-top:0.5rem
      height:2rem
      width:3rem
      overflow:hidden
      img
        height:100%
        width:auto
    .orderName
      display:inline-block
      position:absolute
      top:0.4rem
      left:4rem
      width:6rem
      line-height:0.7rem
      font-size:0.48rem
      font-weight:bold
    .payPrice
      display:inline
      position:absolute
      right:2rem
      bottom:0rem
      font-size:0.44rem
      font-weight:bold
      color:#f64f48
      .pricespan
        text-decoration:line-through
  .pay
    position:fixed
    bottom:0
    width:10.8rem
    height:1.5rem
    line-height:1.5rem
    text-align:center
    font-size:0.5rem
    background:#f64f48
    color:#fff
</style>