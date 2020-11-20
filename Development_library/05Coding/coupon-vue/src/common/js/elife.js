
var loginCallback = $.Callbacks();

function Hybrid () {
    this.ua = navigator.userAgent;
};

//判断是否是融e联 ios
Hybrid.prototype.isRELIphone = function() {
    var me = this;
    if (me.ua.indexOf('ICBCiPhoneBS') > -1) {
        return true;
    }
    return false;
};



//  判断是否是融e联 android
Hybrid.prototype.isRELAndroid = function() {
    var me = this;
    if (me.ua.indexOf('ICBCAndroidBS')>-1 ) { 
        return true;
    }
    return false;
};
/**
 * 检测当前浏览器是否为Android(Chrome)
 */
 Hybrid.prototype.isAndroid = function() {
    var me = this;
    if (me.ua.indexOf('Android')>-1) {
        return true;
    }
    return false;
};

/**
 * 检测当前浏览器是否为iPhone(Safari)
 */
 Hybrid.prototype.isIPhone = function () {
    var me = this;
    if (me.ua.indexOf('iPhone') > -1) {
        return true;
    }
    return false;
};

Hybrid.prototype.connectWebViewJavascriptBridge = function(callback) {
    if(window.WebViewJavascriptBridge){
        return callback(WebViewJavascriptBridge);
    }else{
        document.addEventListener('WebViewJavascriptBridgeReady', function() {
            callback(WebViewJavascriptBridge);
        }, false);
    }
    if(window.WVJBCallbacks) { return window.WVJBCallbacks.push(callback); }
    window.WVJBCallbacks = [callback];
    var WVJBIframe=document.createElement('iFrame');
    WVJBIframe.style.display='none';
    WVJBIframe.src='wvjbscheme://__BRIDGE_LOADED__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function(){document.documentElement.removeChild(WVJBIframe)});    
};

/*  function connectWebViewJavascriptBridge(callback) {


}*/




// 调起端上的分享
Hybrid.prototype.share = function (shareInfo) {
    var me = this;
    if (me.isAndroid()) {
        Myutils.share(shareInfo);
    } else {
        //（ios需要将参数base64加密）
        window.ICBCBridge.callHandler("Myutils.share", me.base64Encode(shareInfo));
    } 
};

//第三方显示或隐藏tabbar isShow:true显示tabbar 反之隐藏
Hybrid.prototype.showToolBar = function(isShow){
    var me = this;
    //如果是安卓
    if(me.isAndroid()){
        Myutils.showToolBar(isShow);
    }else{  //ios
        window.ICBCBridge.callHandler("Myutils.showToolBar",isShow)
    }
}

//第三方返回上一级页面
Hybrid.prototype.back = function(){
    var me = this;
    //如果是安卓
    if(me.isAndroid()){
        Myutils.back();
    }else{  //ios
        window.ICBCBridge.callHandler("Myutils.back");
    }
}

//第三方开启定位
Hybrid.prototype.openGPS = function(){
    var me = this;
    //如果是安卓
    if(me.isAndroid()){
        Myutils.openGPS();
    }else{  //ios
        window.ICBCBridge.callHandler("Myutils.openGPS")
    }
}

//第三方关闭定位
Hybrid.prototype.closeGPS = function(){
    var me = this;
    //如果是安卓
    if(me.isAndroid()){
        Myutils.closeGPS();
    }else{  //ios
        window.ICBCBridge.callHandler("Myutils.closeGPS")
    }
}

//第三方获取定位  参数是回调的函数名的字符串
Hybrid.prototype.getMyLocation = function(getGps){
    var me = this;
    //如果是安卓
    if(me.isAndroid()){
        Myutils.getMyLocation(getGps);
    }else{  //ios
        window.ICBCBridge.callHandler("Myutils.getMyLocation",getGps)
    }
}

//第三方打电话功能  参数是电话号
Hybrid.prototype.callPhoneNumber = function(tel){
    var me = this;
    //如果是安卓
    if(me.isAndroid()){
        Myutils.callPhoneNumber(tel);
    }else{  //ios
        window.ICBCBridge.callHandler("Myutils.callPhoneNumber",tel)
    }
}

//跳转id方法
Hybrid.prototype.getActivityInfo = function(actId){
    var me = this;
    //如果是安卓
    if(me.isAndroid()){
        Myutils.getActivityInfo(actId);
    }else{  //ios
        window.ICBCBridge.callHandler("Myutils.getActivityInfo",actId);
    }
}

//跳转url方法
Hybrid.prototype.ApplicationCard = function(url){
    var me = this;
    //如果是安卓
    if(me.isAndroid()){
        Myutils.ApplicationCard(url);
    }else{  //ios
        window.ICBCBridge.callHandler("Myutils.ApplicationCard",url);
    }
}

//中途登录
Hybrid.prototype.merLogin = function (callback) {
    var me = this;  
    loginCallback.add(callback);
    //如果是安卓
    if (me.isAndroid()) {
        Myutils.open("callLogin");
    } else if (me.isIPhone()) { //ios        
        window.ICBCBridge.callHandler("Myutils.open", "callLogin")
    }
    //else {
    //    var data = "pSs6sElROJtRtloIy8UMGDzS2AmQ8ag8qRqoCs7wK8UvAb26eR4Nm5tdKJGgkZiDUND4VadZ9yFZlogZGo2u+jIR/dwOkV/cNg1PFgqbqkXTQUl4G3huFQvqDLLBb2DQRgzwOUMEgMbWntHQ+DAdVIvTt72AtoNXI0tjuelsbmy2lHMF5dt5XMD1v+eNxeIP01Jr71tH2uBlWePD9oYt7CrpLRDIFj9Rf/dj258d8Ejo18obBDighlLNOaIbuce7yH+dLq5YJLSbmHKWUZJ/txiuovIbm70zROSbueWeevM=";
    //    callLogin(data);
    //}
    
}

Hybrid.prototype.decryptLoginCallback = function (data) {
    var _res = data["Res"][0].Res;
    if (_res == 1) {
        var userId = data["CustInfo"][0].ID;
        var mobile = data["CustInfo"][0].Mobile;
        var custId = data["CustInfo"][0].CustNo;
        $.cookie(WXCOOKIEPRE + "txtMobile", mobile);
        $.cookie(WXCOOKIEPRE + "userId", userId);
        $.cookie(WXCOOKIEPRE + "icbceCustId", custId);
        $.cookie(WXCOOKIEPRE + "storeId", 311497);
        $.cookie(WXCOOKIEPRE + "mobile", mobile);
        $.cookie(WXCOOKIEPRE + "GLOBALUSERID", userId, { path: "/" });
        $.cookie(WXCOOKIEPRE + "GLOBALMOBILE", mobile, { path: "/" });
        $.cookie(WXCOOKIEPRE + "GLOBALSTOREID", 311497, { path: "/" });        
        loginCallback.fire();
    } else {
        alert("中间登录失败");
    }
}
//中途登录的回调
Hybrid.prototype.callLogin = function (param) {
    var me = this;
    //解密参数
    $.ajax({
        type: "post",
        url: "/laomi/IcbcLogin",
        data: { data: param },
        success: me.decryptLoginCallback
    });
}

Hybrid.prototype.decryptLoginCallback = function (data) {
    var _res = data["Res"][0].Res;
    if (_res == 1) {
        var userId = data["CustInfo"][0].ID;
        var mobile = data["CustInfo"][0].Mobile;
        var custId = data["CustInfo"][0].CustNo;
        $.cookie(WXCOOKIEPRE + "txtMobile", mobile);
        $.cookie(WXCOOKIEPRE + "userId", userId);
        $.cookie(WXCOOKIEPRE + "icbceCustId", custId);
        $.cookie(WXCOOKIEPRE + "storeId", 311497);
        $.cookie(WXCOOKIEPRE + "mobile", mobile);
        $.cookie(WXCOOKIEPRE + "GLOBALUSERID", userId, { path: "/" });
        $.cookie(WXCOOKIEPRE + "GLOBALMOBILE", mobile, { path: "/" });
        $.cookie(WXCOOKIEPRE + "GLOBALSTOREID", 311497, { path: "/" });
        
        loginCallback.fire();
    } else {
        alert("中间登录失败");
    }
}
//获取定位信息的回调
Hybrid.prototype.getGps = function (param) {

}
Hybrid.prototype.goBack1 = function () {
    var me = this;
    if (me.isRELIphone() || me.isRELAndroid()) {
        me.back();
    } else {
        window.history.go(-1);
    }
}

Hybrid.prototype.initPage = function () {
    var me = this;
    if (me.ua.indexOf('elife_moblie') < 0) {
        $('.footer_absolute_box').show();
        //隐藏分享按钮
        $('#btnShare').hide();
    }

    me.connectWebViewJavascriptBridge(function(bridge) {
        //bridge.init();
        //bridge.registerHandler("callback",callback)
        bridge.registerHandler("callback");
    });

    var ICBCBridge = {
        callHandler: function(name, params, callback) {
            me.connectWebViewJavascriptBridge(function(bridge) {
                bridge.callHandler(name, params, callback);
            });
        },
        send: function(params, callback) {
            me.connectWebViewJavascriptBridge(function(bridge) {
                bridge.send(params, callback);
            });
        }
    };
    window.ICBCBridge = ICBCBridge;
}

//跳转下载APP
Hybrid.prototype.openApp = function() {
    var me = this;
    var timer;
    var downUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.icbc.elife";

    //判断是否是融e联
    if (me.ua.indexOf('ICBCAndroidBS') > -1) {
        window.location.href = 'com.icbc.elife://elife'; //跳到 安卓 url scheme
        timer = window.setTimeout(function () {
            window.location.href = downUrl;
        }, 1000)
    } else if (me.ua.indexOf('ICBCiPhoneBS') > -1) {
        window.location.href = 'com.icbc.elife://';//跳到ios  url scheme地址
        timer = window.setTimeout(function () {
            window.location.href = downUrl;
        }, 1000)
    } else {
        window.location.href = downUrl;//应用宝地址  
    }
}