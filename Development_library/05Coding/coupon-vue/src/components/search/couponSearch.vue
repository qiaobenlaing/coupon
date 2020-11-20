<template>
    <div id="conponSearch" >
        <header class="header">
            <span class="header-item icon-back" @click='goBack'></span>
            <div class="header-right">
            <span class="haderitem2"><input id="head-input" ref="input" type="text" name="" v-bind:class="headInput===true ? 'headInput' : 'move' " placeholder="搜索优惠券" v-model="searchWord" ></span>
            <span class="header-item "  @click="searchCoupon">搜索</span>
            </div>
        </header>
        <div class="selectType " @click="showCouponType()" v-bind:class="[iconShow==true ? 'activeCity':'']">
           <span class="type1 " >{{type1}}</span>
           <span class="icon-ctrl" v-bind:class="[iconShow==true ? 'iconTurn':'']"></span>
        </div>
        <div class="mask" v-show="maskShow" @click="hiddenMask()">
        </div>
        <div class="couponType1" v-if="show">
            <div class="couponType2" v-bind:class="[couponType1.value===couponType ? 'activeCity' : '']" v-for="couponType1 in couponTypeList" :key="couponType1.name" @click="typeSearch(couponType1)">{{couponType1.name}}</div>
        </div>
    <div class="indexcoupon-wrapper" v-infinite-scroll="searchConponLoadMore" infinite-scroll-disabled="busy" infinite-scroll-distance="10">
        <indexcoupon v-for="each in searchCouponList" :index="each" :key="each.batchCouponCode"></indexcoupon>
        <loadGif v-show="showLoadGif"></loadGif>
    </div>
</div>
</template>
<script>
import { mapState } from 'vuex';
import indexcoupon from '@/components/coupon/indexcoupon.vue';
import loadGif from '@/components/loadGif/loadGif.vue';
export default {
    data() {
        return {
            searchWord: '',  // 搜索用的参数
            couponType: 0, // 搜索用 优惠券类型 默认为0 全部搜索
            page: 1, // 页数 查询用
            searchCouponList: [],
            show: false,
            maskShow: false,  // 判断 遮罩层 用
            type1: '券类型',
            busy: true,
            nextPage: 0,  // 判断用
            currentPage: 0,
            iconShow: false,
            headInput: true,
            showLoadGif: true,
            couponTypeList: [
                {
                    name: '全部',
                    value: 0
                },
                {
                    name: 'N元购',
                    value: 1
                },
                {
                    name: '抵扣券',
                    value: 3
                },
                {
                    name: '折扣券',
                    value: 4
                },
                {
                    name: '实物券',
                    value: 5
                },
                {
                    name: '体验券',
                    value: 6
                },
                {
                    name: '兑换券',
                    value: 7
                },
                {
                    name: '代金券',
                    value: 8
                }
            ]
        };
    },
    computed: {
        ...mapState(['localCity', 'global', 'zoneId'])
    },
    beforeCreate() {
    },
    watch: {
        // 监听search的变化
        searchWord: function(newval, oldval) {
            this.$data.page = 1; // 发现search变化后 将page变为1
        }
    },
    methods: {
        // ListIsNull() {
        //     if (this.searchCouponList.length < 1) {
        //         this.showLoadGif = false;
        //         this.$vux.toast.text('当前没有优惠券', 'bottom');
        //     }
        // },
        hiddenMask() {  //  收起下拉框和遮罩层
            this.show = false;
            this.maskShow = false;
            this.iconShow = false;
        },
        typeSearch(couponType1) {
            this.couponType = couponType1.value;
            this.page = 1; // 进入类型搜索时将page设为1
            this.searchCoupon();
            this.type1 = couponType1.name;
            this.show = false;
            this.maskShow = false;
        },
        showCouponType() {
            if (this.show === true) {
                this.show = false;
                this.maskShow = false;
                this.iconShow = false;
            } else {
                this.show = true;
                this.maskShow = true;
                this.iconShow = true;
            }
        },
        goBack () {
            this.$router.go(-1);
        },
        searchCoupon() {  // 搜索用的方法
            let that = this;
            this.showLoadGif = true;
            this.axios({
                method: 'post',
                url: this.$store.state.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"listCoupon","params":{"couponType":' + this.$data.couponType + ',"searchWord":"' + this.$data.searchWord + '","longitude":' + this.$store.state.longitude + ',"latitude":' + this.$store.state.latitude + ',"page":' + this.page + ',"city":"' + this.$store.state.localCity + '","userCode":"' + this.$store.state.userCode + '","zoneId":"' + this.zoneId + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (that.page === 1) {
                    that.$data.searchCouponList = result.data.result.couponList.CouponListInfo;
                } else {
                    that.$data.searchCouponList = [...that.$data.searchCouponList, ...result.data.result.couponList.CouponListInfo];
                }
                that.nextPage = result.data.result.nextPage;
                that.currentPage = result.data.result.page;
                that.page++;
                that.busy = false;
                // that.ListIsNull();
                that.hiddenLoadGif();
            });
        },
        searchConponLoadMore () {
            this.busy = true;
            if (this.nextPage > this.currentPage) {
                this.searchCoupon();
            } else {
                this.busy = true;
                this.$vux.toast.text('加载完毕', 'bottom');
                this.showLoadGif = false;
            }
        },
        hiddenLoadGif() {
            if (this.nextPage <= this.currentPage) {
                this.$vux.toast.text('加载完毕', 'bottom');
                this.showLoadGif = false;
            }
        }
    },

    created() {
        this.searchCoupon();
    },
    mounted() {
        this.headInput = false;
    },
    components: {
        indexcoupon,
        loadGif
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
@import "../../common/stylus/mixin.styl"
#conponSearch .header{
    position: fixed;
    height:1.24rem;
    line-height:1.24rem;
    width: 10.8rem;
    background:#fff;
    color:#fff;
    z-index:1;
    top: 0;
    left: 0;
}
.icon-back{
    display: inline-block;
    position relative;
    top 0.1rem
    line-height: 1.05rem;
    font-size: 0.7rem;
    font-weight: bold;
    float: left;
    margin-left: 0.3rem;
    color #525252
}
.header-right{
    float: right;
    margin-right: 0.45rem;
    height: 1.24rem;
    line-height: 1.24rem;
    vertical-align:middle
    color #525252
}
.headInput{
    height: 0.8rem;
    margin-right: 0.4rem;
    width 6.53rem;
    border-radius: 1rem;
    vertical-align:middle
    text-align: center;
    outline: none;
    color: #525252;
    font-size: 0.4rem;
}
.move{
    height: 0.8rem;
    margin-right: 0.4rem;
    width: 7.5rem;
    border-radius: 1rem;
    vertical-align:middle
    text-align: center;
    color: #525252;
    font-size: 0.4rem;
    background-color:#e8e8e8;
    outline: none;
    transition: width 0.4s;
}
.header-right .header-item{
    font-size: 0.4rem;
    vertical-align:middle
}
.indexcoupon-wrapper{
    position absolute
    top 1.24rem
    left 0
    margin-top: 1.24rem;
    width 10.55rem
}
.selectType{
    position fixed
    top 1.24rem;
    left 0
    z-index 1
    height 1.15rem
    width 100%
    background-color #fff
    color #525252
}
.selectType .type1{
    display inline-block
    width 100%;
    height 1rem
    line-height 1rem
    text-align center
    font-size 0.45rem
    border-bottom 0.1rem solid #f0f0f0
    border-top 0.15rem solid #f0f0f0
}
.mask{
    position:fixed
    top:2.7rem
    left:0
    width:100%
    height:100%
    background:rgba(91, 91, 91, 0.55)
    z-index:1
}
.couponType1{
    position fixed
    top 2.5rem
    left 0
    z-index 2
    width 100%
}
.couponType1 .couponType2{
    height 1rem
    line-height 1rem
    text-align center
    font-size 0.4rem
    background-color #fff
    color #525252
}
.activeCity{
    color: #ffc900!important
    background-color #fff!important
}
.selectType .icon-ctrl{
    position fixed
    top 1.39rem
    z-index 1
    right 1rem
    color #525252
    font-size 0.7rem
    height 1rem
    line-height 1rem
    font-width sold
    margin-top 0.2rem
    transform rotate(180deg)
    margin-top -0.15rem
}
.iconTurn{
    display inline-block
    transform rotate(0deg)!important
    margin-top 0.15rem!important
    color: #ffc900!important
}
.loadGif
    height 1.5rem
    width 100%
    background-color #fff
    .loadGif1
        height 1.5rem
        width 1.5rem
        margin auto
        img
            height 100%
            width 100%
</style>