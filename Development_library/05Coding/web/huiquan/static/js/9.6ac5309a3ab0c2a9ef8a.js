webpackJsonp([9],{240:function(t,s,e){function i(t){e(347)}var o=e(0)(e(335),e(374),i,"data-v-12ed308f",null);t.exports=o.exports},305:function(t,s,e){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var i=e(13),o=e.n(i),a=e(6);s.default={data:function(){return{isCollect:!1,isCollecting:!1}},computed:o()({},e.i(a.b)(["global","collectShopList","isLogin","zoneId"]),{isEmpty:function(){return this.collectShopList.length},computedShopName:function(){return this.methodGetByteLen(this.eachShopList.shopName,22)},computedStreet:function(){return this.methodGetByteLen(this.eachShopList.street,26)}}),props:["eachShopList"],methods:{toShopDetail:function(t){this.$router.push({path:"/shopDetail/"+t})},collectShop:function(t){var s=this;this.isLogin?(this.isCollecting=!0,this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"showCollect","params":{"shopCode":"'+t+'","userCode":"'+this.$store.state.userCode+'","zoneId":"'+this.zoneId+'"}}',contentType:"application/json",dataType:"json"}).then(function(t){s.isCollecting=!1,1===t.data.result?(s.isCollect=!0,s.$vux.toast.text("已收藏","bottom")):0===t.data.result?(s.isCollect=!1,s.$vux.toast.text("已取消收藏","bottom")):s.$vux.toast.text("数据错误","bottom")})):(this.$store.state.isBLR=1,this.$store.state.showLoginBox=!0,this.$vux.toast.text("请登陆","bottom"))},filterIsCollect:function(){var t=this;this.collectShopList.filter(function(s,e,i){t.eachShopList.shopCode===s.shopCode&&(t.isCollect=!0)})},methodGetByteLen:function(t,s){if(t.replace(/[^\x00-\xff]/g,"01").length<=s)return t;for(var e=Math.floor(s/2);e<t.length;e++)if(t.substr(0,e).replace(/[^\x00-\xff]/g,"01").length>=s)return t.substr(0,2*Math.floor(e/2))+"..."}},created:function(){this.filterIsCollect()}}},306:function(t,s){},308:function(t,s){t.exports={render:function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{staticClass:"background"},[e("div",{staticClass:"shoplist-item"},[e("div",{staticClass:"shopDetail",on:{click:function(s){t.toShopDetail(t.eachShopList.shopCode)}}},[e("div",{staticClass:"shop-logo"},[e("img",{directives:[{name:"lazy",rawName:"v-lazy",value:t.global.hftcomImgSrc+t.eachShopList.logoUrl,expression:"global.hftcomImgSrc+eachShopList.logoUrl"}]})]),t._v(" "),e("div",{staticClass:"shop-name"},[t._v("\n            "+t._s(t.computedShopName)+"\n        ")]),t._v(" "),e("div",{staticClass:"shop-street"},[e("span",{staticClass:"street"},[t._v(t._s(t.computedStreet))]),t._v(" "),t.eachShopList.distance<100?e("span",{staticClass:"distance"},[t._v("<100m")]):t._e(),t._v(" "),100<t.eachShopList.distance&&t.eachShopList.distance<500?e("span",{staticClass:"distance"},[t._v(">100m")]):t._e(),t._v(" "),t.eachShopList.distance>500?e("span",{staticClass:"distance"},[t._v(">500m")]):t._e()])]),t._v(" "),e("div",{staticClass:"collect"},[e("span",{directives:[{name:"show",rawName:"v-show",value:!1===t.isCollect&&!1===t.isCollecting,expression:"isCollect ===false && isCollecting === false"}],staticClass:"icon-collect",on:{click:function(s){t.collectShop(t.eachShopList.shopCode)}}}),t._v(" "),e("transition",{attrs:{name:"rotate"}},[e("span",{directives:[{name:"show",rawName:"v-show",value:t.isCollecting,expression:"isCollecting"}],staticClass:"icon-spinner8"})]),t._v(" "),e("span",{directives:[{name:"show",rawName:"v-show",value:!0===t.isCollect&&!1===t.isCollecting,expression:"isCollect === true && isCollecting === false"}],staticClass:"icon-collect-solid theme-red",on:{click:function(s){t.collectShop(t.eachShopList.shopCode)}}})],1)])])},staticRenderFns:[]}},311:function(t,s,e){function i(t){e(306)}var o=e(0)(e(305),e(308),i,null,null);t.exports=o.exports},335:function(t,s,e){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var i=e(34),o=e.n(i),a=e(13),h=e.n(a),n=e(311),c=e.n(n),l=e(118),r=e.n(l),u=e(115),d=e.n(u),p=e(6);s.default={name:"shops",data:function(){return{searchShopList:[],busy:!0,currentPage:1,nextPage:1,search:"",circle:{zh:"商圈",list:[]},searchCircleName:"商圈",moduleValue:0,searchContent:41,typeList:{zh:"行业",list:[]},searchTypeName:void 0===this.$route.query.searchTypeName?"行业":this.$route.query.searchTypeName,searchShopType:void 0===this.$route.query.searchTypeValue?0:this.$route.query.searchTypeValue,intelligentSorting:{zh:"智能排序",list:[]},searchIntelligentName:"智能排序",searchIntelligentSorting:0,filter:{zh:"筛选",list:[]},serachFilter:0,subList:[],queryName:"",show1:!0,show2:!0,oneList:[],twoList:[],maskShow:!1,searchValue:"",searchValue2:"",iconShow1:!1,iconShow2:!1,iconShow3:!1,showLoadGif:!0,nShowNotHide:!1}},computed:h()({},e.i(p.b)(["localCity","userCode","latitude","longitude","global","collectShopList","zoneId"])),watch:{search:function(t,s){this.nextPage=1},localCity:function(t,s){this.getSearchWords()}},methods:h()({},e.i(p.d)(["getCollectShop"]),{goBack:function(){this.$router.go(-1)},show:function(){var t=this;this.loading(),this.axios({method:"post",url:this.global.hftcomClient,data:'{"id":19,"jsonrpc":"2.0","method":"searchShop","params":{"searchWord":"'+this.search+'","type":'+this.searchShopType+',"userCode":"'+this.userCode+'","longitude":'+this.longitude+',"latitude":'+this.latitude+',"page":'+this.nextPage+',"city":"'+this.localCity+'","moduleValue":'+this.moduleValue+',"content":'+this.searchContent+',"order":'+this.searchIntelligentSorting+',"filter":'+this.serachFilter+',"queryName":"'+this.queryName+'","zoneId":"'+this.zoneId+'"}}',contentType:"application/json",dataType:"json"}).then(function(s){1===t.nextPage?t.searchShopList=s.data.result.shopList:t.searchShopList=[].concat(o()(t.searchShopList),o()(s.data.result.shopList)),t.currentPage=s.data.result.page,t.nextPage=s.data.result.nextPage,t.busy=!1,t.loaded()}),this.iconBack()},searchShopLoadMore:function(){this.busy=!0,this.nextPage>this.currentPage?this.show():(this.busy=!0,this.loaded())},getSearchWords:function(){var t=this;this.axios({method:"post",url:this.global.hftcomClient,data:'{"id":19,"jsonrpc":"2.0","method":"listSearchWords","params":{"city":"'+this.localCity+'","zoneId":"'+this.zoneId+'"}}',contentType:"application/json",dataType:"json"}).then(function(s){t.circle=s.data.result.circle,t.typeList=s.data.result.type,t.intelligentSorting=s.data.result.intelligentSorting,t.filter=s.data.result.filter})},iconBack:function(){this.iconShow1=!1,this.iconShow2=!1,this.iconShow3=!1},hiddenMask:function(){this.nShowNotHide=!1,this.show1=!0,this.maskShow=!1,this.iconBack()},subListIsNull:function(){for(var t=0;t<this.circle.list.length;t++){var s=this.circle.list[t];s.subListIsNull=!1,3===s.moduleValue|-1===s.moduleValue&&s.subList.length<1&&(s.subListIsNull=!0)}},showCircle:function(){this.twoList=[],this.searchType=this.circle.zh,this.nShowNotHide=!0,this.show1?(this.nShowNotHide=!0,this.show1=!1,this.maskShow=!0,this.iconShow1=!0,this.subListIsNull(),this.oneList=this.circle.list):(this.nShowNotHide=!1,this.show1=!0,this.maskShow=!1,this.iconBack())},showType:function(){this.twoList=[],this.searchType=this.typeList.zh,this.show1?(this.nShowNotHide=!0,this.show1=!1,this.maskShow=!0,this.iconShow2=!0):(this.nShowNotHide=!1,this.show1=!0,this.maskShow=!1,this.iconBack()),this.oneList=this.typeList.list},showIntelligentSorting:function(){this.twoList=[],this.searchType=this.intelligentSorting.zh,this.nShowNotHide=!0,this.show1?(this.nShowNotHide=!0,this.show1=!1,this.maskShow=!0,this.iconShow3=!0):(this.nShowNotHide=!1,this.show1=!0,this.maskShow=!1,this.iconBack()),this.oneList=this.intelligentSorting.list},showFilter:function(){this.twoList=[],this.searchType=this.filter.zh,this.nShowNotHide=!0,this.oneList=this.filter.list},showValue1:function(t){this.nextPage=1,this.searchValue=t.queryName,this.searchType===this.typeList.zh&&(this.searchTypeName=t.queryName,this.searchShopType=t.value,this.nShowNotHide=!1,this.show1=!0,this.maskShow=!1,console.log(2222),this.show()),this.searchType===this.intelligentSorting.zh&&(this.searchIntelligentSorting=t.value,this.searchIntelligentName=t.queryName,this.searchIntelligentSorting=t.value,this.nShowNotHide=!1,this.show1=!0,this.maskShow=!1,this.show()),this.searchType===this.filter.zh&&(this.serachFilter=t.value),this.searchType===this.circle.zh&&(this.moduleValue=t.moduleValue,-1===this.moduleValue&&(this.twoList=this.circle.list[1].subList),3===this.moduleValue&&(this.twoList=this.circle.list[2].subList),-2===this.moduleValue&&(this.twoList=[],this.queryName=t.queryName,this.searchCircleName=this.queryName,this.nShowNotHide=!1,this.show1=!0,this.maskShow=!1,this.show()),0===this.moduleValue&&(this.queryName="",this.twoList=[],this.searchCircleName=t.queryName,this.nShowNotHide=!1,this.show1=!0,this.maskShow=!1,this.show()))},showValue2:function(t){this.queryName="",this.searchContent=t.value,this.searchCircleName=t.name,this.searchValue2=t.name,this.nShowNotHide=!1,this.show1=!0,this.maskShow=!1,console.log(1111),this.show()},loaded:function(){this.nextPage<=this.currentPage&&(this.showLoadGif=!1)},loading:function(){this.showLoadGif=!0}}),beforeCreate:function(){},created:function(){console.log(this.searchTypeName),console.log(this.searchShopType),this.show(),this.getSearchWords(),"6"===this.searchShopType&&(this.searchTypeName="行业",this.searchShopType=0)},mounted:function(){this.getCollectShop()},beforeDestroy:function(){},components:{shoplist:c.a,"v-nav":d.a,loadGif:r.a}}},347:function(t,s){},374:function(t,s){t.exports={render:function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{attrs:{id:"shopSearch"}},[e("header",{staticClass:"header"},[e("span",{staticClass:"header-item "}),t._v(" "),e("span",{staticClass:"header-item "},[e("input",{directives:[{name:"model",rawName:"v-model",value:t.search,expression:"search"}],ref:"input",staticClass:"headInput",attrs:{type:"text",name:"",id:"",placeholder:"搜索商家"},domProps:{value:t.search},on:{input:function(s){s.target.composing||(t.search=s.target.value)}}})]),t._v(" "),e("span",{staticClass:"header-item ",on:{click:t.show}},[t._v("搜索")])]),t._v(" "),e("div",{attrs:{id:"newSearch"}},[e("div",{staticClass:"nCircle ",class:[1==t.iconShow1?"activeCity":""],on:{click:t.showCircle}},[t._v(" \n            "+t._s(t.searchCircleName)),e("span",{staticClass:"icon-ctrl icon1",class:[1==t.iconShow1?"iconTurn":""]})]),t._v(" "),e("div",{staticClass:"nType ",class:[1==t.iconShow2?"activeCity":""],attrs:{id:"nType"},on:{click:t.showType}},[t._v(" \n            "+t._s(t.searchTypeName)),e("span",{staticClass:"icon-ctrl icon2",class:[1==t.iconShow2?"iconTurn":""]})]),t._v(" "),e("div",{staticClass:"nIntelligentSorting ",class:[1==t.iconShow3?"activeCity":""],on:{click:t.showIntelligentSorting}},[t._v(" \n            "+t._s(t.searchIntelligentName)),e("span",{staticClass:"icon-ctrl icon3",class:[1==t.iconShow3?"iconTurn":""]})])]),t._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:t.maskShow,expression:"maskShow"}],staticClass:"mask",on:{click:function(s){t.hiddenMask()}}}),t._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:t.nShowNotHide,expression:"nShowNotHide"}],attrs:{id:"nShow"}},[t._l(t.oneList,function(s){return e("div",{key:s.queryName,staticClass:"fShow",class:[s.queryName===t.searchValue?"activeCity":"",!0===s.subListIsNull?"subListIsNull":""],attrs:{value:s.moduleValue,id:s.queryName},on:{click:function(e){t.showValue1(s)}}},[e("span",{staticClass:"fShow1"},[t._v(t._s(s.queryName))]),t._v(" "),e("span",{directives:[{name:"show",rawName:"v-show",value:-1==s.moduleValue||3==s.moduleValue,expression:"one.moduleValue==-1||one.moduleValue==3"}],staticClass:"iconf icon-back"})])}),t._v(" "),e("div",{staticClass:"sShow"},t._l(t.twoList,function(s){return e("div",{key:s.name,staticClass:"tShow",class:[s.name===t.searchValue2?"activeCity":""],on:{click:function(e){t.showValue2(s)}}},[t._v("\n            "+t._s(s.name)+"\n            ")])}))],2),t._v(" "),e("div",{directives:[{name:"infinite-scroll",rawName:"v-infinite-scroll",value:t.searchShopLoadMore,expression:"searchShopLoadMore"}],staticClass:"shoplist",attrs:{"infinite-scroll-disabled":"busy","infinite-scroll-distance":"10"}},[t._l(t.searchShopList,function(t){return e("shoplist",{key:t.shopCode,attrs:{eachShopList:t}})}),t._v(" "),e("loadGif",{directives:[{name:"show",rawName:"v-show",value:t.showLoadGif,expression:"showLoadGif"}]})],2),t._v(" "),e("v-nav",{staticClass:"nav"})],1)},staticRenderFns:[]}}});
//# sourceMappingURL=9.6ac5309a3ab0c2a9ef8a.js.map