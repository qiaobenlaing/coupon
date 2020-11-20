package com.huift.hfq.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.BankList;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.MyHomeAddBankActivity;
import com.huift.hfq.cust.activity.MyHomeBankDetailActivity;
import com.huift.hfq.cust.adapter.BankListAdapter;
import com.huift.hfq.cust.model.MyHomeBankListTask;
import com.huift.hfq.cust.util.BankActivityUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 银行卡列表
 * @author wenis.yu
 * 
 */
public class MyHomeBankListFragment extends Fragment {

	private final static String TAG = MyHomeBankListFragment.class.getSimpleName();

	/** 添加 */
	/*@ViewInject(R.id.iv_bank_function_add)
	ImageView mTvBankAdd;*/
	/** 银行卡的列表 **/
	@ViewInject(R.id.lv_myhome_list)
	ListView mLvBankList;
	/** 加载银行卡列表的数据源 **/
	// List<BankList> mLvBankListData;
	/** 页码 **/
	private int mPage = 1;
	/** 标题 **/
	final static String BANK_TITLE = "我的银行卡";
	/** 适配器*/
	private BankListAdapter bankListAdapter = null; 

	public static MyHomeBankListFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeBankListFragment fragment = new MyHomeBankListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myhomebank_list, container, false);// 说明v，注释// e.g:Fragment的view
		ViewUtils.inject(this, v);
		
		//--------记录绑定银行卡流程-------
		/*ActivityUtils.add(getActivity());*/
		BankActivityUtils.add(getMyActivity());
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
		// 银行卡列表异步
		bankTask();
		// 给mLvBankList添加点击事件
		//mLvBankList.setOnItemClickListener(bankListListener);
		
		LinearLayout view = (LinearLayout) LayoutInflater.from(getMyActivity()).inflate(R.layout.fragment_myhomebank_list_footer,null);
		mLvBankList.addFooterView(view);
		ViewUtils.inject(this, view);
		if (bankListAdapter == null) {
			bankListAdapter = new BankListAdapter(getMyActivity(), null);
			mLvBankList.setAdapter(bankListAdapter);
		}
	}

	/**
	 * 银行卡列表的异步任务
	 */
	private void bankTask() {
		// 银行卡列表的异步
		new MyHomeBankListTask(getMyActivity(), new MyHomeBankListTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (null != result) {
					JSONArray mActBanktArray = (JSONArray) result.get("bankAccountList");
					List<BankList> mLvBankListData = new ArrayList<BankList>();

					for (int i = 0; i < mActBanktArray.size(); i++) {
						JSONObject myHomeBankObject = (JSONObject) mActBanktArray.get(i);
						// 把JsonObject对象转换为实体
						BankList item = Util.json2Obj(myHomeBankObject.toString(), BankList.class);
						mLvBankListData.add(item);
					}
					if (bankListAdapter == null) {
						bankListAdapter = new BankListAdapter(getMyActivity(), mLvBankListData);
						mLvBankList.setAdapter(bankListAdapter);
					} else {
						bankListAdapter.setItems(mLvBankListData);
					}
				}

			}
		}).execute(String.valueOf(mPage));
	}

	/**
	 * 给列表添加点击事件
	 */
	OnItemClickListener bankListListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			BankList bankItem = (BankList) mLvBankList.getAdapter().getItem(position);
			Bundle bundle = new Bundle();
			bundle.putString(MyHomeBankDetailFragment.BANK_CODE, bankItem.getBankAccountCode());
			Log.i(TAG, "bankcode:" + bankItem.getBankAccountCode());
			Intent intent = new Intent(getMyActivity(), MyHomeBankDetailActivity.class);
			intent.putExtras(bundle);
			getMyActivity().startActivity(intent);
			Log.i("TAG", "11");
		}
	};

	/**
	 * 返回到我的信息
	 * 
	 * 对银行卡进行添加
	 */
	@OnClick({ R.id.backup, R.id.add_bank })
	public void btnBankReturnClick(View view) {
		switch (view.getId()) {
		case R.id.backup:// 返回到银行卡列表
			// FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			// startActivity(new Intent(getMyActivity(), MyHomeFragment.class));
			getMyActivity().finish();
			break;
		case R.id.add_bank:// 对银行卡进行添加
			ActivityUtils.add(getMyActivity());
			Intent intentAdd = new Intent(getMyActivity(), MyHomeAddBankActivity.class);
			intentAdd.putExtra("title", getMyActivity().getResources().getString(R.string.myhome_add));
			startActivity(intentAdd);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeBankListFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeBankListFragment.class.getSimpleName()); //统计页面
	}
}
