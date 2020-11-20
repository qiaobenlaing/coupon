var f7Params = {
	    animateNavBackIcon:true
	    /*,
	    pushState: true,
	    pushStateSeparator: '',
	    pushStateRoot: 'http://zkt.cn'*/
	};
var mainViewParams = {
	    // Enable dynamic Navbar
	    dynamicNavbar: true,
	    // Enable Dom Cache so we can use all inline pages
	    domCache: true
	};
//Initialize your app
var myApp = new Framework7(f7Params);
// Export selectors engine
var $$ = Dom7;

// 通用方法
$(function($) {
	// 加载message。
	sz.checkMsg();
});

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

// 命名空间
var sz;
if(!sz) sz = {};
//常量
/** status SUCC */
sz.STATUS_SUCC = 0;
/** status FAIL */
sz.STATUS_FAIL = -1;
/** 显示错误消息
 * @param string msg 显示的消息
 * @param int autoDismissSeconds 显示多少秒之后自动消失。如果不给,则显示3秒,然后自动消失。给0则不自动消失，一直显示。
 */
sz.showErrMsg = function(msg, autoDismissSeconds) {
    // TODO 具体实现
     myApp.alert(msg, '');
};

/** 显示成功消息
 * @param string msg 显示的消息
 * @param int autoDismissSeconds 显示多少秒之后自动消失。如果不给,则显示3秒,然后自动消失。给0则不自动消失，一直显示。
 */
sz.showSuccMsg = function(msg, autoDismissSeconds) {
    // TODO 具体实现
    myApp.alert(msg, '');
};

/**
 * 定时检查message，显示小红点。
 */
sz.checkMsg = function() {
	// 在消息列表页，直接标记所有消息为已读
	if (location.href.indexOf('/Noti/notiList') >= 0) {
		$.cookie('MSG_LAST_CHECK_TIME', Date.parse(new Date()));
		$.cookie('MSG_LAST_HAS_NEWE', false);
	} else {
		var MSG_CHECK_FREQUENCE = 5000; // 每5s检查一次
		var lastCheckTime = $.cookie('MSG_LAST_CHECK_TIME'); // int: timestamp
		var lastHasNewMsg = $.cookie('MSG_LAST_HAS_NEWE'); // boolean: true/false
		// 每隔5s或者在消息列表页，进行新消息检查
		if (Date.parse(new Date()) - lastCheckTime > MSG_CHECK_FREQUENCE) {
			$.get('/Agent/Noti/countUnreadNoti', {}, function(resp){
				if(resp.unreadNotiCount != 0) {
					$('.icon').addClass('msg-ico');
					$('img.msg-ico').show();
					$.cookie('MSG_LAST_HAS_NEWE', true);
				} else {
					$.cookie('MSG_LAST_HAS_NEWE', false);
				}
				$.cookie('MSG_LAST_CHECK_TIME', Date.parse(new Date()));
			});
		}
	}
};

/**
 * 初始化一个单选下拉框。
 * @param selector 下拉框jquery选择器。例: '#city_selector', '.select'
 * @param dataId 该下拉框的ID
 * @param defaultVal [可选]默认选中项的值 
 * @param type [可选]下拉框类型：Normal，或者其他
 * @param prepend [可选]附加到第一项的值, json格式，必须给定name和val字段。例如{name: '全部', val: 0}.
 */
sz.initDropdown = function(selector, dataId, defaultVal, type, prepend) {
	if (!type)
		type == 'Normal';
	if (!defaultVal)
		defaultVal = '';

	var dropdown = $(selector);
	// 首先清空所有现有元素
	dropdown.empty();
	if (dataId) {
		$.get('/Agent/Util/dropdown', {id: dataId, type: type}, function(resp) {
			if (resp.status != sz.STATUS_SUCC)
				return;
			// 从db中获取的数据项
			var optionData = resp.data;
			if (prepend) { // 将prepend插入到首位
				optionData.splice(0, 0, prepend); 
			}
			for (var i = 0; i < optionData.length; ++i) {
				var option = $('<option/>');
				option.val(optionData[i].val);
				option.text(optionData[i].name);
				if (optionData[i].val == defaultVal) {
					option.prop('selected', true);
				}
				dropdown.append(option);
			}
		});
	} else {
		if (prepend) { // 将prepend插入到首位
			var option = $('<option/>');
			option.val(prepend.val);
			option.text(prepend.name);
			dropdown.append(option);
		}
	}
};

/**
 * 初始化浙江省下的默认城市和地区下拉框
 * @param defaultCity 默认选定的城市。默认值为''，即"全部"，如有需要，可设为城市名，例如：杭州。
 * @param defaultDistrict 默认选定的地区。默认值为''，即"全部"，如有需要，可设为地区名，例如：上城区。
 * @param citySelector 城市下拉框的jquery选择器，例如'#city'
 * @param districtSelector 行政区下拉框的jquery选择器，例如'#distrcit'
 * @param cityPrepend 城市下拉框的第一项，json object格式。例：{name: '全部', val: ''}
 * @param districtPrepend 行政区下拉框的第一项，json object格式。例：{name: '全部', val: ''}
 */
sz.initDistrict = function(defaultCity, defaultDistrict, 
		citySelector, districtSelector,  cityPrepend, districtPrepend) {
	// 设置默认参数
	if (!defaultCity)
		defaultCity = '';
	if (!defaultDistrict)
		defaultDistrict = '';
	if (!citySelector)
		citySelector = '#city';
	if (!districtSelector)
		districtSelector = '#district';
	if (!cityPrepend)
		cityPrepend = {name: '全部', val: ''};
	if (!districtPrepend)
		districtPrepend = {name: '全部', val: ''};
	// 初始化下拉框
	sz.initDropdown(citySelector, '浙江省', defaultCity, 'District', cityPrepend);
	sz.initDropdown(districtSelector, defaultCity, defaultDistrict, 'District', districtPrepend);
	$(citySelector).on('change', function() {
		var selected = $(this).find('option:selected');
		sz.initDropdown(districtSelector, selected.val(), defaultDistrict, 'District', districtPrepend);
	});
};

/** Highcharts style */
sz.DEFAULT_HIGHCHARTS_THEME = {
	colors: ["#2b908f", "#90ee7e", "#f45b5b", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
		"#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
	chart: {
		backgroundColor: {
			linearGradient: { x1: 0, y1: 0, x2: 1, y2: 1 },
			stops: [
				[0, '#2a2a2b'],
				[1, '#3e3e40']
			]
		},
		style: {
			fontFamily: "'Unica One', sans-serif"
		},
		plotBorderColor: '#606063'
	},
	title: {
		style: {
			color: '#E0E0E3',
			textTransform: 'uppercase',
			fontSize: '20px'
		}
	},
	subtitle: {
		style: {
			color: '#E0E0E3',
			textTransform: 'uppercase'
		}
	},
	xAxis: {
		gridLineColor: '#707073',
		labels: {
			style: {
				color: '#E0E0E3'
			}
		},
		lineColor: '#707073',
		minorGridLineColor: '#505053',
		tickColor: '#707073',
		title: {
			style: {
				color: '#A0A0A3'

			}
		}
	},
	yAxis: {
		gridLineColor: '#707073',
		labels: {
			style: {
				color: '#E0E0E3'
			}
		},
		lineColor: '#707073',
		minorGridLineColor: '#505053',
		tickColor: '#707073',
		tickWidth: 1,
		title: {
			style: {
				color: '#A0A0A3'
			}
		}
	},
	tooltip: {
		backgroundColor: 'rgba(0, 0, 0, 0.85)',
		style: {
			color: '#F0F0F0'
		}
	},
	plotOptions: {
		series: {
			dataLabels: {
				color: '#B0B0B3'
			},
			marker: {
				lineColor: '#333'
			}
		},
		boxplot: {
			fillColor: '#505053'
		},
		candlestick: {
			lineColor: 'white'
		},
		errorbar: {
			color: 'white'
		}
	},
	legend: {
		itemStyle: {
			color: '#E0E0E3'
		},
		itemHoverStyle: {
			color: '#FFF'
		},
		itemHiddenStyle: {
			color: '#606063'
		}
	},
	credits: {
		style: {
			color: '#666'
		}
	},
	labels: {
		style: {
			color: '#707073'
		}
	},

	drilldown: {
		activeAxisLabelStyle: {
			color: '#F0F0F3'
		},
		activeDataLabelStyle: {
			color: '#F0F0F3'
		}
	},

	navigation: {
		buttonOptions: {
			symbolStroke: '#DDDDDD',
			theme: {
				fill: '#505053'
			}
		}
	},

	// scroll charts
	rangeSelector: {
		buttonTheme: {
			fill: '#505053',
			stroke: '#000000',
			style: {
				color: '#CCC'
			},
			states: {
				hover: {
					fill: '#707073',
					stroke: '#000000',
					style: {
						color: 'white'
					}
				},
				select: {
					fill: '#000003',
					stroke: '#000000',
					style: {
						color: 'white'
					}
				}
			}
		},
		inputBoxBorderColor: '#505053',
		inputStyle: {
			backgroundColor: '#333',
			color: 'silver'
		},
		labelStyle: {
			color: 'silver'
		}
	},

	navigator: {
		handles: {
			backgroundColor: '#666',
			borderColor: '#AAA'
		},
		outlineColor: '#CCC',
		maskFill: 'rgba(255,255,255,0.1)',
		series: {
			color: '#7798BF',
			lineColor: '#A6C7ED'
		},
		xAxis: {
			gridLineColor: '#505053'
		}
	},

	scrollbar: {
		barBackgroundColor: '#808083',
		barBorderColor: '#808083',
		buttonArrowColor: '#CCC',
		buttonBackgroundColor: '#606063',
		buttonBorderColor: '#606063',
		rifleColor: '#FFF',
		trackBackgroundColor: '#404043',
		trackBorderColor: '#404043'
	},

	// special colors for some of the
	legendBackgroundColor: 'rgba(0, 0, 0, 0.5)',
	background2: '#505053',
	dataLabelsColor: '#B0B0B3',
	textColor: '#C0C0C0',
	contrastTextColor: '#F0F0F3',
	maskColor: 'rgba(255,255,255,0.3)'
};


/**
 * 加载数据并显示交易图
 * @param dataUrl 请求数据的url
 * @param chartSelector 图的jquery selector. 默认为#word_performance_chart
 */
sz.loadDealChart = function(dataUrl, chartSelector) {
	Highcharts.setOptions(sz.DEFAULT_HIGHCHARTS_THEME);
	if (!chartSelector)
		chartSelector = '#word_performance_chart';
	$.get(dataUrl, {}, function(resp) {
		if(resp.status !== sz.STATUS_SUCC){
			sz.showErrMsg(resp.msg);
			return;
		};
		// x轴
		var xDates = resp.data.dates; // ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
		// y轴：成交套数
		var yDealNo = resp.data.dealNum; // [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4];
		// y轴：成交金额
		var yDealAmt = resp.data.dealAmt; // [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6];

		var chart = $(chartSelector).highcharts({
			/*chart: {
				zoomType: 'xy'
			},*/
			title: {
				text: '最近五周成交'
			},
			/*subtitle: {
			 text: 'Source: WorldClimate.com'
			 },*/
			xAxis: [{
				categories: xDates
			}],
			yAxis: [{ // Primary yAxis
				labels: {
					format: '{value}套',
					style: {
						color: Highcharts.getOptions().colors[0]
					}
				},
				title: {
					text: '',
					style: {
						color: Highcharts.getOptions().colors[0]
					}
				}
			}, { // Secondary yAxis
				labels: {
					format: '{value}万元',
					style: {
						color: Highcharts.getOptions().colors[1]
					}
				},
				title: {
					text: '',
					style: {
						color: Highcharts.getOptions().colors[1]
					}
				},
				opposite: true
			}],
			tooltip: {
				shared: true
			},
			legend: {
				layout: 'vertical',
				align: 'left',
				x: 120,
				verticalAlign: 'top',
				y: 100,
				floating: true,
				backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
			},
			series: [{
				name: '成交套数',
				type: 'column',
				yAxis: 0,
				data: yDealNo,
				tooltip: {
					valueSuffix: '套'
				}

			}, {
				name: '成交金额',
				type: 'spline',
				yAxis: 1,
				data: yDealAmt,
				tooltip: {
					valueSuffix: '万元'
				}
			}]
		});
		//chart.container.onclick = null;
	});
};

jQuery.cookie=function(name,value,options){
	if(typeof value!='undefined'){
		options=options||{};
		if(value===null){
			value='';
			options.expires=-1;
		}
		var expires='';
		if(options.expires&&(typeof options.expires=='number'||options.expires.toUTCString)){
			var date;
			if(typeof options.expires=='number'){
				date=new Date();
				date.setTime(date.getTime()+(options.expires * 24 * 60 * 60 * 1000));
			}else{
				date=options.expires;
			}
			expires=';expires='+date.toUTCString();
		}
		var path=options.path?';path='+options.path:'';
		var domain=options.domain?';domain='+options.domain:'';
		var secure=options.secure?';secure':'';
		document.cookie=[name,'=',encodeURIComponent(value),expires,path,domain,secure].join('');
	}else{
		var cookieValue=null;
		if(document.cookie&&document.cookie!=''){
			var cookies=document.cookie.split(';');
			for(var i=0;i<cookies.length;i++){
				var cookie=jQuery.trim(cookies[i]);
				if(cookie.substring(0,name.length+1)==(name+'=')){
					cookieValue=decodeURIComponent(cookie.substring(name.length+1));
					break;
				}
			}
		}
		return cookieValue;
	}
};
