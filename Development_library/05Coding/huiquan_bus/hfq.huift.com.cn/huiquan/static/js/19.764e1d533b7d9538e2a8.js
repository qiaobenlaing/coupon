webpackJsonp([19],{232:function(t,e,s){function o(t){s(368)}var i=s(0)(s(327),s(396),o,null,null);t.exports=i.exports},327:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=s(10),i=s.n(o),n=s(115),a=s.n(n),r=s(6);e.default={name:"myInformation",head:function(){return{title:"个人信息"}},data:function(){return{userInfo:{},position:"default",showPositionValue:!0,avatarUrl:"",showPrompt:!1,allPoint:0}},computed:i()({},s.i(r.b)(["mobileNbr","global","isLogin","userCode","zoneId","appType"]),s.i(r.c)(["doneTodosCount"])),methods:i()({},s.i(r.e)({}),{overImg:function(){this.showPrompt=!0},outImg:function(){this.showPrompt=!1},UploadNewImg:function(t){var e=new FormData;e.append("Img",t),e.append("chunk","12"),e.append("id","19"),e.append("jsonrpc","2.0"),e.append("method","uploadHeadImage"),e.append("userCode",this.$store.state.userCode);var s={headers:{"Content-Type":"application/x-www-form-urlencoded"}};this.axios({method:"post",url:this.global.hftcom+"/Api/UploadImage/uploadHeadImage",data:e,config:s}).then(function(t){})},showNewAvatar:function(){var t=document.getElementById("changeAvatar").files[0];console.log(t.size),console.log(t.name),console.log(t.type),t.size>524288?this.$vux.toast.text("图片太大，上传失败","bottom"):this.UploadNewImg(t)},changeInmg:function(){document.getElementById("changeAvatar").click()},login:function(){this.$store.state.isBLR=1,this.$store.state.showLoginBox=!0},register:function(){this.$store.state.isBLR=2,this.$store.state.showLoginBox=!0},bindTel:function(){var t=this;this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"isBindMobile","params":{"userCode":"'+this.userCode+'"}}',contentType:"application/json",dataType:"json"}).then(function(e){console.log(e),5e4===e.data.result.code?(t.$store.state.isBLR=3,t.$store.state.phoneNum=e.data.result.mobileNbr):(t.$store.state.isBLR=0,t.$store.state.phoneNum="")}),this.$store.state.showLoginBox=!0},getUserInfo:function(){var t=this;this.axios({method:"POST",url:"/Api/Client",data:'{"id":19,"jsonrpc":"2.0","method":"getUserInfo","params":{"userCode":"'+this.userCode+'"}}',responseType:"json"}).then(function(e){t.userInfo=e.data.result,console.log(e)}),1===this.appType&&this.axios({method:"POST",url:"/Api/Activity",data:'{"id":19,"jsonrpc":"2.0","method":"getAllPoint","params":{"userCode":"'+this.userCode+'","zoneId":"'+this.zoneId+'"}}',responseType:"json"}).then(function(e){null!==e.data.result[0].allPoint&&(t.allPoint=e.data.result[0].allPoint),console.log(e)})},clearCookie:function(){var t=this;this.$vux.confirm.show({title:"确定要退出登陆吗？",onCancel:function(){t.$vux.confirm.hide()},onConfirm:function(){t.$store.dispatch("clearCookie"),t.$router.push("/myInformation"),t.userInfo={}}})},toPage:function(t){this.$router.push(t)},toPersonalPage:function(t){""===this.$store.state.userCode||void 0===this.$store.state.userCode?(this.$store.state.isBLR=1,this.$store.state.showLoginBox=!0,this.$vux.toast.text("请登录","bottom")):this.$router.push(t)}}),created:function(){this.isLogin&&this.getUserInfo()},components:{"v-nav":a.a}}},368:function(t,e){},396:function(t,e,s){t.exports={render:function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("div",{attrs:{id:"myinformation"}},[o("div",{staticClass:"personalInfo"},[o("div",{staticClass:"img",class:[!0===t.showPrompt?"showPrompt":""],on:{click:function(e){t.changeInmg()},mouseover:function(e){t.overImg()},mouseout:function(e){t.outImg()}}},[o("img",{directives:[{name:"show",rawName:"v-show",value:""===t.userInfo.avatarUrl||void 0===t.userInfo.avatarUrl,expression:"userInfo.avatarUrl\n==='' || userInfo.avatarUrl\n=== undefined"}],attrs:{src:s(119),alt:""}}),""!=t.userInfo.avatarUrl&&void 0!=t.userInfo.avatarUrl?o("img",{attrs:{src:t.userInfo.avatarUrl,alt:""}}):t._e()]),t._v(" "),o("div",{staticClass:"weixinId"},[t._v("微信名称")]),t._v(" "),o("div",{staticClass:"mobile"},[o("span",{directives:[{name:"show",rawName:"v-show",value:""===t.userInfo.nickName,expression:"userInfo.nickName===''"}]},[t._v("快给你自己取个昵称吧")]),t._v(t._s(t.userInfo.nickName))]),t._v(" "),t.isLogin&&1===this.appType?o("div",{staticClass:"allPoint"},[t._v("圈值: "+t._s(t.allPoint))]):t._e(),t._v(" "),o("button",{directives:[{name:"show",rawName:"v-show",value:!t.isLogin,expression:"!isLogin"}],staticClass:"login",on:{click:t.login}},[t._v("登录")]),t._v("  "),o("button",{directives:[{name:"show",rawName:"v-show",value:!t.isLogin,expression:"!isLogin"}],staticClass:"login",on:{click:t.register}},[t._v("注册")]),t._v(" "),t._m(0)]),t._v(" "),o("div",{staticClass:"information"},[o("div",{directives:[{name:"show",rawName:"v-show",value:t.isLogin&&1===this.appType,expression:"isLogin&&this.appType === 1"}],staticClass:"infoItem",on:{click:function(e){t.toPersonalPage("sassmyvipcard")}}},[t._m(1)]),t._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:t.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:function(e){t.toPersonalPage("changemyinfo")}}},[t._m(2)]),t._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:t.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:t.bindTel}},[t._m(3)]),t._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:t.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:function(e){t.toPersonalPage("mycollection")}}},[t._m(4)]),t._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:t.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:function(e){t.toPersonalPage("order")}}},[t._m(5)]),t._v(" "),o("div",{staticClass:"infoItem",on:{click:function(e){t.toPage("aboutus")}}},[t._m(6)]),t._v(" "),o("div",{staticClass:"infoItem",on:{click:function(e){t.toPage("cooperation")}}},[t._m(7)]),t._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:t.isLogin,expression:"isLogin"}],staticClass:"infoItem",on:{click:t.clearCookie}},[t._m(8)])]),t._v(" "),o("input",{attrs:{type:"file",id:"changeAvatar",accept:"image/*"},on:{change:function(e){t.showNewAvatar()}}}),t._v(" "),o("v-nav")],1)},staticRenderFns:[function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"circle"},[s("div",{staticClass:"circle1"}),t._v(" "),s("div",{staticClass:"circle2"}),t._v(" "),s("div",{staticClass:"circle3"})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("p",[t._v("查看会员卡"),s("span",{staticClass:"arrow icon-back"})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("p",[t._v("修改个人信息"),s("span",{staticClass:"arrow icon-back"})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("p",[t._v("绑定手机号"),s("span",{staticClass:"arrow icon-back"})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("p",[t._v("我的收藏"),s("span",{staticClass:"arrow icon-back"})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("p",[t._v("我的订单"),s("span",{staticClass:"arrow icon-back"})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("p",[t._v("关于我们"),s("span",{staticClass:"arrow icon-back"})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("p",[t._v("我要合作"),s("span",{staticClass:"arrow icon-back"})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("p",[t._v("退出登陆"),s("span",{staticClass:"arrow icon-back"})])}]}}});
//# sourceMappingURL=19.764e1d533b7d9538e2a8.js.map