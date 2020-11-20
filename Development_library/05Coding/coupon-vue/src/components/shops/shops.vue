<template>
    <div id="shopSearch" >
        <header class="header">
            <span class="header-item " ></span>
            <span class="header-item "><input  ref="input"  type="text" name="" id="" class="headInput" placeholder="搜索商家" v-model="search"></span>
            <span class="header-item "  @click="show">搜索</span>
        </header>
        <div id="newSearch">
            <!-- 重新开发下拉框 -->
            <div class="nCircle " @click="showCircle" v-bind:class="[iconShow1==true ? 'activeCity':'']"> 
                {{searchCircleName}}<span class="icon-ctrl icon1" v-bind:class="[iconShow1==true ? 'iconTurn':'']"></span>
            </div>
            <div class="nType " id="nType" @click="showType" v-bind:class="[iconShow2==true ? 'activeCity':'']"> 
                {{searchTypeName}}<span class="icon-ctrl icon2" v-bind:class="[iconShow2==true ? 'iconTurn':'']"></span>
            </div>
            <div class="nIntelligentSorting " @click="showIntelligentSorting" v-bind:class="[iconShow3==true ? 'activeCity':'']"> 
                {{searchIntelligentName}}<span class="icon-ctrl icon3" v-bind:class="[iconShow3==true ? 'iconTurn':'']"></span>
            </div>
        </div>
        <div class="mask" v-show="maskShow" @click="hiddenMask()">
        </div>
        <div id="nShow" v-show="nShowNotHide">
            <!-- 用来展示的下拉框 -->
            <div class="fShow"   v-for="one in oneList" :key="one.queryName"  v-bind:value="one.moduleValue" v-bind:class="[one.queryName===searchValue ? 'activeCity' : '',one.subListIsNull===true ? 'subListIsNull':'' ]" v-bind:id='one.queryName'  @click="showValue1(one)">
                <span class="fShow1">{{one.queryName}}</span>
                <span class="iconf icon-back" v-show="one.moduleValue==-1||one.moduleValue==3"></span>
            </div>
            <div class="sShow" >
                <div class="tShow"  v-for="two in twoList" :key="two.name"   v-bind:class="[two.name===searchValue2 ? 'activeCity' : '']"  @click="showValue2(two)">
                {{two.name}}
                <!-- 二级下拉框 -->
                </div>
            </div>
        </div>
        <div class="shoplist" v-infinite-scroll="searchShopLoadMore" infinite-scroll-disabled="busy" infinite-scroll-distance="10">
            <shoplist  v-for="(index) in searchShopList" :key = "index.shopCode"
      :eachShopList='index' ></shoplist>
        <loadGif v-show="showLoadGif"></loadGif>
        </div>
        <v-nav class="nav"></v-nav>
    </div>
</template>
<script>
import shoplist from '../../components/shoplist/shoplist.vue';
import loadGif from '@/components/loadGif/loadGif.vue';
import nav from '@/components/nav/nav.vue';
import { mapState, mapActions } from 'vuex';
export default {
    name: 'shops',
    data() {
        return {
            searchShopList: [], // 商户列表
            busy: true, // 无限加载禁用标识
            currentPage: 1, // 当前页
            nextPage: 1,
            search: '', // 输入框插询的字段
            circle: {'zh': '商圈', 'list': []}, // 筛选框的区域列表
            searchCircleName: '商圈',  // 商圈名称
            moduleValue: 0, // 当前模块，对应circle的类型选择 0-未定义；-2-区；-1-距离；3-商圈； 4-品牌；
            searchContent: 41, // 当前内容值，对应circle相应类型选择的值内容为：区域ID；距离，单位：米；商圈ID；品牌ID；

            typeList: {'zh': '行业', 'list': []}, // 搜索行业的列表
            searchTypeName: this.$route.query.searchTypeName === undefined ? '行业' : this.$route.query.searchTypeName, // 当前行业名称，对应typeList
            searchShopType: this.$route.query.searchTypeValue === undefined ? 0 : this.$route.query.searchTypeValue, // 商户类型值1-美食；2-丽人；3-健身；4-娱乐；5-服装；6-其他；

            intelligentSorting: {'zh': '智能排序', 'list': []}, // 智能排序列表
            searchIntelligentName: '智能排序',
            searchIntelligentSorting: 0, // 智能排序的值，对应intelligentSorting默认值为0。1：离我最近；2人气最高

            filter: {'zh': '筛选', 'list': []}, // 筛选的列表（取消该功能）
            serachFilter: 0, // 筛选的值默认值为0；3为工行折扣， 4为优惠活动， 5为商户活动 （取消该功能）

            subList: [], // 二级选择框使用的列表 如：距离  商圈等等
            queryName: '', // 在选择区县后  再输入的二级内容 地区名称
            show1: true, // 判断用显示一级下拉框
            show2: true, // 判断用显示二级下拉框
            oneList: [], // 一级下拉框用
            twoList: [], // 二级下拉框使用
            maskShow: false,  // 判断 遮罩层 用
            searchValue: '', // 判断用
            searchValue2: '', // 判断用
            iconShow1: false,
            iconShow2: false,
            iconShow3: false,
            showLoadGif: true, // 控制加载动图的显示
            nShowNotHide: false // 判断下拉框出现
        };
    },
    computed: {
        ...mapState([
            'localCity', 'userCode', 'latitude', 'longitude', 'global', 'collectShopList', 'zoneId'
        ])
    },
    watch: {
        // 监听search的变化
        search: function(newval, oldval) {
            this.nextPage = 1; // 发现search变化后 将page变为1
        },
        localCity: function(newval, oldval) {
            // 城市发生变化后  重新加载筛选条件
            this.getSearchWords();
        }
    },
    methods: {
        ...mapActions(['getCollectShop']),
        goBack () {
            this.$router.go(-1);
        },
        show () { // 请求商户列表
            this.loading();
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"searchShop","params":{"searchWord":"' + this.search + '","type":' + this.searchShopType + ',"userCode":"' + this.userCode + '","longitude":' + this.longitude + ',"latitude":' + this.latitude + ',"page":' + this.nextPage + ',"city":"' + this.localCity + '","moduleValue":' + this.moduleValue + ',"content":' + this.searchContent + ',"order":' + this.searchIntelligentSorting + ',"filter":' + this.serachFilter + ',"queryName":"' + this.queryName + '","zoneId":"' + this.zoneId + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (this.nextPage === 1) {
                    this.searchShopList = result.data.result.shopList;
                } else {
                    this.searchShopList = [...this.searchShopList, ...result.data.result.shopList];
                };
                this.currentPage = result.data.result.page;
                this.nextPage = result.data.result.nextPage;
                this.busy = false;
                this.loaded();
            });
            this.iconBack();
        },
        searchShopLoadMore () {
            this.busy = true;
            if (this.nextPage > this.currentPage) {
                this.show();
            } else {
                this.busy = true;
                this.loaded();
            }
        },
        getSearchWords: function() { // 获取查询子列表
            this.axios({
                method: 'post',
                url: this.global.hftcomClient,
                data: '{"id":19,"jsonrpc":"2.0","method":"listSearchWords","params":{"city":"' + this.localCity + '","zoneId":"' + this.zoneId + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                this.circle = result.data.result.circle;
                this.typeList = result.data.result.type;
                this.intelligentSorting = result.data.result.intelligentSorting;
                this.filter = result.data.result.filter;
                // this.showCircle();
            });
        },
        iconBack() {
            this.iconShow1 = false;
            this.iconShow2 = false;
            this.iconShow3 = false;
        },
        hiddenMask() {
            this.nShowNotHide = false;
            this.show1 = true;
            this.maskShow = false;
            this.iconBack();
        },
        subListIsNull() { // 判断subList是否为空
            for (let i = 0; i < this.circle.list.length; i++) {
                const el = this.circle.list[i];
                el.subListIsNull = false; // 给每一个对象添加了一个新属性来判断sublist是否为空
                if (el.moduleValue === 3 | el.moduleValue === -1) {
                    if (el.subList.length < 1) {
                        el.subListIsNull = true;
                    }
                }
            }
        },
        showCircle() {
            this.twoList = [];
            this.searchType = this.circle.zh;
            this.nShowNotHide = true;
            if (this.show1) {
                this.nShowNotHide = true;
                this.show1 = false;
                this.maskShow = true;
                this.iconShow1 = true;
                this.subListIsNull();// 判断是否有的sublist为空
                this.oneList = this.circle.list;
            } else {
                this.nShowNotHide = false;
                this.show1 = true;
                this.maskShow = false;
                this.iconBack();
            };
        },
        showType() {
            this.twoList = [];
            this.searchType = this.typeList.zh; // 当前选择的参数为行业

            if (this.show1) {
                this.nShowNotHide = true;
                this.show1 = false;
                this.maskShow = true;
                this.iconShow2 = true;
            } else {
                this.nShowNotHide = false;
                this.show1 = true;
                this.maskShow = false;
                this.iconBack();
            };
            this.oneList = this.typeList.list;
        },
        showIntelligentSorting() {
            this.twoList = [];
            this.searchType = this.intelligentSorting.zh;
            this.nShowNotHide = true;
            if (this.show1) {
                this.nShowNotHide = true;
                this.show1 = false;
                this.maskShow = true;
                this.iconShow3 = true;
            } else {
                this.nShowNotHide = false;
                this.show1 = true;
                this.maskShow = false;
                this.iconBack();
            };
            this.oneList = this.intelligentSorting.list;
        },
        showFilter() {
            this.twoList = [];
            this.searchType = this.filter.zh;
            this.nShowNotHide = true;
            this.oneList = this.filter.list;
        },
        showValue1(one) {
            // 点击后将page设为1
            this.nextPage = 1;
            // 点击一级下拉框事件
            this.searchValue = one.queryName; // 该参数只是用来判断
            if (this.searchType === this.typeList.zh) { // 行业
                // console.log(this.searchShopType);
                this.searchTypeName = one.queryName;
                this.searchShopType = one.value;
                this.nShowNotHide = false; // 收起下拉框
                this.show1 = true;
                this.maskShow = false;
                console.log(2222);
                this.show();
            };
            if (this.searchType === this.intelligentSorting.zh) { // 智能排序
                this.searchIntelligentSorting = one.value;
                // console.log(this.searchIntelligentSorting);
                this.searchIntelligentName = one.queryName;
                this.searchIntelligentSorting = one.value;
                this.nShowNotHide = false; // 收起下拉框
                this.show1 = true;
                this.maskShow = false;
                this.show();
            };
            if (this.searchType === this.filter.zh) { // 筛选
                this.serachFilter = one.value;
            };
            // 如果为circle 可能会出现二级下拉框
            if (this.searchType === this.circle.zh) { // 商圈
                this.moduleValue = one.moduleValue;
                // 当modelValue 为-1 时 选择的为附近  进入附近二级下拉框
                if (this.moduleValue === -1) {
                    this.twoList = this.circle.list[1].subList;
                };
                // 当modelValue 为3 时 选择的为商圈  进入商圈二级下拉框
                if (this.moduleValue === 3) {
                    this.twoList = this.circle.list[2].subList;
                };
                if (this.moduleValue === -2) { // 其他  将集合置为空
                    this.twoList = [];
                    this.queryName = one.queryName; // 设置queryname
                    this.searchCircleName = this.queryName;
                    this.nShowNotHide = false; // 收起下拉框
                    this.show1 = true;
                    this.maskShow = false;
                    this.show();
                };
                if (this.moduleValue === 0) {  // 选择为全部时
                    this.queryName = ''; // 将选中其他区县的参数置为空
                    this.twoList = [];
                    this.searchCircleName = one.queryName;
                    this.nShowNotHide = false; // 收起下拉框
                    this.show1 = true;
                    this.maskShow = false;
                    this.show();
                };
            }
        },
        showValue2(two) {
            this.queryName = ''; // 将选中其他区县的参数置为空
            this.searchContent = two.value;
            this.searchCircleName = two.name;
            this.searchValue2 = two.name;
            this.nShowNotHide = false; // 收起下拉框
            this.show1 = true;
            this.maskShow = false;
            console.log(1111);
            this.show();
        },

        loaded() { // 页面正在加载中的时候所有状态改变
            if (this.nextPage <= this.currentPage) {
                this.showLoadGif = false;
            }
        },
        loading() { // 页面加载完成后的所有状态改变
            this.showLoadGif = true;
        }
    },
    beforeCreate() {   // 创建之前
    },
    created() {
        console.log(this.searchTypeName);
        console.log(this.searchShopType);
        this.show();
        this.getSearchWords();
        if (this.searchShopType === '6') {
            this.searchTypeName = '行业';
            this.searchShopType = 0;
        }
    },
    mounted() {
        this.getCollectShop();
    },
    beforeDestroy() {
    },
    components: {
        shoplist,
        'v-nav': nav,
        loadGif
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus"  scoped>
@import "../../common/stylus/mixin.styl"
#shopSearch
  background:#f3f3f3
  position:absolute
  width:100%
  height:100%
  .header
    position fixed
    height:1.24rem
    line-height:1.24rem
    width 10.8rem
    background:#fff;
    color:#fff;
    top 0
    left 0
    z-index 2    /* 给header做了固定 */
    .header-item
        height:1.24rem
        line-height:1.24rem
        text-align:center
        vertical-align:middle
    :nth-child(1)
        display inline-block
        width 1.55rem
        height:1.24rem
        line-height:1.24rem
        font-size:0.7rem
        font-weight:bold
    :nth-child(2)
        display inline-block
        position absolute
        top 0
        right 1.8rem
        width 7.5rem
        font-size:0.4rem
        .headInput
            background-color : #dfdede
            vertical-align:middle;
            text-align center    /* 文字居中 */
            height : 0.8rem;
            line-height 0.8rem
            font-size:0.4rem
            color:#525252
            width :100%
            border-radius : 1rem
            font-weight:normal
    :nth-child(3)
            position relative
            top 0.07rem
            float right 
            display inline-block
            width 1.43rem
            height:1.24rem
            line-height:1.24rem
            font-size:0.43rem
            color:#525252	
            vertical-align:middle
            margin-right :0.25rem
  .mask
    position:fixed
    top:2.38rem
    left:0
    width:100%
    height:100%
    background:rgba(91, 91, 91, 0.55)
    z-index:1
  #newSearch 	
    height :1rem
    font-size :0.45rem
    text-align :center
    vertical-align: middle
    width  10.8rem
    background-color :#fff
    color:#525252
    position fixed
    top 1.1rem
    left 0
    z-index 1
    border-top 0.2rem solid #f3f3f3
    .nCircle
        position relative
        display inline
        text-align :center
        vertical-align: middle
        line-height 1rem
        width 3.5rem
        float left
        .icon1
            position absolute
            top 0.01rem
            right 0.75rem
            z-index 3
            font-size 0.49rem
            height 1rem
            line-height 1rem
            margin-top 0.1rem
            transform rotate(180deg)
            margin-top -0.15rem
    .nType
        position relative
        display inline
        line-height 1rem
        text-align :center
        vertical-align: middle
        width 3.5rem
        float left
        .icon2
            position absolute
            top 0.01rem
            right 0.75rem
            z-index 3
            font-size 0.49rem
            height 1rem
            line-height 1rem
            margin-top 0.1rem
            transform rotate(180deg)
            margin-top -0.15rem
    .nIntelligentSorting
        position relative
        display inline
        line-height 1rem
        text-align :center
        vertical-align: middle
        width 3.6rem
        float left
        .icon3
            position absolute
            top 0.01rem
            right 0.34rem
            z-index 3
            font-size 0.49rem
            height 1rem
            line-height 1rem
            margin-top 0.1rem
            transform rotate(180deg)
            margin-top -0.15rem
    .nFilter
        display inline
        margin-top :8px
        text-align :center
        vertical-align: middle
  #nShow
    position: fixed
    top 2.28rem
    z-index 2
    width: 100%
    background-color :#fff
    font-size :0.4rem
    color:#525252
    height:8rem
    overflow-y:scroll
    .fShow
        height :1rem
        line-height 1rem
        width :50%
        text-align :center
        vertical-align: middle
        background-color #fff
        .img1
            height 0.4rem
            width 0.4rem
            margin-right 0.3rem
            line-height 0.4rem
            position relative 
            vertical-align: middle
            left -1rem
            top -0.05rem
        .fShow1
            position absolute
            left 1.5rem
            text-align :center
            display inline-block
            width 2rem
    .sShow
        display : block;
        height :1rem
        line-height 1rem
        width :50%
        position: absolute 
        top:0
        right:0
        .tShow
            height :1rem
            width :100%
            text-align :center
            vertical-align: middle
            background-color #fff
#searchBtn
    height : 40px
    width :100px
    background-color :#f64f48
    border: 2px solid #e7e7e7
    border-radius:25px
    margin: auto
    font-size: 16px
    color: white;
.shoplist
    margin-top: 2.32rem;
    margin-bottom 1.52rem
.activeCity
    color: #ff9c00
.nav
    z-index 2
.iconTurn
    display inline-block
    transform rotate(0deg)!important
    margin-top 0.15rem!important
.iconf
    position:relative
    float right
    right 0.5rem
    font-size 0.4rem
    transform scale(-1,1)
    height 1rem
    line-height 1rem
    z-index 3
.subListIsNull
    color #D9D9D9!important

</style>
