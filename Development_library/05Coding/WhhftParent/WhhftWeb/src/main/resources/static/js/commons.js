//全局变量和全局函数
var $g={
	post: function( url, data, callback, type ) {
		if ( $.isFunction( data ) ) {
			type = type || callback;
			callback = data;
			data = undefined;
		};
		return $.ajax({
			url: url,
			type: "POST",
			dataType: type,
			data: data,
			success: callback,
			statusCode: {
				404: function() {$g.showMsg("操作失败" ,"您访问的功能并不存在！");},
				500: function() {$g.showMsg("操作失败" ,"系统操作出错，请通知系统管理员或稍后再试！");},
			}
		});
	},
	//分页相关的参数
	page:{
		DEFAULT_PAGE_SIZE : 20,
		DEFAULT_ROW_OPTIONS : [ 10, 20, 30, 50 ]
	},
	
	comboData:{
		DICT_DEFINE_TYPE : [{value:1,text:'系统参数'}, {value:2,text:'业务参数'}],
		BOOLEAN_TYPE : [{value:1,text:'是'}, {value:2,text:'否'}],
		ICON_CLS: [{value : '', text : '默认'},
		           {value : 'icon-tCut', text : 'icon-tCut'},
		           {value : 'icon-tOpen', text : 'icon-tOpen'},
		           {value : 'icon-add', text : 'icon-add'},
		           {value : 'icon-edit', text : 'icon-edit'},
		           {value : 'icon-remove', text : 'icon-remove'},
		           {value : 'icon-save', text : 'icon-save'},
		           {value : 'icon-cut', text : 'icon-cut'},
		           {value : 'icon-ok', text : 'icon-ok'},
		           {value : 'icon-no', text : 'icon-no'},
		           {value : 'icon-cancel', text : 'icon-cancel'},
		           {value : 'icon-reload', text : 'icon-reload'},
		           {value : 'icon-search', text : 'icon-search'},
		           {value : 'icon-print', text : 'icon-print'},
		           {value : 'icon-help', text : 'icon-help'},
		           {value : 'icon-undo', text : 'icon-undo'},
		           {value : 'icon-redo', text : 'icon-redo'},
		           {value : 'icon-back', text : 'icon-back'},
		           {value : 'icon-sum', text : 'icon-sum'},
		           {value : 'icon-tip', text : 'icon-tip'}],
	},
	
	//修改系统样式，并保存到COOKIES中
	changeTheme:function(themeName){
		if(themeName == null) {
			themeName = 'default';
		}
		$("#easyUIThemes").attr("href","easyui/themes/"+themeName+"/easyui.css");
		$.cookie('easyuiThemeName', themeName, {expires : 7});
	},
	//系统消息提示弹出框，3秒延迟后自动关闭
	showMsg:function(title, content){
		$.messager.show({  
	 		 title: title,
	 		 msg:content,
	 		 timeout:3000,  
	 		 showType:'slide'  
	 	});
	},
	//系统警告弹出框，3秒延迟后自动关闭
	showAlert:function(title, content){
		$.messager.show({  
	 		 title: title,
	 		 msg:content,
	 		 timeout:3000,  
	 		 showType:'slide'  
	 	});
	},
	
	//仅main页面在使用的方法
	main:{
		refreshTab: function(){  
			var tab = $('#mainTab').tabs('getSelected');  
			if(tab){
				var iframe = $(tab.panel('options').content);
				$('#'+iframe.attr('id')).attr('src', iframe.attr('data-src'));
			}
	    },
		removeTab:function(){  
	        var tab = $('#mainTab').tabs('getSelected');  
	        if (tab && tab.panel('options').closable){  
	            var index = $('#mainTab').tabs('getTabIndex', tab);  
	            $('#mainTab').tabs('close', index);  
	        }  
	    }  
	},
	//以下是用于页面列表GRID控件做增删改查功能时用到的代码,dg的意思是datagrid
	dg:{
		onBeforeLoad:function (param ,searchFormId){
			//和分页功能一同提交到后台的查询数据，将查询条件序列化为JSON对象添加到param中
			$.each( $("#"+searchFormId).serializeArray(), function(index, item){
				param[item.name]=item.value;
			});
		},
		//GRID列表菜单的新增功能，清空编辑页面的表单，编辑页面限定只能有一个FORM表单被提交，默认为第0个FORM
		addRow:function(editorId, formIndex){
			if(formIndex == undefined) {
				formIndex = 0;
			}
			$($("#"+editorId+ " > form")[formIndex]).form('clear');
		    $('#'+editorId).window('open');
		},
		//执行GRID列表菜单单行记录编辑操作，回显行记录到编辑框，然后显示编辑框
		editRow:function (url, editorId, formIndex){
			if(formIndex == undefined) {
				formIndex = 0;
			}
			var $form = $($("#"+editorId+ " > form")[formIndex]);
			//必须要先CLEAR一下再填充值，避免LOAD到的数据，某些字段是NULL，造成脏数据没有清理
			$form.form('clear');
			$form.form('load', url);
			$('#'+editorId).window('open');
		},
		//执行GRID列表菜单单行记录的删除操作
		removeRow:function (url, dgId, content, beforeRemoveRowFn){
			if(content == undefined) {
				content = '删除操作无法撤销，是否确定？';
			}
			$.messager.confirm('删除确认', content, function(result){  
			      if (result){
			    	  beforeRemoveRowFn &&  beforeRemoveRowFn();
			    	  $.post(url).done(function(data){
			    	  	$g.showMsg('消息', '删除成功！');
			    	  	$('#'+dgId).datagrid('reload');
			    	  });
				  }  
			});  
		},
		//用于GRID查询中，输入查询条件
		search:function (datagridId){
			var options = $('#'+datagridId).datagrid('options');
			options.pageNumber = 1;
			$('#'+datagridId).datagrid('reload');
		},
		//用于各列表页的清空查询条件按钮
		clearSearch:function (searchFormId){
			$('#'+searchFormId).form('clear');
		},

		//新增或修改dictDefine对象，提交数据，显示保存结果
		submitEditor:function (url, datagridId, editorId, formIndex){
			if(formIndex == undefined) {
				formIndex = 0;
			}
			$($("#"+editorId+ " > form")[formIndex]).form('submit',{
				url: url,
				success:function(data){
					$('#'+editorId).window('close');
					$('#'+datagridId).datagrid('reload');
					$g.showMsg('消息', '保存成功！');
		        },
		        onLoadError: function(){
					$g.showMsg('消息', '表单数据错误，无法提交！');
		        }
			});
		}
	},
	form:{
		formatType: function(json, value, valueField, textField, defaultValue){
			if(valueField == undefined) {
				valueField = "value";
			}
			if(textField == undefined) {
				textField = "text";
			}
			for (var i=0;i<json.length;i++){
				if(eval("json[i]."+ valueField +" == value")){
					return eval("json[i]."+textField);
				}
			}
			return defaultValue;
		}
	},
	utils: {
		//对象深度克隆函数
		deepCopy: function(source) { 
			var result={};
			for (var key in source) {
		      result[key] = typeof source[key]==='object'? deepCoyp(source[key]): source[key];
		   } 
		   return result; 
		}
	}
}
/**
 * 这个函数仅适用于当GRID控件对单表进行CRUD操作时，它调用的是$g.gd中的对应方法，只是为了省事，
 * 做了一层封装，如果列表显示的是更复杂的内容，这个函数就不适用了
 */
function SingleTableGrid (_editorId, _editFormId, _searchFormId, _dataGridId, _findOneUrl, _saveUrl, _removeUrl, _beforeRemoveRowFn){
	var self = this;
	this.editorId = _editorId;
	this.editFormId = _editFormId;
	this.searchFormId = _searchFormId;
	this.dataGridId = _dataGridId;
	this.findOneUrl = _findOneUrl;
	this.saveUrl = _saveUrl;
	this.removeUrl = _removeUrl;
	this.beforeRemoveRowFn = _beforeRemoveRowFn;
	
	this.addRow = function(){
		$g.dg.addRow(self.editorId);
	};
	this.editRow = function (index, id){
		$g.dg.editRow(self.findOneUrl+"="+id, self.editorId);
	};
	this.removeRow = function (index, id){
		$g.dg.removeRow(self.removeUrl+"="+id, self.dataGridId, null, self.beforeRemoveRowFn);
	};
	this.onBeforeLoad = function (param){
		$g.dg.onBeforeLoad(param, self.searchFormId);
	};
	this.search = function (){
		$g.dg.search(self.dataGridId);
	};
	this.clearSearch = function (){
		$g.dg.clearSearch(self.searchFormId);
	};
	this.submitEditor = function(){
		$g.dg.submitEditor(self.saveUrl, self.dataGridId, self.editorId);
	};
};
