package com.huift.hfq.cust.util;

import android.content.Intent;
import android.net.Uri;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.cust.R;

import com.umeng.analytics.MobclickAgent;

/**
 * 公共方法
 * @author liyanfang
 *
 */
public class CommUtils {
	private static final String TAG = CommUtils.class.getSimpleName();
	
	/**
	 * @param telNum 电话号码
	 */
	public static void takePhone(final String telNum) {

		DialogUtils.showDialog(AppUtils.getActivity(),Util.getString(R.string.dialog_title),Util.getString(R.string.dialog_tel),Util.getString(R.string.dialog_ok),Util.getString(R.string.dialog_cancel),new DialogUtils().new OnResultListener() {
			@Override
			public void onOK() {
				if (!Util.isEmpty(telNum)) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(Util.getString(R.string.tel) + telNum));
					AppUtils.getActivity().startActivity(intent);
				} else {
					Util.getContentValidate(R.string.tel_null);
				}
				// 友盟统计
				MobclickAgent.onEvent(AppUtils.getActivity(), "myhome_fragment_phone");
			}

		});
	}
}
