<template>
    <div id="header2">
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
#header2
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
</style>
