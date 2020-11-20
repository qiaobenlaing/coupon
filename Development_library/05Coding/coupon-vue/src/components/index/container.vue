<template>
	<div class="ad-box">
        <div class="ad-container" v-for="(group,adListIndex) in adList" :key="adListIndex">
            <div class="ad-group-title" v-if="group[0].adtitle">{{group[0].adtitle}}</div>
            <div class="ad-item"
                 v-for="(ad,index) in group"
                 :class="{'ad-item-1':group.length%2===1 && index ===0}"
                 :style="{backgroundImage:ad.back_pic?'url('+ ad.back_pic+')':''}"
                 :key="index"
                 @click="handleAdClick(ad)"
            >
                <div class="ad-title-box">
                    <div class="ad-title">{{ad.title}}</div>
                    <div class="ad-sub-title">{{ad.sub_title}}</div>
                </div>
                <div class="ad-img"><img v-lazy="ad.pic" v-if="ad.pic">
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import {mapState} from 'vuex';
export default{
    props: ['adList'],
    data() {
        return {
        };
    },
    methods: {
        handleAdClick: function (ad) {
            window.location.href = ad.link;
        }
    },
    computed: {
        ...mapState(['global'])
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
.ad-box
        clear both

        .ad-container
            box-shadow: 0 0 1px 0 #ebedf0;
            margin-bottom 10px
            background-color #fff
            padding 20px 0
            overflow hidden

            .ad-group-title
                font-size 0.5rem
                margin-bottom 10px
                padding-left 2px
                border-left 6px solid #409eff
                color #303133

            .ad-item
                box-shadow: 0 0 20px 0 #ebedf0;
                height 60px
                display inline-block
                float left
                margin 5px
                font-size 0.32rem
                background-repeat no-repeat
                background-size 100% 100%
                -moz-background-size 100% 100%
                width calc(50% - 10px)

                &.ad-item-1
                    width calc(100% - 10px)

                .ad-title-box
                    display inline-block
                    width calc(100% - 130px)
                    padding 5px 10px

                    .ad-title
                        height 25px
                        line-height 20px
                        color #303133
                        font-weight bold
                        white-space nowrap
                        text-overflow clip

                    .ad-sub-title
                        height 25px
                        color #909399
                        white-space nowrap
                        text-overflow clip

                .ad-img
                    width 110px
                    height 60px
                    float right
                    text-align center

                    img
                        width 60px
</style>
