webpackJsonp([15],{246:function(e,t,n){function a(e){n(403)}var c=n(0)(n(353),n(441),a,null,null);e.exports=c.exports},261:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={props:["title"],methods:{goBack:function(){this.$router.go(-1)}}}},262:function(e,t){},263:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"header2"}},[n("header",{staticClass:"header"},[n("span",{staticClass:"header-item icon-back",on:{click:e.goBack}}),e._v(" "),n("span",{staticClass:"header-item"},[e._v(e._s(e.title))])])])},staticRenderFns:[]}},264:function(e,t,n){function a(e){n(262)}var c=n(0)(n(261),n(263),a,null,null);e.exports=c.exports},353:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n(10),c=n.n(a),s=n(264),i=n.n(s),o=n(6);t.default={data:function(){return{cardId:this.$route.params.cardId,checkedAgreement:!0,checkedPayMode:!1,companyName:"惠付通"}},computed:c()({},n.i(o.b)(["global"])),watch:{},methods:{consentEvent:function(){this.checkedAgreement&&this.checkedPayMode?this.$router.push({path:"/contractPage/"+this.cardId}):!this.checkedAgreement&&this.checkedPayMode?this.$vux.toast.text("必须勾选用户协议","bottom"):this.checkedAgreement&&!this.checkedPayMode?this.$vux.toast.text("必须勾选支付方式","bottom"):this.$vux.toast.text("必须勾选用户协议和支付方式","bottom")},checkedAgreementEvent:function(){this.checkedAgreement=!this.checkedAgreement},checkedPayModeEvent:function(){this.checkedPayMode=!this.checkedPayMode},viewProtocol:function(){this.$router.push({path:"/agreementInfo"})}},mounted:function(){},created:function(){},components:{header2:i.a}}},403:function(e,t){},441:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"signingAuthorization"}},[n("header2",{attrs:{title:"支付渠道"}}),e._v(" "),n("div",{staticClass:"authorizationLogo"},[n("img",{directives:[{name:"lazy",rawName:"v-lazy",value:this.global.staticSrc+"/huiquan/static/img/discountActive.png",expression:"this.global.staticSrc+'/huiquan/static/img/discountActive.png'"}],attrs:{alt:""}})]),e._v(" "),n("div",{staticClass:"requirementsDiv"},[n("span",{staticClass:"requirementsTitle"},[e._v("\n            您同意"+e._s(e.companyName)+"获取以下权限\n        ")]),e._v(" "),n("br"),e._v(" "),n("br"),e._v(" "),n("span",{staticClass:"requirementsValue"},[e._v("\n            获取使用您的身份信息(姓名、手机号、证件号)办理业务\n        ")]),e._v(" "),n("div",{staticClass:"agreementDiv"},[n("span",{staticClass:"agreementSpan",class:e.checkedAgreement?"checkedAgreementSpan":"",on:{click:e.checkedAgreementEvent}}),e._v(" "),n("span",{staticClass:"userAgreementSpan",on:{click:e.viewProtocol}},[e._v("《用户授权协议》")])]),e._v(" "),n("div",{staticClass:"payModeDiv"},[n("span",{staticClass:"payModeTitle"},[e._v("\n                支付方式:\n            ")]),e._v(" "),n("br"),e._v(" "),n("br"),e._v(" "),n("br"),e._v(" "),n("span",{staticClass:"payModeSpan",class:e.checkedPayMode?"checkedPayModeSpan":"",on:{click:e.checkedPayModeEvent}},[e._v("\n                贵州银行\n            ")])])]),e._v(" "),n("div",{staticClass:"consentToAuthorization",on:{click:e.consentEvent}},[e._v("同意授权，进入下一步")])],1)},staticRenderFns:[]}}});
//# sourceMappingURL=15.89a3a67905ee5ada564e.js.map