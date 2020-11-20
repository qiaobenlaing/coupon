package com.huift.hfq.shop.fragment;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Bonus;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.model.ChangeBonusStatusTask;
import com.huift.hfq.shop.model.getBonusInfoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 红包详情
 * @author wensi.yu
 */
public class ActMoneyDetailFragment extends Fragment{
	
	private final static String TAG = "ActMoneyDetailFragment";
	/** 停用*/
	public final static int STOP_STATUS = 0; 
	/** 启用*/
	public final static int ENABLE_STATUS = 1;
	public final static int ENABLE = 1;
	public final static int STOP = 0;
	public final static String STOP_STR = "停发";
	public final static String LIST_BONUS = "listBonus";
	public final static String BOUNS_SOURCE = "bounsSource";
	/**红包编码*/
	public final static String ACTMONEY_CODE = "mActMoneyCode";
	/**红包批次*/
	public final static String ACTMONEY_BATCH = "mActMoneyBatch";
	public final static String ACTMONEY_SOURCE = "actMoneySource";
	/**返回图片**/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/**请用/停用*/
	@ViewInject(R.id.tv_msg)
	private TextView mTvEdit;
	/**得到的红包code*/
	private String mMoneyCode;
	/**得到的红包的批次*/
	private String mMoneyBatch;
	/**红包的批次*/
	@ViewInject(R.id.tv_actmoneydetail_batch)
	private TextView mActMoneyBatch;
	/**红包数量*/
	@ViewInject(R.id.tv_actmoneydetail_number)
	private TextView mActMoneyNumber;
	/**金额区间*/
	@ViewInject(R.id.tv_actmoneydetail_region)
	private TextView mActMoneyRegion;
	/**红包总额*/
	@ViewInject(R.id.tv_actmoneydetail_amount)
	private TextView mActMoneyAmount;
	/**开始时间*/
	@ViewInject(R.id.tv_actmoneydetail_start)
	private TextView mActMoneyStart;
	/**结束时间*/
	@ViewInject(R.id.tv_actmoneydetail_end)
	private TextView mActMoneyEnd;
	/**用户对象*/
	private UserToken mUserToken;
	private String mTokenCode;
	/** 领取人数的进度条*/
	private ProgressBar mProgressBar;
	/** 进去人数进度条的说明文字*/
	private TextView mTvDrawBouns;
	/** 红包对象*/
	private Bonus mBouns;
	private String mBounsSorce;
	
	public static ActMoneyDetailFragment newInstance() {
		Bundle args = new Bundle();
		ActMoneyDetailFragment fragment = new ActMoneyDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_actmoney_detail,container, false);
		ViewUtils.inject(this, view);
		init(view);
		return view;
	}

	/**
	 * 初始化
	 * @param view
	 */
	private void init(View view) {
		//保存
		Util.addActivity(getActivity());
		Util.addLoginActivity(getActivity());
		//设置标题
		mTvdesc.setText(R.string.tv_actmoney_detail);
		mIvBackup.setVisibility(View.VISIBLE);
		//领取人数
		mProgressBar = (ProgressBar) view.findViewById(R.id.progress_horizontal_receivenumber);
		//进度条的说明文字
		mTvDrawBouns = (TextView) view.findViewById(R.id.tv_actmoneydetail_receiver);
		//传过来的code
		Intent intent = getActivity().getIntent();
		mMoneyCode = intent.getStringExtra(ACTMONEY_CODE);
		Log.d(TAG, "mMoneyCode========="+mMoneyCode);
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();
		//红包详情
		getBonusInfo(view);
	}
	
	
	/**
	 * 红包详情
	 */
	private void getBonusInfo(final View view){
		mTvEdit.setEnabled(false);
		new getBonusInfoTask(getActivity(), new getBonusInfoTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				mTvEdit.setEnabled(true);
				//领取人数
				ProgressBar pgDrawBouns = (ProgressBar) view.findViewById(R.id.pg_drawbouns);
				//进度条的说明文字
				TextView tvDrawBouns = (TextView) view.findViewById(R.id.tv_drawbouns);
				
				if (result == null) {
					return;
				} 
				mBouns = Util.json2Obj(result.toString(), Bonus.class);
				String msg = mTvEdit.getText().toString();
				DB.saveStr(ShopConst.ACTMONEY_TARTUS, msg);
				
				if(Util.isEmpty(mBouns.getBatchNbr())){
					mActMoneyBatch.setVisibility(View.GONE);
				}else{
					mActMoneyBatch.setVisibility(View.VISIBLE);
					mActMoneyBatch.setText(mBouns.getBatchNbr());
				}
				
				if(Util.isEmpty(mBouns.getTotalVolume())){
					mActMoneyNumber.setVisibility(View.GONE);
				}else{
					mActMoneyNumber.setVisibility(View.VISIBLE);
					mActMoneyNumber.setText(mBouns.getTotalVolume() + " 个");
				}
				
				if(Util.isEmpty(mBouns.getUpperPrice())){
					mActMoneyRegion.setVisibility(View.GONE);
				}else{
					mActMoneyRegion.setVisibility(View.VISIBLE);
					mActMoneyRegion.setText(mBouns.getLowerPrice()+ "-" + mBouns.getUpperPrice() + " 元");
				}
				
				if(Util.isEmpty(mBouns.getTotalValue())){
					mActMoneyAmount.setVisibility(View.GONE);
				}else{
					mActMoneyAmount.setVisibility(View.VISIBLE);
					mActMoneyAmount.setText(mBouns.getTotalValue() + " 元");
				}
				
				if(Util.isEmpty(mBouns.getStartTime())){
					mActMoneyStart.setVisibility(View.GONE);
				}else{
					mActMoneyStart.setVisibility(View.VISIBLE);
					mActMoneyStart.setText(mBouns.getStartTime());
				}
				
				if(Util.isEmpty(mBouns.getEndTime())){
					mActMoneyEnd.setVisibility(View.GONE);
				}else{
					mActMoneyEnd.setVisibility(View.VISIBLE);
					mActMoneyEnd.setText(mBouns.getEndTime());
				}
				
				
				if (!Util.isEmpty(mBouns.getStatus()+"")) {
					// 1-停用；0-启用
					if (mBouns.getStatus() == STOP) { 
						mTvEdit.setText(getString(R.string.tv_actmoney_enable));
					} else {
						mTvEdit.setText(getString(R.string.tv_actmoney_user));
					}
				}
				
				try{
					getStopBouns(mTvEdit.getText().toString()); //  领取的进度条设置
					// 使用人数进度条设置
					double getValue = Double.parseDouble(mBouns.getGetValue()); // 领取红包金额
					double totalValue = Double.parseDouble(mBouns.getTotalValue()); // 红包总金额
					
					if (getValue <= 0) {
						tvDrawBouns.setText("未领取(0/"+ mBouns.getTotalVolume() +")"); //批次
						pgDrawBouns.setMax((int)Math.floor(totalValue));
						pgDrawBouns.setProgress(0);
					} else {
						pgDrawBouns.setMax((int)Math.floor(totalValue));
						pgDrawBouns.setProgress((int)Math.floor(getValue));
						tvDrawBouns.setText("已领取"+ mBouns.getGetValuePercent()+"% ("+ mBouns.getGetValue() + "/"+ mBouns.getTotalValue() +")"); //批次
					}
					
					
				} catch (Exception e) {
					Log.e(TAG, "优惠券批次转换出错="+e.getMessage()); //TODO
				}
				
			}
		}).execute(mMoneyCode,mTokenCode);
	}
	
	/**
	 * 启用/停发
	 * @param view
	 */
	@OnClick({R.id.tv_msg,R.id.layout_turn_in})
	public void trunIdenCode(final View view){
		switch (view.getId()) {
		case R.id.tv_msg: //停发
			String stop = getString(R.string.tv_actmoney_user); // 停发
			String enable = getString(R.string.tv_actmoney_enable); //启用
			String edtStop = mTvEdit.getText().toString();
			if (edtStop.equals(stop)) { // 文本等于停发
				getBounsStatus (STOP_STATUS + "",stop,enable);
			} else {
				getBounsStatus (ENABLE_STATUS + "",enable,stop);
			}
			
			break;
		case R.id.layout_turn_in: //返回
			Intent intent = new Intent(getActivity(), ActListMoneyFragment.class);
			intent.putExtra(ActListMoneyFragment.MONEY_STATUS, mTvEdit.getText().toString());
			intent.putExtra(ActListMoneyFragment.BOUBNS_CODE, mMoneyCode);
			getActivity().setResult(ActListMoneyFragment.BOUNS_SUCCESS, intent);
			getActivity().finish();
			break;

		default:
			break;
		}
	}
	
	public static boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK){
	    	//finishClick();
	    	return false;
	    }
	    return true;
	}
	
	
	/**
	 * 退出
	 */
	private void finishClick () {
		Intent intent = null;
		if (mBounsSorce.equals(LIST_BONUS)) {
			intent = new Intent(getActivity(), ActListMoneyFragment.class);
		} 
		intent.putExtra(ActListMoneyFragment.newInstance().MONEY_STATUS, mTvEdit.getText().toString());
		String bounsCode = "";
		if(mBouns != null){
			bounsCode = mBouns.getBonusCode();
		}else{
			bounsCode = "";
		}
		intent.putExtra(ActListMoneyFragment.newInstance().BOUBNS_CODE, bounsCode);
		if(mBounsSorce.equals(LIST_BONUS)){
			//setResult(ActListMoneyFragment.BOUNS_SUCCESS, intent);
		}
	}
	
	/**
	 * 判断红包状态
	 * @param status
	 * @param stop
	 * @param enable
	 */
	private void getBounsStatus (String status,final String stop,final String enable) {
		mTvEdit.setEnabled(false);
		new ChangeBonusStatusTask(getActivity(), new ChangeBonusStatusTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				mTvEdit.setEnabled(true);
				if(result == null){
					mTvEdit.setText(stop);
				}
				if(result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))){
					mTvEdit.setText(enable);
					getStopBouns(enable);
				}else{
					mTvEdit.setText(stop);
				}
				
			}
		}).execute(mMoneyCode,status,mTokenCode);
	}
	
	/**
	 * 进度条的状态
	 * @param stop
	 */
	private void getStopBouns (String stop) {
		int getNub = Integer.parseInt(mBouns.getGetNbr());
		int totalVolume = Integer.parseInt(mBouns.getTotalVolume());
		if (!stop.equals(STOP_STR)) {
			if (Util.isEmpty(mBouns.getGetNbr()) || getNub <= 0) {
				mTvDrawBouns.setText("未领取" + STOP_STR + " (0/"+ mBouns.getTotalVolume() +")"); //批次
				mProgressBar.setMax(totalVolume);
				mProgressBar.setProgress(0);
			} else {
				mTvDrawBouns.setText("已领取"+ STOP_STR + "("+ mBouns.getGetNbr() + "/"+ mBouns.getTotalVolume() +")"); //批次
				mProgressBar.setMax(totalVolume);
				mProgressBar.setProgress(getNub);
			}
		} else {
			if (Util.isEmpty(mBouns.getGetNbr()) || getNub <= 0) {
				mTvDrawBouns.setText("未领取 (0/"+ mBouns.getTotalVolume() +")"); //批次
			} else {
				mTvDrawBouns.setText("已领取"+ mBouns.getGetPercent() +"% ("+ mBouns.getGetNbr() + "/"+ mBouns.getTotalVolume() +")"); //批次
			}
		}
	}
}
