<template>
<div id="payChannel">
    <header2 :title="'支付渠道'"></header2>
    <cardInfocomponent :index="cardInfo"></cardInfocomponent>
    <div class="rescissionButton">我要解约</div>
</div>
</template>
<script>
import header2 from '@/components/header/header2';
import cardInfocomponent from '@/components/paychannel/cardInfocomponent';
export default {
    data() {
        return {
            cardInfo: {
                cardNum: this.$route.params.cardNum
            }
        };
    },
    computed: {
    },
    watch: {

    },
    methods: {
    },
    mounted() {
    },
    created() {
        this.cardInfo.payMode = '快捷支付';
        this.cardInfo.signingState = '已签约，贵州银行代扣';
        this.cardInfo.signingTime = '2019-07-02 08:30';
        this.cardInfo.cardNum = '6214***************4119';
    },
    components: {
        header2,
        cardInfocomponent
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
.rescissionButton
    width: 80%
    height: 1.3rem
    margin-left: 10%
    background: #295AE5
    text-align: center
    line-height: 1.3rem
    font-size: 0.46rem
    color: #fff
    margin-top: 0.5rem
    border-radius: 1rem
</style>