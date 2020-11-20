<template>
    <div id="gotoPay">
        <header2fix :title="'支付订单'"></header2fix>
            <div class="realPay">
                <span class="realPay1">支付金额</span>
                <span class="realPay2">{{order.realPay}}</span>
                <span class="shopName">{{order.shopName}}</span>
            </div>
        <div class="payWay">
            <p class="orderNbr">订单号：{{order.orderNbr}}</p>
            <p class="">支付方式</p>
            <p class="weixin"><label for=""><img class="weixinIco" src="../../../static/ico/weixin.png" alt="">
      <icon class="success" type="success"></icon>
  </label></p>
            
        </div>
        <div class="pay" @click="toPay()">确认支付</div>
    </div>
</template>
<script>
import { mapState } from 'vuex';
import VueBarcode from 'vue-barcode';
import { Icon } from 'vux';
import header2fix from '@/components/header/header2fix';
export default {
    data() {
        return {
            OCS: false,
            info: '',
            order: this.$route.query.order,
            shopName: this.$route.query.order.shopName,
            payCode: '',
            maskShow: false, // 这是一个判断遮罩层的参数
            paydata: {},
            isPay: 0, // 判断支付的状态做出各种操作，0-刚进入不做任何操作，点击物理返回键/触发支付行为后取消支付-需要取消订单后直接返回，1-，2-默认，不做出任何操作
            clickurl: ''
        };
    },
    computed: {
        ...mapState(['global', 'CouponListInfo', 'isBLR', 'isLogin', 'userCode'])
    },
    methods: {
        goBack () {
            this.$router.go(-1);
        },
        cancelTip () {
            let that = this;
            this.$vux.confirm.show({
                title: '是否取消支付',
                // content: '确认表示支付',
                confirmText: '继续支付',
                cancelText: '取消支付',
                onCancel () {
                    console.log('取消支付');  // 取消支付其实是把订单状态由“支付中”改为“未支付”
                    that.cancelOrder();
                    that.goBack();
                },
                onConfirm () {
                    console.log('继续支付');
                }
            });
        },
        splitUrl() {
            let url = window.location.href;
            let kv = url.split('gotopay');
            return kv[0];
        },
        toPay() {
            // 微信支付
            console.log(this.order.payType);
            if (this.order.payType === 'BUYCOUPON') {
                this.weixinPayCoupon();
            } else if (this.order.payType === 'PAYMONEY') {
                this.weixinPayMoney();
            } else {
                alert('流程错误，请重试');
            }
        },
        weixinPayCoupon: function() {
            this.isPay = 1;
            console.log({'consumeCode': this.order.consumeCode, 'batchCouponCode': this.order.batchCouponCode, 'userCode': this.userCode});
            // this.getAccessToken();
            this.axios({
                method: 'post',
                url: this.global.Daipay,
                data: {'consumeCode': this.order.consumeCode, 'batchCouponCode': this.order.batchCouponCode, 'userCode': this.userCode},
                transformRequest: [function (data) {
                    let ret = '';
                    for (let it in data) {
                        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&';
                    }
                    return ret;
                }],
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then((result) => {
                console.log(result.data);
                console.log(JSON.parse(result.data.jsapi));
                this.paydata = JSON.parse(result.data.jsapi);
                this.callpay();
            });
        },
        weixinPayMoney: function() {
            console.log({'consumeCode': this.order.consumeCode, 'orderNbr': this.order.orderNbr, 'userCode': this.userCode});
            console.log('买单');
            console.log(this.global);
            this.axios({
                method: 'post',
                url: this.global.Dipay,
                data: {'consumeCode': this.order.consumeCode, 'orderNbr': this.order.orderNbr, 'userCode': this.userCode},
                transformRequest: [function (data) {
                    let ret = '';
                    for (let it in data) {
                        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&';
                    }
                    return ret;
                }],
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then((result) => {
                console.log(result);
                this.paydata = JSON.parse(result.data.jsapi);
                this.callpay();
            });
        },
        cancelOrder() { // 取消支付其实是把订单状态由“支付中”改为“未支付”
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"cancelBankcardPay","params":{"consumeCode":"' + this.order.consumeCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                console.log(result.data.result.code);
                if (result.data.result.code === 50000) {
                    this.$vux.toast.text('取消支付', 'bottom');
                }
                if (result.data.result.code === 20000) {
                    this.$vux.toast.text('取消支付失败', 'bottom');
                }
                if (result.data.result.code === 50403) {
                    this.$vux.toast.text('该支付已经取消', 'bottom');
                }
            });
        },
        getAccessToken: function() {
            this.axios({
                method: 'get',
                url: this.global.getAT,
                data: '',
                dataType: 'json'
            }).then((result) => {
                this.clickurl = this.global.hftcom + '/Admin/TestClient/click?access_token=' + result.data.access_token + '&batchCouponCode=' + this.order.batchCouponCode;
                console.log(this.clickurl);
            });
        },
        /* eslint-disable */
        jsApiCall: function() {
            let that = this;
            WeixinJSBridge.invoke(
                'getBrandWCPayRequest',
                this.paydata,
                function(res) {
                    if (res.err_msg) {
                        if (res.err_msg === 'get_brand_wcpay_request:cancel') {
                            that.isPay = 0;
                            that.goBack();
                        }else{
                            that.isPay = 1;
                            that.goBack();
                            // window.location = that.clickurl;
                        }
                    }
                }
            );
        },
        callpay: function () {
            if (typeof WeixinJSBridge === 'undefined') {
                if (document.addEventListener) {
                    document.addEventListener('WeixinJSBridgeReady', this.jsApiCall, false);
                } else if (document.attachEvent) {
                    document.attachEvent('WeixinJSBridgeReady', this.jsApiCall);
                    document.attachEvent('onWeixinJSBridgeReady', this.jsApiCall);
                }
            } else {
                this.jsApiCall();
            }
        },
         /* eslint-enable */
        pushHistory: function () {
            let that = this;
            window.addEventListener('popstate', function(e) {
                that.cancelOrder();
            }, false);
        }
    },
    created() {
        if (this.shopName === '' || this.shopName === undefined) {
            console.log('目前为空');
            this.$vux.toast.text('当前页面已经失效', 'bottom');
            this.goBack();
        }
    },
    mounted() {
    },
    beforeDestroy() {
        switch (this.isPay) {
        case 0:
            this.cancelOrder();
            return;
        case 1:
            return;
        }
    },
    components: {
        'barcode': VueBarcode,
        Icon,
        header2fix
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#gotoPay
    position:absolute
    width:100%
    height:100%
    background:rgb(240,240,240)
    .realPay
        height 4.2rem
        width 100%
        margin-top 1.44rem
        background:#fff
        color #8B8B7A
        line-height 0.87rem
        .realPay1
            margin-top 0.44rem
            font-size:0.48rem
            display:inline-block
            width:100%
            text-align:center
        .realPay2
            margin-top 0.32rem
            font-size:0.89rem
            display:inline-block
            width:100%
            text-align:center
            color red
            font-weight: bold  /* 字体加粗 */
        .shopName
            margin-top 0.2rem
            font-size:0.41rem
            display:inline-block
            width:100%
            text-align:center
            color:#3e3e3e
    .payWay
        width:100%
        line-height:1rem
        color:#3e3e3e
        background:#fff
        text-align:left
        margin-top:0.2rem
        padding-left:0.6rem
        font-size:0.44rem
        .orderNbr
            font-size:0.35rem
            color:#cecece
        .weixin
            height:2rem
            line-height:2rem
            padding-left:1rem
            .weixinIco
                width:0.7rem
                height:0.7rem
            .success
                line-height:2rem
                float:right
                margin-right:1.5rem
                font-size:0.45rem
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
