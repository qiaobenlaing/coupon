webpackJsonp([22],{225:function(t,e,i){function n(t){i(360)}var s=i(0)(i(320),i(388),n,null,null);t.exports=s.exports},320:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=i(10),s=i.n(n),a=i(6);e.default={data:function(){return{cityList:[],isActive:!0}},methods:{goBack:function(){this.$router.go(-1)},changeCity:function(t){this.$store.dispatch("setLocalCity",t.currentTarget.innerText),this.$store.dispatch("setLocation",t.currentTarget.innerText),this.$store.state.isChooseCity=!0,this.$router.go(-1)}},computed:s()({},i.i(a.b)(["localCity"])),beforeCreate:function(){},created:function(){var t=this;this.$store.dispatch("getOpenCity").then(function(e){t.cityList=e.result})}}},360:function(t,e){},388:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{attrs:{id:"indexCity"}},[i("header",{staticClass:"header"},[i("span",{staticClass:"header-item icon-back",on:{click:t.goBack}}),t._v(" "),i("span",{staticClass:"header-item "},[t._v("选择城市")]),t._v(" "),i("span",{staticClass:"header-item "})]),t._v(" "),t._l(t.cityList,function(e){return i("div",{key:e.name,staticClass:"city"},[i("div",{staticClass:"changeCity",class:[e.name===t.localCity?"activeCity":""],on:{click:function(e){t.changeCity(e)}}},[t._v(t._s(e.name))])])})],2)},staticRenderFns:[]}}});
//# sourceMappingURL=22.62ac9857523994c7ec11.js.map