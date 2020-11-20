<template>
<div id="changepassword">
    <header class="header">
      <span class="header-item" @click='goBack'><</span>
      <span class="header-item">修改密码</span>
    </header>

</div>
</template>
<script>
export default{
    methods: {
        goBack () {
            this.$router.go(-1);
        }
    }

};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#changepassword
  .header
    display:flex
    height:40px
    line-height:40px
    background:#c7c7c7
    color:#fff
    .header-item
      flex:1
      text-align:center
      border:1px solid #cecece
    :nth-child(1)
      font-size:28px
      font-weight:bold
      line-height:30px
    :nth-child(2)
      flex:6
      font-size:20px
      line-height:35px
      padding-right:20px
</style>
