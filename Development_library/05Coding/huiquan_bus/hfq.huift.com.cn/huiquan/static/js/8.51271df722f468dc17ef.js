webpackJsonp([8],{234:function(t,e,s){function o(t){s(389)}var i=s(0)(s(341),s(427),o,null,null);t.exports=i.exports},271:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={props:["title"],methods:{goBack:function(){this.$router.go(-1)}}}},272:function(t,e){},308:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{attrs:{id:"header2fix"}},[s("header",{staticClass:"header"},[s("span",{staticClass:"header-item icon-back",on:{click:t.goBack}}),t._v(" "),s("span",{staticClass:"header-item"},[t._v(t._s(t.title))])])])},staticRenderFns:[]}},309:function(t,e,s){function o(t){s(272)}var i=s(0)(s(271),s(308),o,null,null);t.exports=i.exports},320:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=s(10),i=s.n(o),a=s(6);e.default={data:function(){return{isCollect:!1,isCollecting:!1}},computed:i()({},s.i(a.b)(["global","collectShopList","isLogin","zoneId"]),{isEmpty:function(){return this.collectShopList.length},computedShopName:function(){return this.methodGetByteLen(this.eachShopList.shopName,22)},computedStreet:function(){return this.methodGetByteLen(this.eachShopList.street,26)}}),props:["eachShopList"],methods:{toShopDetail:function(t){this.$router.push({path:"/shopDetail/"+t})},collectShop:function(t){var e=this;this.isLogin?(this.isCollecting=!0,this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"showCollect","params":{"shopCode":"'+t+'","userCode":"'+this.$store.state.userCode+'","zoneId":"'+this.zoneId+'"}}',contentType:"application/json",dataType:"json"}).then(function(t){e.isCollecting=!1,1===t.data.result?(e.isCollect=!0,e.$vux.toast.text("已收藏","bottom")):0===t.data.result?(e.isCollect=!1,e.$vux.toast.text("已取消收藏","bottom")):e.$vux.toast.text("数据错误","bottom")})):(this.$store.state.isBLR=1,this.$store.state.showLoginBox=!0,this.$vux.toast.text("请登陆","bottom"))},filterIsCollect:function(){var t=this;this.collectShopList.filter(function(e,s,o){t.eachShopList.shopCode===e.shopCode&&(t.isCollect=!0)})},methodGetByteLen:function(t,e){if(t.replace(/[^\x00-\xff]/g,"01").length<=e)return t;for(var s=Math.floor(e/2);s<t.length;s++)if(t.substr(0,s).replace(/[^\x00-\xff]/g,"01").length>=e)return t.substr(0,2*Math.floor(s/2))+"..."}},created:function(){this.filterIsCollect()}}},322:function(t,e){},325:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"background"},[s("div",{staticClass:"shoplist-item"},[s("div",{staticClass:"shopDetail",on:{click:function(e){t.toShopDetail(t.eachShopList.shopCode)}}},[s("div",{staticClass:"shop-logo"},[s("img",{directives:[{name:"lazy",rawName:"v-lazy",value:t.global.hftcomImgSrc+t.eachShopList.logoUrl,expression:"global.hftcomImgSrc+eachShopList.logoUrl"}]})]),t._v(" "),s("div",{staticClass:"shop-name"},[t._v("\n            "+t._s(t.computedShopName)+"\n        ")]),t._v(" "),s("div",{staticClass:"shop-street"},[s("span",{staticClass:"street"},[t._v(t._s(t.computedStreet))]),t._v(" "),t.eachShopList.distance<100?s("span",{staticClass:"distance"},[t._v("<100m")]):t._e(),t._v(" "),100<t.eachShopList.distance&&t.eachShopList.distance<500?s("span",{staticClass:"distance"},[t._v(">100m")]):t._e(),t._v(" "),t.eachShopList.distance>500?s("span",{staticClass:"distance"},[t._v(">500m")]):t._e()])]),t._v(" "),s("div",{staticClass:"collect"},[s("span",{directives:[{name:"show",rawName:"v-show",value:!1===t.isCollect&&!1===t.isCollecting,expression:"isCollect ===false && isCollecting === false"}],staticClass:"icon-collect",on:{click:function(e){t.collectShop(t.eachShopList.shopCode)}}}),t._v(" "),s("transition",{attrs:{name:"rotate"}},[s("span",{directives:[{name:"show",rawName:"v-show",value:t.isCollecting,expression:"isCollecting"}],staticClass:"icon-spinner8"})]),t._v(" "),s("span",{directives:[{name:"show",rawName:"v-show",value:!0===t.isCollect&&!1===t.isCollecting,expression:"isCollect === true && isCollecting === false"}],staticClass:"icon-collect-solid theme-red",on:{click:function(e){t.collectShop(t.eachShopList.shopCode)}}})],1)])])},staticRenderFns:[]}},329:function(t,e,s){function o(t){s(322)}var i=s(0)(s(320),s(325),o,null,null);t.exports=i.exports},341:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=s(10),i=s.n(o),a=s(329),n=s.n(a),c=s(118),l=s.n(c),r=s(116),h=s.n(r),u=s(309),p=s.n(u),d=s(6);e.default={name:"mycollection",data:function(){return{tabs:[{id:0,name:"商家",isActive:!0},{id:1,name:"优惠券",isActive:!1}],currentTab:0,shopList:[],couponList:[]}},computed:i()({},s.i(d.b)(["CouponListInfo","global","zoneId","collectShopList","userCode"]),s.i(d.c)([])),beforeCreate:function(){},methods:i()({},s.i(d.d)(["getCollectShop","getCollectInfo"]),{loadShopPage:function(){var t=this;this.getCollectShop().then(function(){t.shopList=t.collectShopList})},loadCouponPage:function(){var t=this;this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"showCoupon","params":{"userCode":"'+this.userCode+'", "zoneId":"'+this.zoneId+'"}}',contentType:"application/json",dataType:"json"}).then(function(e){if("null"===e.data.result||null===e.data.result)t.couponList=[];else{for(var s=[],o=0;o<e.data.result.length;o++)void 0===e.data.result[o][0]&&(e.data.result[o][0]={collectCouponCode:""}),""===e.data.result[o][0].collectCouponCode||void 0===e.data.result[o][0].collectCouponCode||(e.data.result[o][0].batchCouponCode=e.data.result[o][0].collectCouponCode,s.push(e.data.result[o][0]));t.couponList=s}}),this.getCollectInfo()},changePage:function(t){0===t?this.loadShopPage():this.loadCouponPage();for(var e=0;e<2;e++)this.$data.tabs[e].isActive=!1;this.$data.tabs[t].isActive=!0},goBack:function(){this.$router.go(-1)}}),created:function(){this.loadCouponPage(),this.loadShopPage()},components:{"v-nav":h.a,shoplist:n.a,indexcoupon:l.a,header2fix:p.a}}},389:function(t,e){},427:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{attrs:{id:"mycollection"}},[s("header2fix",{attrs:{title:"我的收藏"}}),t._v(" "),s("ul",{staticClass:"couponTabs"},t._l(t.tabs,function(e){return s("li",{key:e.id,class:{active:e.isActive},on:{click:function(s){t.changePage(e.id)}}},[t._v(t._s(e.name)),!0===e.isActive?s("span",{staticClass:"rec"}):t._e()])})),t._v(" "),s("transition",{attrs:{name:"fade"}},[s("div",{directives:[{name:"show",rawName:"v-show",value:t.tabs[0].isActive,expression:"tabs[0].isActive"}],staticClass:"shoplist"},[t._l(t.shopList,function(e){return s("shoplist",{directives:[{name:"show",rawName:"v-show",value:t.shopList!=={},expression:"shopList!=={}"}],key:e.shopCode,attrs:{eachShopList:e}})}),t._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:0===t.shopList.length,expression:"shopList.length===0"}],staticClass:"shoplist-empty"},[t._v("您还没有收藏任何商家")])],2)]),t._v(" "),s("transition",{attrs:{name:"fade"}},[s("div",{directives:[{name:"show",rawName:"v-show",value:t.tabs[1].isActive,expression:"tabs[1].isActive"}],staticClass:"couponlist"},[t._l(t.couponList,function(e){return s("indexcoupon",{directives:[{name:"show",rawName:"v-show",value:t.couponList!=={},expression:"couponList!=={}"}],key:e.batchCouponCode,attrs:{index:e,CouponListInfo:t.CouponListInfo}})}),t._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:0===t.couponList.length,expression:"couponList.length===0"}],staticClass:"shoplist-empty"},[t._v("您还没有收藏任何优惠券")])],2)])],1)},staticRenderFns:[]}}});
//# sourceMappingURL=8.51271df722f468dc17ef.js.map