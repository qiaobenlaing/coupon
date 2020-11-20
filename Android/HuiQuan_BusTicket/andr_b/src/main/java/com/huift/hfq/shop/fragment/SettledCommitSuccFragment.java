package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 提交成功(返回到登陆页)
 * @author wensi.yu
 *
 */
public class SettledCommitSuccFragment extends Fragment {

	private static final String SETTLET_TITLE = "提交成功";
	/**返回图片*/
	private LinearLayout mIvBackup;
	/**功能描述文本*/
	private TextView mTvdesc;
	/**返回登陆页*/
	private Button mBackLogin;

	public static SettledCommitSuccFragment newInstance() {
		Bundle args = new Bundle();
		SettledCommitSuccFragment fragment = new SettledCommitSuccFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_commitsucc, container,false);
		ViewUtils.inject(this,view);
		ActivityUtils.add(getMyActivity());
		init(view);
		return view;
	}

	private void init(View view) {
		mIvBackup = (LinearLayout)view.findViewById(R.id.layout_turn_in);
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc = (TextView) view.findViewById(R.id.tv_mid_content);
		mTvdesc.setText(SETTLET_TITLE);
		mBackLogin = (Button) view.findViewById(R.id.btn_commitsucc_login);
		mBackLogin.setVisibility(View.VISIBLE);
	}

	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null){
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 返回
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in,R.id.btn_commitsucc_login})
	public void trunIdenCode(View view){
		switch (view.getId()) {
			case R.id.layout_turn_in:
				getMyActivity().finish();
				break;
			case R.id.btn_commitsucc_login://返回登录页
				ActivityUtils.finishAll();
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
