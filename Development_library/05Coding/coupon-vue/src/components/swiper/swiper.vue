<template>
    <div id="swiper">
        <swiper :options="swiperOption" ref="mySwiper">
            <swiper-slide v-for="img in newImgSrcArr" :key="img"><img v-lazy="global.hftcomImgSrc + img"></swiper-slide>
            <div class="swiper-pagination"  slot="pagination"></div>
        </swiper>
            <p v-for="img in newImgSrcArr">img</p>
    </div>
</template>
<script>
import { swiper, swiperSlide } from 'vue-awesome-swiper';
import {mapState} from 'vuex';
export default {
    props: ['imgSrcArr'],
    data() {
        return {
            // 轮播图相关配置
            swiperOption: {
        // notNextTick是一个组件自有属性，如果notNextTick设置为true，组件则不会通过NextTick来实例化swiper，也就意味着你可以在第一时间获取到swiper对象，假如你需要刚加载遍使用获取swiper对象来做什么事，那么这个属性一定要是true
                notNextTick: true,
        // swiper configs 所有的配置同swiper官方api配置
                autoplay: true,
        // direction : 'vertical', //设置为横向轮播还是纵向轮播
                effect: 'effect', // 设置图片切换时的样式
                grabCursor: false, // 设置鼠标移到图片上会不会成为手抓形状
                setWrapperSize: true, // 设置布局 自动补齐高度
                // autoHeight: true, // 自动高度
        // paginationType:"bullets",
                pagination: {
                    el: '.swiper-pagination',  // 设置滚动样式  下面的小点点
                    clickable: true // 点击小点点会转到相应的图片
                },
                prevButton: '.swiper-button-prev',
                nextButton: '.swiper-button-next',
        // scrollbar:'.swiper-scrollbar',
                observeParents: true
            },
            newImgSrcArr: ['/Public/Uploads/20180528/5b0bb30255d5a.jpg', '/Public/Uploads/20180528/5b0bb300010f7.jpg']
        };
    },
    computed: {
        ...mapState(['global'])
    },
    watch: {
        imgSrcArr: function() {
            this.loadSwiper();
        }
    },
    methods: {
        loadSwiper() {
            if (this.imgSrcArr === undefined) {
                this.imgSrcArr = [];
            } else {
                if (Object.prototype.toString.call(this.imgSrcArr).slice(8, -1) === 'String') {
                    this.newImgSrcArr.push(this.imgSrcArr);
                } else {
                    this.newImgSrcArr = this.imgSrcArr;
                }
            }
        }
    },
    created() {
        this.loadSwiper();
    },
    components: {
        swiper,
        swiperSlide
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#swiper
    height:100%
    .swiper-container
        height:100%
        z-index:0
        .swiper-wrapper
            height:100%
            img
              width:100%
              // max-width 100%
              // max-height 100%
        .swiper-pagination
            height:3.1rem
</style>
