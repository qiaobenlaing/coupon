<template>
  <div id="cardInfocomponent">
    <div class="cardLayer">
      <div class="card">
        <div class="cardImg">
          <img v-lazy="this.global.staticSrc+'/huiquan/static/img/guizhoubanklogo.png'" alt="">
        </div>
        <div class="cardNum">
          <span>{{cardInfo.cardNum}}</span>
        </div>
      </div>
      <div class="cardState">
          <span class="titleSpan">
            支付业务:
          </span>
          <span>
            {{cardInfo.payMode}}
          </span>
          <br>
          <br>
          <span class="titleSpan">
            签约状态:
          </span>
          <span>
            {{cardInfo.signingState}}
          </span>
          <br>
          <br>
          <span class="titleSpan">
            签约时间:
          </span>
          <span>
            {{cardInfo.signingTime}}
          </span>
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
            cardInfo: {}
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
        this.cardInfo = this.index;
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
.cardLayer
  background: #F0F0F0
  width: 100%
  height: 7.5rem
  padding-top: 0.8rem

.card
  background: #ffffff
  width: 80%
  height: 4rem
  top: 0.5rem
  margin-left: 10%
  border-radius: 0.2rem

.cardImg
  height:1.4rem
  width:3rem
  padding-top: 0.8rem
  padding-left: 1rem
  img
    width: 3rem
    height:100%

.cardNum
  font-size: 0.46rem
  padding-top: 0.5rem
  padding-left: 1rem

.cardState
  width: 80%;
  margin-left: 10%;
  margin-top: 0.5rem;
  padding-left: 1rem;
  font-size: 0.46rem;
</style>
