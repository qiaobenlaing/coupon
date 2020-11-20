<template>
    <div id="myvipcard">
        <header class="header">
          <span class="header-item icon-back" @click='goBack'></span>
          <div class="header-item">
            <span class="isadd add" :class="{'active':isadd===2}" @click="showcard(2)">未添加</span>
            <span class="isadd added" :class="{'active':isadd===1}" @click="showcard(1)">已添加</span>
          </div>
      </header>
      <div v-show="weixinCardList!=={}" class="cardwrapper" v-for="(index) in weixinCardList">
        <div class="cardback">
            <img :src="global.hftcomImgSrc + index.logoUrl" alt="">
        </div>
        <div class="cardbackcolor" :class="{'cardbacksilvery': index.cardName==='银卡','cardbackgold': index.cardName==='金卡'}"></div>
        <div class="logo"><img :src="global.hftcomImgSrc + index.logoUrl" alt=""></div>
        <div class="cardinfo">
            <div class="shopname">{{index.shopName}}</div>
            <div class="carddetail">{{index.cardName}} <span class="point">积分：{{index.point}}</span></div>
        </div>
        <div class="addbtn" @click="addvipcard(index.shopCode)" v-show="isadd ===2">
          <span class="text" v-show="isadding!==index.shopCode">添加</span>
          <transition name="rotate">
            <span class="icon-spinner8" v-show="isadding===index.shopCode"></span>
          </transition>
        </div>
    </div>
</div>
</template>
<script>
import {mapState} from 'vuex';
export default{
    data() {
        return {
            isadd: 2, // 1-已添加，2-未添加
            weixinCardList: [],
            cardInfo: {},
            signPakage: {},
            isadding: '',
            userCardCode: ''
        };
    },
    computed: {
        ...mapState(['global', 'userCode', 'isLogin'])
    },
    methods: {
        goBack () {
            this.$router.go(-1);
        },
        showcard: function(inWeixinCard) {
            this.isadd = inWeixinCard;
            this.axios({
                method: 'post',
                url: this.global.hftcomWeixinCard,
                data: '{"id":29,"jsonrpc":"2.0","method":"listWeixinCard","params":{"userCode":"' + this.userCode + '","inWeixinCard":"' + inWeixinCard + '"}}'
            }).then((result) => {
                console.log(result.data.result);
                this.weixinCardList = result.data.result;
            });
        },
        addvipcard: function(shopCode) { // 添加会员卡到微信卡包
            this.isadding = shopCode;
            this.axios({
                method: 'post',
                url: this.global.hftcomWeixinCard,
                data: '{"id":29,"jsonrpc":"2.0","method":"addWeixinCard","params":{"userCode":"' + this.userCode + '","shopCode":"' + shopCode + '"}}'
            }).then((result) => {
                console.log(result.data);
                if (result.data.result.signPakage === '' || result.data.result.signPakage === undefined) {
                    this.$vux.toast.text('数据请求错误，请稍后重试', 'bottom');
                } else {
                    this.cardInfo = result.data.result.cardInfo;
                    this.signPakage = result.data.result.signPakage;
                    this.userCardCode = result.data.result.userCardCode;
                    this.addcardtoweixin();
                }
                this.isadding = '';
            });
        },
        updateUserWeixinCard: function() { // 告知后端会员卡添加至卡包成功
            this.axios({
                method: 'post',
                url: this.global.hftcomWeixinCard,
                data: '{"id":29,"jsonrpc":"2.0","method":"updateUserWeixinCard","params":{"userCardCode":"' + this.userCardCode + '"}}'
            }).then((result) => {
                this.$router.go(0);
                console.log(result.data.result);
            });
        },
        addcardtoweixin: function() {
          /* eslint-disable */
          wx.config({
            debug: false,
            appId: this.signPakage.appid,
            timestamp: this.signPakage.timestamp,
            nonceStr: this.signPakage.noncestr,
            signature: this.signPakage.signature,
            jsApiList: [
            'addCard'
            ]
          });
          wx.ready(() => {
            wx.addCard({
              cardList:  [
                    {
                      code:this.cardInfo.code,
                        cardId: this.cardInfo.card_id,
                        cardExt: '{"code":"' + this.cardInfo.code + '","timestamp":"' + this.cardInfo.timestamp + '","signature":"' + this.cardInfo.signature + '","nonce_str":"' + this.cardInfo.nonce_str + '"}'
                    }
                ],
                success: (res) => {
                  this.$vux.toast.text('已添加卡券','bottom');
                  this.updateUserWeixinCard();
                },
                cancel:  (res) => {
                  this.$vux.toast.text('取消添加','bottom')
                }
              });
          });
          wx.error(function(res){
            console.log('err', res)
          });
            /* eslint-enable */
        }
    },
    created() {
        this.showcard(2);
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#myvipcard
  .header
    display:flex
    height:1.24rem
    line-height:1.24rem
    background:#f64f48
    color:#fff
    .header-item
      flex:1
      text-align:center
    & > :nth-child(1)
      font-size:0.78rem
      font-weight:bold
      line-height:1.24rem
    & > :nth-child(2)
      flex:6
      font-size:0.45rem
      line-height:1.24rem
      padding-right:0.5rem
    .isadd
        padding:0.13rem 0.55rem
        border:1px solid #fff
    .add
        border-radius:0.1rem 0rem 0 0.1rem
    .added
        border-radius:0rem 0.1rem 0.1rem 0
    .active
        font-weight:bold
        background:#fff
        color:#f64f48
  .cardwrapper
      height:2rem
      overflow:hidden
      margin:0.2rem 0.3rem
      border-radius:0.1rem
      position:relative
      .cardback
          position:absolute
          top:0
          left:0
          z-index :-1
          filter:blur(0.3rem)
      .cardbackcolor
          position:absolute
          top:0
          left:0
          width:100%
          height:100%
      .cardbacksilvery
          background :rgba(66,66,66,0.5)
      .cardbackgold
          background :rgba(100,100,0,0.5)
      .logo
        position:absolute
        top:0.32rem
        left:0.4rem
        width:1.2rem
        height:1.2rem
        border:0.08rem solid #fff
        border-radius:50%
        overflow:hidden
        img
          height:100% 
      .cardinfo
        position:absolute
        top:0.28rem
        left:2rem
        color:#fff
        width:5.5rem
        .shopname
            font-size:0.44rem
        .carddetail
            margin-top:0.35rem
            font-size:0.7rem
            font-weight:bold
            line-height:0.7rem
            .point
                float:right
                font-size:0.3rem
                line-height:1rem
      .addbtn
        position:absolute
        top:0.6rem
        left:8rem
        color:#fff
        font-size:0.34rem
        background:#62b900
        width: 1.8rem;
        height: 0.8rem;
        line-height:0.8rem
        border-radius:0.5rem
        .text
          position:absolute
          top:0rem
          left:0.58rem
        .icon-spinner8
          position:absolute
          top:0.15rem
          left:0.68rem
          font-size:0.46rem
        .rotate-enter-active
          transition: transform 4s linear
        .rotate-enter
          transform: rotate(-1440deg)
</style>
