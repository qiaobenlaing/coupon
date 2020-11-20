<template>
<div id="payChannel">
    <header2 :title="'支付渠道'"></header2>
    <payChannelComponent v-for="each in paychannelList" :index="each" :key="each.batchCouponCode"></payChannelComponent>
</div>
</template>
<script>
import header2 from '@/components/header/header2';
import payChannelComponent from '@/components/paychannel/payChannelComponent';
export default {
    data() {
        return {
            paychannelList: [{deductionFlag: true, isSigned: true, cardId: 'cardId1'}, {deductionFlag: true, isSigned: false, cardId: 'cardId2'}]
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

    },
    components: {
        header2,
        payChannelComponent
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">

</style>