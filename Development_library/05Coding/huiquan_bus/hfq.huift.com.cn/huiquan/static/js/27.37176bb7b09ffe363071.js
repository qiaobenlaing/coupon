webpackJsonp([27],{234:function(e,t,s){function o(e){s(396)}var n=s(0)(s(341),s(434),o,null,null);e.exports=n.exports},341:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=s(10),n=s.n(o),i=s(115),a=s.n(i),r=s(6);t.default={name:"myInformation",head:function(){return{title:"个人信息"}},data:function(){return{userInfo:{},position:"default",showPositionValue:!0,avatarUrl:"",showPrompt:!1,allPoint:0}},computed:n()({},s.i(r.b)(["mobileNbr","global","isLogin","userCode","zoneId","appType"]),s.i(r.c)(["doneTodosCount"])),methods:n()({},s.i(r.e)({}),{overImg:function(){this.showPrompt=!0},outImg:function(){this.showPrompt=!1},UploadNewImg:function(e){var t=new FormData;t.append("Img",e),t.append("chunk","12"),t.append("id","19"),t.append("jsonrpc","2.0"),t.append("method","uploadHeadImage"),t.append("userCode",this.$store.state.userCode);var s={headers:{"Content-Type":"application/x-www-form-urlencoded"}};this.axios({method:"post",url:this.global.hftcom+"/Api/UploadImage/uploadHeadImage",data:t,config:s}).then(function(e){})},showNewAvatar:function(){var e=document.getElementById("changeAvatar").files[0];console.log(e.size),console.log(e.name),console.log(e.type),e.size>524288?this.$vux.toast.text("图片太大，上传失败","bottom"):this.UploadNewImg(e)},changeInmg:function(){document.getElementById("changeAvatar").click()},login:function(){this.$store.state.isBLR=1,this.$store.state.showLoginBox=!0},register:function(){this.$store.state.isBLR=2,this.$store.state.showLoginBox=!0},bindTel:function(){var e=this;this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"isBindMobile","params":{"userCode":"'+this.userCode+'"}}',contentType:"application/json",dataType:"json"}).then(function(t){console.log(t),5e4===t.data.result.code?(e.$store.state.isBLR=3,e.$store.state.phoneNum=t.data.result.mobileNbr):(e.$store.state.isBLR=0,e.$store.state.phoneNum="")}),this.$store.state.showLoginBox=!0},getUserInfo:function(){var e=this;this.axios({method:"POST",url:"/Api/Client",data:'{"id":19,"jsonrpc":"2.0","method":"getUserInfo","params":{"userCode":"'+this.userCode+'"}}',responseType:"json"}).then(function(t){e.userInfo=t.data.result,console.log(t)}),1===this.appType&&this.axios({method:"POST",url:"/Api/Activity",data:'{"id":19,"jsonrpc":"2.0","method":"getAllPoint","params":{"userCode":"'+this.userCode+'","zoneId":"'+this.zoneId+'"}}',responseType:"json"}).then(function(t){null!==t.data.result[0].allPoint&&(e.allPoint=t.data.result[0].allPoint),console.log(t)})},clearCookie:function(){var e=this;this.$vux.confirm.show({title:"确定要退出登陆吗？",onCancel:function(){e.$vux.confirm.hide()},onConfirm:function(){e.$store.dispatch("clearCookie"),e.$router.push("/myInformation"),e.userInfo={}}})},toPage:function(e){this.$router.push(e)},toPersonalPage:function(e){""===this.$store.state.userCode||void 0===this.$store.state.userCode?(this.$store.state.isBLR=1,this.$store.state.showLoginBox=!0,this.$vux.toast.text("请登录","bottom")):this.$router.push(e)},jumpOutside:function(e){window.location.href=e}}),created:function(){this.isLogin&&this.getUserInfo()},components:{"v-nav":a.a}}},396:function(e,t){},434:function(e,t,s){e.exports={render:function(){var e=this,t=e.$createElement,o=e._self._c||t;return o("div",{attrs:{id:"myinformation"}},[o("div",{staticClass:"personalInfo"},[o("div",{staticClass:"img",class:[!0===e.showPrompt?"showPrompt":""],on:{click:function(t){e.changeInmg()},mouseover:function(t){e.overImg()},mouseout:function(t){e.outImg()}}},[o("img",{directives:[{name:"show",rawName:"v-show",value:""===e.userInfo.avatarUrl||void 0===e.userInfo.avatarUrl,expression:"userInfo.avatarUrl\n==='' || userInfo.avatarUrl\n=== undefined"}],attrs:{src:s(119),alt:""}}),""!=e.userInfo.avatarUrl&&void 0!=e.userInfo.avatarUrl?o("img",{attrs:{src:e.userInfo.avatarUrl,alt:""}}):e._e()]),e._v(" "),o("div",{staticClass:"weixinId"},[e._v("微信名称")]),e._v(" "),o("div",{staticClass:"mobile"},[o("span",{directives:[{name:"show",rawName:"v-show",value:""===e.userInfo.nickName,expression:"userInfo.nickName===''"}]},[e._v("快给你自己取个昵称吧")]),e._v(e._s(e.userInfo.nickName))]),e._v(" "),o("button",{directives:[{name:"show",rawName:"v-show",value:!e.isLogin,expression:"!isLogin"}],staticClass:"login",on:{click:e.login}},[e._v("登录")]),e._v("  "),o("button",{directives:[{name:"show",rawName:"v-show",value:!e.isLogin,expression:"!isLogin"}],staticClass:"login",on:{click:e.register}},[e._v("注册")]),e._v(" "),e._m(0)]),e._v(" "),o("div",{staticClass:"information"},[o("div",{directives:[{name:"show",rawName:"v-show",value:e.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:function(t){e.toPersonalPage("changemyinfo")}}},[e._m(1)]),e._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:e.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:e.bindTel}},[e._m(2)]),e._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:e.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:function(t){e.toPersonalPage("payChannel")}}},[e._m(3)]),e._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:e.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:function(t){e.toPersonalPage("mycollection")}}},[e._m(4)]),e._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:e.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:function(t){e.toPersonalPage("order")}}},[e._m(5)]),e._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:e.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:function(t){e.jumpOutside("https://gzyh.hfq.huift.com.cn/mall/fudu/order/payment.html?type=3")}}},[e._m(6)]),e._v(" "),o("div",{staticClass:"infoItem",on:{click:function(t){e.toPage("cooperation")}}},[e._m(7)]),e._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:e.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:e.clearCookie}},[e._m(8)])]),e._v(" "),o("input",{attrs:{type:"file",id:"changeAvatar",accept:"image/*"},on:{change:function(t){e.showNewAvatar()}}}),e._v(" "),o("v-nav")],1)},staticRenderFns:[function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"circle"},[s("div",{staticClass:"circle1"}),e._v(" "),s("div",{staticClass:"circle2"}),e._v(" "),s("div",{staticClass:"circle3"})])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",[e._v("修改个人信息"),s("span",{staticClass:"arrow icon-back"})])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",[e._v("绑定手机号"),s("span",{staticClass:"arrow icon-back"})])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",[e._v("支付渠道"),s("span",{staticClass:"arrow icon-back"})])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",[e._v("我的收藏"),s("span",{staticClass:"arrow icon-back"})])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",[e._v("我的订单"),s("span",{staticClass:"arrow icon-back"})])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",[e._v("微商城"),s("span",{staticClass:"arrow icon-back"})])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",[e._v("我要合作"),s("span",{staticClass:"arrow icon-back"})])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",[e._v("退出登陆"),s("span",{staticClass:"arrow icon-back"})])}]}}});
//# sourceMappingURL=27.37176bb7b09ffe363071.js.map