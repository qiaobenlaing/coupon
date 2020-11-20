<template>
    <div id="sassmyvipcard">
      <header2fix :title="'会员卡'"></header2fix>
      <div class="cardinfodiv">
        <div v-show="weixinCardList.length !== undefined"  class="cardwrapper" v-for="(index) in weixinCardList">
          <div class="cardback">
              <img :src="global.hftcomImgSrc + index.bgImgCode" alt="">
          </div>
          <div class="cardbackcolor" :class="{'cardbacksilvery': index.cardName==='银卡','cardbackgold': index.cardName==='金卡'}"></div>
          <div class="logo"><img :src="global.hftcomImgSrc + index.logoUrl" alt=""></div>
          <div class="cardinfo">
              <div class="shopname">{{index.shopName}}</div>
              <div class="carddetail">{{index.cardName}} <span class="point">积分：{{index.point}}</span></div>
          </div>
        </div>
      </div>
      <div class="sass-vip-empty" v-if="weixinCardList.length === undefined">
		暂无优惠券
	  </div>
    </div>
</template>
<script>
import {mapState} from 'vuex';
import header2fix from '@/components/header/header2fix.vue';
export default{
    data() {
        return {
            weixinCardList: [],
            cardInfo: {},
            userCardCode: ''
        };
    },
    computed: {
        ...mapState(['global', 'userCode', 'isLogin', 'zoneId'])
    },
    components: {
        header2fix
    },
    methods: {
        goBack () {
            this.$router.go(-1);
        },
        showcard: function() {
            this.axios({
                method: 'post',
                url: '/Api/Activity',
                data: '{"id":29,"jsonrpc":"2.0","method":"showCardList","params":{"userCode":"' + this.userCode + '","zoneId":"' + this.zoneId + '"}}'
            }).then((result) => {
                console.log(result.data.result);
                this.weixinCardList = result.data.result;
            });
        }
    },
    created() {
        this.showcard();
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#sassmyvipcard
  .sass-vip-empty
	  position:absolute
	  top:50%
	  left:calc((100% - 5.1em) / 2)
	  color:#a7a7a7
	  font-size:0.45rem
  .cardinfodiv
      margin-top 1.24rem
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
  .cardlist-empty
		position:absolute
	    height:100%
	    width:10.8rem
	    background:rgb(240,240,240)
	  	position:absolute
	    top:50%
	    left:calc((100% - 5.1em) / 2)
	    color:#a7a7a7
	    font-size:0.45rem
</style>
