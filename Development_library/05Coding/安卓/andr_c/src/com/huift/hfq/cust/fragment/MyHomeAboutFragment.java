package com.huift.hfq.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.AppUpdate;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.ActThemeDetailActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.GetNewestClientAppVersionTask;
import com.huift.hfq.cust.service.UpdateService;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于页面
 * 
 * @author qian.zhou
 */
public class MyHomeAboutFragment extends Fragment {
	private final static String TAG = "MyHomeAboutFragment";
	private static final int UPP_APP = 1;
	/** 版本号 **/
	private TextView myHomeVersion;
	/*** 拨打电话 */
	private LinearLayout mPhone;
	/** 检测版本的列 */
	private RelativeLayout mAppUpp;
	/** 更新的对象 */
	private AppUpdate mAppUpdate;
	/** 版本号 **/
	private TextView mTvAppUpdate;
	/** 当前版本号 */
	private String mCurrentVesion;

	public static MyHomeAboutFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeAboutFragment fragment = new MyHomeAboutFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myhome_about, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getMyActivity());
		init(v);
		return v;
	}
	
	/**
	 * 初始化视图
	 * @return
	 */
	public void init(View v){
		//标题
		TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvContent.setText(R.string.myhome_about);
		myHomeVersion = (TextView) v.findViewById(R.id.myhome_version);
		mTvAppUpdate = (TextView) v.findViewById(R.id.tv_appUpdate);
		mAppUpp = (RelativeLayout) v.findViewById(R.id.release_check);
		mCurrentVesion = Util.getAppVersionCode(getMyActivity());
		myHomeVersion.setText(mCurrentVesion);
		// 拨打电话
		mPhone = (LinearLayout) v.findViewById(R.id.about_call);
		mPhone.setOnClickListener(phoneListener);
		mAppUpdate = DB.getObj(CustConst.Key.APP_UPP, AppUpdate.class); // 保存跟新的对象
		if (null != mAppUpdate) {
			if (!Util.isEmpty(mAppUpdate.getVersionCode()) && !Util.isEmpty(mCurrentVesion)) {
				if (mAppUpdate.getVersionCode().compareTo(mCurrentVesion) > 0 || mAppUpdate.getCanUpdate() == UPP_APP) {
					mTvAppUpdate.setVisibility(View.VISIBLE);
				} else {
					mTvAppUpdate.setVisibility(View.GONE);
				}
			}
		} else {
			isUpdate(); // 调用异步任务类更新
		}
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 版本升级
	 */
	public void isUpdate() {
		new GetNewestClientAppVersionTask(getMyActivity(), new GetNewestClientAppVersionTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					Log.d(TAG, "我进了了..........");
					return;
				} else {
					AppUpdate update = Util.json2Obj(result.toString(), AppUpdate.class);
					Log.d(TAG, "update=" + update.getVersionCode());
					String currentVesion = Util.getAppVersionCode(getMyActivity());
					String newVersionStr = update.getVersionCode();
					if (update != null) { // 有新的版本更新
						// 服务器上新版本比现在app的版本高的话就提示升级
						if (!Util.isEmpty(newVersionStr)) {
							if (newVersionStr.compareTo(currentVesion) > 0) {
								update.setCanUpdate(1); // 有跟新类容
							}
						}
						DB.saveObj(CustConst.Key.APP_UPP, update); // 保存跟新的对象
					}
				}
			}
		}).execute();
	}

	// 点击电话
	OnClickListener phoneListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DialogUtils.showDialog(getActivity(),Util.getString(R.string.cue),Util.getString(R.string.dialog_phone),Util.getString(R.string.ok),Util.getString(R.string.no), new DialogUtils().new OnResultListener() {
						@Override
						public void onOK() {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(
									R.string.myhome_tel)));
							getMyActivity().startActivity(intent);
						}
					});
		}
	};

	/**
	 * 点击我的工行卡到工行列表
	 */
	@OnClick({ R.id.about_huiquan })
	private void lineBankClick(View v) {
		switch (v.getId()) {
		case R.id.about_huiquan:
			Intent intent = new Intent(getMyActivity(), ActThemeDetailActivity.class);
			intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.ABOUT_HUIQUAN);
			getMyActivity().startActivity(intent);
			break;
		}
	}

	/**
	 * 版本升级
	 * 
	 * @param view
	 */
	@OnClick(R.id.release_check)
	private void checkCoupon(View view) {
		mAppUpp.setEnabled(false);
		mTvAppUpdate.setVisibility(View.GONE);
		if (null != mAppUpdate && !Util.isEmpty(mAppUpdate.getVersionCode())) {
			mAppUpp.setEnabled(true);
			if (mAppUpdate.getVersionCode().compareTo(mCurrentVesion) > 0 || mAppUpdate.getCanUpdate() == UPP_APP) {
				String url = mAppUpdate.getUpdateUrl();
				if (null != url || !Util.isEmpty(url)) {
					mAppUpdate.setCanUpdate(0); // 有跟新类容
					DB.saveObj(CustConst.Key.APP_UPP, mAppUpdate); // 保存跟新的对象
					UpdateService.startUpdateService(getActivity(), mAppUpdate.getUpdateUrl()); // 自动更新
					Util.getContentValidate(R.string.downloading);
				} else {
					getNewVersion();
				}
			} else {
				getNewVersion();
			}
		} else {
			getNewVersion();
		}
	}

	/**
	 * 已经是最新版本
	 */
	private void getNewVersion() {
		Util.getContentValidate(R.string.latest_version);
	}

	@OnClick({ R.id.iv_turn_in })
	private void click(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			getActivity().finish();
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeAboutFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeAboutFragment.class.getSimpleName()); //统计页面
	}
}
