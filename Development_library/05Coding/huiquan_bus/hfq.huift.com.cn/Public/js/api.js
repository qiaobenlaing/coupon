// Initialize app
var myApp = new Framework7();

// If we need to use custom DOM library, let's save it to $$ variable:
var $$ = Framework7.$;

// Add view
var mainView = myApp.addView('.view-main', {
    // Because we want to use dynamic navbar, we need to enable it for this view:
    dynamicNavbar: true
});

// Now we need to run the code that will be executed only for About page.
// For this case we need to add event listener for "pageInit" event

// Option 1. Using one 'pageInit' event handler for all pages (recommended way):
$$(document).on('pageInit', function (e) {
    // Get page data from event data
    var page = e.detail.page;
})
// 命名空间
var sz;
if(!sz) sz = {};
/** status SUCC */
sz.STATUS_SUCC = 0;
/** status FAIL */
sz.STATUS_FAIL = -1;
window.InfiniteScroll = function(params) {
    // App
    var app = this;
    //加载flag
	var loading = false;
	
    // default params
    app.params = {
    		'infSelector' : '.infinite-scroll', // 无限下拉容器（列表）
    		'infRowSelector' : '.infinite-scroll .row', // 无限下拉列表的一行
    		'infPreLoaderSelector' : '.infinite-scroll-preloader',  // 无限下拉加载提示
    		'listParamFormSelector' : '#frm_search',  // 无限下拉容器filter表单
    		'listParamFormPageSelector' : '#frm_ele_page',  // 无限下拉容器filter表单中的页数
    		'maxItems' : 200 // 最多可加载的条目
    };
    // Extend defaults with parameters
    for (var param in params) {
        app.params[param] = params[param];
    }

	// 上次加载的序号
	var lastIndex = $(app.params.infRowSelector).length;
	// 每次加载添加多少条目
	//var itemsPerLoad = 2;
	
	// $$('#xxxId').attr('href', 'http://asdfdf.com');
	$$(app.params.infSelector).on('infinite', function () {
		if (loading) return;
		loading = true;
		var searchFrm = $(app.params.listParamFormSelector);
		$(app.params.listParamFormPageSelector).val(parseInt($(app.params.listParamFormPageSelector).val()) + 1);
		$.get(searchFrm.attr('action'), $(app.params.listParamFormSelector).serialize(), function(data) {
			//console.log($(app.params.listParamFormSelector).serialize());
			loading = false;
			if (lastIndex >= app.params.maxItems) { // 超过最大限度不再加载 
		      // 加载完毕，则注销无限加载事件，以防不必要的加载 
		      myApp.detachInfiniteScroll($(app.params.infSelector));
		      // 删除加载提示符 
		      $(app.params.infPreLoaderSelector).remove();
		      return;
		    } else {
		    	var html = data.html;
                if (html != null) {
                    if (html != '') {
                        $(app.params.infSelector+' ul').append(html);
                        // 更新最后加载的序号
                        lastIndex = $(app.params.infRowSelector).length;
                    } else {
                        myApp.detachInfiniteScroll($(app.params.infSelector));
                        // 删除加载提示符
                        $(app.params.infPreLoaderSelector).remove();
                    }
                } else {
                    myApp.detachInfiniteScroll($(app.params.infSelector));
                    // 删除加载提示符
                    $(app.params.infPreLoaderSelector).remove();
                }
		    }
		});
			
	});
}

sz.checkNum = function(defaultSelector){
    if(!defaultSelector)
        defaultSelector = '.checkNum';
    $(defaultSelector).keypress(function(event) {
        var keyCode = event.which;
        if (keyCode == 46 || (keyCode >= 48 && keyCode <=57))
            return true;
        else
            return false;
    }).focus(function() {
        this.style.imeMode='disabled';
    });
};

// 点赞或取消点赞
$('.zan-icon-wrap').on('click', function(){
    var span = $(this);
    var userCode = span.data('userCode');
    if(userCode == '') {
        login_modal();
        return false;
    }
    var productId = span.data('productId');
    var isZan = span.attr('data-is-zan');
    span.parent().attr('disabled', 'disabled');
    $.post(
        '/Api/Browser/zan',
        {'userCode' : userCode, 'productId' : productId, 'isZan' : isZan},
        function(resp) {
            if(resp.status == sz.STATUS_SUCC) {
                if(isZan == '1') { // 用户已赞，则取消赞
                    span.removeClass('zan-icon').addClass('nzan-icon');
                    span.attr('data-is-zan', 0).text(resp.data.zanCount);
                } else { // 用户未赞，则点赞
                    span.removeClass('nzan-icon').addClass('zan-icon');
                    span.attr('data-is-zan', 1).text(resp.data.zanCount);
                }
            } else {
            	
            }
            span.parent().attr('disabled', false);
        }
    )
    
});

// 显示提醒用户登录框
function login_modal() {
    myApp.modal({
        title:  '提醒',
        text: '你还没有登录,请先登录！',
        afterText: '<div style="line-height: 2;margin-bottom: -15px;border-bottom: 1px solid darkgray;"><a href="hs://login" class="external" style="font-size: 17px;">确定</a></div>',
        buttons: [
            {
                text: '取消'
            }
        ]
    })
}
// 显示开通免验码支付的提示
function showFreeValPayTip() {
    myApp.modal({
        text: '免验证码支付更加方便',
        buttons: [
            {
                text: '暂时不用',
                onClick: rejectOpenFreeValPay
            },
            {
                text: '<a href="hq://setFreeValPay" class="external">赶紧设置</a>'
            }
        ]
    });
};
// 点击暂时不用
function rejectOpenFreeValPay(){
    var userCode = $('#user-code').val();
    $.post('/Api/Browser/rejectFreeVal', {'userCode' : userCode}, function(resp){
    });
};



