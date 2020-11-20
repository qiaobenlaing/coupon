<template>
    <div id="orderList">
        <div class="order" v-for="order in orderList" :key="order.consumeCode" @click="showOrder(order)">
            <div class="orderHead">
                <td class="left">订单&nbsp;{{order.orderNbr}}</td>
                <td class="right" v-if="order.userConsumeStatus=='1'">未付款</td>
                <td class="right" v-if="order.userConsumeStatus=='2'">付款中</td>
                <td class="right" v-if="order.userConsumeStatus=='3'">已付款</td>
                <td class="right" v-if="order.userConsumeStatus=='4'">已取消付款</td>
                <td class="right" v-if="order.userConsumeStatus=='5'">付款失败</td>
                <td class="right" v-if="order.userConsumeStatus=='6'">退款申请中</td>
                <td class="right" v-if="order.userConsumeStatus=='7'">已退款</td>
            </div>
            <div class="orderBody">
                <div class="orderLogo">
                    <div class="logo1">
                        <img v-lazy="global.hftcomImgSrc+order.logoUrl" alt="">
                    </div>
                </div>
                <div class="orderDetial">
                    <div class="shopName">{{order.shopName}}</div>
                    <div class="money">订单总金额：<span class="theme-red">{{order.realPay}}元</span></div>
                </div>
            </div>
            <div class="orderFoot">
                <div class="orderTime">下单时间：{{order.orderTime}}</div>
            </div>
        </div>
    </div>
</template>
<script>
import {mapState} from 'vuex';
export default{
    props: ['orderList'],
    computed: {
        ...mapState(['global'])
    },
    methods: {
        showOrder(order) {
            // console.log(consumeCode); // 传入的订单码
            this.$router.push({path: `/orderDetails/${order.consumeCode}`, query: {order: order}});
        }
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#orderList
    background-color #f3f3f3
    .order
        width 100%
        height : 4.5rem
        font-size :0.5rem
        margin-bottom 4px
        color #3D3D3D
        background-color #fff
        .orderHead
            height:1rem
            font-size:0.4rem
            line-height:1rem
            color #A9A9A9
            .left
                float:left
                margin-left 0.4rem
            .right
                float:right
                margin-right 0.6rem
        .orderBody
            height :2.5rem
            display flex
            .orderLogo
                flex 2
                .logo1
                    height 2rem
                    width 2rem
                    display: block;
                    margin: 0 auto;
                    img 
                        height 100%
                        width 100%
                        border-radius: 6%                    
            .orderDetial
                flex 5
                font-size:0.35rem
                .shopName
                    height 50%
                    font-size 0.5rem
                    line-height 1rem
                .orderTime
                    height 50%
                    font-size 0.35rem
                    line-height 1rem
                    color #A9A9A9
        .orderFoot    
            height :1rem
            font-size:0.35rem
            line-height 1rem
            margin-left 0.4rem
            .foot1
                color:#A9A9A9
            .realPay
                font-size:0.37rem
                color #f64f48
                font-weight: bold  /* 字体加粗 */
</style>
