/* 这个js的主要功能就是1.动态添加权限菜单2.给主页面上显示的固定模块添加样式 */
/*easyui样式初始化*/

/*给所有的加载添加onLoadError统一有错误处理*/
var easyuiErrorFunction = function(XMLHttpRequest) {
    console.log(XMLHttpRequest);
    var statusCode=XMLHttpRequest.status;
    if(statusCode=='404'){
    	$g.showMsg("操作失败" ,"您访问的功能并不存在！");
    }else if(statusCode=='500'){
    	$g.showMsg("操作失败" ,"系统操作出错，请通知系统管理员或稍后再试！");
    }
  
};
$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
$.fn.form.defaults.onLoadError = easyuiErrorFunction;

$.fn.tabs.defaults.tabHeight = 32; //tab标签条高度
$.fn.linkbutton.defaults.height = 32; //按钮高度
$.fn.menu.defaults.itemHeight = 28; //menu高度
$.fn.menu.defaults.noline = true;
$.fn.validatebox.defaults.height = 32;
$.fn.textbox.defaults.height = 32;
$.fn.textbox.defaults.iconWidth = 24;
$.fn.datebox.defaults.height = 32;
$.fn.numberspinner.defaults.height = 32;
$.fn.timespinner.defaults.height = 32;
$.fn.numberbox.defaults.height = 32;
$.fn.combobox.defaults.height = 32;
$.fn.passwordbox.defaults.height = 32;
$.fn.filebox.defaults.height = 32;
$.fn.progressbar.defaults.height = 18; //进度条

$.fn.extend({
	/*加载左侧菜单栏*/
	 createMenuTree:function(url){
		 //当前的dom对象
		 var dom = this;
		 //总HTML
		 var html1 = '<ul class="narrow-one-ul" style="display:none;">';//窄菜单
		 var html2 = '<ul class="out-one-ul" >';//宽菜单
		// 菜单的HTML
		var menuHtml;
		// 所有菜单的HTML
		var menusHtml;
		$.post(url,function(data){
			var menuData=JSON.parse(data);
			for(var i=0; i<menuData.length;i++){
				var parentMenu = menuData[i];
				var parentMenuName = parentMenu.text;
				var parentMenuIcon = parentMenu.iconCls;
				// var parentMenuIcon = "fa fa-star";
				var childrenMenu = parentMenu.children;
				menuHtml = '';
				for(var j=0;j<childrenMenu.length;j++){
					var _menu=childrenMenu[j];
					if(_menu.iconCls==null || _menu.iconCls=="" || _menu.iconCls==undefined){
						_menu.iconCls = 'icon-tOpen';
					}
					//二级菜单元素
					menuHtml+=
						'<li class="out-two-li" data-url="'+_menu.authUrl+'">'+
						'<span class="'+_menu.iconCls+'" id="out-two-left-icon"></span>'+
						'<span class="out-two-title">'+ _menu.text +'</span>'+
						'<span class="normal"></span>'+
						'</li>';
					_menu = null;
				}
				menusHtml = 0 === menuHtml.length ? '' : menuHtml;
				//一级菜单元素（窄菜单）
				if(parentMenuIcon==null || parentMenuIcon=="" || parentMenuIcon==undefined ){
					parentMenuIcon = 'icon-tCut';
				}
				html1 +='<li class="narrow-one-li" id="narrow-one-li'+i+'">'+
					'<div class="narrow-one-div" style="width:245px;">'+
					'<span class="'+parentMenuIcon+'" id="narrow-one-left-icon"></span>'+
					'<span class="narrow-one-title">' + parentMenuName + '</span>'+
					'</div>'+
					'<ul class="narrow-two-ul" style="display:none;width:200px;">'+ menusHtml +'</ul>'+
					'</li>';
				//一级菜单元素（宽菜单）
				html2 +=
					'<li class="out-one-li" id="out-one-li'+i+'">'+
					'<div class="out-one-div">'+
					'<span class="'+parentMenuIcon+'" id="out-one-left-icon"></span>'+
					'<span class="out-one-title">' + parentMenuName + '</span>'+
					'<span class="fa fa-angle-down" id="out-one-right-icon"></span>'+
					'</div>'+
					'<ul class="out-two-ul" style="display:none">'+ menusHtml +'</ul>'+
					'</li>';
				parentMenu = null;
			}
			html1 += '</ul>';
			html2 += '</ul>';
			dom.html(html1+html2);
			$(".narrow-two-ul li .out-two-title").attr("class","narrow-two-title");
			//首次加载只让第一个div展开
			var firstOneLi=dom.find("#out-one-li0");
			firstOneLi.children("div").children("#out-one-right-icon").attr("class","fa fa-angle-up");
			firstOneLi.children(".out-two-ul").slideDown();
			
			var allOutTwoLi = dom.find('.out-two-li');
			/* 绑定一级菜单项的点击事件 */
			$('.out-one-div').on("click",oneDivClick)
			function oneDivClick() {
				var $div = $(this);
				var $span=$div.children(".fa-angle-up");
				//点击某个一级菜单时，其他一级菜单收拢
				$div.parent(".out-one-li").siblings().children(".out-one-div").next().slideUp();
				$div.parent(".out-one-li").siblings().children(".out-one-div").children("#out-one-right-icon").attr("class","fa fa-angle-down")
			
				if($span.length) {
					$span.attr('class','fa fa-angle-down');
					$div.next().slideUp();
				} else {
					$span=$div.children(".fa-angle-down");
					$span.attr('class','fa fa-angle-up');
					$div.next().slideDown();
				}
			};
			//当鼠标聚焦在子菜单上面时，改变菜单的背景色
            $(".out-two-li").hover(
            	function(){
                    if($(this)[0].className=="out-two-li"){
                        $(this).addClass("hover");
					}
				},
				function(){
                    $(this).removeClass("hover");
				}
            );
			/*绑定二级菜单点击事件*/
			$(".out-two-li").click(function(e){
				allOutTwoLi.filter('.selected').removeClass('selected');//移除所有selected样式
				$(this).addClass('selected');//给选中添加selected样式				
				var tabUrl =$(this).data('url');//新增一个选项卡
				var tabTitle = $(this).text();
				if($("#tt").tabs('exists', tabTitle)) {//tab是否存在    不存在就添加一个窗口并选中，存在即选中窗口
					$("#tt").tabs('select', tabTitle);
				} else {
                    $('#tt').tabs('add', {
                        title: tabTitle,
                        content:'<iframe src="' + tabUrl + '"frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
                        closable: true
                    });
				}
				if($(".tabs li").length>0){
					rightNewKey();//打开tab页时绑定,是为了给打开的tab绑定事件
				}
			});
			
			/*设置左侧菜单头部工具按钮*/
			var menu_control_panel=$("body").layout("panel","west");//左侧的菜单栏对象
			$(".show-tool").on("click",function(){
				var tool=$(this).children(".fa-bars");//a标签的对象
				//菜单向左侧收拢
				if(tool.length){
					tool.attr("class","fa fa-th-large");//变化a标签图片
					$(".show-title").hide();
					$(".out-one-div").next().slideUp();//动态收回打开的二级菜单  高度向上
					menu_control_panel.panel('resize',{width:45});
					$(".out-one-div .fa-angle-up").attr("class","fa fa-angle-down");//换一级菜单图标
					$(".show-tool").animate({"margin-left":"15px"},120);
					/*$(".menu-show,.menu-contents,.menu-sidebar").removeClass("outerActive");
					$(".menu-show,.menu-contents,.menu-sidebar").addClass("narrowActive");*/
					$(".out-one-div").off("click");
					$(".narrow-one-ul").show();//显示窄菜单
				//菜单向右侧展开	
				}else{
                    tool=$(this).children(".fa-th-large");
					tool.attr("class","fa fa-bars");
					$(".show-title").show();
					$(".narrow-one-ul").hide();
					menu_control_panel.panel('resize',{width:200});
					$(".show-tool").animate({"margin-left":"75px"},120);
					$(".menu-show,.menu-contents,.menu-sidebar").removeClass("narrowActive");
					$(".menu-show,.menu-contents,.menu-sidebar").addClass("outerActive");
					$(".out-one-div").off("click");
					$('.out-one-div').on("click",oneDivClick);
				}
				$("body").layout("resize",{width:'100%'})
			});
			$(".out-one-li,.narrow-one-li").hover(
				function(){
                    $("#narrow-one-li"+$(this).index()).addClass("open");
                    $("#narrow-one-li"+$(this).index()+" .narrow-two-ul").show();
				},function(){
                    $("#narrow-one-li"+$(this).index()).removeClass("open");
                    $("#narrow-one-li"+$(this).index()+" .narrow-two-ul").hide();
				})

		})
		
	}
});

$(function() {
	/*菜单栏目，头像点击操作*/
	$(".user").on("click",function(){
		$('#mm1').menu('show', {
			top: 60,
			left: document.body.scrollWidth-180,
			onShow:function(){
				$(".user>span:last").attr("class","fa fa-angle-up");
			},
			onHide:function(){
				$(".user>span:last").attr("class","fa fa-angle-down");
			}
		});
	});
	/*修改密码*/
    $('#passWordSetting').on('click',function(){
        var passwordEdit = $('#passWordEdit').dialog({
            width: 460,
            height: 260,
            modal: true,
            title: '修改密码',
            buttons: [{
                text: '保存',
                id: 'btn-sure',
                handler: function() {
                    console.log('此时应修改密码');
                    submitChangePwd();
                    passwordEdit.panel('close');
                }
            }, {
                text: '关闭',
                handler: function() {
                    passwordEdit.panel('close');
                    console.log('此时应取消修改密码');
                }
            }],
            onOpen: function() {
            	console.log('点击即打开');
            }
        });
    });
    
	/*修改主题*/
	$('#themeSetting').on('click', function() {
		var themeWin = $('#themeSettingWin').dialog({
			width: 460,
			height: 260,
			modal: true,
			title: '主题设置',
			buttons: [/*{
				text: '保存',
				id: 'btn-sure',
				handler: function() {
					themeWin.panel('close');
				}
			}, *//*{
				text: '关闭',
				handler: function() {
					themeWin.panel('close');
				}
			}*/],
			onOpen: function() {
				$(".themeItem").show();
			}
		});
	});
	$(".themeItem ul li").on('click', function() {
		$(".themeItem ul li").removeClass('themeActive');
		$(this).addClass('themeActive');
		var theme = $(this).children("div").attr('class');
		$("#themeLink").attr("href","/whhft/easyui/themes/"+theme+"/easyui.css");
		var $iframe = $('iframe');  
		if ($iframe.length > 0) {  
		    for ( var i = 0; i < $iframe.length; i++) {  
		        var ifr = $iframe[i];
		        $(ifr).contents().find('#themeLink').attr('href', "/whhft/easyui/themes/"+theme+"/easyui.css");  
		    }
		}  
		$.cookie('themename', theme,{ expires : 7 });
	});
	//得到cookie的值，若有着使用这个主题
	var themename = getCookie('themename');
	if(themename!=null && themename!='' ){
		$("#themeLink").attr("href","/whhft/easyui/themes/"+themename+"/easyui.css");
	}
	//右键菜单绑定点击事件
    $("#m-refresh").click(function(){
        var currTab = $('#tt').tabs('getSelected');
        RefreshTab(currTab);
    });
    $("#m-closeall").click(function(){
        $(".tabs li").each(function(i, n){
            var title = $(n).text();
            if(title!='首页'){
            	$('#tt').tabs('close',title); 
            }
        });
    });
    $("#m-closeother").click(function(){
        var currTab = $('#tt').tabs('getSelected');
        currTitle = currTab.panel('options').title;  
        $(".tabs li").each(function(i, n){
            var title = $(n).text();
            if(currTitle != title && title!="首页"){
                $('#tt').tabs('close',title);            
            }
        });
        $('#tt').tabs('select',currTitle);
    });
    $("#m-close").click(function(){
        var currTab = $('#tt').tabs('getSelected');
        currTitle = currTab.panel('options').title; 
        if(currTitle!="首页"){
        	$('#tt').tabs('close', currTitle);
        }
    });
	
});

//获取cookie值
function getCookie(cookieName) {
    var strCookie = document.cookie;
    var arrCookie = strCookie.split("; ");
    for(var i = 0; i < arrCookie.length; i++){
        var arr = arrCookie[i].split("=");
        if(cookieName == arr[0]){
            return arr[1];
        }
    }
    return "";
}

//刷新当前标签Tabs
function RefreshTab(currentTab) {
     var url = $(currentTab.panel('options')).attr('href');
     $('#tt').tabs('update', {
         tab: currentTab,
         options: { href: url }
     });
     currentTab.panel('refresh');
}

//给当前选中的tab绑定事件
function rightNewKey(){
	$(".tabs li").each(function(i,n){
		 $(n).on('contextmenu',function(e){
			 var subtitle =$(this).text();
		     $('#tt').tabs('select',subtitle);
		     $('#menu').menu('show', {
		            left: e.pageX,
		            top: e.pageY
		     });
		     return false;
		 });
	});
	
	
}




