webpackJsonp([10],{232:function(t,e,r){function o(t){r(357)}var n=r(0)(r(327),r(384),o,null,null);t.exports=n.exports},288:function(t,e,r){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={props:["title"],methods:{goBack:function(){this.$router.go(-1)}}}},289:function(t,e){},290:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{attrs:{id:"header2"}},[r("header",{staticClass:"header"},[r("span",{staticClass:"header-item icon-back",on:{click:t.goBack}}),t._v(" "),r("span",{staticClass:"header-item"},[t._v(t._s(t.title))])])])},staticRenderFns:[]}},291:function(t,e,r){function o(t){r(289)}var n=r(0)(r(288),r(290),o,null,null);t.exports=n.exports},327:function(t,e,r){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=r(13),n=r.n(o),s=r(6),i=r(291),a=r.n(i);e.default={data:function(){return{count:1,isSucess:Boolean,currentOrderInfo:{countMyReceived:0,logoUrl:"",payPrice:0},batchCouponCode:this.$route.params.batchCouponCode}},computed:n()({},r.i(s.b)(["global","CouponListInfo","isBLR","isLogin","userCode"]),{totalPrice:function(){return(this.count*this.currentOrderInfo.payPrice).toFixed(2)}}),watch:{count:function(){void 0===this.currentOrderInfo.countMyReceived&&(this.currentOrderInfo.countMyReceived=0),this.count=Math.round(this.count),(this.count>this.currentOrderInfo.nbrPerPerson-this.currentOrderInfo.countMyReceived||this.count>this.currentOrderInfo.nbrPerPerson)&&(this.count=this.currentOrderInfo.nbrPerPerson-this.currentOrderInfo.countMyReceived,this.$vux.toast.text("您已到达领取限额","middle"))}},methods:{goBack:function(){this.$router.go(-1)},add:function(){this.count++},minus:function(){this.count<2||this.count--},showIndexCouponDetails:function(){var t=this;this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"getBachCouponInfo","params":{"batchCouponCode":"'+this.batchCouponCode+'"}}',contentType:"application/json",dataType:"json"}).then(function(e){t.currentOrderInfo=e.data.result.BatchCouponInfo,t.shopDecoration=e.data.result.ShopDecoration,t.currentOrderInfo.logoUrl=e.data.result.ShopDecoration.imgUrl,t.getUserCouponNbr()})},getUserCouponNbr:function(){var t=this;this.axios({method:"post",url:this.global.hftcomShop,data:'{"id":19,"jsonrpc":"2.0","method":"getUserCouponNbr","params":{"userCode":"'+this.userCode+'","batchCouponCode":"'+this.batchCouponCode+'"}}',contentType:"application/json",dataType:"json"}).then(function(e){void 0===t.currentOrderInfo.countMyReceived&&(t.currentOrderInfo.countMyReceived=0),e.data.result===[]||void 0===e.data.result?e.data.result=[]:e.data.result.filter(function(e,r,o){"1"!==e.status&&"2"!==e.status||t.currentOrderInfo.countMyReceived++})})},submitOrder:function(){var t=this;"0"!==this.currentOrderInfo.nbrPerPerson&&this.currentOrderInfo.nbrPerPerson-this.currentOrderInfo.countMyReceived<1?this.$vux.toast.text("您已经购买了"+this.currentOrderInfo.countMyReceived+"张，无法再次购买","middle"):this.axios({method:"post",url:this.global.hftcomClient,data:'{"id":19,"jsonrpc":"2.0","method":"addCouponOrder","params":{"userCode":"'+this.userCode+'","shopCode":"'+this.currentOrderInfo.shopCode+'","batchCouponCode":"'+this.currentOrderInfo.batchCouponCode+'","couponNbr":'+this.count+',"platBonus":0,"shopBonus":0}}'}).then(function(e){switch(e.data.result.code){case 5e4:t.order=e.data.result,t.order.batchCouponCode=t.currentOrderInfo.batchCouponCode,t.order.payType="BUYCOUPON",console.log(e.data.result),t.$router.replace({path:"/gotopay",query:{order:t.order}});break;case"80238":t.$vux.toast.text("您已经达到购买上限","middle");break;case"80221":t.$vux.toast.text("此优惠券已抢光，无法购买","middle");break;default:console.log(e.data.result.code)}})}},created:function(){this.showIndexCouponDetails()},components:{header2:a.a}}},357:function(t,e){},384:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{attrs:{id:"submitorder"}},[r("header2",{attrs:{title:"提交订单"}}),t._v(" "),r("div",{staticClass:"order-item order-item-more"},[r("div",{staticClass:"img"},[r("img",{attrs:{src:t.global.hftcom+t.currentOrderInfo.logoUrl,alt:""}})]),t._v(" "),r("div",{staticClass:"orderName"},[t._v(t._s(t.currentOrderInfo.couponName)+t._s(t.currentOrderInfo.function)+t._s(t.currentOrderInfo.remark))]),t._v(" "),r("div",{staticClass:"payPrice"},[t._v("￥"+t._s(t.currentOrderInfo.payPrice)+"元")])]),t._v(" "),r("div",{staticClass:"order-item"},[r("label",{attrs:{for:"count"}},[t._v("数量:")]),t._v(" "),r("div",{staticClass:"count-inp"},[r("span",{directives:[{name:"show",rawName:"v-show",value:"0"!==t.currentOrderInfo.nbrPerPerson,expression:"currentOrderInfo.nbrPerPerson!=='0'"}],staticClass:"nbrPerPerson"},[t._v("每个用户限领"+t._s(t.currentOrderInfo.nbrPerPerson)+"张")]),t._v(" "),r("div",{staticClass:"minus",class:{"minus-disabled":t.count<2},on:{click:t.minus}},[t._v("-")]),t._v(" "),r("div",{staticClass:"inp"},[r("input",{directives:[{name:"model",rawName:"v-model.number",value:t.count,expression:"count",modifiers:{number:!0}}],attrs:{id:"count",value:"1"},domProps:{value:t.count},on:{input:function(e){e.target.composing||(t.count=t._n(e.target.value))},blur:function(e){t.$forceUpdate()}}})]),t._v(" "),r("div",{staticClass:"add",on:{click:t.add}},[t._v("+")])])]),t._v(" "),r("div",{staticClass:"order-item"},[r("label",{attrs:{for:"price"}},[t._v("实付款:")]),t._v(" "),r("span",{staticClass:"totalPrice"},[t._v(t._s(t.totalPrice)+"元")])]),t._v(" "),r("div",{staticClass:"pay",on:{click:t.submitOrder}},[t._v("提交订单")])],1)},staticRenderFns:[]}}});
//# sourceMappingURL=10.5a661ee8b9058f2f9a79.js.map