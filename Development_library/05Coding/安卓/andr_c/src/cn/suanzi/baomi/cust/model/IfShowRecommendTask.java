package cn.suanzi.baomi.cust.model;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 推荐得好礼
 * @author yanfang.li
 */
public class IfShowRecommendTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = IfShowRecommendTask.class.getSimpleName();
	/** 调用API返回对象*/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback mCallback;
	
	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(int retCode) ;
	}
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public IfShowRecommendTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * @param acti 上下文
	 * @param mCallback 回调方法
	 */
	public IfShowRecommendTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	/**
	 * 业务逻辑操作
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			int code = Integer.parseInt(mResult.get("isShow").toString());
			mCallback.getResult(code);
		}else {
			Util.showToastZH("失败");
			mCallback.getResult(ErrorCode.FAIL);
		}
	}

	/**
	 * 调用Api
	 */
	@Override
	protected Integer doInBackground(String... params) {
		try {
			int retCode = ErrorCode.ERROR_RET_CODE;
			mResult = (JSONObject)API.reqCust("ifShowRecommend", null);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if ( mResult != null || !"".equals(mResult.toJSONString())) {
				retCode = ErrorCode.RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
