webpackJsonp([16],{244:function(t,e,o){function a(t){o(366)}var s=o(0)(o(344),o(394),a,"data-v-2fc1d619",null);t.exports=s.exports},344:function(t,e,o){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=o(34),s=o.n(a),n=o(13),i=o.n(n),c=o(6),h=o(117),r=o.n(h),u=o(118),d=o.n(u);e.default={data:function(){return{searchWord:"",couponType:0,page:1,searchCouponList:[],show:!1,maskShow:!1,type1:"券类型",busy:!0,nextPage:0,currentPage:0,iconShow:!1,headInput:!0,showLoadGif:!0,couponTypeList:[{name:"全部",value:0},{name:"N元购",value:1},{name:"抵扣券",value:3},{name:"折扣券",value:4},{name:"实物券",value:5},{name:"体验券",value:6},{name:"兑换券",value:7},{name:"代金券",value:8}]}},computed:i()({},o.i(c.b)(["localCity","global","zoneId"])),beforeCreate:function(){},watch:{searchWord:function(t,e){this.$data.page=1}},methods:{hiddenMask:function(){this.show=!1,this.maskShow=!1,this.iconShow=!1},typeSearch:function(t){this.couponType=t.value,this.page=1,this.searchCoupon(),this.type1=t.name,this.show=!1,this.maskShow=!1},showCouponType:function(){!0===this.show?(this.show=!1,this.maskShow=!1,this.iconShow=!1):(this.show=!0,this.maskShow=!0,this.iconShow=!0)},goBack:function(){this.$router.go(-1)},searchCoupon:function(){var t=this;this.showLoadGif=!0,this.axios({method:"post",url:this.$store.state.global.hftcomClient,data:'{"id":19,"jsonrpc":"2.0","method":"listCoupon","params":{"couponType":'+this.$data.couponType+',"searchWord":"'+this.$data.searchWord+'","longitude":'+this.$store.state.longitude+',"latitude":'+this.$store.state.latitude+',"page":'+this.page+',"city":"'+this.$store.state.localCity+'","userCode":"'+this.$store.state.userCode+'","zoneId":"'+this.zoneId+'"}}',contentType:"application/json",dataType:"json"}).then(function(e){1===t.page?t.$data.searchCouponList=e.data.result.couponList.CouponListInfo:t.$data.searchCouponList=[].concat(s()(t.$data.searchCouponList),s()(e.data.result.couponList.CouponListInfo)),t.nextPage=e.data.result.nextPage,t.currentPage=e.data.result.page,t.page++,t.busy=!1,t.hiddenLoadGif()})},searchConponLoadMore:function(){this.busy=!0,this.nextPage>this.currentPage?this.searchCoupon():(this.busy=!0,this.$vux.toast.text("加载完毕","bottom"),this.showLoadGif=!1)},hiddenLoadGif:function(){this.nextPage<=this.currentPage&&(this.$vux.toast.text("加载完毕","bottom"),this.showLoadGif=!1)}},created:function(){this.searchCoupon()},mounted:function(){this.headInput=!1},components:{indexcoupon:r.a,loadGif:d.a}}},366:function(t,e){},394:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("div",{attrs:{id:"conponSearch"}},[o("header",{staticClass:"header"},[o("span",{staticClass:"header-item icon-back",on:{click:t.goBack}}),t._v(" "),o("div",{staticClass:"header-right"},[o("span",{staticClass:"haderitem2"},[o("input",{directives:[{name:"model",rawName:"v-model",value:t.searchWord,expression:"searchWord"}],ref:"input",class:!0===t.headInput?"headInput":"move",attrs:{id:"head-input",type:"text",name:"",placeholder:"搜索优惠券"},domProps:{value:t.searchWord},on:{input:function(e){e.target.composing||(t.searchWord=e.target.value)}}})]),t._v(" "),o("span",{staticClass:"header-item ",on:{click:t.searchCoupon}},[t._v("搜索")])])]),t._v(" "),o("div",{staticClass:"selectType ",class:[1==t.iconShow?"activeCity":""],on:{click:function(e){t.showCouponType()}}},[o("span",{staticClass:"type1 "},[t._v(t._s(t.type1))]),t._v(" "),o("span",{staticClass:"icon-ctrl",class:[1==t.iconShow?"iconTurn":""]})]),t._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:t.maskShow,expression:"maskShow"}],staticClass:"mask",on:{click:function(e){t.hiddenMask()}}}),t._v(" "),t.show?o("div",{staticClass:"couponType1"},t._l(t.couponTypeList,function(e){return o("div",{key:e.name,staticClass:"couponType2",class:[e.value===t.couponType?"activeCity":""],on:{click:function(o){t.typeSearch(e)}}},[t._v(t._s(e.name))])})):t._e(),t._v(" "),o("div",{directives:[{name:"infinite-scroll",rawName:"v-infinite-scroll",value:t.searchConponLoadMore,expression:"searchConponLoadMore"}],staticClass:"indexcoupon-wrapper",attrs:{"infinite-scroll-disabled":"busy","infinite-scroll-distance":"10"}},[t._l(t.searchCouponList,function(t){return o("indexcoupon",{key:t.batchCouponCode,attrs:{index:t}})}),t._v(" "),o("loadGif",{directives:[{name:"show",rawName:"v-show",value:t.showLoadGif,expression:"showLoadGif"}]})],2)])},staticRenderFns:[]}}});
//# sourceMappingURL=16.2276a5d895d8faeaf955.js.map