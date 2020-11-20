package cn.suanzi.baomi.shop.fragment;


import net.minidev.json.JSONObject;
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
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.AppUpdate;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.AboutActivity;
import cn.suanzi.baomi.shop.activity.SysWechatActivity;
import cn.suanzi.baomi.shop.model.GetNewestShopAppVersionTask;
import cn.suanzi.baomi.shop.service.UpdateService;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于
 * @author wensi.yu
 */
public class MyHomeAboutFragment extends Fragment {

	private final static String TAG = "MyHomeAboutFragment";
	private static final String VERSION_TITLE ="关于";
	private static final int UPP_APP = 1;
	/**返回图片**/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/**版本号*/
	private TextView myHomeVersion;
	/**拨打电话*/
	private LinearLayout mPhone;
	/**版本号**/
	private TextView mTvAppUpdate;
	/** 更新的对象*/
	private AppUpdate mAppUpdate; 
	/** 当前版本*/
	private String mCurrentVesion;
	
	public static MyHomeAboutFragment newInstance() { 
		Bundle args = new Bundle();
		MyHomeAboutFragment fragment = new MyHomeAboutFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_myhome_about, container,false);
		ViewUtils.inject(this,view);
		Util.addLoginActivity(getActivity());
		init(view);
		return view;
	}

	private void init(View view) {
		//设置标题
		TextView msg = (TextView) view.findViewById(R.id.tv_msg);
		msg.setVisibility(View.GONE);
		mTvdesc.setText(VERSION_TITLE);
		mIvBackup.setVisibility(View.VISIBLE);
		mTvAppUpdate = (TextView) view.findViewById(R.id.tv_appUpdate);
		myHomeVersion = (TextView) view.findViewById(R.id.myhome_version);
		mCurrentVesion = Util.getAppVersionCode(getActivity());
		myHomeVersion.setText(mCurrentVesion);
		//拨打电话
		mPhone = (LinearLayout) view.findViewById(R.id.ly_myhome_phone);
		mPhone.setOnClickListener(phoneListener);
		mAppUpdate = DB.getObj(ShopConst.Key.APP_UPP,AppUpdate.class); // 保存跟新的对象
		if (null != mAppUpdate) {
			if (!Util.isEmpty(mAppUpdate.getVersionCode())) {
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
	
	/**
	 * 版本升级
	 */
	public void isUpdate() {
		new GetNewestShopAppVersionTask(getActivity(), new GetNewestShopAppVersionTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null ) {
					Log.d(TAG, "我进了了..........");
					return ;
				} else {
					AppUpdate update = Util.json2Obj(result.toString(), AppUpdate.class);
					Log.d(TAG, "update="+update.getVersionCode());
					 String vesion = Util.getAppVersionCode(getActivity());
			         String vesionStr = vesion.replace(".", "");
			         String newVersionStr = update.getVersionCode().replace(".", "");
			         int oldVersionCode = 0;
			         if (update != null) { // 有新的版本更新
			        	 int newVersionCode = 0;
			        	 if (Util.isEmpty(vesionStr)) { // 得到新的版本号
			        		 return;
			        	 } else {
			        		 try {
			        			 newVersionCode = Integer.parseInt(newVersionStr); // 转换
			        			 oldVersionCode = Integer.parseInt(vesionStr);
			        		 } catch (Exception e) {
			        			 return;
			        		 }
			        	 }
			        	 update.setCurrentVersionCode(oldVersionCode);
			        	 // 服务器上新版本比现在app的版本高的话就提示升级
			        	 if (oldVersionCode < newVersionCode) {
			        		 update.setNewVersionCode(newVersionCode); // 把int类型的版本号放进更新对象中
			        		 update.setCanUpdate(1); // 有跟新类容
			        	 } else {
			        		 update.setNewVersionCode(0); // 把int类型的版本号放进更新对象中
			        		 update.setCanUpdate(0); // 有跟新类容
			        	 }
			        	 DB.saveObj(ShopConst.Key.APP_UPP, update); // 保存跟新的对象
			         }
				}
			}
		}).execute();
	}
	
	//点击电话
	OnClickListener  phoneListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.dialog_tel), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
				@Override
				public void onOK() {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.myhome_tel)));
					getActivity().startActivity(intent);
					//友盟统计
					MobclickAgent.onEvent(getActivity(), "myhome_fragment_phone");
				}
			});
		}
	};
	
	/**
	 * 跳转
	 */
	@OnClick({ R.id.about_huiquan , R.id.ly_weixin})
	private void lineBankClick(View v) {
		switch (v.getId()) {
		/*case R.id.about_call:
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(getResources().getString(R.string.myhome_tel)));
			getActivity().startActivity(intent);
			break;
		case R.id.about_wechat:
			//startActivity(new Intent(getActivity(), MyHomeBankListActivity.class));
			break;*/
		case R.id.about_huiquan:
			Intent intentAbout = new Intent(getActivity(),AboutActivity.class);
			startActivity(intentAbout);
			break;
		case R.id.ly_weixin:
			startActivity(new Intent(getActivity(), SysWechatActivity.class));
			//友盟统计
			MobclickAgent.onEvent(getActivity(), "myhome_fragment_weixin");
			break;
		}
	}
	
	/**
	 *  版本升级
	 * @param view
	 */
	@OnClick(R.id.release_check)
	private void checkCoupon (View view) {
		mTvAppUpdate.setVisibility(View.GONE);
		if (null != mAppUpdate) {
			if (!Util.isEmpty(mAppUpdate.getVersionCode())) {
				if (mAppUpdate.getVersionCode().compareTo(mCurrentVesion) > 0 || mAppUpdate.getCanUpdate() == UPP_APP) {
					String url = mAppUpdate.getUpdateUrl();
					if (null != url || !Util.isEmpty(url)) {
			            DB.saveObj(ShopConst.Key.APP_UPP, mAppUpdate); // 保存跟新的对象
						UpdateService.startUpdateService(getActivity(), mAppUpdate.getUpdateUrl()); // TODO 自动更新
						mAppUpdate.setCanUpdate(0); // 有跟新类容
						getNewVersion(R.string.downloading);
					} else {
						getNewVersion(R.string.latest_version);
					}
				} else {
					getNewVersion(R.string.latest_version);
				}
			} else {
				getNewVersion(R.string.latest_version);
			}
		} else {
			getNewVersion(R.string.latest_version);
		}
	}
	
	/**
	 * 已经是最新版本
	 */
	private void getNewVersion (int id) {
		Util.getContentValidate(id);
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void trunIdenCode(View view){
		getActivity().finish();
	}
	
}
