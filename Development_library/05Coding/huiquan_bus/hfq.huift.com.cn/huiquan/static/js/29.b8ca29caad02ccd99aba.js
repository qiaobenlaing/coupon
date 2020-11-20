webpackJsonp([29],{231:function(e,t,s){function o(e){s(378)}var a=s(0)(s(338),s(416),o,null,null);e.exports=a.exports},338:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=s(17),a=s.n(o),n=s(10),i=s.n(n),r=s(6);t.default={name:"myInformation",head:function(){return{title:"个人信息"}},data:function(){return{oldUserInfo:{},userInfo:{nickName:"",realName:"",city:"",sex:""},newUserInfo:{},position:"default",showPositionValue:!0,isLogin:!0,cityList:{},isopen:!1}},computed:i()({},s.i(r.b)(["mobileNbr","global","userCode"]),s.i(r.c)(["doneTodosCount"])),beforeCreate:function(){},methods:{goBack:function(){this.$router.go(-1)},getUserInfo:function(){var e=this;this.axios({method:"post",url:this.global.hftcomClient,data:'{"id":19,"jsonrpc":"2.0","method":"getUserInfo","params":{"userCode":"'+this.userCode+'"}}',contentType:"application/json",dataType:"json"}).then(function(t){for(var s in e.userInfo)e.userInfo[s]=t.data.result[s],e.oldUserInfo[s]=t.data.result[s]})},updateUserInfo:function(){var e=this,t=!1,s=/^[a-zA-Z0-9_\u4E00-\u9FA5]{1,15}$/,o=/^[\u4E00-\u9FA5]{2,7}$/;if(""===this.userInfo.nickName?this.$vux.toast.text("昵称不允许为空","bottom"):s.test(this.userInfo.nickName)?""===this.userInfo.realName?this.$vux.toast.text("真实姓名不允许为空","bottom"):o.test(this.userInfo.realName)?"U"===this.userInfo.sex?this.$vux.toast.text("请选择性别","bottom"):""===this.userInfo.city?this.$vux.toast.text("请选择城市","bottom"):t=!0:this.$vux.toast.text("真实姓名只允许为中文2-7位","bottom"):this.$vux.toast.text("昵称应为1-15位的中英文、数字及下划线","bottom"),t){for(var n in this.userInfo)this.userInfo[n]!==this.oldUserInfo[n]&&(this.newUserInfo[n]=this.userInfo[n]);"{}"===a()(this.newUserInfo)&&(this.$vux.toast.text("您未修改任何信息","bottom"),t=!1)}t&&(console.log('{"id":19,"jsonrpc":"2.0","method":"updateUserInfo","params":{"mobileNbr":"'+this.$store.state.mobileNbr+'","userCode":"'+this.$store.state.userCode+'","updateInfo":'+a()(this.newUserInfo)+"}}"),this.axios({method:"post",url:this.global.hftcomClient,data:'{"id":19,"jsonrpc":"2.0","method":"updateUserInfo","params":{"mobileNbr":"'+this.mobileNbr+'","userCode":"'+this.userCode+'","updateInfo":'+a()(this.newUserInfo)+"}}",contentType:"application/json",dataType:"json"}).then(function(s){switch(s.data.result.code){case 50001:e.$vux.toast.text("请将信息填写完整","bottom");break;case 2e4:e.$vux.toast.text("失败，请重试","bottom");break;case 5e4:e.$vux.toast.text("修改成功","bottom");for(var o in e.newUserInfo)e.oldUserInfo[o]=e.newUserInfo[o];e.newUserInfo={},t=!1;break;case 50003:e.$vux.toast.text("您没有修改任何信息","bottom");break;case 50008:e.$vux.toast.text("用户不存在","bottom");break;default:e.$vux.toast.text(s.data.result,"bottom")}}))},openselect:function(){!0===this.isopen?this.isopen=!1:this.isopen=!0},chooseCity:function(e){this.userInfo.city=e,this.isopen=!1},closeSelect:function(){this.isopen=!1}},created:function(){var e=this;this.getUserInfo(),this.$store.dispatch("getOpenCity").then(function(t){e.cityList=t.result})},components:{}}},378:function(e,t){},416:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{attrs:{id:"changemyinfo"}},[s("header",{staticClass:"header"},[s("span",{staticClass:"header-item icon-back",on:{click:e.goBack}}),e._v(" "),s("span",{staticClass:"header-item"},[e._v("修改个人信息")]),e._v(" "),s("span",{staticClass:"header-item",on:{click:function(t){e.updateUserInfo()}}},[e._v("保存")])]),e._v(" "),s("div",{staticClass:"panel"},[s("p",[s("label",{attrs:{for:"nickName"}},[e._v("呢称")]),e._v(" "),s("input",{directives:[{name:"model",rawName:"v-model.lazy",value:e.userInfo.nickName,expression:"userInfo.nickName",modifiers:{lazy:!0}}],attrs:{id:"nickName",value:"edit me",placeholder:"请填写昵称"},domProps:{value:e.userInfo.nickName},on:{change:function(t){e.$set(e.userInfo,"nickName",t.target.value)}}})]),e._v(" "),s("p",[s("label",{attrs:{for:"realName"}},[e._v("真实姓名")]),e._v(" "),s("input",{directives:[{name:"model",rawName:"v-model.lazy",value:e.userInfo.realName,expression:"userInfo.realName",modifiers:{lazy:!0}}],attrs:{id:"realName",value:"女",placeholder:"请填写真实姓名"},domProps:{value:e.userInfo.realName},on:{change:function(t){e.$set(e.userInfo,"realName",t.target.value)}}})]),e._v(" "),s("p",[s("label",{attrs:{for:"sex"}},[e._v("性别")]),e._v(" "),s("span",{staticClass:"radio"},[s("label",{attrs:{for:"male"}},[s("input",{directives:[{name:"model",rawName:"v-model.lazy",value:e.userInfo.sex,expression:"userInfo.sex",modifiers:{lazy:!0}}],attrs:{type:"radio",id:"male",value:"M"},domProps:{checked:e._q(e.userInfo.sex,"M")},on:{change:function(t){e.$set(e.userInfo,"sex","M")}}}),e._v("男")])]),e._v(" "),s("span",{staticClass:"radio"},[s("label",{attrs:{for:"female"}},[s("input",{directives:[{name:"model",rawName:"v-model.lazy",value:e.userInfo.sex,expression:"userInfo.sex",modifiers:{lazy:!0}}],staticClass:"radio",attrs:{type:"radio",id:"female",value:"F"},domProps:{checked:e._q(e.userInfo.sex,"F")},on:{change:function(t){e.$set(e.userInfo,"sex","F")}}}),e._v("女")])]),e._v(" "),s("span",{staticClass:"radio"},[s("label",{attrs:{for:"U"}},[s("input",{directives:[{name:"model",rawName:"v-model.lazy",value:e.userInfo.sex,expression:"userInfo.sex",modifiers:{lazy:!0}}],staticClass:"radio",attrs:{type:"radio",id:"U",value:"U"},domProps:{checked:e._q(e.userInfo.sex,"U")},on:{change:function(t){e.$set(e.userInfo,"sex","U")}}}),e._v("未填")])])]),s("div",{staticClass:"city"},[s("label",{attrs:{for:"city"}},[e._v("城市")]),e._v(" "),s("div",{staticClass:"cityInfo"},[s("div",{attrs:{id:"select"},on:{click:e.openselect}},[s("span",{directives:[{name:"show",rawName:"v-show",value:""!==e.userInfo.city,expression:"userInfo.city!==''"}]},[e._v(e._s(e.userInfo.city))]),e._v(" "),s("span",{directives:[{name:"show",rawName:"v-show",value:""===e.userInfo.city,expression:"userInfo.city===''"}]},[e._v("请选择")])]),e._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:e.isopen,expression:"isopen"}],staticClass:"select-item"},e._l(e.cityList,function(t){return s("p",{attrs:{value:t.name},on:{click:function(s){e.chooseCity(t.name)}}},[e._v(e._s(t.name))])})),e._v(" "),s("span",{staticClass:"icon-ctrl",on:{click:e.openselect}})]),e._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:e.isopen,expression:"isopen"}],staticClass:"selectMask",on:{click:e.closeSelect}})])]),e._v(" "),s("input",{attrs:{type:"file",id:"changeAvatar",accept:"image/*"},on:{change:function(t){e.showNewAvatar()}}})])},staticRenderFns:[]}}});
//# sourceMappingURL=29.b8ca29caad02ccd99aba.js.map