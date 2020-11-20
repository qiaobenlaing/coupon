<template>
<div id="aboutus">
    <header2 :title="'关于我们'"></header2>
    <div class="information">
      <p>惠圈致力于打造真实、可靠、便捷的本地化生活服务平台，基于消费者的地理位置，随时随地为消费者提供最新最全的本地化活动优惠信息以及相关服务。精准位置的券分享使消费者随时随地发现身边的实惠体验。</p>
      <p>惠圈以电子会员卡的形式打通顾客与商家的沟通渠道，建立顾客与商家的关联度，让消费者可随时、随地、简单、便捷地享受会员优享待遇，提升会员对商家品牌的认知度。</p>
    </div>
</div>
</template>

<script>
import {mapState, mapGetters} from 'vuex';
import header2 from '@/components/header/header2.vue';
export default{
    name: 'myInformation',
    head() {
        return {
            title: '个人信息'
        };
    },
    data () {
        return {
            position: 'default',
            showPositionValue: true,
            isLogin: true
        };
    },
    computed: {
        ...mapState(['mobileNbr']),
        ...mapGetters(['doneTodosCount'])
    },
    beforeCreate() {
    },
    methods: {
    },
    components: {
        header2
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl"
#aboutus
  .information
    font-size:0.5rem
    margin-bottom:50px
    padding:1rem 0.5rem
    p
      text-indent:2em
      line-height:1rem
</style>
