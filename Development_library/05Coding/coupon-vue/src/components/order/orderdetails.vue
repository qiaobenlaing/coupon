<template>
    <div id="orderdetails">
        <div id="header2">
            <header class="header">
                <span class="header-item icon-back" @click='goBack'></span>
                <span class="header-item"></span>
            </header>
        </div>
        <div class="consumeTime">
            {{orderdetail.consumeTime}}&nbsp;&nbsp;
        </div>
        <div class="shopName">
            {{orderdetail.shopName}}
        </div>
        <div class="orderNur">
                订单号：{{orderdetail.orderNbr}}
        </div>
        <barcode v-bind:value="orderNbr" class="barcode" :options="{ lineColor: '#990000' }">
        </barcode>
        <div class="orderAmount">
            <td class="Amount1">消费金额</td>
            <td class="Amount2">{{orderdetail.orderAmount}}元</td>
        </div>
        <div class="fill"></div>
        <div class="status">
            <td class="status1">订单状态</td>
            <td class="status2" v-if="orderdetail.status=='1'">未付款</td>
            <td class="status2" v-if="orderdetail.status=='2'">付款中</td>
            <td class="status2" v-if="orderdetail.status=='3'">已付款</td>
            <td class="status2" v-if="orderdetail.status=='4'">已取消付款</td>
            <td class="status2" v-if="orderdetail.status=='5'">付款失败</td>
            <td class="status2" v-if="orderdetail.status=='6'">退款申请中</td>
            <td class="status2" v-if="orderdetail.status=='7'">已退款</td>
        </div>
        <div class="deduction" >
            <td class="deduction1">优惠金额</td>
            <td class="deduction2">{{orderdetail.deduction}}元</td>
        </div>
        <div class="realPay">
            <td class="realPay1">总金额</td>
            <td class="realPay2">{{orderdetail.realPay}}元</td>
        </div>
        <!-- <div class="paybox">
            <div class="pay" v-if="orderdetail.status=='1'" @click="buyCoupon">付款</div>
            <div class="cancel" v-if="orderdetail.status=='1'" @click="cancelOrder()">取消订单</div>
            <div class="cancel" v-if="orderdetail.status=='3'" @click="torefund">退款</div>
        </div> -->
    </div>
</template>
<script>
import VueBarcode from 'vue-barcode';
import {mapState} from 'vuex';
export default{
    data() {
        return {
            consumeCode: this.$route.params.consumeCode,   // 搜索详情使用的消费码
            orderdetail: [],   // 订单详情信息
            orderNbr: '',
            order: this.$route.query.order,
            ispay: this.$route.query.ispay  // 这个参数判断页面是否是由
        };
    },
    computed: {
        ...mapState(['global'])
    },
    methods: {
        goBack () {
            this.$router.go(-1);
            // console.log('返回');
            // if (this.ispay === 1) {
            //     this.$router.replace({path: `/index`});   // 如果ispay===1 说明是由支付结果页跳转到详情页面
            // } else {
            //     this.$router.replace({path: `/order`});
            // }
        },
        showDeduction() {
            console.log('显示优惠详情');   // 该方法暂时保留
        },
        getConsumeInfo() {
            console.log(this.$store.state.global.hftcomShop);
            console.log(this.consumeCode);
            this.axios({
                method: 'post',
                url: this.$store.state.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"getConsumeInfo","params":{"consumeCode":"' + this.consumeCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result1) => {
                console.log(result1.data.result);
                this.orderdetail = result1.data.result;
                this.orderNbr = result1.data.result.orderNbr;
            });
        },
        buyCoupon: function() {
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"bankcardPayConfirm","params":{"consumeCode":"' + this.consumeCode + '","bankAccountCode":"","valCode":""}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (result.data.result.code === 50000) {
                    this.$router.replace({path: '/payResult', query: {isSuccess: true, consumeCode: this.consumeCode}});
                } else {
                    switch (result.data.result.code) {
                    case 80221:
                        this.$vux.toast.text('抢完了', 'bottom');
                        break;
                    default:
                        this.$vux.toast.text(result.data.result, 'bottom');
                    }
                }
            });
        },
        cancelOrder: function() {
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"cancelBankcardPay","params":{"consumeCode":"' + this.consumeCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (result.data.result.code === 50000) {
                    this.$router.go(0);
                }
            });
        },
        torefund: function() {
            console.log('跳转到退款模拟页面');
            console.log(this.order);
            this.$router.push({path: `/refund/${this.order.consumeCode}`});
        }
    },
    created() {
        this.getConsumeInfo();
    },
    components: {
        'barcode': VueBarcode
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#orderdetails
    color #3D3D3D
    .consumeTime
        width 100%
        height :0.6rem
        line-height 0.6rem
        text-align:right
        margin-right 0.1rem
        background-color #f3f3f3
    .shopName
        line-height 1rem
        height :1rem
        text-indent:0.4rem
        font-size 0.4rem
        border-bottom 2px solid #f3f3f3
    .orderNur
        height 0.7rem
        line-height 0.7rem
        text-indent:0.4rem  /* 文字缩进 */
    .barcode
        .vue-barcode-element   /* 这是一个隐藏的元素  */
            height 3rem
            display: block;
            margin: 0 auto;
    .orderAmount
        border-top  1px solid #f3f3f3
        height 1rem       
        width 100%
        line-height 1rem
        text-indent:0.4rem  /* 文字缩进 */
        border-bottom 1px solid #f3f3f3
        .Amount1
            float left 
        .Amount2
            float right
            margin-right 0.4rem
            font-weight: bold  /* 字体加粗 */
    .fill
        height 0.3rem
        background-color #f3f3f3
        width 100%
    .status
        height 1rem       
        width 100%
        line-height 1rem
        text-indent:0.4rem  /* 文字缩进 */
        border-bottom 1px solid #f3f3f3
        .status2
            color #ADADAD 	
    .deduction
        height 1rem       
        width 100%
        line-height 1rem
        text-indent:0.4rem  /* 文字缩进 */
        border-bottom 1px solid #f3f3f3
        .deduction1
            float left 
        .deduction2
            float right
            margin-right 0.4rem
            font-weight: bold  /* 字体加粗 */
    .realPay
        height 1rem       
        width 100%
        line-height 1rem
        text-indent:0.4rem  /* 文字缩进 */
        border-bottom 1px solid #f3f3f3
        .realPay1
            float left 
        .realPay2
            float right
            margin-right 0.4rem
            font-weight: bold  /* 字体加粗 */
            color #f64f48
    .paybox
        position:fixed
        bottom:0
        left:0
        width:100%
        height:1.2rem
        font-size:0.44rem
        .cancel
            float:right
            display:inline-block
            width:2.5rem
            height:0.8rem
            border:1px solid #cecece
            line-height:0.8rem
            text-align:center
            border-radius:0.1rem
            margin-right:0.5rem
        .pay
            float:right
            display:inline-block
            width:2rem
            height:0.8rem
            line-height:0.8rem
            text-align:center
            border:1px solid #f64f48
            border-radius:0.1rem
            color:#f64f48
            margin-right:0.5rem
#header2
    .header
        display:flex
        height:1.24rem
        line-height:1.24rem
        background:#f64f48
        color:#fff
        .header-item
          flex:1
          text-align:center
        :nth-child(1)
          font-size:0.78rem
          font-weight:bold
          line-height:1.24rem
        :nth-child(2)
          flex:6
          font-size:0.5rem
          line-height:1.24rem
          padding-right:0.5rem
</style>
