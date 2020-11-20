// 命名空间
var sz;     
if(!sz) sz = {};
// 常量
/** 列表上方的搜索框 */
sz.SEARCH_FORM_ID = '#data_search_form';
/** 列表上方的搜索框中的提交按钮ID */
sz.SEARCH_FORM_SUBBTN_ID = '#search_form_submit_btn';
/** 列表表格 */
sz.DATA_LIST_TABLE_ID = '#data_table';
/** 列表表格外层div */
sz.DATA_LIST_TABLE_WRAPPER_ID = '#data_table_wrapper';
/** 页面的删除按钮 */
sz.DELETE_BUTTON_ID = '.link-delete';
/** 页面的状态转换按钮 */
sz.CHANGE_BUTTON_ID = '.td-distant';
/** status SUCC */
sz.STATUS_SUCC = 0;
/** status FAIL */
sz.STATUS_FAIL = -1;
/** 百度UMEditor默认参数 */
sz.DEFAULT_UM_PARAMS = {
         imageUrl: '/Admin/Umeditor/imageUp',
         imagePath: '/',
         focus: true,
         textarea: 'content' // [默认值：'editorValue'] 提交表单时，服务器获取编辑器提交内容的所用的参数，多实例时可以给容器name属性，会将name给定的值最为每个实例的键值，不用每次实例化的时候都设置这个值
 };
sz.HQ_CODE = '00000000-0000-0000-0000-000000000000';

/** 显示错误消息 
 * @param string msg 显示的消息
 * @param int autoDismissSeconds 显示多少秒之后自动消失。如果不给则不自动消失，一直显示。
 */

sz.showErrMsg = function(msg, autoDismissSeconds) {
	$('.alert-top-tip').addClass('alert-warning').html(msg).show(function() {
		if (autoDismissSeconds > 0) {
			setTimeout(function() {
				$('.alert-success').hide();
			}, autoDismissSeconds * 1000);
		}
	});
	
	
};

/** 显示成功消息
 * @param string msg 显示的消息
 * @param int autoDismissSeconds 显示多少秒之后自动消失。如果不给则不自动消失，一直显示。
 */
sz.showSuccMsg = function(msg, autoDismissSeconds) {
    $('.alert-top-tip').removeClass('alert-warning').html(msg).show(function() {
		if (autoDismissSeconds > 0) {
			setTimeout(function() {
				$('.alert-success').hide();
			}, autoDismissSeconds * 1000);
		}
	});
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
		$.get('/Admin/Util/dropdown', {id: dataId, type: type}, function(resp) {
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
 * @param defaultProvince 默认选定的省份。默认值为''，即"全部"，如有需要，可设为省份名，例如：浙江省
 * @param defaultCity 默认选定的城市。默认值为''，即"全部"，如有需要，可设为城市名，例如：杭州。
 * @param defaultDistrict 默认选定的地区。默认值为''，即"全部"，如有需要，可设为地区名，例如：上城区。
 * @param provinceSelector 省份下拉框的jquery选择器，例如'#province'
 * @param citySelector 城市下拉框的jquery选择器，例如'#city'
 * @param districtSelector 行政区下拉框的jquery选择器，例如'#distrcit'
 * @param provincePrepend 省份下拉框的第一项，json object格式。例:{name: '全部省份', val: ''}
 * @param cityPrepend 城市下拉框的第一项，json object格式。例：{name: '全部城市', val: ''}
 * @param districtPrepend 行政区下拉框的第一项，json object格式。例：{name: '全部地区', val: ''}
 */
sz.initDistrict = function(defaultProvince, defaultCity, defaultDistrict, provinceSelector,
		citySelector, districtSelector,  provincePrepend, cityPrepend, districtPrepend) {
	// 设置默认参数
    defaultProvince = defaultProvince || '';
	defaultCity = defaultCity || '';
	defaultDistrict = defaultDistrict || '';
    provinceSelector = provinceSelector || '#province';
	citySelector = citySelector || '#city';
	districtSelector = districtSelector || '#district';
    provincePrepend = provincePrepend || {name: '请选择省份', val: ''};
	cityPrepend = cityPrepend || {name: '请选择城市', val: ''};
	districtPrepend = districtPrepend || {name: '请选择地区', val: ''};

	// 初始化下拉框
    sz.initDropdown(provinceSelector, '中国', defaultProvince, 'Province', provincePrepend);

	sz.initDropdown(citySelector, defaultProvince, defaultCity, 'District', cityPrepend);
	sz.initDropdown(districtSelector, defaultCity, defaultDistrict, 'District', districtPrepend);
    // 选择省级区域时，获取相应的市级区域
    $(provinceSelector).on('change', function() {
        var selected = $(this).find('option:selected');
        sz.initDropdown(citySelector, selected.val(), defaultCity, 'District', cityPrepend);
    });
    // 选择市级区域时，获取相应的县级区域
	$(citySelector).on('change', function() {
		var selected = $(this).find('option:selected');
		sz.initDropdown(districtSelector, selected.val(), defaultDistrict, 'District', districtPrepend);
	});
};

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

/**
 * 通用的初始化
 */
$(function($) {
	// 点击列表页上部的搜索框的搜索按钮时，重置页面为1.
	$(sz.SEARCH_FORM_SUBBTN_ID).on('click', function() {
        // 如果页码不显示的话
        if($(sz.SEARCH_FORM_ID + ' input[name=page]').hasClass('show-page') == false) {
            $(sz.SEARCH_FORM_ID + ' input[name=page]').val(1);
        }
	});

});


