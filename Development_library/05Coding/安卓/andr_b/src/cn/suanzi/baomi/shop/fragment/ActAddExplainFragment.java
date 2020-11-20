package cn.suanzi.baomi.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.ActAddPeopleActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author  wensi.yu
 * 营销活动说明
 */
public class ActAddExplainFragment extends Fragment {
	
	private final static String TAG = "ActAddExplainFragment";
	
	/** 返回图片**/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 上一步**/
	@ViewInject(R.id.btn_actaddexplain_laststep)
	private Button mBtnActAddLastStep;
	/** 下一步**/
	@ViewInject(R.id.btn_actaddexplain_nextstep)
	private Button mBtnActAddNextStep;
	/** 活动说明**/
	@ViewInject(R.id.et_actadd_context)
	private EditText mIvActAddContext;

	public static ActAddExplainFragment newInstance() {
		Bundle args = new Bundle();
		ActAddExplainFragment fragment = new ActAddExplainFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_actlist_addexplain,container, false);// 说明v，注释 e.g:Fragment的view
		ViewUtils.inject(this, view);
		inint();
		return view;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if(act == null){
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	/**
	 * 初始化
	 */
	private void inint() {
		//添加
		Util.addActivity(getMyActivity());
		ActivityUtils.add(getActivity());
		//设置标题
		mTvdesc.setText(R.string.tv_actlist_title);
		mIvBackup.setVisibility(View.VISIBLE);
	}

	/**
	 * 点击上一步返回到添加活动主题，开始时间，结束时间，活动地点
	 * @param view
	 */
	@OnClick(R.id.btn_actaddexplain_laststep)
	public void btnActAddLastStepClick(View view) {
		getActivity().finish();
	}

	/**
	 * 点击下一步到添加活动说明
	 * @param view
	 */
	@OnClick(R.id.btn_actaddexplain_nextstep)
	public void btnActAddNextStepClick(View view) {
		//活动说明
		String  txtContent = mIvActAddContext.getText().toString();
		switch (view.getId()) {
		case R.id.btn_actaddexplain_nextstep:
			Util.addActivity(getMyActivity());
			if (Util.isEmpty(txtContent)){
				Util.getContentValidate(R.string.toast_actadd_content);
				mIvActAddContext.findFocus();
				break;
			}
			//保存值
			SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(ShopConst.ActAdd.ACT_ADD, Context.MODE_PRIVATE);
		    Editor editor = mSharedPreferences.edit();       
		    editor.putString("txtContent", txtContent);
		    editor.commit();
			Intent intent = new Intent(getMyActivity(), ActAddPeopleActivity.class);
			getMyActivity().startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 点击返回按钮
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddBackClick(View view) {
		getMyActivity().finish();
	}
}
