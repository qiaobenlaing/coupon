webpackJsonp([5],{237:function(e,t,r){function n(e){r(363)}var o=r(0)(r(332),r(390),n,null,null);e.exports=o.exports},288:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={props:["title"],methods:{goBack:function(){this.$router.go(-1)}}}},289:function(e,t){},290:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{attrs:{id:"header2"}},[r("header",{staticClass:"header"},[r("span",{staticClass:"header-item icon-back",on:{click:e.goBack}}),e._v(" "),r("span",{staticClass:"header-item"},[e._v(e._s(e.title))])])])},staticRenderFns:[]}},291:function(e,t,r){function n(e){r(289)}var o=r(0)(r(288),r(290),n,null,null);e.exports=o.exports},292:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"icon",props:{type:String,isMsg:Boolean},computed:{className:function(){return"weui-icon weui_icon_"+this.type+" weui-icon-"+this.type.replace(/_/g,"-")}}}},295:function(e,t){},298:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement;return(e._self._c||t)("i",{class:[e.className,e.isMsg?"weui-icon_msg":""]})},staticRenderFns:[]}},303:function(e,t,r){function n(e){r(295)}var o=r(0)(r(292),r(298),n,null,null);e.exports=o.exports},332:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=r(35),o=r.n(n),a=r(339),i=r.n(a),s=r(338),c=r.n(s),u=r(13),l=r.n(u),d=r(6),h=r(303),f=r.n(h),p=r(291),v=r.n(p);t.default={data:function(){return{batchCouponCode:this.$route.params.batchCouponCode,couponAllInfo:{},shopDecoration:{},userCouponNbrList:[],selectCouponNbrArr:[],refundReason:"",suserCouponNbr:{userCouponNbr:"00305702513"},orderListNbr:{}}},computed:l()({},r.i(d.b)(["global","userCode"]),{couponTypeText:function(){switch(this.couponAllInfo.couponType){case"1":return"N元购";case"3":return"抵用券";case"4":return"折扣券";case"5":return"实物券";case"6":return"体验券";case"7":return"兑换券";case"8":return"代金券";case"32":return"注册送的抵扣券";case"33":return"送邀请人的抵扣券"}},compuRefundMoney:function(){return this.selectCouponNbrArr.length*this.couponAllInfo.payPrice}}),watch:{selectCouponNbrArr:function(){this.selectCouponNbrArr.length===this.userCouponNbrList.length?this.allSelected=!0:this.allSelected=!1}},methods:{showIndexCouponDetails:function(){var e=this;this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"getBachCouponInfo","params":{"batchCouponCode":"'+this.batchCouponCode+'"}}',contentType:"application/json",dataType:"json"}).then(function(t){console.log(t.data.result.BatchCouponInfo),e.$data.couponAllInfo=t.data.result.BatchCouponInfo,e.$data.shopDecoration=t.data.result.ShopDecoration})},getUserCouponNbr:function(){var e=this;this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"getUserCouponNbr","params":{"userCode":"'+this.userCode+'","batchCouponCode":"'+this.batchCouponCode+'"}}',contentType:"application/json",dataType:"json"}).then(function(t){console.table(t.data.result),t.data.result.filter(function(t){"1"===t.status&&e.userCouponNbrList.push(t)})})},refundCoupon:function(){function e(){return t.apply(this,arguments)}var t=c()(i.a.mark(function e(){var t,r=this;return i.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:if(!(this.selectCouponNbrArr.length<=0)){e.next=4;break}this.$vux.toast.text("您没有选择任何优惠券","middle"),e.next=32;break;case 4:if(/^[1-6]{1}/.test(this.refundReason)){e.next=8;break}this.$vux.toast.text("退款原因有误","middle"),e.next=32;break;case 8:void 0===this.message&&(this.message=""),this.orderListNbr={},this.selectCouponNbrArr.filter(function(e){void 0===r.orderListNbr[e.orderNbr]&&(r.orderListNbr[e.orderNbr]={Nbr:[],status:0}),r.orderListNbr[e.orderNbr].Nbr.push(e.userCouponNbr)}),console.log(this.orderListNbr),this.$vux.loading.show({text:"退款中"}),e.prev=13,e.t0=i.a.keys(this.orderListNbr);case 15:if((e.t1=e.t0()).done){e.next=22;break}return t=e.t1.value,console.log(this.orderListNbr[t].Nbr),e.next=20,this.refund(t);case 20:e.next=15;break;case 22:this.$vux.loading.hide(),this.$vux.toast.text("退款申请已提交","middle"),this.$router.go(-1),e.next=32;break;case 27:e.prev=27,e.t2=e.catch(13),this.$vux.loading.hide(),this.$vux.toast.text("退款申请出错,请联系管理员或稍后重试","middle"),this.$router.go(-1);case 32:case"end":return e.stop()}},e,this,[[13,27]])}));return e}(),refund:function(e){var t=this;return new o.a(function(r,n){t.axios({method:"post",url:t.global.hftcomapi+"/Admin/Lcy/weixinRefund",data:{userCouponNbr:t.orderListNbr[e].Nbr}}).then(function(o){console.log(o.data),"success"===o.data?(t.orderListNbr[e].status=1,r()):n()})})},changeSelected:function(){var e=this;this.selectCouponNbrArr=[],this.allSelected?this.userCouponNbrList.forEach(function(t,r){e.selectCouponNbrArr.push(t)}):this.selectCouponNbrArr=[]}},created:function(){this.showIndexCouponDetails(),this.getUserCouponNbr()},components:{header2:v.a,Icon:f.a}}},338:function(e,t,r){"use strict";t.__esModule=!0;var n=r(35),o=function(e){return e&&e.__esModule?e:{default:e}}(n);t.default=function(e){return function(){var t=e.apply(this,arguments);return new o.default(function(e,r){function n(a,i){try{var s=t[a](i),c=s.value}catch(e){return void r(e)}if(!s.done)return o.default.resolve(c).then(function(e){n("next",e)},function(e){n("throw",e)});e(c)}return n("next")})}}},339:function(e,t,r){e.exports=r(369)},363:function(e,t){},369:function(e,t,r){var n=function(){return this}()||Function("return this")(),o=n.regeneratorRuntime&&Object.getOwnPropertyNames(n).indexOf("regeneratorRuntime")>=0,a=o&&n.regeneratorRuntime;if(n.regeneratorRuntime=void 0,e.exports=r(370),o)n.regeneratorRuntime=a;else try{delete n.regeneratorRuntime}catch(e){n.regeneratorRuntime=void 0}},370:function(e,t){!function(t){"use strict";function r(e,t,r,n){var a=t&&t.prototype instanceof o?t:o,i=Object.create(a.prototype),s=new f(n||[]);return i._invoke=u(e,r,s),i}function n(e,t,r){try{return{type:"normal",arg:e.call(t,r)}}catch(e){return{type:"throw",arg:e}}}function o(){}function a(){}function i(){}function s(e){["next","throw","return"].forEach(function(t){e[t]=function(e){return this._invoke(t,e)}})}function c(e){function t(r,o,a,i){var s=n(e[r],e,o);if("throw"!==s.type){var c=s.arg,u=c.value;return u&&"object"==typeof u&&g.call(u,"__await")?Promise.resolve(u.__await).then(function(e){t("next",e,a,i)},function(e){t("throw",e,a,i)}):Promise.resolve(u).then(function(e){c.value=e,a(c)},i)}i(s.arg)}function r(e,r){function n(){return new Promise(function(n,o){t(e,r,n,o)})}return o=o?o.then(n,n):n()}var o;this._invoke=r}function u(e,t,r){var o=L;return function(a,i){if(o===k)throw new Error("Generator is already running");if(o===A){if("throw"===a)throw i;return v()}for(r.method=a,r.arg=i;;){var s=r.delegate;if(s){var c=l(s,r);if(c){if(c===E)continue;return c}}if("next"===r.method)r.sent=r._sent=r.arg;else if("throw"===r.method){if(o===L)throw o=A,r.arg;r.dispatchException(r.arg)}else"return"===r.method&&r.abrupt("return",r.arg);o=k;var u=n(e,t,r);if("normal"===u.type){if(o=r.done?A:R,u.arg===E)continue;return{value:u.arg,done:r.done}}"throw"===u.type&&(o=A,r.method="throw",r.arg=u.arg)}}}function l(e,t){var r=e.iterator[t.method];if(r===m){if(t.delegate=null,"throw"===t.method){if(e.iterator.return&&(t.method="return",t.arg=m,l(e,t),"throw"===t.method))return E;t.method="throw",t.arg=new TypeError("The iterator does not provide a 'throw' method")}return E}var o=n(r,e.iterator,t.arg);if("throw"===o.type)return t.method="throw",t.arg=o.arg,t.delegate=null,E;var a=o.arg;return a?a.done?(t[e.resultName]=a.value,t.next=e.nextLoc,"return"!==t.method&&(t.method="next",t.arg=m),t.delegate=null,E):a:(t.method="throw",t.arg=new TypeError("iterator result is not an object"),t.delegate=null,E)}function d(e){var t={tryLoc:e[0]};1 in e&&(t.catchLoc=e[1]),2 in e&&(t.finallyLoc=e[2],t.afterLoc=e[3]),this.tryEntries.push(t)}function h(e){var t=e.completion||{};t.type="normal",delete t.arg,e.completion=t}function f(e){this.tryEntries=[{tryLoc:"root"}],e.forEach(d,this),this.reset(!0)}function p(e){if(e){var t=e[_];if(t)return t.call(e);if("function"==typeof e.next)return e;if(!isNaN(e.length)){var r=-1,n=function t(){for(;++r<e.length;)if(g.call(e,r))return t.value=e[r],t.done=!1,t;return t.value=m,t.done=!0,t};return n.next=n}}return{next:v}}function v(){return{value:m,done:!0}}var m,y=Object.prototype,g=y.hasOwnProperty,b="function"==typeof Symbol?Symbol:{},_=b.iterator||"@@iterator",x=b.asyncIterator||"@@asyncIterator",C=b.toStringTag||"@@toStringTag",N="object"==typeof e,w=t.regeneratorRuntime;if(w)return void(N&&(e.exports=w));w=t.regeneratorRuntime=N?e.exports:{},w.wrap=r;var L="suspendedStart",R="suspendedYield",k="executing",A="completed",E={},S={};S[_]=function(){return this};var j=Object.getPrototypeOf,P=j&&j(j(p([])));P&&P!==y&&g.call(P,_)&&(S=P);var O=i.prototype=o.prototype=Object.create(S);a.prototype=O.constructor=i,i.constructor=a,i[C]=a.displayName="GeneratorFunction",w.isGeneratorFunction=function(e){var t="function"==typeof e&&e.constructor;return!!t&&(t===a||"GeneratorFunction"===(t.displayName||t.name))},w.mark=function(e){return Object.setPrototypeOf?Object.setPrototypeOf(e,i):(e.__proto__=i,C in e||(e[C]="GeneratorFunction")),e.prototype=Object.create(O),e},w.awrap=function(e){return{__await:e}},s(c.prototype),c.prototype[x]=function(){return this},w.AsyncIterator=c,w.async=function(e,t,n,o){var a=new c(r(e,t,n,o));return w.isGeneratorFunction(t)?a:a.next().then(function(e){return e.done?e.value:a.next()})},s(O),O[C]="Generator",O[_]=function(){return this},O.toString=function(){return"[object Generator]"},w.keys=function(e){var t=[];for(var r in e)t.push(r);return t.reverse(),function r(){for(;t.length;){var n=t.pop();if(n in e)return r.value=n,r.done=!1,r}return r.done=!0,r}},w.values=p,f.prototype={constructor:f,reset:function(e){if(this.prev=0,this.next=0,this.sent=this._sent=m,this.done=!1,this.delegate=null,this.method="next",this.arg=m,this.tryEntries.forEach(h),!e)for(var t in this)"t"===t.charAt(0)&&g.call(this,t)&&!isNaN(+t.slice(1))&&(this[t]=m)},stop:function(){this.done=!0;var e=this.tryEntries[0],t=e.completion;if("throw"===t.type)throw t.arg;return this.rval},dispatchException:function(e){function t(t,n){return a.type="throw",a.arg=e,r.next=t,n&&(r.method="next",r.arg=m),!!n}if(this.done)throw e;for(var r=this,n=this.tryEntries.length-1;n>=0;--n){var o=this.tryEntries[n],a=o.completion;if("root"===o.tryLoc)return t("end");if(o.tryLoc<=this.prev){var i=g.call(o,"catchLoc"),s=g.call(o,"finallyLoc");if(i&&s){if(this.prev<o.catchLoc)return t(o.catchLoc,!0);if(this.prev<o.finallyLoc)return t(o.finallyLoc)}else if(i){if(this.prev<o.catchLoc)return t(o.catchLoc,!0)}else{if(!s)throw new Error("try statement without catch or finally");if(this.prev<o.finallyLoc)return t(o.finallyLoc)}}}},abrupt:function(e,t){for(var r=this.tryEntries.length-1;r>=0;--r){var n=this.tryEntries[r];if(n.tryLoc<=this.prev&&g.call(n,"finallyLoc")&&this.prev<n.finallyLoc){var o=n;break}}o&&("break"===e||"continue"===e)&&o.tryLoc<=t&&t<=o.finallyLoc&&(o=null);var a=o?o.completion:{};return a.type=e,a.arg=t,o?(this.method="next",this.next=o.finallyLoc,E):this.complete(a)},complete:function(e,t){if("throw"===e.type)throw e.arg;return"break"===e.type||"continue"===e.type?this.next=e.arg:"return"===e.type?(this.rval=this.arg=e.arg,this.method="return",this.next="end"):"normal"===e.type&&t&&(this.next=t),E},finish:function(e){for(var t=this.tryEntries.length-1;t>=0;--t){var r=this.tryEntries[t];if(r.finallyLoc===e)return this.complete(r.completion,r.afterLoc),h(r),E}},catch:function(e){for(var t=this.tryEntries.length-1;t>=0;--t){var r=this.tryEntries[t];if(r.tryLoc===e){var n=r.completion;if("throw"===n.type){var o=n.arg;h(r)}return o}}throw new Error("illegal catch attempt")},delegateYield:function(e,t,r){return this.delegate={iterator:p(e),resultName:t,nextLoc:r},"next"===this.method&&(this.arg=m),E}}}(function(){return this}()||Function("return this")())},390:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{attrs:{id:"refund"}},[r("header2",{attrs:{title:"申请退款"}}),e._v(" "),r("div",{staticClass:"shopName itemName"},[e._v(e._s(e.couponAllInfo.couponName))]),e._v(" "),r("div",{staticClass:"coupons"},[r("p",{staticClass:"item"},[r("label",{attrs:{for:"allSelected"}},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.allSelected,expression:"allSelected"}],attrs:{type:"checkbox",id:"allSelected"},domProps:{checked:Array.isArray(e.allSelected)?e._i(e.allSelected,null)>-1:e.allSelected},on:{change:[function(t){var r=e.allSelected,n=t.target,o=!!n.checked;if(Array.isArray(r)){var a=e._i(r,null);n.checked?a<0&&(e.allSelected=r.concat([null])):a>-1&&(e.allSelected=r.slice(0,a).concat(r.slice(a+1)))}else e.allSelected=o},e.changeSelected]}}),e._v("全选")])]),e._v(" "),e._l(e.userCouponNbrList,function(t){return r("p",{directives:[{name:"show",rawName:"v-show",value:"1"===t.status,expression:"item.status==='1'"}],staticClass:"item"},[r("label",{attrs:{for:t.index}},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.selectCouponNbrArr,expression:"selectCouponNbrArr"}],attrs:{id:t.index,type:"checkbox"},domProps:{value:t,checked:Array.isArray(e.selectCouponNbrArr)?e._i(e.selectCouponNbrArr,t)>-1:e.selectCouponNbrArr},on:{change:function(r){var n=e.selectCouponNbrArr,o=r.target,a=!!o.checked;if(Array.isArray(n)){var i=t,s=e._i(n,i);o.checked?s<0&&(e.selectCouponNbrArr=n.concat([i])):s>-1&&(e.selectCouponNbrArr=n.slice(0,s).concat(n.slice(s+1)))}else e.selectCouponNbrArr=a}}}),e._v("\n                "+e._s(e.couponTypeText)+e._s(t.userCouponNbr)+"\n                "),r("span",{staticClass:"function"},[e._v(e._s(e.couponAllInfo.function))])])])})],2),e._v(" "),r("div",{staticClass:"item"},[e._v("退款金额："),r("div",{staticClass:"refundMoney"},[e._v("￥"+e._s(e.compuRefundMoney))])]),e._v(" "),r("div",{staticClass:"itemName"},[e._v("退款说明")]),e._v(" "),r("div",{staticClass:"item"},[e._v("3-10个工作日内退款至原支付账户")]),e._v(" "),r("div",{staticClass:"itemName"},[e._v("服务电话")]),e._v(" "),r("div",{staticClass:"item"},[r("a",{attrs:{href:"tel:"+e.shopDecoration.tel}},[e._v(e._s(e.shopDecoration.tel))])]),e._v(" "),r("div",{staticClass:"itemName"},[e._v("退款原因")]),e._v(" "),r("div",{staticClass:"refundReason"},[r("label",{staticClass:"item",attrs:{for:"one"}},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.refundReason,expression:"refundReason"}],attrs:{type:"radio",id:"one",value:"1"},domProps:{checked:e._q(e.refundReason,"1")},on:{change:function(t){e.refundReason="1"}}}),e._v("\n        计划有变，没时间消费")]),e._v(" "),r("label",{staticClass:"item",attrs:{for:"two"}},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.refundReason,expression:"refundReason"}],attrs:{type:"radio",id:"two",value:"2"},domProps:{checked:e._q(e.refundReason,"2")},on:{change:function(t){e.refundReason="2"}}}),e._v("\n        后悔了不想买")]),e._v(" "),r("label",{staticClass:"item",attrs:{for:"three"}},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.refundReason,expression:"refundReason"}],attrs:{type:"radio",id:"three",value:"3"},domProps:{checked:e._q(e.refundReason,"3")},on:{change:function(t){e.refundReason="3"}}}),e._v("\n        买多了/不想买了")]),e._v(" "),r("label",{staticClass:"item",attrs:{for:"four"}},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.refundReason,expression:"refundReason"}],attrs:{type:"radio",id:"four",value:"4"},domProps:{checked:e._q(e.refundReason,"4")},on:{change:function(t){e.refundReason="4"}}}),e._v("\n        商家停业/装修/转让")]),e._v(" "),r("label",{staticClass:"item",attrs:{for:"five"}},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.refundReason,expression:"refundReason"}],attrs:{type:"radio",id:"five",value:"5"},domProps:{checked:e._q(e.refundReason,"5")},on:{change:function(t){e.refundReason="5"}}}),e._v("\n        商家不承认")]),e._v(" "),r("label",{staticClass:"item",attrs:{for:"six"}},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.refundReason,expression:"refundReason"}],attrs:{type:"radio",id:"six",value:"6"},domProps:{checked:e._q(e.refundReason,"6")},on:{change:function(t){e.refundReason="6"}}}),e._v("\n        商家说可以现金/刷卡来享受折扣")])]),e._v(" "),r("div",{staticClass:"otherReason"},[r("textarea",{directives:[{name:"model",rawName:"v-model",value:e.message,expression:"message"}],attrs:{placeholder:"更多问题？请吐槽！"},domProps:{value:e.message},on:{input:function(t){t.target.composing||(e.message=t.target.value)}}})]),e._v(" "),r("div",{staticClass:"refundCoupon"},[r("span",{staticClass:"tips"},[r("icon",{staticClass:"safe_warn",attrs:{type:"safe_warn"}}),e._v("退款申请一经提交后不可撤销")],1),e._v(" "),r("div",{staticClass:"refundCouponBtn",on:{click:e.refundCoupon}},[e._v("申请退款")])])],1)},staticRenderFns:[]}}});
//# sourceMappingURL=5.daa0b1f659ed61ae1942.js.map