<template>
<div class="recharge">
    <div class="wrap">
        <div class="name">充值金额</div>
        <div class="money">
            <span class="money-item" :class="{'money-item-active': moneyactive===50000}" @click="choose(50000)">￥50.00</span>
            <span class="money-item" :class="{'money-item-active': moneyactive===10000}" @click="choose(10000)">￥100.00</span>
            <span class="money-item" :class="{'money-item-active': moneyactive===1}" @click="choose(1)">￥0.01</span>
        </div>
    </div>
        <div class="rechargebtn" @click="recharge">充值</div>
        支付回执显示:{{msg}}
        <br><br><br><br>
        <center>跳转工银e生活测试</center>
        
        <br><br>
        <div class="rechargebtn" @click="openApp()">点击打开e生活</div>

</div>
</template>
<script>
    import {mapState} from 'vuex';
    export default{
        data () {
            return {
                sum: 1,
                moneyactive: 1,
                msg: '',
                paydata: {}
            };
        },
        computed: {
            ...mapState(['userCode', 'global'])
        },
        methods: {
            choose: function(sum) {
                this.sum = sum;
                this.moneyactive = sum;
            },
            recharge: function() {
                console.log({'userCode': this.userCode, 'payPrice': this.sum});
                this.axios({
                    method: 'post',
                    url: 'https://hfq.huift.com.cn/Admin/Lcy/weiPay',
                    data: {'userCode': this.userCode, 'payPrice': this.sum},
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
                    this.paydata = result.data;
                    this.callpay();
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
                            that.$vux.toast.show({
                                type: 'text',
                                text: '取消充值'
                            });
                        }else{
                            that.$vux.toast.show({
                                type: 'success',
                                text: '充值成功'
                            });
                        }
                    }
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
        },
        openApp: function(){
            var ua = navigator.userAgent;
    var timer;
    var downUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.icbc.elife";

    //判断是否是融e联
    if (ua.indexOf('ICBCAndroidBS') > -1) {
        window.location.href = 'com.icbc.elife://elife'; //跳到 安卓 url scheme
        timer = window.setTimeout(function () {
            window.location.href = downUrl;
        }, 1000)
    } else if (ua.indexOf('ICBCiPhoneBS') > -1) {
        window.location.href = 'com.icbc.elife://';//跳到ios  url scheme地址
        timer = window.setTimeout(function () {
            window.location.href = downUrl;
        }, 1000)
    } else {
        window.location.href = downUrl;//应用宝地址  
    }
        }
         /* eslint-enable */
        },
        created() {
            /*eslint-disable */
            !(function(win, lib) {
                var timer;
                var doc = win.document;
                var docElem = doc.documentElement;
                var vpMeta = doc.querySelector('meta[name="viewport"]');
                var flexMeta = doc.querySelector('meta[name="flexible"]');
                var dpr = 0;
                var scale = 0;
                var flexible = lib.flexible || (lib.flexible = {});
    // 设置了 viewport meta
    if (vpMeta) {
        console.warn('将根据已有的meta标签来设置缩放比例');
        var initial = vpMeta.getAttribute('content').match(/initial\-scale=([\d\.]+)/);
        if (initial) {
                    scale = parseFloat(initial[1]); // 已设置的 initialScale
                    dpr = parseInt(1 / scale);      // 设备像素比 devicePixelRatio
                }
            }
    // 设置了 flexible Meta
    else if (flexMeta) {
        var flexMetaContent = flexMeta.getAttribute('content');
        if (flexMetaContent) {
            var initial = flexMetaContent.match(/initial\-dpr=([\d\.]+)/);
            var maximum = flexMetaContent.match(/maximum\-dpr=([\d\.]+)/);

            if (initial) {
                dpr = parseFloat(initial[1]);
                scale = parseFloat((1 / dpr).toFixed(2));
            }

            if (maximum) {
                dpr = parseFloat(maximum[1]);
                scale = parseFloat((1 / dpr).toFixed(2));
            }
        }
    }
    // viewport 或 flexible meta 均未设置
    if (!dpr && !scale) {
        var u = (win.navigator.appVersion.match(/android/gi), win.navigator.appVersion.match(/iphone/gi));
        var _dpr = win.devicePixelRatio;
        dpr = u ? ((_dpr >= 3 && (!dpr || dpr >= 3))
            ? 3
            : (_dpr >= 2 && (!dpr || dpr >= 2))
            ? 2
            : 1
            )
        : 1;

        scale = 1 / dpr;
    }

    docElem.setAttribute('data-dpr', dpr);

    // 插入 viewport meta
    if (!vpMeta) {
        vpMeta = doc.createElement('meta');

        vpMeta.setAttribute('name', 'viewport');
        vpMeta.setAttribute('content',
            'initial-scale=' + scale + ', maximum-scale=' + scale + ', minimum-scale=' + scale + ', user-scalable=no');

        if (docElem.firstElementChild) {
            docElem.firstElementChild.appendChild(vpMeta);
        } else {
            var div = doc.createElement('div');
            div.appendChild(vpMeta);
            doc.write(div.innerHTML);
        }
    }

    function setFontSize() {
        var winWidth = docElem.getBoundingClientRect().width;

        // if (winWidth / dpr > 540) {
        //     (winWidth = 540 * dpr);
        // }

        // 根节点 fontSize 根据宽度决定
        var baseSize = winWidth / 10.8;

        docElem.style.fontSize = baseSize + 'px';
        flexible.rem = win.rem = baseSize;
    }

    // 调整窗口时重置
    win.addEventListener('resize', function() {
        clearTimeout(timer);
        timer = setTimeout(setFontSize, 300);
    }, false);
    // orientationchange 时也需要重算下
    win.addEventListener('orientationchange', function() {
        clearTimeout(timer);
        timer = setTimeout(setFontSize, 300);
    }, false);

    // pageshow
    // keyword: 倒退 缓存相关
    win.addEventListener('pageshow', function(e) {
        if (e.persisted) {
            clearTimeout(timer);
            timer = setTimeout(setFontSize, 300);
        }
    }, false);

    // 设置基准字体
    if (doc.readyState === 'complete') {
        doc.body.style.fontSize = 12 * dpr + 'px';
    } else {
        doc.addEventListener('DOMContentLoaded', function() {
            doc.body.style.fontSize = 12 * dpr + 'px';
        }, false);
    }

    setFontSize();

    flexible.dpr = win.dpr = dpr;

    flexible.refreshRem = setFontSize;

    flexible.rem2px = function(d) {
        var c = parseFloat(d) * this.rem;
        if (typeof d === 'string' && d.match(/rem$/)) {
            c += 'px';
        }
        return c;
    };

    flexible.px2rem = function(d) {
        var c = parseFloat(d) / this.rem;

        if (typeof d === 'string' && d.match(/px$/)) {
            c += 'rem';
        }
        return c;
    };
}(window, window.lib || (window.lib = {})));

/* eslint-enable */
        },
        components: {
        }
    };
</script>
<style lang="stylus" rel="stylesheet/stylus">
.recharge
    position:absolute
    width:100%
    height:100%
    background:#EDEDED
    .wrap
        margin-top:1rem
        padding:0.5rem 0.5rem 0.2rem 0.5rem
        background:#fff
        .name
            font-size:0.5rem
        .money
            display:flex
            margin:0.4rem 0.2rem
            font-size:0.45rem
            font-weight:bold
            .money-item
                flex:1
                margin:0 0.3rem
                padding:0.5rem 0.45rem
                text-align:center
                border:1px solid #cecece
                border-radius:0.1rem
            .money-item-active
                color:#62b900
                border:1px solid #62b900
    .rechargebtn
        margin:0.5rem auto
        width:9rem
        height:1rem
        line-height:1rem
        text-align:center
        background:#62b900
        color:#fff
        border-radius:0.1rem
</style>
