<template>
    <div id="refund">
        <header2 :title="'申请退款'"></header2>
        <div class="shopName itemName">{{couponAllInfo.couponName}}</div>
        <div class="coupons">
            <p class="item"><label for="allSelected"><input type="checkbox" id="allSelected" v-model="allSelected" @change="changeSelected">全选</label></p>
            <p class="item" v-show="item.status==='1'" v-for="item in userCouponNbrList">
                <label :for="item.index">
                    <input :id="item.index" type="checkbox" :value="item" v-model="selectCouponNbrArr">
                    {{couponTypeText}}{{item.userCouponNbr}}
                    <span class="function">{{couponAllInfo.function}}</span>
                </label>
            </p>
        </div>
        <div class="item">退款金额：<div class="refundMoney">￥{{compuRefundMoney}}</div></div>
        <div class="itemName">退款说明</div>
        <div class="item">3-10个工作日内退款至原支付账户</div>
        <div class="itemName">服务电话</div>
        <div class="item">
            <a :href="'tel:' + shopDecoration.tel">{{shopDecoration.tel}}</a></div>
        <div class="itemName">退款原因</div>
        <div class="refundReason">
            <label class="item" for="one"><input type="radio" id="one" value="1" v-model="refundReason">
            计划有变，没时间消费</label>
            <label class="item" for="two"><input type="radio" id="two" value="2" v-model="refundReason">
            后悔了不想买</label>
            <label class="item" for="three"><input type="radio" id="three" value="3" v-model="refundReason">
            买多了/不想买了</label>
            <label class="item" for="four"><input type="radio" id="four" value="4" v-model="refundReason">
            商家停业/装修/转让</label>
            <label class="item" for="five"><input type="radio" id="five" value="5" v-model="refundReason">
            商家不承认</label>
            <label class="item" for="six"><input type="radio" id="six" value="6" v-model="refundReason">
            商家说可以现金/刷卡来享受折扣</label>
        </div>
        <div class="otherReason"><textarea  v-model="message" placeholder="更多问题？请吐槽！"></textarea></div>
        <div class="refundCoupon">
            <span class="tips"><icon class="safe_warn" type="safe_warn"></icon>退款申请一经提交后不可撤销</span>
        <div class="refundCouponBtn" @click="refundCoupon">申请退款</div></div>
    </div>
</template>
<script>
import {mapState} from 'vuex';
import { Icon } from 'vux';
import header2 from '@/components/header/header2.vue';
export default{
    data() {
        return {
            batchCouponCode: this.$route.params.batchCouponCode,
            couponAllInfo: {},
            shopDecoration: {},
            userCouponNbrList: [],
            selectCouponNbrArr: [],
            refundReason: '',
            suserCouponNbr: {
                userCouponNbr: '00305702513'
            },
            orderListNbr: {}
        };
    },
    computed: {
        ...mapState(['global', 'userCode']),
        couponTypeText: function() {
            switch (this.couponAllInfo.couponType) {
            case '1':
                return 'N元购';
            case '3':
                return '抵用券';
            case '4':
                return '折扣券';
            case '5':
                return '实物券';
            case '6':
                return '体验券';
            case '7':
                return '兑换券';
            case '8':
                return '代金券';
            case '32':
                return '注册送的抵扣券';
            case '33':
                return '送邀请人的抵扣券';
            }
        },
        compuRefundMoney: function() {
            return this.selectCouponNbrArr.length * this.couponAllInfo.payPrice;
        }
    },
    watch: {
        selectCouponNbrArr: function() {
            if (this.selectCouponNbrArr.length === this.userCouponNbrList.length) {
                this.allSelected = true;
            } else {
                this.allSelected = false;
            }
        }
    },
    methods: {
        showIndexCouponDetails: function() {
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"getBachCouponInfo","params":{"batchCouponCode":"' + this.batchCouponCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                console.log(result.data.result.BatchCouponInfo
                    );
                this.$data.couponAllInfo = result.data.result.BatchCouponInfo;
                this.$data.shopDecoration = result.data.result.ShopDecoration;
            });
        },
        getUserCouponNbr: function() { // 拿到同一批券的券码，进行数据更新
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"getUserCouponNbr","params":{"userCode":"' + this.userCode + '","batchCouponCode":"' + this.batchCouponCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                console.table(result.data.result);
                result.data.result.filter((element) => {
                    if (element.status === '1') {
                        this.userCouponNbrList.push(element);
                    }
                });
            });
        },
        refundCoupon: async function() {
            if (this.selectCouponNbrArr.length <= 0) {
                this.$vux.toast.text('您没有选择任何优惠券', 'middle');
            } else if (!(/^[1-6]{1}/).test(this.refundReason)) {
                this.$vux.toast.text('退款原因有误', 'middle');
            } else {
                if (this.message === undefined) {
                    this.message = '';
                }

                this.orderListNbr = {};
                this.selectCouponNbrArr.filter((element) => {
                    if (this.orderListNbr[element.orderNbr] === undefined) {
                        this.orderListNbr[element.orderNbr] = {'Nbr': [], status: 0};
                    }
                    this.orderListNbr[element.orderNbr]['Nbr'].push(element.userCouponNbr);
                });
                console.log(this.orderListNbr);
                this.$vux.loading.show({
                    text: '退款中'
                });
                try {
                    for (let i in this.orderListNbr) {
                        console.log(this.orderListNbr[i]['Nbr']);
                        await this.refund(i);
                    }
                    this.$vux.loading.hide();
                    this.$vux.toast.text('退款申请已提交', 'middle');
                    this.$router.go(-1);
                } catch (err) {
                    this.$vux.loading.hide();
                    this.$vux.toast.text('退款申请出错,请联系管理员或稍后重试', 'middle');
                }
            }
        },
        refund: function(i) {
            return new Promise((resolve, reject) => {
                this.axios({
                    method: 'post',
                    url: this.global.hftcomApi + '/Admin/Lcy/weixinRefund',
                    data: {
                        userCouponNbr: this.orderListNbr[i]['Nbr']
                    }
                }).then((res) => {
                    console.log(res.data);
                    if (res.data === 'success') {
                        this.orderListNbr[i].status = 1;
                        resolve();
                    } else {
                        reject();
                    }
                }).catch(() => {
                    reject();
                });
            });
        },
        changeSelected: function() {
            this.selectCouponNbrArr = [];
            if (this.allSelected) {
                this.userCouponNbrList.forEach((item, index) => {
                    this.selectCouponNbrArr.push(item);
                });
            } else {
                this.selectCouponNbrArr = [];
            }
        }
    },
    created() {
        this.showIndexCouponDetails();
        this.getUserCouponNbr();
    },
    components: {
        header2,
        Icon
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#refund
    width:100%
    .itemName
        font-size:0.44rem
        line-height:1.2rem
        height:1.2rem
        padding-left:0.4rem
        background:rgb(235, 235, 235)
        color:#6c6c6c
    .item
        display:block
        height:1.2rem
        line-height:1.2rem
        padding-left:0.88rem
        font-size:0.48rem
        .refundMoney
            display:inline-block
            float:right
            padding-right:1rem
            color:#f64f48
            font-weight:bold
    .coupons
        .function
            padding-left:1rem
    .otherReason
        textarea
            border:1px #cecece solid
            margin:0.5rem 0.7rem
            width:9rem
            height:2rem
            margin-bottom:3.5rem
    .refundCoupon
        position:fixed
        bottom:0
        left:0
        width:100%
        height:2.8rem
        line-height:2rem
        text-align:center
        background:#ebebeb
        .safe_warn
            display: inline-block
            font-size:0.45rem
        .tips
            display: inherit;
            margin-left: 0.5rem;
            margin-top: 0.3rem;
            font-size: 0.4rem;
            line-height: 0.5rem;
            text-align: left;
            height: 0.5rem;
        .refundCouponBtn
            font-size:0.45rem
            line-height:1.4rem
            height:1.4rem
            width:10rem
            margin:0.2rem auto
            margin-bottom:0.2rem
            border-radius:0.1rem
            color:#fff
            background:#f64f48
</style>
