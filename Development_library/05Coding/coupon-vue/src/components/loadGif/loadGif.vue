<template>
        <div class="loadGif" v-show="showLoadGif">
            <div class="loadGif1" >
                <img src="../../../static/img/loadmore.gif" alt="">
            </div>
        </div>
</template>
<script type="text/ecmascript-6">
export default {
};
</script>
<style lang="stylus" rel="stylesheet/stylus"  scoped>
.loadGif
    height 1.5rem
    width 100%
    background-color #fff
    .loadGif1
        height 1rem
        width 1rem
        margin auto!important
        img
            height 90%
            width 90%
</style>
