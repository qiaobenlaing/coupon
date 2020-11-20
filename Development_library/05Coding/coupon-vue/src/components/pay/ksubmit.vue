<template>
    <div id="ksubmit">
        <div id="header2fix">
            <header class="header">
                <span v-show="from==='weixin'?false:true" class="header-item icon-back" @click='goBack'></span>
                <span class="header-item">买单</span>
            </header>
        </div>
        <div class="shopname">
            <span class="shopname1">商家名称</span><span class="shopname2">{{coupon.shopName}}</span>
        </div>
        <div class="consume">
            <span class="consume1">消费金额</span>
            <span class="consume2"><input type="text" class="consume3" v-model.number="inputMoney" @change='checkInputMoney()' maxlength="8">元</span>
        </div>
        <!--<div class="unpreferential" >
            <input type="checkbox" id="checkbox" v-model="checked" class='unpreferential1' >
            <span  class="unpreferential1" >输入不参与优惠的金额(例如酒水、海鲜等等)</span>
        </div>-->
        <div class="Nodiscount"  v-if='checked' >
            <span class="Nodiscount1">不参与优惠金额</span>
            <span class="Nodiscount2"><input type="text" class="Nodiscount3" v-model.number='Nodiscount' @change='checkNodiscount()' maxlength="8">元</span>
        </div>
        <div class="coupon">
            <span v-if="coupon.couponType==='3'" class="coupon1">抵扣券</span>
            <span v-if="coupon.couponType==='4'" class="coupon1">折扣券</span>
            <span class="coupon2" v-if="coupon.couponType==='3'">满{{coupon.availablePrice}}减免{{coupon.insteadPrice}}元</span>
            <span class="coupon2" v-if="coupon.couponType==='4'">满{{coupon.availablePrice}}打{{coupon.discountPercent}}折</span>
            <span class="coupon3" >减免{{couponMoney}}元</span>
        </div>
        <div class="selectCoupon" v-show="coupon.couponType==='3'">
            <span class="selectCoupon1"> 
                <span class="selectCoupon11">最多可用{{limitedNbr}}张</span>
                <span class="selectCoupon12">您拥有{{userCount}}张</span>
            </span>
            <span class="selectCoupon2">
                <div class="reduce" @click="cReduce()">-</div>
                {{couponCount}}
                <div class="add" @click="cAdd()">+</div>
            </span>
        </div>
        <div class="realpay">
            <span class="realpay1">实际支付</span><span class="realpay2">{{realmoney}}&nbsp&nbsp&nbsp元</span>
        </div>
        <div class="tonext" @click="bankcardPay()" v-show='ablePay'>
            下一步
        </div>
        <div class="tonext2" v-show='!ablePay'>
            下一步
        </div>
        <!-- <div class='cs1' @click='getNewPriceForAndroid()'>getNewPriceForAndroid</div>
        <div class='cs1' @click='getBankAccountList()'>getBankAccountList</div>
        <div class='cs1' @click='validatePayPwd()'>validatePayPwd</div> -->
    </div>
</template>
<script>
import {mapState} from 'vuex';
export default {
    data() {
        return {
            userCouponCode: this.$route.params.userCouponCode,
            checked: false,
            coupon: {},
            shop: {},
            inputMoney: 0, // 用户输入的消费金额
            consumeMoney: '',   // 用户参与计算的消费金额(除去不参与优惠的金额后)
            couponMoney: 0,   // 优惠的金额
            realmoney: '', // 实际支付的金额
            kOrder: '', // 这是折扣券生成的订单
            Nodiscount: 0, // 这是不参与优惠的金额
            ablePay: false, // 判断是否可以去支付
            submitmoney: '', // 提交的金额
            // 以下参数是当券类型为抵扣券的时候才会用到
            limitedNbr: '', // 一次消费最多使用的抵扣券数量
            userCount: '', // 用户拥有的这类抵扣券的数量
            couponCount: 0, // 用户使用的券的数量
            ableCount: ''  // 这是根据用户输入的金额计算出的最大可用优惠券数量
        };
    },
    watch: {
        consumeMoney() {
            // console.log(this.consumeMoney);
            this.couponCount = 0; // 支付金额改变时重置优惠券数量
            // 以下要计算折扣金额
            if (this.coupon.couponType === '4') {   // 折扣券
                this.consumeMoney = this.inputMoney - this.Nodiscount;
                if (this.consumeMoney >= this.coupon.availablePrice) {
                    this.realmoney = (this.consumeMoney * (this.coupon.discountPercent / 10)) + this.Nodiscount;
                    this.couponMoney = this.inputMoney - this.realmoney;
                    this.realmoney = this.realmoney.toFixed(2);
                    this.couponMoney = this.couponMoney.toFixed(2);
                }
            };
            if (this.coupon.couponType === '3') {
                // console.log('当前为抵扣券');
                var ableCount = this.consumeMoney / this.coupon.availablePrice;
                this.ableCount = parseInt(ableCount); // 这是最大可以使用的优惠券数量
                this.couponMoney = this.coupon.availablePrice * this.couponCount;
                this.realmoney = this.inputMoney - this.couponMoney;
            };
            this.isToPay();
        },
        couponCount() {
            if (this.coupon.couponType === '3') {  // 只有为抵扣券的时候才会有这个参数
                this.couponMoney = this.couponCount * this.coupon.insteadPrice;
                this.realmoney = this.inputMoney - this.couponMoney;
            }
            this.isToPay();
        },
        Nodiscount() {
            this.consumeMoney = this.inputMoney - this.Nodiscount;
        },
        inputMoney() {
            this.consumeMoney = this.inputMoney - this.Nodiscount;
        }
    },
    computed: {
        ...mapState(['global', 'from']),
        realmoney: function() {
            return this.consumeMoney + this.Nodiscount;
        }
    },
    methods: {
        getCouponDetail: function() { // 获取优惠券详情(乔本亮修改过2019-06-01，判断了优惠券状态)
            let that = this;
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"getUserCouponInfo","params":{"userCouponCode":"' + this.userCouponCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                console.table(result.data.result[0], ['index', 'value']);

                that.statusByqiao = result.data.result[0]['status'];
                if (this.statusByqiao === 5) {
                    this.$vux.toast.text('未到使用时间', 'bottom');
                    this.goBack();
                } else if (this.statusByqiao === 4) {
                    this.$vux.toast.text('优惠券已过期', 'bottom');
                    this.goBack();
                } else if (this.statusByqiao === 3) {
                    this.$vux.toast.text('优惠券冻结', 'bottom');
                    this.goBack();
                } else if (this.statusByqiao === 2) {
                    this.$vux.toast.text('优惠券已被使用', 'bottom');
                    this.goBack();
                } else if (this.statusByqiao === 0) {
                    this.$vux.toast.text('优惠券不可用', 'bottom');
                    this.goBack();
                }

                that.coupon = result.data.result[0];
                if (this.coupon.couponType === '3') {  // 如果是折扣券 需要判断折扣券的使用条件
                    this.listUserPayInfo();
                }
            });
        },
        checkNodiscount() {
            // 这是检查输入不参加金额的标准的方法
            var istrue = false;
            var textNodiscount = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
            if (this.Nodiscount !== undefined) {
                if (this.Nodiscount !== '') {
                    if (this.Nodiscount >= 0) {
                        if (this.Nodiscount < this.inputMoney) {
                            if (textNodiscount.test(this.Nodiscount)) {
                                istrue = true;
                            }
                        }
                    }
                }
            }
            if (istrue === true) {

            } else {
                this.Nodiscount = 0;
                this.$vux.toast.text('输入金额不合法', 'bottom');
            }
        },
        checkInputMoney() {
            // 这是检查输入金额的标准的方法
            var istrue = false;
            var textinputMoney = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
            if (this.inputMoney !== undefined) {
                if (this.inputMoney !== '') {
                    if (this.inputMoney >= 0) {
                        if (textinputMoney.test(this.inputMoney)) {
                            istrue = true;
                        }
                    }
                }
            }
            if (istrue === true) {

            } else {
                this.inputMoney = 0;
                this.$vux.toast.text('输入金额不合法', 'bottom');
            }
        },
        isToPay() {
            // 这是一个判断是否可以去支付的方法
            this.ablePay = false;
            if (this.coupon.couponType === '4') {
                // 判断折扣券时的条件
                if (this.consumeMoney >= this.coupon.availablePrice) {
                    // 只要提交的金额超过折扣券使用条件即可
                    this.ablePay = true;
                }
            }
            if (this.coupon.couponType === '3') {
                // 判断抵扣券时的条件
                if (this.couponCount >= 1) {
                    // 使用券的数量必须大于1
                    if (this.consumeMoney >= (this.coupon.availablePrice * this.couponCount)) {
                        this.ablePay = true;
                    }
                }
            }
        },
        cReduce() {
            // 减少使用券的数量  只在抵扣券时有用
            if (this.couponCount > 0) {
                this.couponCount--;
            }
        },
        cAdd() {
            // 增加使用券的数量  只在抵扣券时有用
            if (this.couponCount >= 0) {
                if (this.couponCount < this.limitedNbr && this.couponCount < this.userCount && this.couponCount < this.ableCount) {  // 不能超过最大可用张数和拥有的数量
                    this.couponCount++;
                }
            }
        },
        goBack() {
            this.$router.go(-1);
        },
        listUserPayInfo() {
            // 在支付前得到支付信息
            let that = this;
            // 栏目列表
            this.axios({
                method: 'post',
                url: this.$store.state.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"listUserPayInfo","params":{"userCode":"' + this.$store.state.userCode + '","shopCode":"' + this.coupon.shopCode + '","consumeAmount":"' + this.consumeMoney + '","batchCouponCode":"' + this.coupon.batchCouponCode + '"}}',
                dataType: 'json'
            }).then((result) => {
                that.limitedNbr = result.data.result.coupon.couponInfo.limitedNbr;

                console.log(result.data.result.coupon); // 一次消费最多使用的抵扣券数量
                that.userCount = result.data.result.coupon.couponInfo.userCount;
                console.log(that.userCount);  // 用户拥有的这类抵扣券的数量
            });
        },
        bankcardPay() {  // 这是一个支付方法
            this.submitmoney = this.consumeMoney + this.Nodiscount;
            let that = this;
            if (this.coupon.couponType === '4') { // 折扣券使用的方法
                this.axios({
                    method: 'post',
                    url: this.$store.state.global.hftcomClient,
                    data: '{"id":19,"jsonrpc":"2.0","method":"bankcardPay","params":{"userCode":"' + this.$store.state.userCode + '","shopCode":"' + this.coupon.shopCode + '","orderAmount":"' + this.inputMoney + '","userCouponCode":"' + this.coupon.userCouponCode + '","platBonus":"","shopBonus":"","noDiscountPrice":"' + this.Nodiscount + '"}}',
                    dataType: 'json'
                }).then((result) => {
                    console.log(result.data.result.code);
                    var Rcode = result.data.result.code;
                    if (Rcode === 80227) {
                        that.$vux.toast.text('优惠券当前不可用', 'bottom');
                    };
                    if (Rcode === 80220) {
                        that.$vux.toast.text('优惠券已过期', 'bottom');
                    };
                    if (Rcode === 50000) {
                        console.log('成功');
                        that.kOrder = result.data.result;
                        that.kOrder.payType = 'PAYMONEY';
                        that.$router.replace({path: '/gotopay', query: {order: that.kOrder}});
                    }
                });
            }
            if (this.coupon.couponType === '3') {  // 抵扣券使用的方法
                this.axios({
                    method: 'post',
                    url: this.$store.state.global.hftcomClient,
                    data: '{"id":19,"jsonrpc":"2.0","method":"bankcardPayForAndroid","params":{"userCode":"' + this.$store.state.userCode + '","shopCode":"' + this.coupon.shopCode + '","orderAmount":"' + this.inputMoney + '","batchCouponCode":"' + this.coupon.batchCouponCode + '","nbrCoupon":"' + this.couponCount + '","platBonus":"","shopBonus":"","noDiscountPrice":"' + this.Nodiscount + '"}}',
                    dataType: 'json'
                }).then((result) => {
                    var Rcode = result.data.result.code;
                    if (Rcode === 80227) {
                        that.$vux.toast.text('优惠券当前不可用', 'bottom');
                    };
                    if (Rcode === 80220) {
                        that.$vux.toast.text('优惠券已过期', 'bottom');
                    };
                    if (Rcode === 50000) {
                        console.log('成功');
                        that.kOrder = result.data.result;
                        that.kOrder.payType = 'PAYMONEY';
                        that.$router.replace({path: '/gotopay', query: {order: that.kOrder}});
                    }
                });
            }
        }
    },
    created() {
        this.getCouponDetail();
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#header2fix
    .header
        width 100%
        height 1.24rem
        background-color #f64f48
        .header-item
            text-align:center
            color:#fff  
        :nth-child(1)
            display inline-block
            height 1.24rem
            line-height 1.24rem
            width 1.5rem
            font-size:28px
            font-weight:bold
        :nth-child(2)
            display inline-block
            position absolute
            left 2.2rem
            height 1.24rem
            line-height 1.24rem
            font-size:0.5rem
            width 6.3rem
#ksubmit
    background:#EDEDED
    height 100vh
    width 100vh
    position:absolute
    width:100%
    height:100%
    .shopname
        width 10.8rem
        height 1.24rem
        background-color #fff
        .shopname1
            line-height 1.24rem
            margin-left 0.5rem
        .shopname2
            float right 
            margin-right 0.5rem
            line-height 1.24rem
    .consume
        width 10.8rem
        height 1.24rem
        margin-top 0.3rem
        background-color #fff
        .consume1
            line-height 1.24rem
            margin-left 0.5rem
        .consume2
            float right 
            margin-right 0.5rem
            line-height 1.24rem
            font-weight bold
            color red
            .consume3
                width 5rem
                text-align right 
                color red 
                font-weight bold
                margin-right 0.5rem
    .unpreferential
        width 10.8rem
        height 1.24rem
        margin-top 0.3rem
        background-color #fff
        line-height 1.24rem
        color #EE9A00
        .unpreferential1
            margin-left 0.5rem
    .Nodiscount
        width 10.8rem
        height 1.24rem
        background-color #fff
        margin-top 0.3rem
        .Nodiscount1
            line-height 1.24rem
            margin-left 0.5rem
        .Nodiscount2
            float right 
            margin-right 0.5rem
            line-height 1.24rem
            font-weight bold
            color red
            .Nodiscount3
                width 5rem
                text-align right 
                color red 
                font-weight bold
                margin-right 0.5rem
    .selectCoupon
        width 10.8rem
        height 1.24rem
        background-color #fff
        margin-top 0.3rem
        color #B8860B
        .selectCoupon1
            line-height 1.24rem
            width 3rem
            margin-left 0.5rem
            height 1.24rem
            .selectCoupon11
                display inline-block
            .selectCoupon12
                display inline-block
        .selectCoupon2
            height 1.24rem
            width 3rem
            float right 
            line-height 1.24rem
            .reduce
                display:inline
                margin-left:0rem
                width:0.5rem
                height:0.5rem
                line-height:0.5rem
                font-size:0.4rem
                padding:0.1rem 0.38rem
                border:1px solid #f64f48
                border-radius:10%
            .add
                display:inline
                margin-left:0rem
                width:0.5rem
                height:0.5rem
                line-height:0.5rem
                font-size:0.4rem
                padding:0.1rem 0.38rem
                border:1px solid #f64f48
                border-radius:10%
    .realpay
        width 10.8rem
        height 1.24rem
        margin-top 0.3rem
        background-color #fff
        .realpay1
            line-height 1.24rem
            margin-left 0.5rem
        .realpay2
            float right
            margin-right 0.5rem
            line-height 1.24rem
            font-weight:bold
            color red
    .tonext
        width 9.8rem
        height 1.24rem
        margin-top 0.3rem
        background-color #fff
        margin-left 0.5rem
        margin-right 0.5rem
        color #fff
        background-color #f64f48
        text-align center
        line-height 1.24rem
        border-radius 0.2rem
    .tonext2
        width 9.8rem
        height 1.24rem
        margin-top 0.3rem
        background-color #fff
        margin-left 0.5rem
        margin-right 0.5rem
        color #fff
        background-color #C2C2C2
        text-align center
        line-height 1.24rem
        border-radius 0.2rem
    .coupon
        width 10.8rem
        height 1.24rem
        background-color #fff
        margin-top 0.3rem
        color #B8860B
        .coupon1
            line-height 1.24rem
            margin-left 0.5rem
        .coupon3
            float right 
            margin-right 0.5rem
            line-height 1.24rem
    .cs1    
        height 1rem
        border 1px red solid
        margin-bottom 0.3rem
    


</style>
