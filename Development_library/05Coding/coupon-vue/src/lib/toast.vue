<template>
<transition :name="fadeIn">
  <div class="alertBox" v-show="show">
   <div class="alert-mask" v-show="isShowMask"></div>
   <transition :name="translate">
    <div class="box" :class="position" v-show="show">
     {{text}}
    </div>
   </transition>
  </div>
 </transition>
</template>

<script>
export default {
    data() {
        return {
        };
    },
    props: {
        show: { // 是否显示此toast
            default: false
        },
        text: { // 提醒文字
            default: 'loading'
        },
        position: { // 提醒容器位置
            default: 'center'
        },
        isShowMask: { // 是否显示遮罩层
            default: false
        },
        time: { // 显示时间
            default: 1500
        },
        transition: { // 是否开启动画
            default: true
        }
    },
    mounted() { // 时间控制
        setTimeout(() => {
            this.show = false;
        }, this.time);
    },
    computed: {
        translate() { // 根据props，生成相对应的动画
            if (!this.transition) {
                return '';
            } else {
                if (this.position === 'top') {
                    return 'translate-top';
                } else if (this.position === 'middle') {
                    return 'translate-middle';
                } else if (this.position === 'bottom') {
                    return 'translate-bottom';
                }
            }
        },
        fadeIn() { // 同上
            if (!this.transition) {
                return '';
            } else {
                return 'fadeIn';
            }
        }
    }
};

</script>
<style lang="stylus" rel="stylesheet/stylus">

</style>
