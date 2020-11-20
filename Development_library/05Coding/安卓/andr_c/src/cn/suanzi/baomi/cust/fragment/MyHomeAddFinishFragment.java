package cn.suanzi.baomi.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.MyHomeBankListActivity;
import cn.suanzi.baomi.cust.model.MyHomeAddBankTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 绑定银行卡完成
 * 
 * @author wensi.yu
 *
 */
public class MyHomeAddFinishFragment extends Fragment {
	
	private final static String TAG = MyHomeAddFinishFragment.class.getSimpleName();

	/**标题**/
	final static String BANK_TITLE = "绑定完成";
	/**功能描述 */
	@ViewInject(R.id.tv_bank_function_desc)
	TextView mTvBankTitle;
	/**添加*/
	@ViewInject(R.id.iv_bank_function_add)
	ImageView mTvBankAdd;
	/**返回**/
	@ViewInject(R.id.iv_bank_function_backup)
	ImageView mIvBankBack;
	/**完成**/
	@ViewInject(R.id.btn_addfinish_finish)
	Button mBtnAddFinish;
	
	
	public static MyHomeAddFinishFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeAddFinishFragment fragment = new MyHomeAddFinishFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myhomeaddfinish, container, false);// 说明v，注释// e.g:Fragment的view
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getMyActivity());
		init();
		return v;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if(act==null){
			act=AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		mTvBankTitle.setText(BANK_TITLE);
		//设置添加隐藏
		mTvBankAdd.setVisibility(0x00000004);
	}
	
	/**
	 * 返回到短信验证
	 * 
	 * 完成到银行卡列表
	 */
	@OnClick({R.id.iv_bank_function_backup,R.id.btn_addfinish_finish})
	public void btnBankReturnClick(View view) {
		switch (view.getId()) {
		case R.id.iv_bank_function_backup:
			getMyActivity().finish();
			break;
		case R.id.btn_addfinish_finish:
			//获得一个用户信息对象
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			String userCode = userToken.getUserCode();//用户编码
			String tokenCode = userToken.getTokenCode();//需要令牌认证
			
			//获得值
			SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences("actadd", Context.MODE_PRIVATE);
			//账户姓名
			String accountName=mSharedPreferences.getString("accountName", " ");
			//证件类型
			String certificateType=mSharedPreferences.getString("certificateType", " ");
			//证件号
			String certificateNbr=mSharedPreferences.getString("certificateNbr", " ");
			//银行卡号
			String accountNbr=mSharedPreferences.getString("accountNbr", " ");
			//预留手机号
			String reservedMobileNbr=mSharedPreferences.getString("reservedMobileNbr", " ");
			Log.i(TAG, "accountName+++++++++"+accountName);
		    Log.i(TAG, "certificateType+++++++"+certificateType);
		    Log.i(TAG, "certificateNbr+++++++"+certificateNbr);
		    Log.i(TAG, "accountNbr+++++++"+accountNbr);
		    Log.i(TAG, "reservedMobileNbr++++++++"+reservedMobileNbr);
			
			
			new MyHomeAddBankTask(getMyActivity()).execute(userCode,accountName,certificateType,certificateNbr,accountNbr,reservedMobileNbr,tokenCode);
			
			startActivity(new Intent(getMyActivity(), MyHomeBankListActivity.class));
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeAddFinishFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeAddFinishFragment.class.getSimpleName()); //统计页面
	}
}
