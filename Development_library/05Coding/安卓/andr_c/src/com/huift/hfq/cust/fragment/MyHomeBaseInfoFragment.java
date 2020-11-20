package com.huift.hfq.cust.fragment;

import java.lang.reflect.Type;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huift.hfq.base.pojo.User;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.model.UserInfoTask;
import com.huift.hfq.cust.model.UserInfoTask.Callback;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

public class MyHomeBaseInfoFragment extends Fragment {
	private static final String TAG = MyHomeBaseInfoFragment.class.getSimpleName();
	private Type jsonType = new TypeToken<User>() {
	}.getType();
	private Gson gson;
	private User user;

	public static MyHomeBaseInfoFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeBaseInfoFragment fragment = new MyHomeBaseInfoFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myhome_baseinfo, container, false);
		ViewUtils.inject(this, v);
		init();
		initData();
		return v;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if(act==null){
			act=AppUtils.getActivity();
		}
		return act;
	}

	private void initData() {
		getData();
	}

	private void getData() {
		new UserInfoTask(getMyActivity(), new Callback() {

			@Override
			public void getResult(JSONObject result) {
				Log.d(TAG, result.toJSONString());
				user = gson.fromJson(result.toJSONString(), jsonType);
			}
		}).execute();
	}

	private void init() {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeBaseInfoFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeBaseInfoFragment.class.getSimpleName()); //统计页面
	}
}
