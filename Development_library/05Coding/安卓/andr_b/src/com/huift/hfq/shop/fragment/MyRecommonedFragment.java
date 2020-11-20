package com.huift.hfq.shop.fragment;

import java.util.Calendar;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Invite;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.model.GetShopInviteCodeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 用户推荐
 * @author wenis.yu
 *
 */
public class MyRecommonedFragment extends Fragment{
	
	private static final String TAG = MyRecommonedFragment.class.getSimpleName();
	
	private static final String RECOMMONED_PER = "1";
	private static final String RECOMMONED_TWELFTH = "12";
	private static final int NUM_AERO = 1;
	private static final int NUM_TWELFTH = 12;
	private static final int NOW_MONTH = 1;
	/** 返回图片 */
	@ViewInject(R.id.layout_turn_in)  
	private LinearLayout mIvBackup;
	/** 功能描述文本 */
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 显示上一个月*/
	private ImageView mTvRecommonedLastMonth;
	/** 显示下一个月*/
	private ImageView mTvRecommonedNextMonth;
	/** 推荐码*/
	private TextView mTvRecommonedCode;
	/** 注册人数*/
	private TextView mTvRecommonedRegister;
	/** 消费人数*/
	private TextView mTvRecommonedConsume;
	/** 分享*/
	private TextView mTvRecommonedShare;
	/** 当前月份*/
	private TextView mTvMonth;
	private String mTvNowMonth;
	/** 使用的类*/
	private Invite mInvite;
	/** 加载*/
	private ProgressBar mPrRecommonedData;
	/** 月份*/
	private int mMonth;
	
	public static MyRecommonedFragment newInstance() {
		Bundle args = new Bundle();
		MyRecommonedFragment fragment = new MyRecommonedFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myrecommoned, container, false);
		ViewUtils.inject(this, v);
		Util.addRecommonedActivity(getMyActivity());
		init(v);
		return v;
	}
	
	/**
	 * 保证activity不为空
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 初始化
	 * @param v
	 */
	private void init(View v) {
		//设置标题
		TextView msg = (TextView) v.findViewById(R.id.tv_msg);
        msg.setVisibility(View.GONE);
		mTvdesc.setText(R.string.my_recommoned);
		mIvBackup.setVisibility(View.VISIBLE);
		mTvRecommonedCode = (TextView) v.findViewById(R.id.tv_recommoned_code);
		mTvRecommonedLastMonth = (ImageView) v.findViewById(R.id.tv_myrecommoned_back);
		mTvRecommonedNextMonth = (ImageView) v.findViewById(R.id.tv_myrecommoned_to);
		mTvRecommonedRegister = (TextView) v.findViewById(R.id.tv_myrecommoned_person);
		mTvRecommonedConsume = (TextView) v.findViewById(R.id.tv_myrecommoned_bonus);
		mTvMonth = (TextView) v.findViewById(R.id.tv_myrecommoned_month);
		Calendar c = Calendar.getInstance();
		mMonth = c.get(Calendar.MONTH)+1;//获取当前月份;
		Log.d(TAG, "当前月份==="+mMonth);
		mTvMonth.setText(String.valueOf(mMonth));
		DB.saveStr(ShopConst.RECOMMONED_MONTH,mTvNowMonth);
		mTvRecommonedShare = (TextView)v.findViewById(R.id.tv_myrecommoned_share);
		mPrRecommonedData = (ProgressBar) v.findViewById(R.id.pr_recommoned_data);
		//调用
		getUserInviteCode();
	}
	
	@OnClick({R.id.tv_myrecommoned_back,R.id.tv_myrecommoned_to})
	private void monthClick(View view){
		switch (view.getId()) {
		case R.id.tv_myrecommoned_back:
			//mMonth = Integer.parseInt(mTvNowMonth);
			if (mMonth == NUM_AERO) {
				Log.d(TAG, "进来了0");
				mTvNowMonth = RECOMMONED_PER;
				break;
			} else {
				mMonth--;
				Log.d(TAG, "进来了1");
				mTvNowMonth = mMonth + "";
			}
			mTvMonth.setText(mTvNowMonth);
			Log.d(TAG, "strMonth=="+String.valueOf(mMonth));
			getUserInviteCode();
			break;
			
		case R.id.tv_myrecommoned_to:
			mMonth = Integer.parseInt(mTvNowMonth);
			if (mMonth == NUM_TWELFTH) {
				Log.d(TAG, "进来了0");
				mTvNowMonth = RECOMMONED_TWELFTH;
				break;
			} else {
				mMonth++;
				Log.d(TAG, "进来了1");
				mTvNowMonth = mMonth + "";
			}
			mTvMonth.setText(mTvNowMonth);
			Log.d(TAG, "strMonth=="+String.valueOf(mMonth));
			getUserInviteCode();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 推荐码
	 * @param v
	 */
	private void getUserInviteCode(){
		
		new GetShopInviteCodeTask(getMyActivity(), new GetShopInviteCodeTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mPrRecommonedData.setVisibility(View.GONE);
				if (null == result) {
					return;
				}
				mInvite = Util.json2Obj(result.toString(), Invite.class);
				
				if (Util.isEmpty(mInvite.getInviteCode())) {
					mTvRecommonedCode.setText(RECOMMONED_PER);
				} else {
					mTvRecommonedCode.setText(mInvite.getInviteCode());
				}
				
				if (Util.isEmpty(mInvite.getRegNbr())) {
					mTvRecommonedRegister.setText(RECOMMONED_PER);
				} else {
					mTvRecommonedRegister.setText(mInvite.getRegNbr());
				}
				
				if (Util.isEmpty(mInvite.getRegACNbr())){
					mTvRecommonedConsume.setText(RECOMMONED_PER);
				} else {
					mTvRecommonedRegister.setText(mInvite.getRegACNbr());
				}
				
				mTvRecommonedShare.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (Util.isEmpty(mInvite.getInviteCode())) {
							Util.getContentValidate(R.string.get_recommoned);
							return;
						} 
						String filePath = Tools.getFilePath(getMyActivity()) + Tools.APP_ICON;
//						String logoUrl = Const.IMG_URL + coupon.getLogoUrl();
						String logoUrl = "";
						Tools.showRecommShare(getMyActivity(), "Index/invitationCodeShare/code/" ,"","",mTvRecommonedCode.getText().toString(),filePath,logoUrl);
					}
				});
			}
		}).execute(String.valueOf(mMonth));
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void trunIdenCode(View view){
		getMyActivity().finish();
	}
}
