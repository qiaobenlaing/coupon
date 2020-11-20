<template>
<div id="payChannel">
    <header2 :title="'签约银行'"></header2>
    <contractPageComponent :index="bankInfo"></contractPageComponent>
    <div class="agreement">
        <span @click="viewProtocol">点击确定，表示已阅读《贵阳银行银行卡xxxxxx协议》</span>
    </div>
    <div class="determineButton">确定</div>
</div>
</template>
<script>
import header2 from '@/components/header/header2';
import contractPageComponent from '@/components/paychannel/contractPageComponent';
export default {
    data() {
        return {
        };
    },
    computed: {
    },
    watch: {

    },
    methods: {
        viewProtocol() {
            this.$router.push({path: `/agreementInfo`});
        }
    },
    mounted() {
    },
    created() {
    },
    components: {
        header2,
        contractPageComponent
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
.agreement
    color: blue
    margin-left: 0.2rem
    font-size: 0.4rem
    margin-top: 0.2rem

.determineButton
    width: 80%
    height: 1.3rem
    margin-left: 10%
    background: #295AE5
    text-align: center
    line-height: 1.3rem
    font-size: 0.46rem
    color: #fff
    margin-top: 0.8rem
    border-radius: 1rem
</style>