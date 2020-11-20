<template>
    <div id="weixinPay">
        <button @click="callpay">callpay</button>
        <div>{{msg}}</div>
    </div>
</template>
<script>
/* eslint-disable */
import Services from '@/store/services';
import {mapState} from 'vuex';
export default{
    data() {
        return {
            msg:'',
            htmldata: this.$route.query.htmldata
        };
    },
    computed: {
        ...mapState(['weixin'])
    },
    props: [],
    methods: {
        jsApiCall: function() {
            let that = this;
            WeixinJSBridge.invoke(
            'getBrandWCPayRequest',
            this.htmldata,
            function(res) {
                that.msg = res;
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
        }
    },
    created() {
        console.log(this.htmldata);
    },
    mounted() {
        // var html = document.querySelector('body');
        // var script = document.createElement('script');
        // console.log(script);
        // html.append(this.htmldata);
        // html.append(script);
    },
    updated() {
    }
};
 /* eslint-enable */
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
</style>
