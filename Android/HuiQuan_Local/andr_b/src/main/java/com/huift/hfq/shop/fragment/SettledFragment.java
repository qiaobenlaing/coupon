package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.activity.SettledShopInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我要入驻
 * @author wensi.yu
 *
 */
public class SettledFragment extends Fragment {

	private final static String TAG = "SettledActivity";
	
	private static final String SETTLET_TITLE = "我要入驻";
	/** 返回图片*/
	private LinearLayout mIvBackup;
	/** 功能描述文本*/
	private TextView mTvdesc;
	
	public static SettledFragment newInstance() { 
		Bundle args = new Bundle();
		SettledFragment fragment = new SettledFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_settled, container,false);
		ViewUtils.inject(this,view);
		ActivityUtils.add(getMyActivity());
		Util.addActivity(getMyActivity());
		AppUtils.setActivity(getMyActivity());
		init(view);
		return view;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null){
			act = AppUtils.getActivity();
		}
		return act;       
	}

	private void init(View view) {
		mIvBackup = (LinearLayout)view.findViewById(R.id.layout_turn_in);
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc = (TextView) view.findViewById(R.id.tv_mid_content);
		mTvdesc.setText(SETTLET_TITLE);
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in,R.id.btn_settled_shop,R.id.tv_settled_phone})
	public void trunIdenCode(View view){
		switch (view.getId()) {
		case R.id.layout_turn_in:
			getMyActivity().finish();
			break;
		case R.id.btn_settled_shop://去开店
			startActivity(new Intent(getMyActivity(),SettledShopInfoActivity.class ));
			break;
		case R.id.tv_settled_phone://联系
			DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.myafter_order_phone_ok), getString(R.string.ok), getString(R.string.no),new DialogUtils().new OnResultListener() {
				@Override
				public void onOK() {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(Util.getString(R.string.myhome_tel)));
					getActivity().startActivity(intent);
				}
			});
			break;
		default:
			break;
		}
		
	}

	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(getMyActivity());
    }
}
