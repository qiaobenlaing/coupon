<template>
<div id="changemyinfo">
    <header class="header">
      <span class="header-item icon-back" @click='goBack'></span>
      <span class="header-item">修改个人信息</span>
      <span class="header-item" @click="updateUserInfo()">保存</span>
    </header>
    <div class="panel">
      <!-- <p>
      <label for="avatar">头像</label>
      <span @click="changeInmg()">点击修改个人头像</span>
      </p> -->
      <p><label for="nickName">呢称</label>
      <input v-model.lazy="userInfo.nickName" id="nickName" value="edit me" placeholder="请填写昵称"></p>
      <p><label for="realName">真实姓名</label>
      <input v-model.lazy="userInfo.realName" id="realName" value="女" placeholder="请填写真实姓名"></p>
      <p>
        <label for="sex">性别</label>
        <span class="radio"><label for="male"><input type="radio" id="male" value="M" v-model.lazy="userInfo.sex">男</label></span>
        <span class="radio"><label for="female"><input class="radio" type="radio" id="female" value="F" v-model.lazy="userInfo.sex">女</label></span>
        <span class="radio"><label for="U"><input class="radio" type="radio" id="U" value="U" v-model.lazy="userInfo.sex">未填</label></span>
      <div class="city"><label for="city">城市</label>
        <div class="cityInfo">
        <div id="select" @click="openselect">
          <span v-show="userInfo.city!==''">{{userInfo.city}}</span>
          <span v-show="userInfo.city===''">请选择</span>
        </div>
        <div class="select-item" v-show="isopen">
          <p v-for="city in cityList" v-bind:value="city.name" @click="chooseCity(city.name)">{{city.name}}</p>
        </div>
        <span class="icon-ctrl" @click="openselect"></span>
        </div>
        <div v-show="isopen" class="selectMask" @click="closeSelect"></div>
      </div>
    </div>
    <input type="file"  id="changeAvatar" accept="image/*" @change="showNewAvatar()">
</div>

</template>

<script>
import {mapState, mapGetters} from 'vuex';

export default{
    name: 'myInformation',
    head() {
        return {
            title: '个人信息'
        };
    },
    data () {
        return {
            oldUserInfo: {}, // 数据库信息
            userInfo: {'nickName': '', 'realName': '', 'city': '', 'sex': ''}, // 用户编辑中的信息
            newUserInfo: {}, // 要发送的修改过的信息，过滤掉了用户未修改的字段
            position: 'default',
            showPositionValue: true,
            isLogin: true,
            cityList: {},
            isopen: false
        };
    },
    computed: {
        ...mapState(['mobileNbr', 'global', 'userCode']),
        ...mapGetters(['doneTodosCount'])
    },
    beforeCreate() {
    },
    methods: {
        goBack () {
            this.$router.go(-1);
        },
        getUserInfo: function() {
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"getUserInfo","params":{"userCode":"' + this.userCode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                for (var item in this.userInfo) {
                    this.userInfo[item] = result.data.result[item];
                    this.oldUserInfo[item] = result.data.result[item];
                }
            });
        },
        updateUserInfo: function() {
            var flag = false;// 是否通过校验
            var testNickName = /^[a-zA-Z0-9_\u4E00-\u9FA5]{1,15}$/;
            var testRealName = /^[\u4E00-\u9FA5]{2,7}$/;

            if (this.userInfo.nickName === '') {
                this.$vux.toast.text('昵称不允许为空', 'bottom');
            } else if (!testNickName.test(this.userInfo.nickName)) {
                this.$vux.toast.text('昵称应为1-15位的中英文、数字及下划线', 'bottom');
            } else if (this.userInfo.realName === '') {
                this.$vux.toast.text('真实姓名不允许为空', 'bottom');
            } else if (!testRealName.test(this.userInfo.realName)) {
                this.$vux.toast.text('真实姓名只允许为中文2-7位', 'bottom');
            } else if (this.userInfo.sex === 'U') {
                this.$vux.toast.text('请选择性别', 'bottom');
            } else if (this.userInfo.city === '') {
                this.$vux.toast.text('请选择城市', 'bottom');
            } else {
                flag = true;
            }
            if (flag) {
                for (var item in this.userInfo) {
                    if (this.userInfo[item] !== this.oldUserInfo[item]) {
                        this.newUserInfo[item] = this.userInfo[item];
                    }
                }
                if (JSON.stringify(this.newUserInfo) === '{}') {
                    this.$vux.toast.text('您未修改任何信息', 'bottom');
                    flag = false;
                }
            }
            if (flag) {
                console.log('{"id":19,"jsonrpc":"2.0","method":"updateUserInfo","params":{"mobileNbr":"' + this.$store.state.mobileNbr + '","userCode":"' + this.$store.state.userCode + '","updateInfo":' + JSON.stringify(this.newUserInfo) + '}}');

                this.axios({
                    method: 'post',
                    url: this.global.hftcomClient,
                    data: '{"id":19,"jsonrpc":"2.0","method":"updateUserInfo","params":{"mobileNbr":"' + this.mobileNbr + '","userCode":"' + this.userCode + '","updateInfo":' + JSON.stringify(this.newUserInfo) + '}}',
                    contentType: 'application/json',
                    dataType: 'json'
                }).then((result) => {
                    switch (result.data.result.code) {
                    case 50001:
                        this.$vux.toast.text('请将信息填写完整', 'bottom');
                        break;
                    case 20000:
                        this.$vux.toast.text('失败，请重试', 'bottom');
                        break;
                    case 50000:
                        this.$vux.toast.text('修改成功', 'bottom');
                        for (var i in this.newUserInfo) {
                            this.oldUserInfo[i] = this.newUserInfo[i];
                        }
                        this.newUserInfo = {};
                        flag = false;
                        break;
                    case 50003:
                        this.$vux.toast.text('您没有修改任何信息', 'bottom');
                        break;
                    case 50008:
                        this.$vux.toast.text('用户不存在', 'bottom');
                        break;
                    default:
                        this.$vux.toast.text(result.data.result, 'bottom');
                    };
                });
            }
        },
        openselect: function() {
            if (this.isopen === true) {
                this.isopen = false;
            } else {
                this.isopen = true;
            }
        },
        chooseCity: function(city) {
            this.userInfo.city = city;
            this.isopen = false;
        },
        closeSelect: function() {
            this.isopen = false;
        }
    },
    created() {
        this.getUserInfo();
        this.$store.dispatch('getOpenCity').then((data) => {
            this.cityList = data.result;
        });
    },
    components: {
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#changemyinfo
  z-index 1
  position: absolute;
  width: 10.8rem;
  height: 100%;
  background: #f0f0f0;
  .header
    display:flex
    height:1.24rem
    line-height:1.24rem
    background:#f64f48
    color:#fff
    .header-item
      flex:1
      text-align:center
    :nth-child(1)
      font-size:0.78rem
      font-weight:bold
      line-height:1.24rem
    :nth-child(2)
      flex:6
      font-size:0.5rem
      line-height:1.24rem
      padding-right:0.5rem
  .panel
    & > p
      height:1.5rem
      line-height:1.5rem
      border-bottom:1px solid #cecece90
      & > label
        display:inline-block
        width:3.4rem
        font-size:0.44rem
        text-align:left
        text-indent:1rem
      & > input
        float:right
        font-size:0.44rem
        margin-right:0.5rem
        display:inline-block
        width:6.8rem
        height:1.5rem
        outline:none
        outline-color:#f64f48
        background:#f0f0f0
      .radio
        display:inline-block
        height:1.5rem
        line-height:1.5rem
        width:2.3rem
        input
          margin:0
          padding:0
          width:0.5rem
          height:1.5rem
          float:left
          margin-right:0.5rem
        label
          margin:0
          padding:0
          font-size:0.44rem
          display:inline-block
          width:100%
          height:1.5rem
    .city
      height:1.5rem
      line-height:1.5rem
      border-bottom:1px solid #cecece90
      & > label
        display:inline-block
        width:3.4rem
        font-size:0.44rem
        text-align:left
        text-indent:1rem
      .cityInfo
        display:inline-block
        position:relative
        z-index:1
        & > #select
          margin:0.25rem 0
          height:1rem
          line-height:1rem
          width:4rem
          padding-left:0.8rem
          border: 1px #cecece90 solid
          border-radius:0.15rem
        .select-item
          position:absolute
          top:1.4rem
          left:0
          height:5rem
          width:4rem
          overflow-y:scroll
          border:1px solid #cecece90
          border-radius:0.15rem
          background:#fff
          padding-left:0.8rem
        .icon-ctrl
          position:absolute
          top:0.24rem
          right:0.2rem
          transform:rotate(180deg)
          font-size:0.7rem
      .selectMask
        position:fixed
        top:0
        left:0
        height:100%
        width:100%
        z-index:0
#changeAvatar
    z-index: -100!important
    visibility:hidden
</style>