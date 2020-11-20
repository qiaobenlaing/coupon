// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;
import net.minidev.json.JSONObject;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Campaign;
import com.huift.hfq.base.pojo.Share;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ActThemeDetailActivity;
import com.huift.hfq.shop.activity.CampaignAddActivity;
import com.huift.hfq.shop.activity.CampaignListActivity;
import com.huift.hfq.shop.activity.EditAcitityMsgActivity;
import com.huift.hfq.shop.activity.PredeterListActivity;
import com.huift.hfq.shop.model.ChangeActivityStatusTask;
import com.huift.hfq.shop.model.DelActivityTask;
import com.huift.hfq.shop.model.SgetActInfoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 活动管理界面
 * @author qian.zhou
 */
public class ActivityManagerFragment extends Fragment {
	private static final String TAG = ActivityManagerFragment.class.getSimpleName();
	/** 活动编码*/
	private String mActivityCode;
	/** 正在加载数据 */
	private LinearLayout mLyNodate;
	/** 正在加载的内容 */
	private LinearLayout mLyContent;
	/** 标示(1 已发布、活动满员  , 2 未发布 、活动已取消, 3 活动已停止报名  , 4 活动已结束)*/
	private String mIsAct;
	/** 分享*/
	private Share mShare;
	/** 导向箭头*/
	private ImageView mIvArrow;
	/** 编辑活动*/
	private RelativeLayout mRyEditAct;
	/** 删除活动*/
	private RelativeLayout mRyDeltAct;
	/** 全局视图*/
	private View mView;
	
	/**
	 * 需要传递参数时有利于解耦 
	 * @return PosPayFragment
	 */
	public static ActivityManagerFragment newInstance() {
		Bundle args = new Bundle();
		ActivityManagerFragment fragment = new ActivityManagerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_actmanager, container, false);
		ViewUtils.inject(this, mView);
        Util.addLoginActivity(getMyActivity());
		init(mView);
		return mView;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	/**
	 * 初始化数据
	 */
	private void init(View v) {
		//取值
		mActivityCode = DB.getStr(ShopConst.UppActStatus.ACT_CODE);
		// 加载数据
		mLyNodate = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mLyContent = (LinearLayout) v.findViewById(R.id.ly_content);
		//标题
		TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvContent.setText(R.string.act_manager);
		mIvArrow = (ImageView) v.findViewById(R.id.iv_arrow);
		setData(0); // 没有数据
		//获得活动管理信息
		getActInfo(v);
	}
	
	/**
	 * 加载活动h5
	 */
	OnClickListener actListener  = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getMyActivity(), ActThemeDetailActivity.class);
			String url = "Browser/getActInfo?activityCode="+ mActivityCode;
			intent.putExtra(ActThemeDetailActivity.THEME_URL,url);
			startActivity(intent);
		}
	};
	
	/**
	 * 设置数据
	 * @param type
	 * 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData(int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyContent.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 获得活动管理的信息
	 */
	public void getActInfo(final View v) {
		new SgetActInfoTask(getActivity(), new SgetActInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				}
				setData(1); // 有数据
				ImageView ivActImage = (ImageView) v.findViewById(R.id.iv_act_image);//活动图片
				TextView tvActContent = (TextView) v.findViewById(R.id.tv_act_content);//活动内容
				TextView tvActRead = (TextView) v.findViewById(R.id.tv_read_num);//活动阅读量
				TextView tvActCollect = (TextView) v.findViewById(R.id.tv_collect_num);//活动收藏量
				TextView tvActStatus = (TextView) v.findViewById(R.id.tv_act_status);//活动状态
				TextView tvActSignStatus = (TextView) v.findViewById(R.id.tv_act_sign_status);//活动报名人数
				TextView tvEditAct = (TextView) v.findViewById(R.id.tv_editact);//编辑活动
				TextView tvDelAct = (TextView) v.findViewById(R.id.tv_delact);//删除活动
				RelativeLayout ryPredeterList = (RelativeLayout) v.findViewById(R.id.ry_predetermined_list);//预定名单
				mRyEditAct = (RelativeLayout) v.findViewById(R.id.ry_activity_edit);//编辑活动
				mRyDeltAct = (RelativeLayout) v.findViewById(R.id.ry_del_activity);//删除活动
				Button btnact = (Button) v.findViewById(R.id.btn_release_activity);//发布活动
				//加载活动H5
				LinearLayout lyActivity = (LinearLayout) v.findViewById(R.id.ly_activity);
				lyActivity.setOnClickListener(actListener);
				//活动的信息
				Campaign campaign = Util.json2Obj(result.toString(), Campaign.class);
				//分享的信息
				mShare = Util.json2Obj(result.get("shareArr").toString(), Share.class);
				//活动图片
				if (!Util.isEmpty(campaign.getActivityImg())) {
					Util.showImage(getActivity(), campaign.getActivityImg(), ivActImage);
				} else {
					Log.d(TAG, "图片的路径为空");
				}
				//活动内容
				tvActContent.setText(!Util.isEmpty(campaign.getActivityName()) ? campaign.getActivityName() : "");
				//活动阅读量
				tvActRead.setText(!Util.isEmpty(campaign.getPageviews()) ? campaign.getPageviews() : "");
				//收藏量
				tvActCollect.setText(!Util.isEmpty(campaign.getCollectNbr()) ? campaign.getCollectNbr() : "");
				//根据活动状态显示发布活动按钮的状态
				getStatus(campaign.getStatus(), tvActStatus, btnact);
				//活动状态（免费与不免费）
				if (!Util.isEmpty(campaign.getTotalPayment()) && campaign.getTotalPayment().equals(String.valueOf(Util.NUM_ZERO)) ) {//免费
					getActStatusIsFree(campaign.getStatus(), tvEditAct, tvDelAct, mRyEditAct, mRyDeltAct);
					ryPredeterList.setVisibility(View.GONE);
					tvActSignStatus.setVisibility(View.GONE);
				} else {//不免费
					ryPredeterList.setVisibility(View.VISIBLE);
					getActStatusNotFree(campaign.getStatus(), tvEditAct, tvDelAct, mRyEditAct, mRyDeltAct);
					tvActSignStatus.setVisibility(View.VISIBLE);
				}
				//活动报名人数
				if (campaign.getLimitedParticipators().equals(String.valueOf(Util.NUM_ZERO))) {
					tvActSignStatus.setText(!Util.isEmpty(campaign.getParticipators()) ? "已报名 ：" +  campaign.getParticipators() : "已报名 ：" + "0");
				} else {
					tvActSignStatus.setText(!Util.isEmpty(campaign.getParticipators()) ? "已报名 ：" +  campaign.getParticipators() + "/" + campaign.getLimitedParticipators() : "已报名 ：" + "0");
				}
				ryPredeterList.setOnClickListener(clickListener);
				mRyEditAct.setOnClickListener(clickListener);
				mRyDeltAct.setOnClickListener(clickListener);
				btnact.setOnClickListener(clickListener);
			}
		}).execute(mActivityCode);
	}
	
	/**
	 * 点击事件
	 */
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ry_predetermined_list://预定名单
				Intent intent = new Intent(getMyActivity(), PredeterListActivity.class);
				intent.putExtra(PredeterListFragment.ACTIVITY_CODE, mActivityCode);
				startActivity(intent);
				break;
			case R.id.ry_activity_edit:
				if (mIsAct.equals(String.valueOf(Util.NUM_ONE))) {//暂停报名
					DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.stop_sign_up), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
						@Override
						public void onOK() {
							
							updateActivity(ShopConst.UppActStatus.STOP_SIGN_UP);
						} 
					});
				} else if (mIsAct.equals(String.valueOf(Util.NUM_TWO))) {//编辑活动
					
					Intent edtintent = new Intent(getMyActivity(), EditAcitityMsgActivity.class);
					//edtintent.putExtra(EditAcitityMsgFragment.ACTIVITY_CODE, mActivityCode);
					startActivity(edtintent); 
					
				} else if (mIsAct.equals(String.valueOf(Util.NUM_THIRD))) {//活动开始报名
					DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.begin_activity), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
						@Override
						public void onOK() {
							
							updateActivity(ShopConst.UppActStatus.ENABLE_OR_RELEASE);
						} 
					});
					 
				} else if (mIsAct.equals(String.valueOf(Util.NUM_FOUR))) {//重新发起活动
					
					Intent addintent = new Intent(getMyActivity(), CampaignAddActivity.class);
					startActivity(addintent); 
				}
				break;
			case R.id.ry_del_activity:
				if (mIsAct.equals(String.valueOf(Util.NUM_ONE))) {//活动取消
					
					DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.cancel_activity), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
						@Override
						public void onOK() {
							updateActivity(ShopConst.UppActStatus.EVENTS_CANCELED);
						} 
					});
				} else if (mIsAct.equals(String.valueOf(Util.NUM_TWO))) {//删除活动
					
					DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.del_activity), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
						@Override
						public void onOK() {
							delActivity();
						} 
					});
				} 
				break;
			case R.id.btn_release_activity://发布活动
				
				DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.start_activity), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
					@Override
					public void onOK() {
						
						updateActivity(ShopConst.UppActStatus.ENABLE_OR_RELEASE);
					} 
				});
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 删除活动信息
	 */
	public void delActivity(){
		new DelActivityTask(getMyActivity(), new DelActivityTask.Callback() {
			@Override
			public void getResult(int retCode) {
				if (retCode == 0) {
					return ;
				} else {
					if (retCode == ErrorCode.SUCC) {
						Intent intent = new Intent(getMyActivity(), CampaignListActivity.class);
						getMyActivity().setResult(CampaignListFragment.RESULT_DEL_ACT, intent);
						getMyActivity().finish();
					} else {
						Util.getContentValidate(R.string.myafter_order_detail_error);
					}
				}
			}
		}).execute(mActivityCode);
	}
	
	/**
	 * 改变活动状态
	 */
	public void updateActivity(String status){
		new ChangeActivityStatusTask(getMyActivity(), new ChangeActivityStatusTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					 return;
				} else {
					if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
						Intent intent = new Intent(getMyActivity(), CampaignListActivity.class);
						getMyActivity().setResult(CampaignListFragment.RESULT_DEL_ACT, intent);
						getMyActivity().finish();
					} else {
						if (ShopConst.UppActStatus.ACTIVITY_NOT_EXIST.equals(result.get("code").toString())) {
							
							Util.getContentValidate(R.string.activity_not_exist);
							
						} else if (ShopConst.UppActStatus.EVENTS_UNALTERABLE.equals(result.get("code").toString())) {
							
							Util.getContentValidate(R.string.activity_events_unalterable);
							
						} else if (ShopConst.UppActStatus.ACTIVITY_NO_CHANGE.equals(result.get("code").toString())) {
							
							Util.getContentValidate(R.string.activity_no_change);
						} else {
							
							Util.getContentValidate(R.string.update_fail);
						}
					} 
				}
			}
		}).execute(mActivityCode, status);
	}
	
	/**
	 * 根据活动状态判断操作(不免费)
	 * @param status 活动状态 (1 已发布   0 未发布)
	 * @param editact 编辑活动
	 * @param delact 删除活动
	 */
	public void getActStatusNotFree(String status, TextView editact, TextView delact, RelativeLayout ryEditAct, RelativeLayout rydelact){
		 if (status.equals(String.valueOf(Util.NUM_ZERO))) {//未发布
			mIvArrow.setVisibility(View.VISIBLE);
			editact.setText(R.string.edit_act);
			delact.setText(R.string.activity_del);
			setVisibility(true, true, ryEditAct, rydelact);
			mIsAct = ShopConst.UppActStatus.STOP_SIGN_UP;
			
		} else if (status.equals(String.valueOf(Util.NUM_ONE))) {//已发布
			mIvArrow.setVisibility(View.GONE);
			editact.setText(R.string.suspend_registration);
			delact.setText(R.string.events_canceled);
			setVisibility(true, true, ryEditAct, rydelact);
			mIsAct = ShopConst.UppActStatus.ENABLE_OR_RELEASE;
			
		} else if (status.equals(String.valueOf(Util.NUM_TWO))) {//停止报名
			mIvArrow.setVisibility(View.GONE);
			editact.setText(R.string.begin_registration);
			setVisibility(true, false, ryEditAct, rydelact);
			mIsAct = ShopConst.UppActStatus.EVENTS_CANCELED;
			
		} else if (status.equals(String.valueOf(Util.NUM_THIRD))) {//活动取消
			mIvArrow.setVisibility(View.VISIBLE);
			editact.setText(R.string.edit_act);
			delact.setText(R.string.activity_del);
			setVisibility(true, true, ryEditAct, rydelact);
			mIsAct = ShopConst.UppActStatus.STOP_SIGN_UP;
			
 		} else if (status.equals(String.valueOf(Util.NUM_FOUR))) {//活动已结束
			mIvArrow.setVisibility(View.GONE);
			editact.setText(R.string.relaunching_activities);
			setVisibility(true, false, ryEditAct, rydelact);
			mIsAct = ShopConst.UppActStatus.ACTIVITY_HAS_ENDED;
			
 		} else if (status.equals(String.valueOf(Util.NUM_FIVE))) {//活动已满员
			mIvArrow.setVisibility(View.GONE);
			delact.setText(R.string.events_canceled);
			setVisibility(false, true, ryEditAct, rydelact);
			mIsAct = ShopConst.UppActStatus.ACTIVITY_HAS_ENDED;
 		} 
	}
	
	/**
	 * 根据活动状态判断操作(免费)
	 * @param status 活动状态 (1 已发布   0 未发布)
	 * @param editact 编辑活动
	 * @param delact 删除活动
	 */
	public void getActStatusIsFree(String status, TextView editact, TextView delact, RelativeLayout ryEditAct, RelativeLayout rydelact){
		 if (status.equals(String.valueOf(Util.NUM_ZERO))) {//未发布
			mIvArrow.setVisibility(View.VISIBLE);
			editact.setText(R.string.edit_act);
			delact.setText(R.string.activity_del);
			setVisibility(true, true, ryEditAct, rydelact);
			mIsAct = "2";
			
		} else if (status.equals(String.valueOf(Util.NUM_ONE))) {//已发布
			mIvArrow.setVisibility(View.GONE);
			delact.setText(R.string.events_canceled);
			setVisibility(false, true, ryEditAct, rydelact);
			mIsAct = "1";
			
		} else if (status.equals(String.valueOf(Util.NUM_THIRD))) {//活动取消
			mIvArrow.setVisibility(View.VISIBLE);
			editact.setText(R.string.edit_act);
			delact.setText(R.string.activity_del);
			setVisibility(true, true, ryEditAct, rydelact);
			mIsAct = "2";
			
 		} else if (status.equals(String.valueOf(Util.NUM_FOUR))) {//活动已结束
			mIvArrow.setVisibility(View.GONE);
			editact.setText(R.string.relaunching_activities);
			setVisibility(true, false, ryEditAct, rydelact);
			mIsAct = "4";
 		} 
	}
	
	/**
	 * 根据状态判断发布活动按钮的显示
	 * @param status
	 * @param tvstatus
	 * @param btnAct
	 */
	public void getStatus(String status, TextView tvstatus, Button btnAct){
		 if (status.equals(String.valueOf(Util.NUM_ZERO))) {//未发布
			 tvstatus.setText(R.string.act_unpublished);
			 btnAct.setBackgroundResource(R.drawable.login_btn);
			 btnAct.setEnabled(true);
			 
		 } else {//已发布
			 tvstatus.setText(R.string.act_published);
			 btnAct.setBackgroundResource(R.drawable.verification_fail);
			 btnAct.setEnabled(false);
		 }
	}
	
	/**
	 * 设置数据显示隐藏问题
	 * @param editact
	 * @param delact
	 * @param ryEditAct
	 * @param rydelact
	 */
	public void setVisibility(boolean editact, boolean delact, RelativeLayout ryEditAct, RelativeLayout rydelact){
		if (editact) {
			ryEditAct.setVisibility(View.VISIBLE);
		} else {
			ryEditAct.setVisibility(View.GONE);
		}
		if (delact) {
			rydelact.setVisibility(View.VISIBLE);
		} else {
			rydelact.setVisibility(View.GONE);
		}
	}
	
	/** 点击返回 **/
	@OnClick(R.id.layout_turn_in)
	private void ivuppBackClick(View v) {
		getMyActivity().finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActInfo(mView);
	}
}
