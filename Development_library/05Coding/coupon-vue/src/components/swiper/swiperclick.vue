<template>
<div class="swiper">
    <swiper :options="swiperOption" >
        <swiper-slide v-for="index in swiperinfo" :key="index.url" >
            <a :href= index.activityWeb ><img v-lazy="global.hftcomImgSrc + index.url" alt=""></a>
        </swiper-slide>
        <div class="swiper-pagination"  slot="pagination"></div>
    </swiper>
</div>
<!-- <div class="swiper" :disabled="dis" @touchstart="touchstart" @touchend="touchend" @touchmove="touchmove" >
    <div class="container" ref="imgarr"  v-for="each in imgsrcarr">
        <img :src="each" alt="">
    </div>
</div> -->
   
</template>
<script>
import {mapState} from 'vuex';
import { swiper, swiperSlide } from 'vue-awesome-swiper';
export default {
    components: {
        swiper,
        swiperSlide
    },
    props: ['swiperinfo'],
    data() {
        return {
            homeSwiper: [],
            swiperOption: {
                pagination: {
                    el: '.swiper-pagination',  // 设置滚动样式  下面的小点点
                    clickable: true // 点击小点点会转到相应的图片
                }
                // setWrapperSize: true, // 设置布局 自动补齐高度
                // autoHeight: true // 自动高度
            },
            dis: true,
            startX: Number, // 鼠标的开始位置
            sub: Number, // 鼠标移动的偏移位置
            positionX: Number, // 第一个容器的left位置
            imgsrcarr: ['https://test.hfq.huift.com.cn/Public/Uploads/20180413/5ad064ef1d0d5.jpg', 'https://test.hfq.huift.com.cn/Public/Uploads/20180409/5acad00934901.jpg'],
            currentpage: 0,
            speed: 10
        };
    },
    computed: {
        ...mapState(['global']),
        swiperwidth: function() {
            return this.$refs.imgarr[0].offsetWidth;
        }
    },
    watch: {
        positionX: function() {
            for (let i = 0; i < this.imgsrcarr.length; i++) {
                this.$refs.imgarr[i].style.left = this.positionX + (this.swiperwidth * i) + 'px';
            }
        }
    },
    methods: {
        touchstart: function(evt) {
            var touch = evt.touches[0]; // 获取第一个触点
            var x = Number(touch.pageX); // 页面触点X坐标
            this.startX = x;
        },
        touchmove: function(evt) {
            var touch = evt.touches[0]; // 获取第一个触点
            var x = Number(touch.pageX); // 页面触点X坐标
            this.sub = x - this.startX; // 鼠标偏移位置
            if (this.sub !== 0) {
                this.positionX = (this.sub - (this.swiperwidth * this.currentpage));
            }
        },
        touchend: function() {
            console.log('结束位置' + this.positionX);

            if (Math.abs(this.sub) < (this.swiperwidth / 4)) { // 没过半
                this.toPage(this.currentpage);
            } else { // 过半
                if (this.sub > 0) { // 右划
                    var prevpage = this.currentpage - 1;
                    if (prevpage < 0) {
                        prevpage = 0;
                    }
                    this.toPage(prevpage);
                } else { // 左划
                    var nextpage = this.currentpage + 1;
                    console.log(nextpage);
                    if (nextpage > this.imgsrcarr.length - 1) {
                        nextpage = this.imgsrcarr.length - 1;
                    }
                    this.toPage(nextpage);
                }
            }
        },
        toPage: function (index) {
            if (this.currentpage < index) {
                this.toleft(index);
            } else if (this.currentpage > index) {
                this.toright(index);
            } else {
                if (this.sub > 0) {
                    this.toleft(index);
                } else {
                    this.toright(index);
                }
            }
            this.currentpage = index;
        },
        toleft: function(index) {
            let that = this;
            let target = -(that.swiperwidth * index);
            let now = that.positionX;
            var timer = setInterval(function() {
                console.log(target - now);
                that.positionX -= (-(that.swiperwidth * index) - that.positionX) / that.speed;
                // if (distance > 10) {
                //     distance -= 2;
                // }
                console.log(that.positionX);
                if (that.positionX > ((-(that.swiperwidth * index))) && that.positionX < ((-(that.swiperwidth * index)))) {
                    that.positionX = (-(that.swiperwidth * index));
                    clearInterval(timer);
                }
            }, 10);
        },
        toright: function(index) {
            let that = this;
            var distance = that.speed;
            var timer1 = setInterval(function() {
                console.log(-(that.swiperwidth * index));
                that.positionX += distance;
                if (distance > 10) {
                    distance -= 2;
                }
                if (that.positionX > ((-(that.swiperwidth * index)) - distance) && that.positionX < ((-(that.swiperwidth * index)) + distance)) {
                    that.positionX = (-(that.swiperwidth * index));
                    clearInterval(timer1);
                }
            }, 10);
        },
        showimgarr: function() {
            this.positionX = 0;
        }
    },
    created() {

    },
    mounted() {
    }
};

</script>
<style lang="stylus">
  .swiper
    margin-top:1.24rem
    height:4.4rem
    width:10.8rem
    text-align:center
    background-color: #333333
    .swiper-container
        height:100%
        z-index:0
        .swiper-wrapper
            height:100%
            img
                width:100%
        .swiper-pagination
            height:0.2rem
    .container
        height: 3.4rem;
        width: 10.8rem;
        overflow: hidden;
        position: absolute;
        left: 0;
        top: 1.24rem;
        img
            width:100%
            overflow:hidden
</style>