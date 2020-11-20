<template>
  <div>
      <!-- <keep-alive include="index,myInformation,myCoupon"> -->
    <router-view></router-view>
    <!-- </keep-alive> -->
<div id="login" v-show="showLoginBox">
    <div class="mask" @click="isShowLoginBox(false)"></div>
    <div class="table">
        <div class="img"><img src="../../common/image/logo.jpg" alt=""></div>
        <form action="">
        <span class="bindTips" v-show="isBLR===3">已绑定手机号：</span>
        <p><input v-model.trim="usermobileNbr" type="text" placeholder="请输入手机号"></p>
        <p v-show="isBLR!==3"><input class="vcodeInp" v-model.trim="vcode" type="text" placeholder="验证码" autocomplete="off"><span class="vcode" :class="{'disabled':isGetVcode}" :disabled="isGetVcode"  @click="getVcode"><span v-show="!isGetVcode">获取短信验证码</span><span v-show="isGetVcode">{{time}}s</span></span></p>
        <span class="loginbtn" @click="submitForm"><span v-show="isBLR===0">绑定</span><span v-show="isBLR===3">修改</span><span v-show="isBLR===1">登录</span><span v-show="isBLR===2">注册</span></span>
        <span class="cancel" @click="cancelBind">取消</span>
        <span class="toRegister" v-show="isBLR===0"></span>
        <span class="toRegister" v-show="isBLR===1" @click="toRegister">没有账号？切换为注册-></span>
        <span class="toRegister" v-show="isBLR===2" @click="toLogin">有账号？切换为登录-></span>
        </form>
    </div>
</div>
  </div>
</template>
<script>
import {mapState, mapMutations} from 'vuex';
// import md5 from 'js-md5';

export default{
    data() {
        return {
            usermobileNbr: '',
            vcode: '',
            isGetVcode: false,
            time: Number,
            cityList: []
        };
    },
    computed: {
        ...mapState(['showLoginBox', 'mobileNbr', 'password', 'isChooseCity', 'localCity', 'isBLR', 'global', 'phoneNum', 'userCode'])
    },
    beforeCreate() {
    },
    methods: {
        ...mapMutations(['isShowLoginBox']),
        timer: function () {
            if (this.time > 1) {
                this.time--;
                setTimeout(this.timer, 1000);
            } else {
                this.isGetVcode = false;
            }
        },
        getVcode: function() {
            var mobi = /^[1][3,4,5,7,8][0-9]{9}$/;
            if (!mobi.test(this.usermobileNbr)) {
                this.$vux.toast.text('手机号格式错误', 'middle');
            } else {
                this.time = 60;
                this.timer();
                this.isGetVcode = true;
                this.axios({
                    method: 'POST',
                    url: this.global.hftcomShop,
                    data: '{"id":19,"jsonrpc":"2.0","method":"send","params":{"mobileNbr":' + this.usermobileNbr + '}}',
                    responseType: 'json'
                }).then((result) => {
                    console.log(result);
                    if (result.data.result.info.Code === 'OK') {
//                      this.vcode = result.data.result.Vcode;
                    } else {
                        this.$vux.toast.text('验证码错误', 'bottom');
                    }
                });
            }
        },
        // 登录，注册，绑定
        submitForm: function() {
            let that = this;
            var mobi = /^[1][3,4,5,7,8][0-9]{9}$/;
            if (!mobi.test(this.usermobileNbr) && this.isBLR !== 3) {
                this.$vux.toast.text('请输入正确手机号', 'bottom');
                return;
            } else if ((this.vcode === '' || this.vcode === null) && this.isBLR !== 3) {
                this.$vux.toast.text('请输入验证码', 'bottom');
                return;
            } else {
                switch (this.isBLR) {
                case 0:
                    this.$store.dispatch({
                        type: 'bindMobile',
                        mobileNbr: this.usermobileNbr,
                        Vcode: this.vcode
                    }).then((data) => {
                        if (data) {
                            that.$vux.toast.text('绑定成功', 'bottom');
                            that.hideLoginBox();
                            // that.$root.reload();
                            that.$router.go(0);
                        }
                    }).catch((errorCode) => {
                        that.$vux.toast.text(errorCode.data.result.error, 'bottom');
                    });
                    break;

                case 1:
                    this.$store.dispatch({
                        type: 'loginUser',
                        mobileNbr: this.usermobileNbr,
                        Vcode: this.vcode
                    }).then((data) => {
                        if (data) {
                            that.$vux.toast.text('登录成功', 'bottom');
                            that.hideLoginBox().then(() => {
                                that.$router.go(0);
                            });
                        }
                    }).catch((errorCode) => {
                        if (errorCode === 20207) {
                            that.$vux.toast.text('账号不存在', 'bottom');
                        } else {
                            that.$vux.toast.text(errorCode, 'bottom');
                        }
                    });
                    break;
                case 2:
                    this.$store.dispatch({
                        type: 'registerUser',
                        mobileNbr: this.usermobileNbr,
                        Vcode: this.vcode
                    }).then((data) => {
                        if (data) {
                            that.$vux.toast.text('注册成功，已登录', 'bottom');
                            that.hideLoginBox();
                            that.$router.go(0);
                        }
                    }).catch((errorCode) => {
                        this.$vux.toast.text(errorCode, 'bottom');
                    });
                    break;
                case 3:
                    this.$store.state.isBLR = 0;
                    break;
                default:
                    this.$vux.toast.text('数据错误，请刷新', 'bottom');
                }
            }
        },
        // 取消绑定
        cancelBind: function() {
            this.usermobileNbr = this.phoneNum;
            this.$store.state.showLoginBox = false;
        },
        hideLoginBox: function() {
            return new Promise((resolve, reject) => {
                this.isShowLoginBox(false);
                resolve();
            });
        },
        toRegister: function() {
            this.$store.state.isBLR = 2;
        },
        toLogin: function() {
            this.$store.state.isBLR = 1;
        },
        filterName: function (arr, data) { // 筛选已开通列表里有无当前的定位城市
            var flag = false;
            arr.filter(function (element, index, self) {
                if (element.name === data) {
                    flag = true;
                }
            });
            return flag;
        },
        getUserLocalcity: function() {
            let that = this;
            this.$store.dispatch('getLocation').then((h5data) => {
                this.$store.dispatch('getOpenCity').then((cities) => {
                    that.$data.cityList = cities.result;
                    if (h5data !== that.localCity && this.filterName(that.$data.cityList, h5data)) {
                        this.$vux.confirm.show({
                            title: '定位到您在' + h5data,
                            content: '是否切换？',
                            onCancel () {
                                that.isChooseCity = true;
                            },
                            onConfirm () {
                                that.isChooseCity = true;
                                that.$store.dispatch('setLocalCity', h5data);

                                that.$vux.toast.text('已切换到' + h5data.result.city, 'bottom');
                            }
                        });
                    }
                });
            }).catch((error) => {
                this.$vux.toast.text(error + ',经纬度定在所在城市中心', 'bottom');
            // 百度ip获取地理位置
                this.$store.dispatch('getLocationBaidu').then((baidudata) => {
                    this.$store.dispatch('getOpenCity').then((cities) => {
                        that.$data.cityList = cities.result;
                        if (baidudata.content.address_detail.city !== that.localCity && this.filterName(cities.result, baidudata.content.address_detail.city)) {
                            that.$vux.confirm.show({
                                title: '定位到您在' + baidudata.content.address_detail.city,
                                content: '是否切换？',
                                onCancel () {
                                    that.isChooseCity = true;
                                },
                                onConfirm () {
                                    that.isChooseCity = true;
                                    that.$store.dispatch('setLocalCity', baidudata.content.address_detail.city);
                                    that.$vux.toast.text('已切换到' + baidudata.content.address_detail.city, 'bottom');
                                    that.$store.state.localCity = baidudata.content.address_detail.city;
                                }
                            });
                        }
                    });
                }).catch((err) => {
                    this.$vux.toast.text(err, 'bottom');
                });
            });
        }
    },
    created() {
        // 获取实时位置
        if (this.isChooseCity === true) {
        } else {
            this.getUserLocalcity();
        }

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
    },
    components: {
    },
    mounted() {
        this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"isBindMobile","params":{"userCode":"' + this.userCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (result.data.result.code === 50000) {
                    this.usermobileNbr = result.data.result.mobileNbr;
                } else {
                    this.usermobileNbr = '';
                }
            });
    }
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
.fade-enter-active, .fade-leave-active
    transition: opacity .3s
.fade-enter, .fade-leave-to
    opacity: 0;
#login
    position:fixed;
    top:0
    width:100%
    height:100%
    z-index: 1
    .mask
        position:absolute
        z-index:-1
        width:100%
        height:100%
        background:rgba(17, 17, 17, 0.7)       
    .table
        position:initial
        width:70%
        // max-width:350px
        margin:0 auto
        margin-top:18%
        padding:0.5rem
        padding-bottom:1.8rem
        font-size:0.3rem
        background:#fff
        border-radius:5px
        .bindTips
            font-size: 18px
        .img
            width:1.5rem
            height:1.5rem
            margin:0 auto
            margin-bottom:0.5rem
            img
                height:100%
        p
            width:100%
            margin-bottom:0.4rem
            padding-left:0.5rem
            height:1.2rem
            width:calc(100% - 0.5rem)
            border-radius:0.1rem
            border:1px solid #cecece
            font-size:0.48rem
            input
                height:1.2rem
                width:100%
                border-radius:0.1rem
                font-size:0.48rem
                outline:none
            .vcodeInp
                width:3.8rem
            .vcode
                display:inline-block
                width:3rem
                height:0.7rem
                line-height:0.7rem
                text-align:center
                margin:auto 0
                margin-bottom:0.15rem
                vertical-align: middle
                background:#f64f48
                border:#f64f48
                border-radius:0.1rem
                color:#fff
                font-size:0.28rem
            .disabled
                background:#aaaaaa
                border:none
        .loginbtn
            display:inline-block
            margin: 0.1rem auto
            margin-right:calc((100% - 7.2rem))
            width:1.6rem
            text-align:center
            font-size:0.5rem
            padding:0.3rem 0.9rem
            color:#fff
            background:#f64f48
            border:none
            border-radius:5px
        .cancel
            display:inline-block
            width:1.6rem
            text-align:center
            margin: 0.1rem auto
            font-size:0.5rem
            padding:0.3rem 0.9rem
            color:#fff
            background:#aaaaaa
            border:none
            border-radius:5px
        .toRegister
            margin-top:0.8rem
            float:right
            font-size:0.35rem
            color:#f64f48
            text-decoration:underline
</style>
