<template>
    <div id="payresult" @click="toOrderDetail">
        <div v-show="issuccess==='true'">
            <icon class="iconv success" type="success" is-msg></icon>
            <span class="text">支付成功!</span>
            <span class="text textname">{{orderdetail.shopName}}</span>
            <span class="text textmoney">￥{{orderdetail.realPay}}</span>
            <span class="text texttime">{{orderdetail.orderTime}}</span>
        </div>
        <div v-show="issuccess==='false'">
            <icon class="iconv warn" type="warn" is-msg></icon>
            <span class="text">支付失败，请返回重试!</span>
            <p></p>
        </div>
        <p class="tip">（{{time}}s后返回）</p>
        <p class="tip">单击任意处返回</p>
    </div>
</template>
<script>
import { Icon } from 'vux';

export default {
    data() {
        return {
            time: 10,
            consumeCode: this.$route.query.consumeCode,
            issuccess: this.$route.query.issuccess,
            orderdetail: {}
        };
    },
    methods: {
        timer: function () {
            if (this.time > 1) {
                this.time--;
                setTimeout(this.timer, 1000);
            } else {
                this.toOrderDetail();
            }
        },
        toOrderDetail: function() {
            // this.$router.replace({path: `/orderDetails/${this.consumeCode}`});
            this.$router.go(-1);
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
            }).then((result) => {
                console.log(result.data.result);
                this.orderdetail = result.data.result;
                this.orderNbr = result.data.result.orderNbr;
            });
        }
    },
    created() {
        this.timer();
        this.getConsumeInfo();
    },
    components: {
        Icon
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#payresult
    position:absolute
    width:100%
    height:100%
    .iconv
        margin-top:2.5rem
        width:100%
        display:block
        text-align:center
        font-size:2.5rem
    .text
        display:block
        width:100%
        text-align:center
        margin-top:1rem
        font-size:0.7rem
    .textname
        margin-top:2rem
        color:#09BB07
        font-size:0.48rem
    .textmoney
        margin-top:0.5rem
        font-weight:bold
        font-size:0.78rem
    .texttime
        margin-top:0.5rem
        font-size:0.48rem
        margin-bottom:3rem
    .tip
        text-align:center
        line-height:0.8rem
</style>
