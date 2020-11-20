<template>
<div id="signingAuthorization">
    <header2 :title="'支付渠道'"></header2>
    <div class="authorizationLogo">
        <img v-lazy="this.global.staticSrc+'/huiquan/static/img/guizhoubanklogo.png'" alt="">
    </div>
    <div class="requirementsDiv">
        <span class="requirementsTitle">
            您同意{{companyName}}获取以下权限
        </span>
        <br>
        <br>
        <span class="requirementsValue">
            获取使用您的身份信息(姓名、手机号、证件号)办理业务
        </span>
        <div class="agreementDiv">
            <span class="agreementSpan" @click="checkedAgreementEvent" :class="checkedAgreement? 'checkedAgreementSpan' : ''">
                
            </span>
            <span class="userAgreementSpan" @click="viewProtocol">《用户授权协议》</span>
        </div>
        <div class="payModeDiv">
            <span class="payModeTitle">
                支付方式:
            </span>
            <br>
            <br>
            <br>
            <span class="payModeSpan" @click="checkedPayModeEvent" :class="checkedPayMode? 'checkedPayModeSpan' : ''">
                贵州银行
            </span>
        </div>
    </div>
    <div class="consentToAuthorization" @click="consentEvent">同意授权，进入下一步</div>
</div>
</template>
<script>
import header2 from '@/components/header/header2';
import {mapState} from 'vuex';
export default {
    data() {
        return {
            cardId: this.$route.params.cardId,
            checkedAgreement: true,
            checkedPayMode: false,
            companyName: '惠付通'
        };
    },
    computed: {
        ...mapState(['global'])
    },
    watch: {

    },
    methods: {
        consentEvent() {
            if (this.checkedAgreement && this.checkedPayMode) {
                this.$router.push({path: `/contractPage/${this.cardId}`});
            } else if (!this.checkedAgreement && this.checkedPayMode) {
                this.$vux.toast.text('必须勾选用户协议', 'bottom');
            } else if (this.checkedAgreement && !this.checkedPayMode) {
                this.$vux.toast.text('必须勾选支付方式', 'bottom');
            } else {
                this.$vux.toast.text('必须勾选用户协议和支付方式', 'bottom');
            }
        },
        checkedAgreementEvent() {
            this.checkedAgreement = !this.checkedAgreement;
        },
        checkedPayModeEvent() {
            this.checkedPayMode = !this.checkedPayMode;
        },
        viewProtocol() {
            this.$router.push({path: `/agreementInfo`});
        }
    },
    mounted() {
    },
    created() {
    },
    components: {
        header2
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
.consentToAuthorization
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

.requirementsDiv
    width: 60%
    margin-left: 20%
    .requirementsTitle
        font-size: 0.46rem
    .requirementsValue
        font-size: 0.35rem
        color: #C1C1C1
    .agreementDiv
        width: 100%
        margin-top: 0.5rem

.authorizationLogo
    width: 60%
    height: 4rem
    margin-left: 20%
    img
        width: 100%
        height: 3rem
        margin-top: 0.5rem

.userAgreementSpan
    color: #2E5175
    font-size: 0.46rem

.payModeDiv
    margin-top: 0.5rem

.payModeTitle
    font-size: 0.46rem

.payModeSpan
    font-size: 0.46rem

.agreementSpan::before
    content: "\a0";
    display: inline-block;
    vertical-align: middle;
    width: 0.5rem;
    height: 0.5rem;
    margin-right: .4rem;
    border-radius: 50%;
    border: 1px solid #01cd78;

.checkedAgreementSpan::before
    background-color: #01cd78;
    background-clip: content-box;
    padding: .1rem;
    width: 0.3rem;
    height: 0.3rem;

.payModeSpan::before
    content: "\a0";
    display: inline-block;
    vertical-align: middle;
    width: 0.5rem;
    height: 0.5rem;
    margin-right: .4rem;
    border-radius: 50%;
    border: 1px solid #01cd78;

.checkedPayModeSpan::before
    background-color: #01cd78;
    background-clip: content-box;
    padding: .1rem;
    width: 0.3rem;
    height: 0.3rem;
</style>