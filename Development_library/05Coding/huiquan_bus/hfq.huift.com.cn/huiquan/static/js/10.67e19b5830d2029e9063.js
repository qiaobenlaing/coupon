webpackJsonp([10],{244:function(e,t,n){function a(e){n(407)}var o=n(0)(n(351),n(445),a,null,null);e.exports=o.exports},261:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={props:["title"],methods:{goBack:function(){this.$router.go(-1)}}}},262:function(e,t){},263:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"header2"}},[n("header",{staticClass:"header"},[n("span",{staticClass:"header-item icon-back",on:{click:e.goBack}}),e._v(" "),n("span",{staticClass:"header-item"},[e._v(e._s(e.title))])])])},staticRenderFns:[]}},264:function(e,t,n){function a(e){n(262)}var o=n(0)(n(261),n(263),a,null,null);e.exports=o.exports},351:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n(264),o=n.n(a),r=n(448),i=n.n(r);t.default={data:function(){return{}},computed:{},watch:{},methods:{viewProtocol:function(){this.$router.push({path:"/agreementInfo"})}},mounted:function(){},created:function(){},components:{header2:o.a,contractPageComponent:i.a}}},359:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n(10),o=n.n(a),r=n(6);t.default={props:["index"],data:function(){return{userInfo:{name:"",phoneNum:"",idCard:"",bankCardNum:""},verificationCode:""}},computed:o()({},n.i(r.b)(["global"])),watch:{},methods:{},created:function(){}}},389:function(e,t){},407:function(e,t){},427:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"contractPageComponent"}},[n("div",{staticClass:"bankLogo"},[n("img",{directives:[{name:"lazy",rawName:"v-lazy",value:this.global.staticSrc+"/huiquan/static/img/guizhoubanklogo.png",expression:"this.global.staticSrc+'/huiquan/static/img/guizhoubanklogo.png'"}],attrs:{alt:""}})]),e._v(" "),n("div",{staticClass:"bankCardInfo"},[n("p",[n("label",[e._v("姓名")]),e._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:e.userInfo.name,expression:"userInfo.name"}],attrs:{id:"name",placeholder:"请填写姓名"},domProps:{value:e.userInfo.name},on:{input:function(t){t.target.composing||e.$set(e.userInfo,"name",t.target.value)}}})]),e._v(" "),n("p",[n("label",[e._v("手机号")]),e._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:e.userInfo.phoneNum,expression:"userInfo.phoneNum"}],attrs:{id:"phoneNum",placeholder:"请填写银行预留手机号码"},domProps:{value:e.userInfo.phoneNum},on:{input:function(t){t.target.composing||e.$set(e.userInfo,"phoneNum",t.target.value)}}})]),e._v(" "),n("p",[n("label",[e._v("证件号")]),e._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:e.userInfo.idCard,expression:"userInfo.idCard"}],attrs:{id:"idCard",placeholder:"请填写证件号"},domProps:{value:e.userInfo.idCard},on:{input:function(t){t.target.composing||e.$set(e.userInfo,"idCard",t.target.value)}}})]),e._v(" "),n("p",[n("label",[e._v("银行卡号")]),e._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:e.userInfo.bankCardNum,expression:"userInfo.bankCardNum"}],attrs:{id:"bankCardNum",placeholder:"请填写银行卡号"},domProps:{value:e.userInfo.bankCardNum},on:{input:function(t){t.target.composing||e.$set(e.userInfo,"bankCardNum",t.target.value)}}})]),e._v(" "),n("p",[n("label",[e._v("验证码")]),e._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:e.verificationCode,expression:"verificationCode"}],staticClass:"verificationCode",attrs:{id:"verificationCode",placeholder:"请填写短信验证码"},domProps:{value:e.verificationCode},on:{input:function(t){t.target.composing||(e.verificationCode=t.target.value)}}}),e._v(" "),n("span",{staticClass:"getVerificationCodeSpan"},[e._v("获取短信验证码")])])])])},staticRenderFns:[]}},445:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"payChannel"}},[n("header2",{attrs:{title:"签约银行"}}),e._v(" "),n("contractPageComponent",{attrs:{index:e.bankInfo}}),e._v(" "),n("div",{staticClass:"agreement"},[n("span",{on:{click:e.viewProtocol}},[e._v("点击确定，表示已阅读《贵阳银行银行卡xxxxxx协议》")])]),e._v(" "),n("div",{staticClass:"determineButton"},[e._v("确定")])],1)},staticRenderFns:[]}},448:function(e,t,n){function a(e){n(389)}var o=n(0)(n(359),n(427),a,null,null);e.exports=o.exports}});
//# sourceMappingURL=10.67e19b5830d2029e9063.js.map