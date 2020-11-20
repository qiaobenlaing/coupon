$.extend($.fn.validatebox.defaults.rules, {
	/* 扩展验证两次密码 */
	eq : {
		validator : function(value, params) {
			// eq[false]的时候取消此验证
			 if (params == false)
			 	return true;
			return value == $(params).val();
		},
		message : '两次输入不一致!'
	},
	 unique: {
		 //和easyui的remote方法类似，都是同步方法，网速不好的时候有点干扰用户体验
		validator : function(value, params) {
			var rtnValue = false;
			var id= $(params[1]).val();
			$.ajax({
				   type: "POST",
				   url: params[0],
				   data: 'fieldValue=' + value + '&id='+id,
				   async: false,
				   success: function(data){
					   rtnValue = (data == 'true');
				   }
				});
			return rtnValue;
		},
		message : '该值已经存在！'
	},
	minLength : {
		validator : function(value, param) {
			return value.length >= param[0];
		},
		message : '输入的字数不得小于{0}！'
	},
	maxLength : {
		validator : function(value, param) {
			return value.length <= param[0];
		},
		message : '输入的字数不得大于{0}！'
	}
});