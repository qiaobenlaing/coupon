<template>
  <div id="payChannelComponent">
    <div class="paychannelItem">
      <div class="toPayChannelDetails" @click="toPayChannelDetails(index.cardId)">
        <div class="img">
          <img v-lazy="this.global.staticSrc+'/huiquan/static/img/guizhoubanklogo.png'" alt="">
        </div>
        <div class="text">
          <span class="deductionFlag" v-if="index.deductionFlag">扣款渠道</span>
          <span class="signed" v-if="index.isSigned">已签约</span>
          <span class="unsigned" v-if="!index.isSigned">未签约</span>
          <span class="arrow icon-back"></span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {mapState} from 'vuex';
export default{
    props: ['index'],
    data() {
        return {

        };
    },
    computed: {
        ...mapState(['global'])
    },
    watch: {

    },
    methods: {
        toPayChannelDetails(cardId) {
            if (this.index.isSigned) {
                this.$router.push({path: `/cardInfo/${cardId}`});
            } else {
                this.$router.push({path: `/signingAuthorization/${cardId}`});
            }
        }
    },
    created () {
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#payChannelComponent
  border-bottom:2px solid #f0f0f0
  .paychannelItem
    position:relative
    background:#fff
    height:2.4rem
    width:100%
    .img
      position:absolute
      top:0.5rem
      left:0.2rem
      height:1.4rem
      width:3rem
      img
        height:100%
    .text
      position:absolute
      left:3.03rem
      display:inline-block
      margin-left:0.1rem
      height:2.35rem
      width:7rem
      font-size:0.5rem
      line-height:2.4rem

    .arrow
      float:right
      font-size:20px
      line-height:2.4rem
      transform:rotate(180deg)

    .deductionFlag
      margin-left: 0.5rem
      color: blue

    .signed
      margin-left: 2rem;

    .unsigned
      margin-left: 2rem;
</style>
