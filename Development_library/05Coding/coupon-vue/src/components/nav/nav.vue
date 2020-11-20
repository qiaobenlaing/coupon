<template>
  <div id="nav">
    <div class="tab border-1px">
      <div class="tab-item">
        <router-link to="/index" replace class="icon-index-solid">首页</router-link></div>
      <div class="tab-item">
        <router-link to="/shops" replace class="icon-shop">附近</router-link></div>
       <!--  <div class="tab-item">
        <router-link to="/accounts" replace class="icon-icon-money">买单</router-link></div> -->
        <div class="tab-item">
        <router-link to="/myCoupon" replace class="icon-coupon">我的优惠券</router-link></div>
        <div class="tab-item">
        <router-link to="/myInformation" replace class="icon-myinfo">个人</router-link></div>
    </div>
    </div>
</template>

<script>
export default {};
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import "../../common/stylus/mixin.styl"
#nav
  .tab
    display :flex
    width:100%
    height :1.52rem
    line-height:1.52rem
    border-1px(rgba(181,181,181,0.3))
    position:fixed
    bottom:0
    left:0
    background-color:#fff
    .tab-item
      flex:1
      text-align :center
      line-height:1.52rem
      & > a
        display :block
        height:0.36rem
        line-height:0.35rem
        font-size :0.28rem
        color: rgb(77,85,93)
        &:before
          display:inline-block
          height:0.65rem
          line-height:1.3rem
          font-size:0.7rem
          width:100%
        &.active
          color :rgb(240,20,20)
</style>
