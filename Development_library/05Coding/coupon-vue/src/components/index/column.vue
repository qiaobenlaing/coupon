<template>
	<div class="column" :style="{'height': getColumnHeight}">
        <div class="text" v-show="shopTypeList.length===0">本城市暂无栏目信息</div>
		<div v-show="shopTypeList.length!==0" v-for="item,index in shopTypeList" :key = "item.sign" class="column-item" :class="[index<4?'first-line-column-item':'', index>7?'more-line-column-item': '']" v-bind:name="item.typeZh" @click="goSearch(item)">
			<div class="columnImg">
				<img :src="global.hftcomImgSrc + item.shopTypeImg" alt="" >
			</div>
			<div class="columnText">
			{{item.typeZh}}
			</div>
		</div>
        <div v-show="shopTypeList.length > 8" class="more-line-button" @click="loadMoreLine()">更多内容</div>
	</div>
</template>
<script>
import { mapState } from 'vuex';
export default{
    props: ['shopTypeList'],
    data() {
        return {
            flag: true
        };
    },
    methods: {
        goSearch(item) {
            this.$router.push({path: '/shops',
                query: {
                    'searchTypeName': item.typeZh, // 首页选择相关行业时 的行业类型 默认为0  全部类型
                    'searchTypeValue': item.typeValue // 首页选择相关行业时 的行业名称   用来传递
                }});  // 跳转到商户搜索页
        },
        loadMoreLine() {
            var moreLineButton = document.querySelector('.more-line-button');
            var moreLine = document.querySelectorAll('.more-line-column-item');
            var columnDiv = document.querySelector('.column');
            if (this.flag) {
                moreLineButton.innerHTML = '收起';
                columnDiv.style.height = (Math.ceil((this.shopTypeList.length / 4)) * 2.1 + 1.2) + 'rem';
                for (let i = 0; i < moreLine.length; i++) {
                    moreLine[i].style.display = 'block';
                }
                this.flag = false;
            } else {
                moreLineButton.innerHTML = '更多内容';
                columnDiv.style.height = 5.4 + 'rem';
                for (let i = 0; i < moreLine.length; i++) {
                    moreLine[i].style.display = 'none';
                }
                this.flag = true;
            }
        }
    },
    computed: {
        ...mapState([
            'global'
        ]),
        getColumnHeight() {
            if (Math.ceil((this.shopTypeList.length / 4)) > 2) {
                return 5.4 + 'rem';
            } else if (Math.ceil((this.shopTypeList.length / 4)) === 2) {
                return 4.2 + 'rem';
            } else {
                return 2.1 + 'rem';
            }
            // return (Math.ceil((this.shopTypeList.length / 4)) * 2.1) + 'rem';
        }
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">
.column
    width:10.8rem
    background:#fff
    .text
        width:10.8rem
        text-align:center
        line-height:2.1rem   
    .column-item
        flex:1
        height:2.1rem
        width: 25%
        float: left
        text-align:center
        .columnImg
            height:50%
            width:100%
            img
                height:100%
        .columnText
            line-height:0.75rem
            font-size:0.42rem
            color:#000

    .first-line-column-item
        margin-top: 0.2rem

    .more-line-column-item
        display: none

    .more-line-button
        height: 1rem
        float: left
        width: 10.8rem
        text-align: center
        font-size: 0.42rem
        line-height: 1rem
        border-top: 1px solid #ebedf0
</style>
