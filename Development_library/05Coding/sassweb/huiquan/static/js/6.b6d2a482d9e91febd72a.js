webpackJsonp([6],{238:function(t,o,s){function e(t){s(349)}var i=s(0)(s(336),s(375),e,null,null);t.exports=i.exports},293:function(t,o,s){"use strict";Object.defineProperty(o,"__esModule",{value:!0});var e=s(13),i=s.n(e),a=s(116),n=(s.n(a),s(6));o.default={props:["imgSrcArr"],data:function(){return{swiperOption:{notNextTick:!0,autoplay:!0,effect:"effect",grabCursor:!1,setWrapperSize:!0,pagination:{el:".swiper-pagination",clickable:!0},prevButton:".swiper-button-prev",nextButton:".swiper-button-next",observeParents:!0},newImgSrcArr:["/Public/Uploads/20180528/5b0bb30255d5a.jpg","/Public/Uploads/20180528/5b0bb300010f7.jpg"]}},computed:i()({},s.i(n.b)(["global"])),watch:{imgSrcArr:function(){this.loadSwiper()}},methods:{loadSwiper:function(){void 0===this.imgSrcArr?this.imgSrcArr=[]:"String"===Object.prototype.toString.call(this.imgSrcArr).slice(8,-1)?this.newImgSrcArr.push(this.imgSrcArr):this.newImgSrcArr=this.imgSrcArr}},created:function(){this.loadSwiper()},components:{swiper:a.swiper,swiperSlide:a.swiperSlide}}},295:function(t,o){},298:function(t,o){t.exports={render:function(){var t=this,o=t.$createElement,s=t._self._c||o;return s("div",{attrs:{id:"swiper"}},[s("swiper",{ref:"mySwiper",attrs:{options:t.swiperOption}},[t._l(t.newImgSrcArr,function(o){return s("swiper-slide",{key:o},[s("img",{directives:[{name:"lazy",rawName:"v-lazy",value:t.global.hftcom+o,expression:"global.hftcom + img"}]})])}),t._v(" "),s("div",{staticClass:"swiper-pagination",attrs:{slot:"pagination"},slot:"pagination"})],2),t._v(" "),t._l(t.newImgSrcArr,function(o){return s("p",[t._v("img")])})],2)},staticRenderFns:[]}},301:function(t,o,s){function e(t){s(295)}var i=s(0)(s(293),s(298),e,null,null);t.exports=i.exports},305:function(t,o,s){t.exports={default:s(306),__esModule:!0}},306:function(t,o,s){s(308),t.exports=s(4).Object.keys},307:function(t,o,s){var e=s(10),i=s(4),a=s(20);t.exports=function(t,o){var s=(i.Object||{})[t]||Object[t],n={};n[t]=o(s),e(e.S+e.F*a(function(){s(1)}),"Object",n)}},308:function(t,o,s){var e=s(46),i=s(36);s(307)("keys",function(){return function(t){return i(e(t))}})},336:function(t,o,s){"use strict";Object.defineProperty(o,"__esModule",{value:!0});var e=s(305),i=s.n(e),a=s(13),n=s.n(a),r=s(117),p=s.n(r),l=s(301),c=s.n(l),d=s(6);o.default={name:"shopdetail",data:function(){return{ok:!0,albumImg:"本店暂无图片",shopImgList:[],shopAllInfo:{couponList:{shopCoupon:[]},shopInfo:{shopName:""}},shopDecoList:[],shopAllInfoFlag:!1,isCollect:!1,viptype:1,isadding:""}},computed:n()({},s.i(d.b)(["collectShopList","global","userCode","isLogin"])),beforeCreate:function(){},methods:{goBack:function(){this.$router.go(-1)},getShopDetail:function(){var t=this;this.axios({method:"post",url:this.global.hftcomClient,data:'{"id":19,"jsonrpc":"2.0","method":"getShopInfo","params":{"shopCode":"'+this.$route.params.shopCode+'","userCode":"'+this.$store.state.userCode+'","longitude":'+this.$store.state.longitude+',"latitude":'+this.$store.state.latitude+"}}",contentType:"application/json",dataType:"json"}).then(function(o){t.shopAllInfo=o.data.result,t.shopAllInfoFlag=!0,0!==t.collectShopList.length&&t.collectShopList.filter(function(o,s,e){t.shopAllInfo.shopInfo.shopCode===o.shopCode&&(t.isCollect=!0)}),t.showvipcard()})},getShopDecoration:function(){var t=this;this.axios({method:"post",url:this.global.hftcomClient,data:'{"id":19,"jsonrpc":"2.0","method":"cGetShopDecoration","params":{"shopCode":"'+this.$route.params.shopCode+'","page":1}}',contentType:"application/json",dataType:"json"}).then(function(o){t.shopDecoList=o.data.result.shopDecoList;for(var s=i()(t.$data.shopDecoList).length,e=0;e<s;e++)t.$data.shopImgList[e]=t.shopDecoList[e].imgUrl})},getShopCollectList:function(){var t=this;this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"collectShop","params":{"userCode":"'+this.$store.state.userCode+'","latitude":'+this.$store.state.latitude+',"longitude":'+this.$store.state.longitude+"}}",contentType:"application/json",dataType:"json"}).then(function(o){"null"!==o.data.result&&null!==o.data.result?t.$store.state.collectShopList=o.data.result[0]:t.$store.state.collectShopList=[]})},collectShop:function(t){var o=this;this.isLogin?this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"showCollect","params":{"shopCode":"'+t+'","userCode":"'+this.$store.state.userCode+'"}}',contentType:"application/json",dataType:"json"}).then(function(t){1===t.data.result?(o.isCollect=!0,o.$vux.toast.text("已收藏","bottom")):0===t.data.result?(o.isCollect=!1,o.$vux.toast.text("已取消收藏","bottom")):o.$vux.toast.text("数据错误，请稍后重试","bottom")}):(this.$store.state.isBLR=1,this.$store.state.showLoginBox=!0,this.$vux.toast.text("请登录","bottom"))},showvipcard:function(){var t=this;this.axios({method:"post",url:this.global.hftcomWeixinCard,data:'{"id":29,"jsonrpc":"2.0","method":"hasCard","params":{"userCode":"'+this.userCode+'","shopCode":"'+this.shopAllInfo.shopInfo.shopCode+'"}}'}).then(function(o){5e4===o.data.result.code?!1===o.data.result.state?t.viptype=0:t.viptype=o.data.result.state:t.$vux.toast.text(o.data.result.code,"bottom")})},getvipcard:function(){var t=this;this.isLogin?0===this.viptype?this.axios({method:"post",url:this.global.hftcomWeixinCard,data:'{"id":29,"jsonrpc":"2.0","method":"addCardUser","params":{"userCode":"'+this.userCode+'","shopCode":"'+this.shopAllInfo.shopInfo.shopCode+'"}}'}).then(function(o){if(console.log(o.data),5e4===o.data.result.code)switch(o.data.result.state){case!0:t.viptype=2,t.$vux.toast.text("领取成功","bottom"),t.addvipcard(t.shopAllInfo.shopInfo.shopCode);break;case!1:t.$vux.toast.text("领取失败，请重试","bottom");break;default:t.$vux.toast.text(o.data.result.state,"bottom")}else t.$vux.toast.text(o.data.result.code,"bottom")}):this.addvipcard(this.shopAllInfo.shopInfo.shopCode):(this.$store.state.isBLR=1,this.$store.state.showLoginBox=!0,this.$vux.toast.text("请登录","bottom"))},addvipcard:function(t){var o=this;this.isadding=t,this.axios({method:"post",url:this.global.hftcomWeixinCard,data:'{"id":29,"jsonrpc":"2.0","method":"addWeixinCard","params":{"userCode":"'+this.userCode+'","shopCode":"'+t+'"}}'}).then(function(t){console.log(t.data),""===t.data.result.signPakage||void 0===t.data.result.signPakage?o.$vux.toast.text("数据请求错误，请稍后重试","bottom"):(o.cardInfo=t.data.result.cardInfo,o.signPakage=t.data.result.signPakage,o.userCardCode=t.data.result.userCardCode,o.addcardtoweixin()),o.isadding=""})},updateUserWeixinCard:function(){var t=this;this.axios({method:"post",url:this.global.hftcomWeixinCard,data:'{"id":29,"jsonrpc":"2.0","method":"updateUserWeixinCard","params":{"userCardCode":"'+this.userCardCode+'"}}'}).then(function(o){t.viptype=1,t.$vux.toast.text("已添加","bottom")})},addcardtoweixin:function(){var t=this;wx.config({debug:!1,appId:this.signPakage.appid,timestamp:this.signPakage.timestamp,nonceStr:this.signPakage.noncestr,signature:this.signPakage.signature,jsApiList:["addCard"]}),wx.ready(function(){wx.addCard({cardList:[{code:t.cardInfo.code,cardId:t.cardInfo.card_id,cardExt:'{"code":"'+t.cardInfo.code+'","timestamp":"'+t.cardInfo.timestamp+'","signature":"'+t.cardInfo.signature+'","nonce_str":"'+t.cardInfo.nonce_str+'"}'}],success:function(o){t.updateUserWeixinCard()},cancel:function(o){t.$vux.toast.text("取消添加","bottom")}})}),wx.error(function(t){console.log("err",t)})}},created:function(){this.getShopCollectList(),this.getShopDetail(),this.getShopDecoration(),this.$store.dispatch("getCollectInfo")},components:{swiper:c.a,indexcoupon:p.a}}},349:function(t,o){},375:function(t,o){t.exports={render:function(){var t=this,o=t.$createElement,s=t._self._c||o;return s("div",{attrs:{id:"shopdetail"}},[s("div",{staticClass:"album"},[s("header",{staticClass:"header"},[s("div",{staticClass:"header-item icon-back",on:{click:t.goBack}}),t._v(" "),s("div",{staticClass:"header-item"},[s("span",{directives:[{name:"show",rawName:"v-show",value:!1===t.isCollect,expression:"isCollect ===false"}],staticClass:"icon-collect",on:{click:function(o){t.collectShop(t.shopAllInfo.shopInfo.shopCode)}}}),t._v(" "),s("span",{directives:[{name:"show",rawName:"v-show",value:!0===t.isCollect,expression:"isCollect === true"}],staticClass:"icon-collect-solid",on:{click:function(o){t.collectShop(t.shopAllInfo.shopInfo.shopCode)}}})])]),t._v(" "),0!==t.shopImgList.length?s("swiper",{attrs:{imgSrcArr:t.shopImgList}}):t._e()],1),t._v(" "),s("div",{staticClass:"shopInfoWrapper"},[s("p",{staticClass:"shopName"},[t._v(t._s(t.shopAllInfo.shopInfo.shopName)+" "),s("span",{staticClass:"popularity"},[t._v("人气："+t._s(t.shopAllInfo.shopInfo.popularity))])]),t._v(" "),s("div",{staticClass:"shopInfo"},[s("div",{staticClass:"left"},[s("div",{staticClass:"clock"},[s("div",{staticClass:"icon icon-clock"}),t._v(" "),s("div",{staticClass:"clocktext"},t._l(t.shopAllInfo.shopInfo.businessHours,function(o){return s("p",[t._v(t._s(o.open)+"-"+t._s(o.close))])}))]),t._v(" "),s("div",{staticClass:"location"},[s("div",{staticClass:"icon icon-location"}),t._v(" "),s("div",{staticClass:"locationtext"},[s("p",[s("span",[t._v(t._s(t.shopAllInfo.shopInfo.city))]),s("span",[t._v(t._s(t.shopAllInfo.shopInfo.district))]),s("span",[t._v(t._s(t.shopAllInfo.shopInfo.street))])])])])]),t._v(" "),s("div",{staticClass:"right"},[s("div",{staticClass:"content"},[s("a",{attrs:{href:"tel:"+t.shopAllInfo.shopInfo.tel}},[s("p",{staticClass:"icon-phone"})])])])])]),t._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:1!==t.viptype,expression:"viptype!==1"}],staticClass:"vipcard"},[s("div",{staticClass:"left"},[s("div",{staticClass:"logo"},[s("span",{directives:[{name:"show",rawName:"v-show",value:0===t.viptype,expression:"viptype===0"}]},[t._v("您有一张会员卡未领取!")]),t._v(" "),s("span",{directives:[{name:"show",rawName:"v-show",value:2===t.viptype,expression:"viptype===2"}]},[t._v("会员卡可添加到微信卡包!")])]),t._v(" "),s("div",{staticClass:"vipinfo"},[t._v("会员卡专享:九折优惠")])]),t._v(" "),s("div",{staticClass:"right",on:{click:t.getvipcard}},[s("span",{directives:[{name:"show",rawName:"v-show",value:0===t.viptype,expression:"viptype===0"}]},[t._v("领取会员卡")]),t._v(" "),s("span",{directives:[{name:"show",rawName:"v-show",value:2===t.viptype&&t.isadding!==t.shopAllInfo.shopInfo.shopCode,expression:"viptype===2 && isadding!==shopAllInfo.shopInfo.shopCode"}]},[t._v("添加会员卡")]),t._v(" "),s("transition",{attrs:{name:"rotate"}},[s("span",{directives:[{name:"show",rawName:"v-show",value:t.isadding===t.shopAllInfo.shopInfo.shopCode,expression:"isadding===shopAllInfo.shopInfo.shopCode"}],staticClass:"icon-spinner8"})])],1)]),t._v(" "),s("div",{staticClass:"toCard"}),t._v(" "),s("div",{staticClass:"shopCoupon"},[s("p",{staticClass:"name"},[t._v("优惠券")]),t._v(" "),0===t.shopAllInfo.couponList.shopCoupon.length?s("p",{staticClass:"text"},[t._v("本店暂无可领优惠券")]):t._e(),t._v(" "),t._l(t.shopAllInfo.couponList.shopCoupon,function(o){return t.shopAllInfoFlag?s("indexcoupon",{key:o.batchCouponCode,attrs:{index:o}}):t._e()})],2),t._v(" "),s("div",{staticClass:"shopdetail"},[s("p",{staticClass:"name"},[t._v("商家详情")]),t._v(" "),""===t.shopAllInfo.shopInfo.shortDes?s("p",{staticClass:"text"},[t._v("本店暂无商家详情")]):t._e(),t._v(" "),s("p",{staticClass:"content"},[t._v(t._s(t.shopAllInfo.shopInfo.shortDes))])])])},staticRenderFns:[]}}});
//# sourceMappingURL=6.b6d2a482d9e91febd72a.js.map