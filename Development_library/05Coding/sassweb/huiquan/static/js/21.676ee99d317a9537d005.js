webpackJsonp([21],{221:function(t,e,i){function n(t){i(350)}var s=i(0)(i(319),i(377),n,null,null);t.exports=s.exports},319:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=i(13),s=i.n(n),a=i(6);e.default={data:function(){return{cityList:[],isActive:!0}},methods:{goBack:function(){this.$router.go(-1)},changeCity:function(t){this.$store.dispatch("setLocalCity",t.currentTarget.innerText),this.$store.dispatch("setLocation",t.currentTarget.innerText),this.$store.state.isChooseCity=!0,this.$router.go(-1)}},computed:s()({},i.i(a.b)(["localCity"])),beforeCreate:function(){},created:function(){var t=this;this.$store.dispatch("getOpenCity").then(function(e){t.cityList=e.result})}}},350:function(t,e){},377:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{attrs:{id:"indexCity"}},[i("header",{staticClass:"header"},[i("span",{staticClass:"header-item icon-back",on:{click:t.goBack}}),t._v(" "),i("span",{staticClass:"header-item "},[t._v("选择城市")]),t._v(" "),i("span",{staticClass:"header-item "})]),t._v(" "),t._l(t.cityList,function(e){return i("div",{key:e.name,staticClass:"city"},[i("div",{staticClass:"changeCity",class:[e.name===t.localCity?"activeCity":""],on:{click:function(e){t.changeCity(e)}}},[t._v(t._s(e.name))])])})],2)},staticRenderFns:[]}}});
//# sourceMappingURL=21.676ee99d317a9537d005.js.map