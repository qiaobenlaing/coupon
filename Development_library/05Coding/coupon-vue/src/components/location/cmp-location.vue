<template>
 <div id="indexCity">
   <header class="header">
            <span class="header-item icon-back" @click='goBack'></span>
            <span class="header-item ">选择城市</span>
             <span class="header-item "></span>
    </header>
    <div  class="city" v-for="city in cityList"  :key="city.name" >  
        <div class="changeCity" v-bind:class="[city.name===localCity ? 'activeCity' : '']" @click="changeCity($event)">{{city.name}}</div>
        <!-- <button @click="changeCity($event)" id="cityBtn" >{{city.name}}</button> &nbsp; -->
    </div>
 </div>
</template>

<script>
import { mapState } from 'vuex';
export default {
    data() {
        return {
            cityList: [],
            isActive: true
        };
    },
    methods: {
        goBack() {
            this.$router.go(-1);
        },
        changeCity(event) {
            this.$store.dispatch('setLocalCity', event.currentTarget.innerText);
            this.$store.dispatch('setLocation', event.currentTarget.innerText);
            this.$store.state.isChooseCity = true;
            this.$router.go(-1); // 选择完成后自动会跳上一级
        }
    },
    computed: {
        ...mapState([
            'localCity'
        ])
    },
    beforeCreate() {
    },
    created() {
        this.$store.dispatch('getOpenCity').then((data) => {
            this.cityList = data.result;
        });
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#indexCity
  background:rgb(240,240,240)
  .header
    display:flex
    height:1.24rem
    line-height:1.24rem
    background:#f64f48
    .header-item
      flex:1
      height:1.24rem
      line-height:1.24rem
      text-align:center
      vertical-align:middle
    :nth-child(1)
      color:#fff;
      height:1.24rem
      line-height:1.24rem
      font-size:0.66rem
      font-weight:bold
    :nth-child(2)
      color:#fff;
      flex:5
      height:1.24rem
      line-height:1.24rem
      font-size:0.45rem
      text-align:center
      .headInput
        background-color : #EAEAEA
        vertical-align:middle;
        text-indent:0.4rem;    /*文本缩进的距离*/ 
        height : 0.7rem;
        font-size:0.44rem
        color :#B3B3B3
        font-family :Georgia;
        width :5.4rem
        margin-bottom: 4px;
  .city
    width:10.8rem
    background-color :#fff;
    .changeCity
      margin:0 ;
      height:1.3rem;
      font-size :0.44rem;
      line-height:1.3rem;
      text-indent:0.6rem;
      border-bottom:1px solid #cecece;
    .activeCity
      color:#f64f48
      background:rgba(232,232,232,0.522)
</style>