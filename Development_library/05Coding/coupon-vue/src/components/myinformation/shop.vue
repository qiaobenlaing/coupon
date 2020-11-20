<template>
<div id="order" >
    <header2fix :title="'我的订单'"></header2fix>
    <div class="change">
        <div class="change0">
            <div class="finished" id="finished" @click="showFinished()" v-bind:class="[isFinished===1 ? 'isFinished' : 'isUnfinished' ]">已完成</div>
            <div class="unfinished" id="unfinished" @click="showUnfinished()" v-bind:class="[isFinished===1 ? 'isUnfinished' : 'isFinished' ]">未完成</div>
        </div>
    </div>
    <div class="orderList" v-infinite-scroll="loadMoreOrder" infinite-scroll-disabled="busy" infinite-scroll-distance="10">
        <orderList :orderList="orderList" ></orderList>
    </div>
    <div class="info" v-show="orderList.length<1&&isFinish===1">
    目前没有已完成的订单
    </div>
    <div class="info" v-show="orderList.length<1&&isFinish===0">
    目前没有未完成的订单
    </div>
</div>
</template>
<script>
import { mapState } from 'vuex';
import header2fix from '@/components/header/header2fix.vue';
import orderList from '@/components/order/orderList.vue';
export default{
    data() {
        return {
            orderList: [],  // 订单列表
            busy: true,  // 滚动加载判断用
            page: 1,
            isFinish: 1,  // 订单的状态 （已完成：1  未完成：0）
            isFinished: 1, // 该参数只用于颜色判断
            count: 0
        };
    },
    methods: {
        loadMoreOrder() {  // 滚动加载
            this.busy = true;
            console.log('当前页数' + this.page);
            let that = this;
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"getUserOrderList","params":{"userCode":"' + this.$store.state.userCode + '","isFinish":' + this.isFinish + ',"page":' + this.page + '}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                that.orderList = [...that.orderList, ...result.data.result.orderList];
                if (result.data.result.count !== 0) {
                    that.busy = false;
                    that.page++;
                } else {
                    that.busy = true;  // 如果收到的结果为0  就禁用滚动加载
                }
            });
        },
        showFinished() {
            if (this.isFinish === 1) {
            } else {
                // 切换样式
                this.isFinished = 1;
                this.isFinish = 1;
                this.page = 1;
                this.show();
            }
        },
        showUnfinished() {
            // console.log('未完成的');
            if (this.isFinish === 0) {
            } else {
                // 切换样式
                this.isFinished = 0;
                this.isFinish = 0;
                this.page = 1;
                this.show();
            }
        },
        show() {
            this.busy = true;
            let that = this;
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"getUserOrderList","params":{"userCode":"' + this.$store.state.userCode + '","isFinish":' + this.isFinish + ',"page":' + this.page + '}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                that.orderList = result.data.result.orderList;
                that.busy = false;
                that.page = 2;
            });
        }
    },
    created() { // 页面创建完成后 加载用户的订单列表
        // let that = this;
        // $.ajax({
        //     url: this.$store.state.global.hftcomClient,
        //     type: 'POST',
        //     data: '{"id":19,"jsonrpc":"2.0","method":"getUserOrderList","params":{"userCode":"' + this.$store.state.userCode + '","isFinish":1,"page":1}}',
        //     contentType: 'application/json',
        //     dataType: 'json',
        //     success: function(data) {
        //         that.orderList = data.result.orderList;
        //         that.busy = false;
        //         that.page = 2;
        //     }
        // });
        this.show();
    },
    computed: {
        ...mapState(['localCity', 'global'])
    },
    components: {
        orderList,
        header2fix
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
#order
    .change
        position fixed 
        top 1.24rem
        left 0
        z-index 1
        width 100%
        height :1.5rem
        border-bottom 0.1rem solid #f3f3f3
        background-color #fff
        margin:0 auto
        .change0
            position absolute
            top 0.24rem
            left 25%
            height :1rem
            width :5rem
            .finished
                float left
                width 49%
                text-align center
                line-height 1rem
                border solid #f64f48 1px
                border-right none
                background-color #f64f48
                color #f3f3f3
                font-size 0.35rem
                border-radius 5px 0px 0px 5px
            .unfinished
                float right
                width 49.5%
                text-align center
                line-height 1rem
                color #f64f48
                font-size 0.35rem
                border solid #f64f48 1px
                border-left none
                border-radius 0px 5px 5px 0px
    .orderList 
        position absolute
        top 2.89rem
        left 0
        width 100%
.info
    position absolute
    top 3rem
    left 0
    width 96%
    text-align center
    height 1.5rem
    margin-left 2%
    line-height 1.5rem
    border solid 1px #CDCDC1
    font-size 0.45rem
    color #CDCDC1
    z-index 3
.isFinished
    background-color #f64f48!important
    color #fff!important
.isUnfinished
    background-color #fff!important
    color #f64f48!important
</style>