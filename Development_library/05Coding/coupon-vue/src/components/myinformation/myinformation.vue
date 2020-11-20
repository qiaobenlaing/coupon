<template>
    <div id="myinformation">
    <div class="personalInfo">
        <div class="img" @click="changeInmg()" @mouseover="overImg()" @mouseout="outImg()" v-bind:class="[showPrompt===true ? 'showPrompt':'']"><img v-show="userInfo.avatarUrl
==='' || userInfo.avatarUrl
=== undefined" src="../../common/image/logo.jpg" alt=""><img v-if="userInfo.avatarUrl
!=''&&userInfo.avatarUrl
!= undefined" :src="userInfo.avatarUrl" alt=""></div>
        <div class="weixinId">微信名称</div>
        <div class="mobile"><span v-show="userInfo.nickName===''">快给你自己取个昵称吧</span>{{userInfo.nickName}}</div>
        <!-- <div class="allPoint" v-if="isLogin&&this.appType === 1">圈值: {{allPoint}}</div> -->
        <button class="login" v-show="!isLogin" @click="login">登录</button>&nbsp;&nbsp;<button class="login" v-show="!isLogin" @click="register">注册</button>
        <div class="circle">
            <div class="circle1"></div>
            <div class="circle2"></div>
            <div class="circle3"></div>
        </div>
    </div>
    <div class="information">
        <!-- <div class="infoItem" v-show="isLogin" @click="toPersonalPage('myvipcard')">
            <p>添加会员卡至微信卡包<span class="arrow icon-back"></span></p>
        </div> -->
       <!--  <div class="infoItem" v-show="isLogin&&this.appType === 1" @click="toPersonalPage('sassmyvipcard')">
            <p>查看会员卡<span class="arrow icon-back"></span></p>
        </div> -->
    	<div class="infoItem" v-show="isLogin" @click="toPersonalPage('changemyinfo')">
    		<p>修改个人信息<span class="arrow icon-back"></span></p>
    	</div>
    	<div class="infoItem" v-show="isLogin" @click="bindTel">
    		<p>绑定手机号<span class="arrow icon-back"></span></p>
    	</div>
     <!--    <div class="infoItem" v-show="isLogin" @click="toPersonalPage('payChannel')">
    		<p>支付渠道<span class="arrow icon-back"></span></p>
    	</div> -->
        <div v-show="isLogin" class="infoItem" @click="toPersonalPage('mycollection')">
            <p>我的收藏<span class="arrow icon-back"></span></p>
        </div>
    	<div v-show="isLogin" class="infoItem" @click="toPersonalPage('order')">
    		<p>我的订单<span class="arrow icon-back"></span></p>
    	</div>
  <!--   	<div v-show="isLogin" class="infoItem" @click="jumpOutside('https://gzyh.hfq.huift.com.cn/mall/fudu/order/payment.html?type=3')">
    		<p>微商城<span class="arrow icon-back"></span></p>
    	</div> -->
    	<div class="infoItem" @click="toPage('cooperation')">
    		<p>我要合作<span class="arrow icon-back"></span></p>
    	</div>
    	<div class="infoItem" @click="clearCookie" v-show="isLogin">
    		<p>退出登陆<span class="arrow icon-back"></span></p>
    	</div>
    </div>
    <input type="file"  id="changeAvatar" accept="image/*" @change="showNewAvatar()">
    <!-- <div class="prompt" v-show="showPrompt">更换图片</div> -->
    <v-nav></v-nav>
</div>
</template>
<script>
import nav from '@/components/nav/nav.vue';
import {mapState, mapGetters, mapMutations} from 'vuex';
export default{
    name: 'myInformation',
    head() {
        return {
            title: '个人信息'
        };
    },
    data () {
        return {
            userInfo: {},
            position: 'default',
            showPositionValue: true,
            avatarUrl: '',
            showPrompt: false,
            allPoint: 0
        };
    },
    computed: {
        ...mapState(['mobileNbr', 'global', 'isLogin', 'userCode', 'zoneId', 'appType']),
        ...mapGetters(['doneTodosCount'])
    },
    methods: {
        ...mapMutations({
        }),
        overImg() {
            // console.log('鼠标悬停');
            this.showPrompt = true;
        },
        outImg() {
            // console.log('鼠标移开');
            this.showPrompt = false;
        },
        UploadNewImg(img) {
            let param = new FormData();  // 创建from 表单
            param.append('Img', img); // 通过append向form对象添加数据,可以通过append继续添加数据
            // let that = this;
            param.append('chunk', '12');
            param.append('id', '19');
            param.append('jsonrpc', '2.0');
            param.append('method', 'uploadHeadImage');
            param.append('userCode', this.$store.state.userCode);
            let config = {
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            };  // 添加请求头
            this.axios({
                method: 'post',
                url: this.global.hftcom + '/Api/UploadImage/uploadHeadImage',  // 暂时能用得请求
                data: param,
                // {
                //     'id': 19,
                //     'jsonrpc': '2.0',
                //     'method': 'uploadHeadImage',
                //     'imgUrl': '13541551',
                //     'params': {
                //         'title': 'js',
                //         'shopCode': '16531'
                //     }
                // }
                // 'file': nImg,
                // param: param,
                config
            }).then((result) => {
                // console.log(result);
                // this.$router.go(0);  // 上传完成后刷新页面
            });
        },
        showNewAvatar() {
            var nImg = document.getElementById('changeAvatar').files[0]; // 这是用户上传的头像
            console.log(nImg.size);
            console.log(nImg.name);
            console.log(nImg.type);
            if (nImg.size > 1024 * 512) {  // 头像超过500K就进行不容许上传
                // console.log('图像太大了' + nImg.size);
                // // 以下创建了一个FileReader
                // var fileReader = new FileReader();
                // fileReader.readAsDataURL(nImg);
                // fileReader.onload = function(event) {  // 在读取成功后会调用这个方法
                //     var result = event.target.result;
                //     var image = new Image();
                //     image.src = result;
                //     // console.log(image.src);
                // };
                this.$vux.toast.text('图片太大，上传失败', 'bottom');
            } else {
                // 调用上传图片
                this.UploadNewImg(nImg);
            }
        },
        changeInmg() {
            document.getElementById('changeAvatar').click();// 打开input框
        },
        login: function() {
            this.$store.state.isBLR = 1;
            this.$store.state.showLoginBox = true;
        },
        register: function() {
            this.$store.state.isBLR = 2;
            this.$store.state.showLoginBox = true;
        },
        bindTel: function() {
            this.axios({
                method: 'post',
                url: this.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"isBindMobile","params":{"userCode":"' + this.userCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                console.log(result);
                if (result.data.result.code === 50000) {
                    this.$store.state.isBLR = 3;
                    this.$store.state.phoneNum = result.data.result.mobileNbr;
                } else {
                    this.$store.state.isBLR = 0;
                    this.$store.state.phoneNum = '';
                }
            });
            this.$store.state.showLoginBox = true;
        },
        getUserInfo: function() {
            this.axios({
                method: 'POST',
                url: '/Api/Client',
                data: '{"id":19,"jsonrpc":"2.0","method":"getUserInfo","params":{"userCode":"' + this.userCode + '"}}',
                responseType: 'json'
            }).then((result) => {
                this.userInfo = result.data.result;
                console.log(result);
                // if (result.data.result.avatarUrl.slice(0, 4) !== 'http') {
                //     this.userInfo.avatarUrl = this.global.hftcom + result.data.result.avatarUrl;
                // }
            });
            if (this.appType === 1) {
                this.axios({
                    method: 'POST',
                    url: '/Api/Activity',
                    data: '{"id":19,"jsonrpc":"2.0","method":"getAllPoint","params":{"userCode":"' + this.userCode + '","zoneId":"' + this.zoneId + '"}}',
                    responseType: 'json'
                }).then((result) => {
                    if (result.data.result[0].allPoint !== null) {
                        this.allPoint = result.data.result[0].allPoint;
                    }
                    console.log(result);
                });
            }
            // 这是获得头像的方法
            // this.axios({
            //     method: 'post',
            //     url: that.global.hftcomShop,
            //     data: '{"id":19,"jsonrpc":"2.0","method":"getUserHeadImage","params":{"userCode":"' + this.$store.state.userCode + '"}}',
            //     responseType: 'json'
            // }).then((result) => {
            //     console.log(result.data.result.avatarUrl);
            //     this.avatarUrl = result.data.result.avatarUrl;
            // });
        },
        clearCookie: function() {
            let that = this;
            this.$vux.confirm.show({
                title: '确定要退出登陆吗？',
                onCancel () {
                    that.$vux.confirm.hide();
                },
                onConfirm () {
                    that.$store.dispatch('clearCookie');
                    that.$router.push('/myInformation');
                    that.userInfo = {};
                    // that.$router.go(0);
                }
            });
        },
        toPage (page) {
            this.$router.push(page);
        },
        toPersonalPage (page) {
            if (this.$store.state.userCode === '' || this.$store.state.userCode === undefined) {
                this.$store.state.isBLR = 1;
                this.$store.state.showLoginBox = true;
                this.$vux.toast.text('请登录', 'bottom');
            } else {
                this.$router.push(page);
            }
        },
        jumpOutside (e) {
            window.location.href = e;
        }
    },
    created() {
        if (this.isLogin) {
            this.getUserInfo();
        }
    },
    components: {
        'v-nav': nav
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#myinformation
    width:100%
    overflow:hidden
  .personalInfo
    position:relative
    width:100%
    height:7.5rem
    text-align:center
    .circle
        position:absolute
        bottom:1rem
        left:0
        z-index:-1
        .circle1
            position:absolute
            bottom:0
            left:-3.465rem
            width:17.73rem
            height:12.68rem
            background:#f64f48
            opacity:1
            border-radius:50%
        .circle2
            position:absolute
            bottom:0.19rem
            left:-0.21rem
            width:11.22rem
            height:11.22rem
            background:#fff
            border-radius:50%
            opacity:1
        .circle3
            position:absolute
            bottom:-2.7rem
            left:-7.465rem
            width:25.73rem
            height:12.68rem
            z-index:-1
            border-radius:50%
            // background-image: -webkit-gradient(radial,center center,95,center center,99.99999999,from(#f64f48),to(#fff),color-stop(98%,#fff));
            background: -webkit-radial-gradient(50% 50%, farthest-corner, red, #fff, #fff);
    .img
        margin:0.6rem auto
        width:2.89rem
        height:2.89rem
        line-height:2.89rem
        border:0px solid #f64f4880
        border-radius:100%
        text-align:center
        background:#fff
        overflow:hidden
        img
            height:100%
            width:100%
            
    .weixinId
        font-size:0.48rem
        line-height:1rem
        font-weight:bold
        color:rgb(13,41,73)
    .mobile
        font-size:0.4rem
        color:rgb(162,170,180)
    .allPoint
        margin-top: 10px
    .login
        color:#FFF
        padding:0.1rem 0.5rem
        background:#f64f48
        border:#f64f48 1px solid
        border-radius:0.05rem
  .information
    font-size:0.5rem
    margin-bottom:50px
    .infoItem
      width:8.5rem
      height:1.4rem
      margin:0 auto
      line-height:1.4rem
      text-align:left
      .arrow
        float:right
        font-size:20px
        line-height:1.4rem
        transform:rotate(180deg)
#changeAvatar
    z-index: -100!important
    visibility:hidden
.prompt
    position absolute
    top 3.5rem
    border 1px solid #DBDBDB
    border-radius 5px
    height 0.75rem
    line-height 0.75rem
    left 6rem
    color #DBDBDB
.showPrompt
    box-shadow: 2px 2px 2px 2px #888888;
</style>
