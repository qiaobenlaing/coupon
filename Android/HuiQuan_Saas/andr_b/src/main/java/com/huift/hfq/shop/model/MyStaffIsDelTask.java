package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;
import com.huift.hfq.shop.R;

/**
 * 判断员工是否有添加、删除、修改员工的权限
 * @author qian.zhou
 */
public class MyStaffIsDelTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG = "MyStaffIsDelTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public MyStaffIsDelTask(Activity acti) {
		super(acti) ;
	}

		
	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public MyStaffIsDelTask(Activity acti , Callback callback) {
		super(acti) ;
		mCallback = callback ;
	}

	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONObject  object) ;  
    }
    
    /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if(retCode == RIGHT_RET_CODE) {
				mCallback.getResult(mResult);
			} else {
				if (retCode == ERROR_RET_CODE ) {
					mCallback.getResult(null);
					Toast.makeText(this.mActivity, mActivity.getResources().getString(R.string.toast_nulldata),Toast.LENGTH_SHORT).show();
				   }
			}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String , Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("staffCode" , params[0]) ;//员工编码
		reqParams.put("tokenCode" , params[1]) ;//需要令牌认证
		try {
			mResult = (JSONObject) API.reqShop("isStaffHasPerms" , reqParams) ;
			int retCode = ERROR_RET_CODE;
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if ( !(mResult == null || mResult.size() == 0 || "".equals(mResult.toJSONString())) ) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
