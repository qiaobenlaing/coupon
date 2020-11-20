<template>
<div id="cooperation">
    <header2 :title="'协议'"></header2>
    <div class="information">
      <p>xxxxxx协议
      </p>
    </div>
</div>
</template>

<script>
import header2 from '@/components/header/header2';

export default{
    data () {
        return {
        };
    },
    computed: {
    },
    beforeCreate() {
    },
    methods: {
        goBack () {
            this.$router.go(-1);
        }
    },
    components: {
        header2
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#cooperation
  .information
    font-size:0.5rem
    margin-bottom:50px
    padding:1rem 0.5rem
    p
      text-indent:2em
      line-height:1rem
</style>
