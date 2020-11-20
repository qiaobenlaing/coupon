<template>
<div id="settlement">
    <header2 :title="'买单'"></header2>
    <div class="settlement-div">
      <label>总金额(元):</label>
      <input id="sum-money" ref="sumImput" class="settlement-input" v-model="sumMoney" autofocus="autofocus">
    </div>
    <div class="settlement-div">
      <label>商家信息:</label>
      <span class="store-info">{{storeInfo}}</span>
    </div>
    <div class="settlement-div">
      <label>优惠金额(元):</label>
      <span class="store-info">{{discountMoney}}</span>
    </div>
    <div class="settlement-div">
        <label>实付款:</label>
      <span class="totalPrice">{{totalPrice}}元</span>
    </div>
    <div class="pay" @click="submitOrder">买单</div>
</div>
</template>
<script>
import header2 from '@/components/header/header2';
export default {
    data() {
        return {
            sumMoney: '',
            totalPrice: 0,
            storeInfo: '',
            discountMoney: '',
            storeCode: this.$route.params.storeCode
        };
    },
    computed: {
    },
    watch: {
        sumMoney(val) {
            if (val <= this.discountMoney) {
                this.totalPrice = 0;
            } else {
                this.totalPrice = val - this.discountMoney;
            }
        }
    },
    methods: {
        goBack () {
            this.$router.go(-1);
        },
        submitOrder() {
            console.log(this.storeIfo);
        }
    },
    mounted() {
        this.$refs.sumImput.focus();
    },
    created() {
        this.discountMoney = 10;
        this.storeInfo = '测试商家';
        console.log(this.storeCode);
    },
    components: {
        header2
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#settlement
  background:rgb(240,240,240)
  .settlement-div
    width:9.8rem
    height:1.5rem
    line-height:1.5rem
    border-bottom:1px solid #cecece60
    display: inline-block
    padding:0 0.5rem
    background:#fff
    label
      font-size:0.46rem
      color:#3e3e3e
    .totalPrice
      float:right
      font-size:0.55rem
      font-weight:bold
      color:#f64f48
      display inline-block
    .payPrice
      display:inline
      position:absolute
      right:2rem
      bottom:0rem
      font-size:0.44rem
      font-weight:bold
      color:#f64f48
      .pricespan
        text-decoration:line-through
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
  .store-info
    font-size: 0.46rem
    float: right

  .settlement-input
    float: right
    font-size: 0.46rem
    height: 1.5rem
    text-align: right
</style>