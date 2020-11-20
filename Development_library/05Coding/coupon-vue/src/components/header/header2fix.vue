<template>
<div id="header2fix">
        <header class="header">
          <span class="header-item icon-back" @click='goBack'></span>
          <span class="header-item">{{title}}</span>
      </header>
  </div>
</template>
<script>
export default{
    props: ['title'],
    methods: {
        goBack () {
            this.$router.go(-1);
        }
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#header2fix
    .header
        width 100%
        height 1.24rem
        background-color #f64f48
        position fixed
        top 0
        left 0
        z-index 1
        .header-item
            text-align:center
            color:#fff  
        :nth-child(1)
            display inline-block
            height 1.24rem
            line-height 1.24rem
            width 1.5rem
            font-size:28px
            font-weight:bold
        :nth-child(2)
            display inline-block
            position absolute
            left 2.2rem
            height 1.24rem
            line-height 1.24rem
            font-size:0.5rem
            width 6.3rem
</style>
