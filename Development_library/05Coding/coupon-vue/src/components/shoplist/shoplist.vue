<template>
    <div class="background">
        <div  class="shoplist-item">
            <div class="shopDetail" @click="toShopDetail(eachShopList.shopCode)">
            <div class="shop-logo">
                <img v-lazy="global.hftcomImgSrc+eachShopList.logoUrl">
            </div>
            <div class="shop-name">
                {{computedShopName}}
            </div>
            <div class="shop-street">
                <span class="street">{{computedStreet}}</span>
                <span class="distance" v-if="eachShopList.distance < 100"><100m</span>
                <span class="distance" v-if="100 < eachShopList.distance && eachShopList.distance < 500">>100m</span>
                <span class="distance" v-if="eachShopList.distance > 500">>500m</span>
            </div>
            </div>
            <div class="collect">
                <span v-show="isCollect ===false && isCollecting === false" class="icon-collect" @click="collectShop(eachShopList.shopCode)"></span>
                <transition name="rotate">
                <span class="icon-spinner8" v-show="isCollecting"></span>
            </transition>
                <span v-show="isCollect === true && isCollecting === false" class="icon-collect-solid theme-red" @click="collectShop(eachShopList.shopCode)"></span></div>
        </div>
    </div>
</template>
<script>
import {mapState} from 'vuex';
export default{
    data() {
        return {
            isCollect: false,
            isCollecting: false // 正在加载
        };
    },
    computed: {
        ...mapState(['global', 'collectShopList', 'isLogin', 'zoneId']),
        isEmpty: function() {
            return this.collectShopList.length;
        },
        computedShopName: function() {
            return this.methodGetByteLen(this.eachShopList.shopName, 22);
        },
        computedStreet: function() {
            return this.methodGetByteLen(this.eachShopList.street, 26);
        }
    },
    props: ['eachShopList'],
    methods: {
        toShopDetail: function(shopCode) {
            this.$router.push({path: `/shopDetail/${shopCode}`});
        },
        collectShop: function(shopCode) {
            let that = this;
            if (!this.isLogin) {
                this.$store.state.isBLR = 1;
                this.$store.state.showLoginBox = true;
                this.$vux.toast.text('请登陆', 'bottom');
            } else {
                this.isCollecting = true;
                this.axios({
                    method: 'post',
                    url: this.global.hftcomShop,
                    data: '{"id":19,"jsonrpc":"2.0","method":"showCollect","params":{"shopCode":"' + shopCode + '","userCode":"' + this.$store.state.userCode + '","zoneId":"' + this.zoneId + '"}}',
                    contentType: 'application/json',
                    dataType: 'json'
                }).then((result) => {
                    that.isCollecting = false;
                    if (result.data.result === 1) {
                        that.isCollect = true;
                        that.$vux.toast.text('已收藏', 'bottom');
                    } else if (result.data.result === 0) {
                        that.isCollect = false;
                        that.$vux.toast.text('已取消收藏', 'bottom');
                    } else {
                        that.$vux.toast.text('数据错误', 'bottom');
                    }
                });
            }
        },
        filterIsCollect: function() { // 筛选显示收藏按钮与否
            this.collectShopList.filter((element, index, self) => {
                if (this.eachShopList.shopCode === element.shopCode) {
                    this.isCollect = true;
                }
            });
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
    created() {
        this.filterIsCollect();
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
.background
    background-color #f0f0f0
    height 100%
    width  100%
    .shoplist-item
        position:relative
        height:2.8rem
        line-height:2.75rem
        text-align:center
        background:#fff
        width:10.8rem
        border-bottom: 2px solid #f0f0f0
        .shopDetail
            display:inline-block
            width: 100%;
            height: 100%;
            .shop-logo
                position:absolute
                top:0.1rem
                left:0.18rem
                height:2.55rem
                width:2.55rem
                border-radius:6%
                overflow:hidden
                img
                    height:100%
            .shop-name
                position:absolute
                left:3.05rem
                top:0.56rem
                width:7rem
                font-size:0.5rem
                color:rgb(33,33,33)
                text-align:left
                line-height:1.2em
                font-weight:bold
        .collect
            position:absolute
            top:0.56rem
            right:0.7rem
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
        .shop-street
            position:absolute
            bottom:0.36rem
            left:3.05rem
            width:7rem
            height:0.5rem
            font-size:0.3rem
            text-align:left
            line-height:0.5rem
            color:rgb(95,95,95)
            .street
                display:inline-block
            .distance
                float:right
                line-height:0.5rem
</style>
