<template>
  <div id="contractPageComponent">
    <div class="bankLogo">
        <img v-lazy="this.global.staticSrc+'/huiquan/static/img/guizhoubanklogo.png'" alt="">
    </div>
    <div class="bankCardInfo">
        <p>
            <label>姓名</label>
            <input v-model="userInfo.name" id="name" placeholder="请填写姓名">
        </p>
        <p>
            <label>手机号</label>
            <input v-model="userInfo.phoneNum" id="phoneNum" placeholder="请填写银行预留手机号码">
        </p>
        <p>
            <label>证件号</label>
            <input v-model="userInfo.idCard" id="idCard" placeholder="请填写证件号">
        </p>
        <p>
            <label>银行卡号</label>
            <input v-model="userInfo.bankCardNum" id="bankCardNum" placeholder="请填写银行卡号">
        </p>
        <p>
            <label>验证码</label>
            <input v-model="verificationCode" id="verificationCode" class="verificationCode" placeholder="请填写短信验证码">
            <span class="getVerificationCodeSpan">获取短信验证码</span>
        </p>
    </div>
  </div>
</template>
<script>
import {mapState} from 'vuex';
export default{
    props: ['index'],
    data() {
        return {
            userInfo: {
                name: '',
                phoneNum: '',
                idCard: '',
                bankCardNum: ''
            },
            verificationCode: ''
        };
    },
    computed: {
        ...mapState(['global'])
    },
    watch: {

    },
    methods: {

    },
    created () {
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#contractPageComponent
    background: #fff
.bankLogo
    margin-left: 20%
    width: 60%
    height: 4rem
    text-align: center
    img
        width: 100%
        height: 3rem
        margin-top: 1rem

.bankCardInfo
    & > p
      height:1.5rem
      line-height:1.5rem
      border-bottom:1px solid #cecece90
      & > label
        display:inline-block
        width:3.4rem
        font-size:0.44rem
        margin-left: 0.2rem
      & > input
        float:right
        font-size:0.44rem
        margin-right:0.5rem
        display:inline-block
        width:6.6rem
        height:1.5rem
        outline:none

.verificationCode
    width: 5rem

.getVerificationCodeSpan
    position: absolute
    left: 8rem
    color: blue
</style>
