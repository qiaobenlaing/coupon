<template>
    <div class="barcode">
        <div v-show="userCouponNbrList1.length>0" class="use">{{userCouponNbrList1.length}}张可用|{{couponAllInfo.expireTime}}到期 <span class="refundbtn" v-show="couponAllInfo.payPrice>0 && userCouponNbrList1.length>0" @click="toRefund()">退款></span>
            <!-- <p class="tips">点击添加按钮添加到微信卡包</p> -->
        </div>
        <div v-if="isShowBarCode" class="barwrapper">
            <barcode class="barcontainer" v-bind:value="currenUserCouponNbr" :options="{displayValue: false}">数据请求错误，请刷新</barcode>
        </div>
        <div class="userCouponNbrList" v-for="item in userCouponNbrList1">
            <div class="barcodeName" :class="{isActive:currenUserCouponNbr === item.userCouponNbr}" @click="showBarCode(item)" >
                <span class="icon-barcode" v-show="item.status === '1'"></span>
                <span>券码:{{item.userCouponNbr}}</span>

                <span class="disuse" v-show="item.status === '0'">不可用</span>
                <!-- <span class="nouse"  v-show="item.status === '1'">未消费</span> -->
                <span class="used" v-show="item.status === '2'">已使用</span>
                <span class="disuse canrefund" v-show="item.status === '3'">已失效</span>
            </div>
            <!-- <div class="addbtn" @click="addWeixinCoupon(item.userCouponCode)" v-show="item.inWeixinCard==='2'">
                <span v-show="isadding!==item.userCouponCode ">添加到微信</span>
                <transition name="rotate">
                    <span class="icon-spinner8" v-show="isadding===item.userCouponCode"></span>
                </transition>
            </div> -->
        </div>
        <div v-show="userCouponNbrList2.length>0" class="more" @click="moreCoupon">
            点击<span v-show="!showMoreCoupon">查看</span><span v-show="showMoreCoupon">收起</span>已领不可用优惠券<span class="icon-ctrl" :class="[showMoreCoupon?'icon-more-y':'icon-more']"></span></div>
        <div v-show="showMoreCoupon" class="userCouponNbrList" v-for="item in userCouponNbrList2">
            <div class="barcodeName" :class="{isActive:currenUserCouponNbr === item.userCouponNbr}" @click="showBarCode(item)" >
                <span class="icon-barcode"></span>
                <span>券码:{{item.userCouponNbr}}</span>
                <span class="disuse" v-show="item.status === '0'">不可用</span>
                <span class="nouse" v-show="item.status === '1'">未消费</span>
                <span class="used" v-show="item.status === '2'">已使用</span>
                <span class="disuse" v-show="item.status === '3'">已失效</span>
            </div>
        </div>
    </div>
</template>

<script>
import VueBarcode from 'vue-barcode';
import {mapState} from 'vuex';
export default{
    props: ['couponAllInfo'],
    data() {
        return {
            isShowBarCode: false,
            userCouponNbrList1: [],
            userCouponNbrList2: [],
            currenUserCouponNbr: '',
            showMoreCoupon: false, // 是否显示更多不可用的优惠券
            CouponInfo: {},
            signPakage: {},
            isadding: '',
            userCouponCode: ''  // 当前正添加到卡包的优惠券码
        };
    },
    computed: {
        ...mapState(['global', 'userCode', 'zoneId']),
        isShowCode: function() { // 是否为抵扣券与折扣券
            if (this.couponAllInfo.couponType === '3' || this.couponAllInfo.couponType === '4') {
                return true;
            } else {
                return false;
            }
        }
    },
    methods: {
        showBarCode: function(item) {
            if (this.isShowCode && item.status === '1') {
                this.toKSubmit(item.userCouponCode);
            } else if (item.status !== '1') {
                this.$vux.toast.text('券已不可用', 'bottom');
            } else {
                if (item.userCouponNbr === this.currenUserCouponNbr) {
                    this.isShowBarCode = false;
                    this.currenUserCouponNbr = '';
                } else {
                    this.currenUserCouponNbr = item.userCouponNbr;
                    this.isShowBarCode = true;
                }
            }
        },
        getUserCouponNbr: function(batchCouponCode) { // 拿到同一批券的券码，进行数据更新
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"getUserCouponNbr","params":{"userCode":"' + this.userCode + '","batchCouponCode":"' + batchCouponCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                this.userCouponNbrList1 = [];
                this.userCouponNbrList2 = [];
                if (result.data.result === undefined) {
                } else {
                    result.data.result.forEach((item) => {
                        if (item.status === '1') {
                            this.userCouponNbrList1.push(item);
                        } else {
                            this.userCouponNbrList2.push(item);
                        }
                    });
                }
            });
        },
        toRefund: function() {
            this.$router.push({ path: `/refund/${this.couponAllInfo.batchCouponCode}` });
        },
        toKSubmit: function(userCouponCode) {
            this.$router.push({ path: `/ksubmit/${userCouponCode}` });
        },
        moreCoupon: function() {
            if (this.showMoreCoupon) {
                this.showMoreCoupon = false;
            } else {
                this.showMoreCoupon = true;
            }
        },
        addWeixinCoupon: function(userCouponCode) { // 添加会员卡到微信卡包
            if (this.isadding === '') {
                this.isadding = userCouponCode;
                this.userCouponCode = userCouponCode;
                this.axios({
                    method: 'post',
                    url: this.global.hftcomWeixinCard,
                    data: '{"id":29,"jsonrpc":"2.0","method":"addWeixinCoupon","params":{"userCode":"' + this.userCode + '","shopCode":"' + this.couponAllInfo.shopCode + '","userCouponCode":"' + userCouponCode + '","zoneId":"' + this.zoneId + '"}}'
                }).then((result) => {
                    if (result.data.result.signPakage === '' || result.data.result.signPakage === undefined) {
                        this.$vux.toast.text('数据请求错误，请稍后重试', 'bottom');
                    } else {
                        this.CouponInfo = result.data.result.CouponInfo;
                        this.signPakage = result.data.result.signPakage;
                        this.addcoupontoweixin();
                    }
                    this.isadding = '';
                });
            } else {
                this.$vux.toast.text('操作频繁，请稍后继续');
            }
        },
        updateUserWeixinCoupon: function() { // 告知后端优惠券添加至卡包成功
            this.axios({
                method: 'post',
                url: this.global.hftcomWeixinCard,
                data: '{"id":29,"jsonrpc":"2.0","method":"updateUserWeixinCoupon","params":{"userCouponCode":"' + this.userCouponCode + '"}}'
            }).then((result) => {
                this.$router.go(0);
            });
        },
        addcoupontoweixin: function() {
          /* eslint-disable */
          wx.config({
            debug: true,
            appId: this.signPakage.appid,
            timestamp: this.signPakage.timestamp,
            nonceStr: this.signPakage.noncestr,
            signature: this.signPakage.signature,
            jsApiList: [
            'addCard'
            ]
          });
          wx.ready(() => {
            wx.addCard({
              cardList:  [
                    {
                      code:this.CouponInfo.code,
                        cardId: this.CouponInfo.card_id,
                        cardExt: '{"code":"' + this.CouponInfo.code + '","timestamp":"' + this.CouponInfo.timestamp + '","signature":"' + this.CouponInfo.signature + '","nonce_str":"' + this.CouponInfo.nonce_str + '"}'
                    }
                ],
              success: (res) => {
                this.updateUserWeixinCoupon();
                    console.log('已添加卡券：' + JSON.stringify(res.cardList));
                  },
              cancel:  (res) => {
                    this.$vux.toast.text('取消添加','bottom')
                  }
              });
          });
          wx.error(function(res){
            console.log('err', res)
          });
            /* eslint-enable */
        }
    },
    created() {
    },
    components: {
        'barcode': VueBarcode
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
.barcode
    margin-top:0.1rem
    width:100%
    background:#fff
    .use
        margin: 0.1rem 0.4rem
        padding: 0.4rem 0.4rem
        font-size: 0.42rem
        .refundbtn
            float:right
            color:#f64f48
        .tips
            color:#cecece
            margin-top:0.2rem
            font-size:0.4rem
    .barwrapper
        margin:0 0.4rem
        border-bottom:1px solid #cecece
        .barcontainer
            width:7rem  
            margin:0 auto
            padding:0.5rem 0
            .vue-barcode-element
              width:7rem
              height:4rem
              border:1px solid #3e3e3e75
              border-radius:0.1rem
    .userCouponNbrList
        position:relative
        .barcodeName
            margin:0.1rem 0.4rem
            padding:0.4rem 0.4rem
            font-size:0.5rem
            // line-height:0.3rem
            .icon-barcode
                color:#000
                font-size:0.5rem
                padding-right:0.4rem
            .nouse
                float:right
                color:#149f14
                padding-right:1.5rem
            .used
                float:right
                color:#f64f48
                padding-right:1.5rem
            .disuse
                float:right
                color:#f64f48
                padding-right:1.5rem
        .addbtn
            position:absolute
            right:0.5rem
            top:0.23rem
            width:2.3rem
            height:0.8rem
            line-height:0.8rem
            text-align:center
            border-radius:0.1rem
            background:#f64f48
            color:#fff
            .icon-spinner8
              position:absolute
              top:0.2rem
              left:0.9rem
              font-size:0.46rem
            .rotate-enter-active
              transition: transform 4s linear
            .rotate-enter
              transform: rotate(-1440deg)
        .isActive
            background:rgba(232, 232, 232, 0.522)
    .more
        position:relative
        width:13.5em
        margin: 0.1rem auto;
        padding: 0.4rem 0.4rem;
        font-size: 0.42rem;
        .icon-more
            position:absolute
            top:0.32rem
            transform:rotate(180deg)
        .icon-more-y
            position:absolute
            top:0.5rem
</style>
