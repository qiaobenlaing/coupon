package com.huift.hfq.shop.fragment;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;
import com.huift.hfq.shop.R;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.Util.onUploadFinish;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.ActivityUpLaod;
import com.huift.hfq.base.pojo.Campaign;
import com.huift.hfq.base.pojo.PromotionPrice;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ActivityManagerActivity;
import com.huift.hfq.shop.activity.UpdateCampaignFreeActivity;
import com.huift.hfq.shop.activity.UpdateCampaignIntroducesActivity;
import com.huift.hfq.shop.activity.UpdateCampaignTypeActivity;
import com.huift.hfq.shop.adapter.GetActRefundChoiseAdapter;
import com.huift.hfq.shop.model.GetActRefundChoiceTask;
import com.huift.hfq.shop.model.GetActivityInfoTask;
import com.huift.hfq.shop.model.UpdateActivityTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 编辑活动的所有基本信息
 * @author qian.zhou
 */
public class EditAcitityMsgFragment extends Fragment {
	/** 活动编码*/
	public static final String ACTIVITY_CODE = "activityCode";
	/** 全局视图*/
	private View mView;
	/** 活动标题*/
	private EditText mEtCampaignTitle;
	/** 获得过地址*/
	private EditText mEtCampaignAddress;
	/** 联系人*/
	private EditText mEtCampaignPerson;
	/** 电话*/
	private EditText mEtCampaginPhone;
	/** 提前预约 */
	private EditText mEtCampaignAdvance;
	/** 单人*/
	private EditText mEtCampaignSingle;
	/** 最大参与人数*/
	private EditText mEtCampaignMaxPerson;
	/** 活动开始时间*/
	private EditText mEtCamaddStartTime;
	/** 活动结束时间*/
	private EditText mEtCamaddEndTime;
	/** 活动类型*/
	public final static int UPP_CAMPAIGN_TYPE_FLAG = 1001;
	/** 活动类型返回成功*/
	public final static int UPP_CAMPAIGN_TYPE_SUCC = 1002;
	/** 活动类型成功的key*/
	public final static String CAMPAIGN_TYPE_KEY = "campaignTypeKey";
	/** 活动类型成功的Name*/
	public final static String CAMPAIGN_TYPE_NAME= "campaignTypeName";
	/** 保存所有活动信息的对象*/
	private Campaign mCampaign;
	/** 保存退款信息的对象*/
	private List<Campaign> mCampaignRefundData;
	/** 视图层*/
	private View mPopWindowView;
	/** 活动类型*/
	private String mActType;
	/** 活动标题*/
	private String mActTitle;
	/** 活动图片*/
	private String mActImg;
	/** 活动地址*/
	private String mActAddress;
	/** 活动开始时间*/
	private String mActStartTime;
	/** 活动结束时间*/
	private String mActEndTime;
	/** 活动联系人*/
	private String mActPerson;
	/** 活动联系人电话*/
	private String mActPersonMobernbr;
	/** 活动退款要求*/
	private String mRefundRequired;
	/** 活动提前预约天数*/
	private String mDayOfBooking;
	/** 活动单人报名人数*/
	private String mRegisterNbrRequired;
	/** 活动最大参与人数*/
	private String mLimitedParticipators;
	/** 活动code*/
	private String mActivityCode;
	/** 显示的图片的控件 */
	private ImageView mIvaddimage;
	/** 选择图片路径*/
	private String mTmpPic;
	/** PopupWindow是一个容器 **/
	private PopupWindow mPopupWindow;
	private final static int TIME_ZERO = 0;
	/** 裁剪图片*/
	private final static int CROP_PIC = 1;
	/** 从图库选择图片*/
	private final static int SELECT_PIC = 0;
	/** 活动规格的标志*/
	public final static int CAMPAIGN_FREE_FLAG = 1003;
	/** 活动规格成功*/
	public final static int CAMPAIGN_FREE_SUCC = 1004;
	/** 得到的活动规格的list*/
	public final static String CAMPAIGN_FREE_LIST = "list";
	/** 活动介绍的标志*/
	public final static int CAMPAIGN_INTRO_FLAG = 1005;
	/** 活动介绍的成功*/
	public final static int CAMPAIGN_INTRO_SUCC = 1006;
	/** 得到活动介绍*/
	public final static String CAMPAIGN_INTRO_LIST = "introList";
	/** 时间对话框年月日**/
	private DatePickerDialog mDatePickerDialog;
	private DatePickerDialog mPickerDialog;
	/** 时间对话框时分秒**/
	private TimePickerDialog mPickerStartTime;
	private TimePickerDialog mPickerEndTime;
	/** 设置开始时间**/
	private String mStartDatePicker;
	private String mStartTimePicker;
	/** 设置结束时间**/
	private String mEndDatePicker;
	private String mEndTimePicker;
	/** 判断选择的时间不能小于今天*/
	private boolean mStartDateFlag = false;
	private boolean mEndDateFlag = false;
	/** 退款*/
	private RelativeLayout mRlCampaignRefund;
	/** 退款的值*/
	private TextView mTvCampaignRefund;
	/** 记录点击次数*/
	private int mClickNum;
	/** 显示退款信息的popu*/
	private PopupWindow mRefundPopupWindow;
	/** 退款信息的ListView*/
	private ListView mListView;
	/** 规格的种类*/
	private TextView mTvCampaignSpecifications;
	/** 价格*/
	private TextView mEtCampaignPrice;
	/** 切割刀的图盘路径*/
	private List<String> mUpLoadImgPathList;
	/**未修改的内容*/
	private List<ActivityUpLaod> mUnChangeUpLaods;
	/** 传递的图片路径*/
	private String mMultiImgPath;
	/** 拼接的内容*/
	private StringBuffer mBuffer;
	/** 活动类型*/
	private TextView mTvCampignTypeName;
	/** 得到的价格*/
	private double mPrice = 0.0;
	/** 活动显示*/
	private LinearLayout mLyActIntro;
	/**图文页对象列表*/
	private List<ActivityUpLaod> mActivityUpLaods;
	/** 是否修改了数据*/
	private boolean mUppFlag = false;
	/** 是否修改了规格*/
	private String mActivityfication;
	/** 保存活动*/
	private Button mBtnSaveAct;

	/** 执行非线程*/
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					updateActivity(mBuffer.toString());
					break;

				default:
					break;
			}
		};
	};

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static EditAcitityMsgFragment newInstance() {
		Bundle args = new Bundle();
		EditAcitityMsgFragment fragment = new EditAcitityMsgFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_activity_edit, container, false);
		ViewUtils.inject(this, mView);
		Util.addLoginActivity(getMyActivity());
		init(mView);
		return mView;
	}

	// 初始化方法
	private void init(View v) {
		findView(v); // 获取控件
		setActAddTime();
		getActivityInfo(v);//获得活动详情
	}

	/**
	 * 初始化所有控件
	 */
	private void findView(final View view) {
		//标题
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(R.string.activity_edit);
		TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
		tvMsg.setVisibility(View.GONE);
		mIvaddimage = (ImageView) view.findViewById(R.id.tv_campaign_upload);//显示的图片
		mIvaddimage.setOnClickListener(ivAddImageClick);
		mEtCamaddStartTime = (EditText) view.findViewById(R.id.et_campaignadd_starttime);//开始时间
		mEtCamaddEndTime = (EditText) view.findViewById(R.id.et_campaignadd_endtime);//结束时间
		mRlCampaignRefund = (RelativeLayout) view.findViewById(R.id.ly_refund_line);//退款
		mRlCampaignRefund.setOnClickListener(rlRefundClick);
		mTvCampaignRefund = (TextView) view.findViewById(R.id.tv_select_error);//退款的值
		final ImageView campaignSetArrow = (ImageView) view.findViewById(R.id.iv_campaign_arrow);//图标
		RelativeLayout campaignSet = (RelativeLayout) view.findViewById(R.id.rl_campaign_set);//高级设置的一行
		final LinearLayout campaignAdvancedset = (LinearLayout) view.findViewById(R.id.ly_campaign_advancedset);//高级设置的多中选项
		mTvCampaignSpecifications = (TextView) view.findViewById(R.id.et_campaignadd_specifications);//退款的规格种类
		mEtCampaignPrice = (TextView) view.findViewById(R.id.et_campaignadd_price);//价格
		mTvCampignTypeName = (TextView) view.findViewById(R.id.et_campaignadd_type);//活动类型
		mEtCampaignAdvance = (EditText) view.findViewById(R.id.et_campaignadd_advance);//预约
		mEtCampaignTitle  = (EditText) view.findViewById(R.id.et_campaignadd_title);//活动标题
		mEtCampaignAddress = (EditText) view.findViewById(R.id.et_campaignadd_address);//活动地址
		mEtCampaignPerson = (EditText) view.findViewById(R.id.et_campaignadd_person);//联系人
		mEtCampaginPhone = (EditText) view.findViewById(R.id.et_campaignadd_phone);//联系电话
		mEtCampaignSingle = (EditText) view.findViewById(R.id.et_campaignadd_singelperson);//单人报名人数
		mEtCampaignMaxPerson = (EditText) view.findViewById(R.id.et_campaignadd_maxperson);//活动最大参与人数
		mLyActIntro = (LinearLayout) view.findViewById(R.id.ly_campaign_intro_detail);//活动介绍
		mBtnSaveAct = (Button) view.findViewById(R.id.campaign_save);
		if (mClickNum % 2 == 0) {
			campaignSetArrow.setImageResource(R.drawable.downc_arrow);
			campaignAdvancedset.setVisibility(View.GONE);
		} else {
			campaignSetArrow.setImageResource(R.drawable.upc_arrow);
			campaignAdvancedset.setVisibility(View.VISIBLE);
		}
		//默认进来隐藏高级设置
		campaignSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mClickNum++;
				if (mClickNum % 2 == 0) {
					campaignSetArrow.setImageResource(R.drawable.downc_arrow);
					campaignAdvancedset.setVisibility(View.GONE);
				} else {
					campaignSetArrow.setImageResource(R.drawable.upc_arrow);
					campaignAdvancedset.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * 设置数据
	 */
	private void initData(Campaign campaign) {
		mUnChangeUpLaods = new ArrayList<ActivityUpLaod>();
		mUppFlag = DB.getBoolean(ShopConst.UppActStatus.UPP_ACTIVITY);
		mBuffer = new StringBuffer();
		if (campaign != null ) {
			if (null != campaign && !Util.isEmpty(campaign.getActivityImg())) {
				Util.showImage(getActivity(), campaign.getActivityImg(), mIvaddimage);
				mActImg = campaign.getActivityImg();
				mTmpPic =  campaign.getActivityImg();
			} else {
			}
			//活动标题
			String actTitle = !Util.isEmpty(campaign.getActivityName()) ? campaign.getActivityName() : "";
			mActTitle = actTitle;
			mEtCampaignTitle.setText(actTitle);
			mEtCampaignTitle.setSelection(actTitle.length());
			//活动开始时间
			mEtCamaddStartTime.setText(!Util.isEmpty(campaign.getStartTime()) ? campaign.getStartTime() : "");
			mActStartTime = campaign.getStartTime();
			//活动结束时间
			mEtCamaddEndTime.setText(!Util.isEmpty(campaign.getEndTime()) ? campaign.getEndTime() : "");
			mActEndTime = campaign.getEndTime();
			//活动详细地址
			String actAddress = !Util.isEmpty(campaign.getActivityLocation()) ? campaign.getActivityLocation() : "";
			mEtCampaignAddress.setText(actAddress);
			mEtCampaignAddress.setSelection(actAddress.length());
			mActAddress = actAddress;
			//活动类型
			if (!Util.isEmpty(campaign.getActType())) {

				setActType(campaign.getActType(), mTvCampignTypeName);
				mActType = campaign.getActType();
			}
			//联系人contactName
			String person = !Util.isEmpty(campaign.getContactName()) ? campaign.getContactName() : "";
			mEtCampaignPerson.setText(person);
			mEtCampaignPerson.setSelection(person.length());
			mActPerson = person;
			//联系人电话
			String personMobileNbr = !Util.isEmpty(campaign.getContactMobileNbr()) ? campaign.getContactMobileNbr() : "";
			mEtCampaginPhone.setText(personMobileNbr);
			mEtCampaginPhone.setSelection(personMobileNbr.length());
			mActPersonMobernbr = personMobileNbr;
			//活动规格
			setPayFree(campaign);
			String actFication = !Util.isEmpty(new Gson().toJson(campaign.getFeeScale())) ? new Gson().toJson(campaign.getFeeScale()) : "";
			mActivityfication = actFication;
			//退款信息
			getActRefundChoice();
			//提前多少天预约
			String advance = !Util.isEmpty(campaign.getDayOfBooking()) ? campaign.getDayOfBooking() : "";
			mEtCampaignAdvance.setText(advance);
			mEtCampaignAdvance.setSelection(advance.length());
			mDayOfBooking = advance;
			//单人报名人数
			String single = !Util.isEmpty(campaign.getRegisterNbrRequired()) ? campaign.getRegisterNbrRequired() : "";
			mEtCampaignSingle.setText(single);
			mEtCampaignSingle.setSelection(single.length());
			mRegisterNbrRequired = single;
			//活动参与人数
			String maxPerson = !Util.isEmpty(campaign.getLimitedParticipators()) ? campaign.getLimitedParticipators() : "";
			mEtCampaignMaxPerson.setText(maxPerson);
			mEtCampaignMaxPerson.setSelection(maxPerson.length());
			mLimitedParticipators = maxPerson;
			//活动显示
			Interception(campaign.getRichTextContent());
		} else {
		}

	}

	/**
	 * 截取出活动介绍的图片和文字
	 */
	public void Interception (String richTextContent) {
		//去除<BR/>
		String replace = richTextContent.replace("<BR />", "").replace("<BR/>", "");

		List<String> list = new ArrayList<String>();
		int start=0;
		int end = 0;
		for (int i = 0; i < replace.length(); i++) {
			String str = String.valueOf(replace.charAt(i));
			if ("<".equals(str)) {
				if (i == 0) {
					end = replace.indexOf(">", start);
					list.add(replace.substring(start, end+1));
					i = end;
					start = end+1;
				} else {
					if (">".equals(String.valueOf(replace.charAt(i-1)))) {
						start = i;
						end = replace.indexOf(">", start);
						list.add(replace.substring(start, end+1));
						i = end;
					} else {
						end = i;
						list.add(replace.substring(start, end));
						start = i;
						end =  replace.indexOf(">", start);
						list.add(replace.substring(start,end+1));
						start = end+1;
						i = end;
					}
				}
			}
		}
		if (!">".equals(String.valueOf(replace.charAt(replace.length()-1)))) {
			int lastIndexOf = replace.lastIndexOf(">");
			list.add(replace.substring(lastIndexOf+1,replace.length()));
		}

		mActivityUpLaods = new ArrayList<ActivityUpLaod>();
		for (int i = 0 ; i < list.size(); i++) {
			String string = list.get(i);
			ActivityUpLaod activityUpLaod = null;
			if(string.startsWith("<")){ //图片
				//取出图片服务器路径
				String netPath = string.substring(string.indexOf("/"), string.lastIndexOf("\""));
				activityUpLaod = new ActivityUpLaod(1, i+1, "", "", netPath);
			} else { //文字
				activityUpLaod = new ActivityUpLaod(0,i+1, string, "", "");
			}
			mActivityUpLaods.add(activityUpLaod);
		}

		showActivityDetail(mActivityUpLaods);
	}

	/**
	 * 显示活动的图文页
	 */
	private void showActivityDetail(List<ActivityUpLaod> activityUpLaods){
		if ( null == activityUpLaods || activityUpLaods.size() == 0){
			return;
		}
		for (ActivityUpLaod upLaod:activityUpLaods) {
			if (upLaod.getType()==0) { //文字
				TextView textView = new TextView(getActivity());
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layoutParams.setMargins(10, 10, 10, 10);
				textView.setText(upLaod.getContent());
				mLyActIntro.addView(textView, mLyActIntro.getChildCount(),layoutParams);
			} else if (upLaod.getType()==1) { //图片
				ImageView imageView = new ImageView(getActivity());
				imageView.setScaleType(ScaleType.FIT_XY);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 500);
				layoutParams.setMargins(10, 10, 10, 10);
				if (!Util.isEmpty(upLaod.getNetPath())) { //网络图片
					Util.showBannnerImage(getActivity(), upLaod.getNetPath(), imageView);
				} else if (!Util.isEmpty(upLaod.getLoaclPath())) { //本地图片
					Bitmap bitmap = BitmapFactory.decodeFile(upLaod.getLoaclPath());
					if (null != bitmap) {
						imageView.setImageBitmap(bitmap);
					}
				}
				mLyActIntro.addView(imageView,mLyActIntro.getChildCount(),layoutParams);
			}
		}
	}

	/**
	 * 设置收费价格规格
	 */
	public void setPayFree(Campaign campaign) {
		if (campaign != null) {
			ArrayList<PromotionPrice> list = (ArrayList<PromotionPrice>) campaign.getFeeScale();
			List<Double> listPric = new ArrayList<Double>();
			for (int i = 0; i < list.size(); i++) {
				PromotionPrice pro = list.get(i);
				mPrice = pro.getPrice();
				listPric.add(mPrice);
			}
			//得到最大值和最小值
			double max =-1;
			double min =-1;
			for (int i = 0; i < listPric.size(); i++) {
				double temp = listPric.get(i);
				if(i == 0) {
					max = temp;
					min = temp;
				}
				if (temp > max) {
					max = temp;
				}
				if (temp < min) {
					min = temp;
				}
			}
			if (list != null) {
				if (list.size() > 0) {
					if (list.size() == 1) {//一种规格
						mTvCampaignSpecifications.setText("1");
						mEtCampaignPrice.setText(String.valueOf(mPrice));
					} else {//多种规格
						mTvCampaignSpecifications.setText(String.valueOf(list.size()));
						mEtCampaignPrice.setText(String.valueOf(min) + " - " + String.valueOf(max));
					}
				} else {//没有保存规格
					mTvCampaignSpecifications.setText("");
					mEtCampaignPrice.setText("0");
				}
			}
			setEnable(mPrice);
		}
	}

	/**
	 * 设置活动名称
	 * @param status
	 * @param tvTypeName
	 */
	public void setActType (String status, TextView  tvTypeName) {
		if (status.equals(String.valueOf(Util.NUM_ONE))) {//聚会
			tvTypeName.setText(R.string.edit_get_together);

		} else if (status.equals(String.valueOf(Util.NUM_TWO))) {//运动
			tvTypeName.setText(R.string.edit_movement);

		} else if (status.equals(String.valueOf(Util.NUM_THIRD))) {//户外
			tvTypeName.setText(R.string.edit_outdoor);

		} else if (status.equals(String.valueOf(Util.NUM_FOUR))) {//亲子
			tvTypeName.setText(R.string.edit_paternity);

		} else if (status.equals(String.valueOf(Util.NUM_FIVE))) {//体验课
			tvTypeName.setText(R.string.edit_experience_class);

		} else if (status.equals(String.valueOf(Util.NUM_SIX))) {//音乐
			tvTypeName.setText(R.string.edit_music);

		} else if (status.equals(String.valueOf(Util.NUM_SEVEN))) {//其他
			tvTypeName.setText(R.string.edit_othter);
		}
	}

	/**
	 * 获得活动详情
	 */
	public void getActivityInfo (final View v) {
		mActivityCode = DB.getStr(ShopConst.UppActStatus.ACT_CODE);
		new GetActivityInfoTask(getMyActivity(), new GetActivityInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				} else {
					mCampaign =  Util.json2Obj(result.toString() , Campaign.class);
					//设置数据
					initData(mCampaign);//设置数据

				}
			}

		}).execute(mActivityCode);
	}

	/**
	 * 当前的activity不为空
	 * @return
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 *设置时间格式
	 */
	private void setActAddTime(){
		final  String ACT_ZERO = "0";
		final  int TIME_NUMBER = 9;
		//默认时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		final  String mNowDate = sdf.format(new Date());
		/** 当前时间*/
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		//开始时间年月日
		mDatePickerDialog = new DatePickerDialog(getMyActivity(), new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				mStartDatePicker = year +"-"+ ((monthOfYear+1) > TIME_NUMBER ? (monthOfYear+1) : ACT_ZERO + (monthOfYear+1)) + "-" + (dayOfMonth>9?dayOfMonth : ACT_ZERO + dayOfMonth);
				mDatePickerDialog.dismiss();
			}
		}, cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));
		//开始时间时分秒
		mPickerStartTime = new TimePickerDialog(getMyActivity(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mStartTimePicker = (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
				String startTime = mStartDatePicker +" "+mStartTimePicker;
				mEtCamaddStartTime.setText(startTime);
				String nowtime = mNowDate;
				String startDate = mEtCamaddStartTime.getText().toString();
				double actStartTime = Util.timeSizeData(getActivity(), startDate, nowtime)/1000;
				if (actStartTime < TIME_ZERO ) {
					mStartDateFlag = true;
				} else {
					mStartDateFlag = false;
				}

			}
		}, cal.get(cal.HOUR_OF_DAY), cal.get(cal.MINUTE), true);
		//结束时间年月日
		mPickerDialog = new DatePickerDialog(getMyActivity(), new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				mEndDatePicker = year +"-"+ ((monthOfYear+1) > TIME_NUMBER ? (monthOfYear+1) : ACT_ZERO + (monthOfYear+1)) + "-" + (dayOfMonth>9?dayOfMonth : ACT_ZERO + dayOfMonth);
				mPickerDialog.dismiss();
			}
		}, cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));
		//结束时间时分秒
		mPickerEndTime = new TimePickerDialog(getMyActivity(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mEndTimePicker = (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
				String endTime = mEndDatePicker +" "+mEndTimePicker;
				mEtCamaddEndTime.setText(endTime);
				String nowtime = mNowDate;
				String endDate = mEtCamaddEndTime.getText().toString();
				double actEndTime = Util.timeSizeData(getActivity(), endDate, nowtime)/1000;
				if (actEndTime < TIME_ZERO) {
					mEndDateFlag = true;
				} else {
					mEndDateFlag = false;
				}
			}
		}, cal.get(cal.HOUR_OF_DAY), cal.get(cal.MINUTE), true);
	}

	/**
	 * 弹出拍照图库
	 */
	OnClickListener ivAddImageClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.select_pic, null);
			mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btncardset));
			mPopupWindow.setOutsideTouchable(true);
			// 设置mPopupWindow的显示位置
			mPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
			Button btnImage = (Button) view.findViewById(R.id.btn_pick_photo);// 图库
			Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);// 取消
			// 点击图库按钮
			btnImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					if (i.resolveActivity(getMyActivity().getPackageManager()) != null) {
						startActivityForResult(i, SELECT_PIC);
					}
					mPopupWindow.dismiss();
				}
			});
			// 点击取消按钮
			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mPopupWindow.dismiss();
				}
			});
		}
	};

	/**
	 * 从图库选择图片并裁剪
	 * @param uri 图片资源
	 */
	private void cropPic1(Uri uri) {
		if (uri == null) {
			return;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_PIC);
	}

	/**
	 * 拍照和调用图库时要执行的方法
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CROP_PIC://裁剪
				if (resultCode == Activity.RESULT_OK) {
					if (data != null) {
						updateHead(data);
					}
				}
			case SELECT_PIC://相册
				if (resultCode == Activity.RESULT_OK) {
					if (data == null) {
						return;
					} else {
						Uri uri = data.getData();
						cropPic1(uri);
					}
				}
				break;
			case UPP_CAMPAIGN_TYPE_FLAG://活动类型
				if (resultCode == UPP_CAMPAIGN_TYPE_SUCC) {
					String campaignTypeValue = data.getStringExtra(CAMPAIGN_TYPE_KEY);
					String typeName = data.getStringExtra(CAMPAIGN_TYPE_NAME);
					mActType = campaignTypeValue;
					mCampaign.setActType(!Util.isEmpty(campaignTypeValue) ? campaignTypeValue : "");
					mTvCampignTypeName.setText(!Util.isEmpty(typeName) ? typeName : "");
				}
				break;
			case CAMPAIGN_FREE_FLAG://活动规格
				if (resultCode == CAMPAIGN_FREE_SUCC) {
					ArrayList<PromotionPrice> list = (ArrayList<PromotionPrice>) data.getSerializableExtra(CAMPAIGN_FREE_LIST);
					mCampaign.setFeeScale(list);
					mActivityfication = new Gson().toJson(mCampaign.getFeeScale());
					List<Double> listPric = new ArrayList<Double>();
					for (int i = 0; i < list.size(); i++) {
						PromotionPrice pro = list.get(i);
						mPrice = pro.getPrice();
						listPric.add(mPrice);
					}
					//得到最大值和最小值
					double max =-1;
					double min =-1;
					for (int i = 0; i < listPric.size(); i++) {
						double temp = listPric.get(i);
						if(i == 0) {
							max = temp;
							min = temp;
						}
						if (temp > max) {
							max = temp;
						}
						if (temp < min) {
							min = temp;
						}
					}
					if (list != null) {
						if (list.size() > 0) {
							if (list.size() == 1) {//一种规格
								mTvCampaignSpecifications.setText("1");
								mEtCampaignPrice.setText(String.valueOf(mPrice));
							} else {//多种规格
								mTvCampaignSpecifications.setText(String.valueOf(list.size()));
								mEtCampaignPrice.setText(String.valueOf(min) + " - " + String.valueOf(max));
							}
						} else {//没有保存规格
							mTvCampaignSpecifications.setText("");
							mEtCampaignPrice.setText("0");
						}
					}
					setEnable(mPrice);
				}
				break;
			case CAMPAIGN_INTRO_FLAG://活动介绍
				if (resultCode == CAMPAIGN_INTRO_SUCC) {
					ArrayList<ActivityUpLaod> introList = (ArrayList<ActivityUpLaod>) data.getSerializableExtra(CAMPAIGN_INTRO_LIST);
					mActivityUpLaods = introList;
					//本页显示图文页
					mLyActIntro.removeAllViews();
					showActivityDetail(introList);
					//上传----
					mCampaign.setUpload(introList);
					mUpLoadImgPathList = new ArrayList<String>();
					for (int i = 0; i < introList.size(); i++) {
						ActivityUpLaod upload = introList.get(i);
						if (upload.getType() == 1 && !Util.isEmpty(upload.getLoaclPath())) {
							String imgPath = upload.getLoaclPath();
							mUpLoadImgPathList.add(imgPath);
						} else {
							mUnChangeUpLaods.add(upload);
						}
					}
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 如果价格是0，则退款和提前预约都不可点击
	 */
	private void setEnable(double price){
		if (!Util.isEmpty(String.valueOf(price))) {
			if (price == 0.0) {
				mRlCampaignRefund.setEnabled(false);
				mRlCampaignRefund.setFocusable(false);
				mRlCampaignRefund.setFocusableInTouchMode(false);

				mEtCampaignAdvance.setEnabled(false);
				mEtCampaignAdvance.setFocusable(false);
				mEtCampaignAdvance.setFocusableInTouchMode(false);
			} else {
				mRlCampaignRefund.setEnabled(true);
				mRlCampaignRefund.setFocusable(true);
				mEtCampaignAdvance.setFocusableInTouchMode(true);
				mEtCampaignAdvance.requestFocus();

				mEtCampaignAdvance.setEnabled(true);
				mEtCampaignAdvance.setFocusable(true);
				mEtCampaignAdvance.setFocusableInTouchMode(true);
				mEtCampaignAdvance.requestFocus();
			}
		}
	}

	/**
	 * 活动介绍的图片上传
	 * @param upLoadIamge
	 */
	private void uploadImage(List<String> upLoadIamge) {
		Util.getMultiImageUpLoad(getActivity(),upLoadIamge, new Util.onUploadFinish() {
			@Override
			public void getImgUrl(String multiImgPath) {
				mMultiImgPath = multiImgPath;
				processUpLoadInfo(mMultiImgPath);
			}
		});
	}

	/**
	 * 根据从网络返回的路径 拼接上传信息的字符串
	 * @param mMultiImgPath
	 */
	private void processUpLoadInfo(String mMultiImgPath) {
		String[] split = mMultiImgPath.split("\\|");
		List<String> noSpace = new ArrayList<String>();
		//去空格
		for (int i = 0; i < split.length; i++) {
			if(!"".equals(split[i])){
				noSpace.add(split[i]);
			}
		}
		//新建集合来确保原来的顺序
		List<ActivityUpLaod> activityUpLaods = new ArrayList<ActivityUpLaod>();
		for (int i = 0; i < noSpace.size(); i++) {
			ActivityUpLaod  activityUpLaod = new ActivityUpLaod(1, 0, "", "", noSpace.get(i));
			activityUpLaods.add(activityUpLaod);
		}
		for (int i = 0; i < mUnChangeUpLaods.size(); i++) {
			ActivityUpLaod activityUpLaod = mUnChangeUpLaods.get(i);
			activityUpLaods.add(activityUpLaod.getPosition()-1, activityUpLaod);
		}
		//mBuffer = new StringBuffer();
		//拼接字符串
		for (int i = 0; i < activityUpLaods.size(); i++) {
			ActivityUpLaod activityUpLaod = activityUpLaods.get(i);
			if(activityUpLaod.getType() == 0){ //文字
				mBuffer.append(activityUpLaod.getContent());
				//TODO
			}else if(activityUpLaod.getType() == 1){ //图片
				mBuffer.append("<img src=\""+activityUpLaod.getNetPath()+"\""+" /><BR />");
			}
		}

		handler.sendEmptyMessage(0);
	}

	/**
	 * 上传图片
	 * @param data  获取的图片数据
	 */
	private void updateHead(Intent data) {
		if (data == null) {
			return;
		} else{
			if (data.hasExtra("data")) {
				Bitmap bmap = data.getParcelableExtra("data");
				Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getMyActivity().getContentResolver(), bmap, null,null));
				Glide.with(getMyActivity()).load(uri).centerCrop().dontAnimate().placeholder(R.drawable.no_data).into(mIvaddimage);
				if (bmap == null) { return; }
				String picPath = Tools.getFilePath(getMyActivity()) + System.currentTimeMillis() + ".jpg";
				Tools.savBitmapToJpg(bmap, picPath);
				Util.getImageUpload(getMyActivity(), picPath, new onUploadFinish() {
					@Override
					public void getImgUrl(String img) {
						mTmpPic = img;
					}
				});
			}
		}
	}

	/**
	 * 点击事件
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in,R.id.et_campaignadd_starttime,R.id.et_campaignadd_endtime,R.id.campaign_save,
			R.id.rl_campaign_introduces,R.id.ly_campaign_type,R.id.ly_campaign_intro_detail,R.id.ly_campaign_specifications})
	public void trunIdenCode(View view){
		Intent intent;
		switch (view.getId()) {
			case R.id.layout_turn_in://返回
				getMyActivity().finish();
				break;
			case R.id.et_campaignadd_starttime://弹出开始时间格式
				mPickerStartTime.show();
				mDatePickerDialog.show();
				break;
			case R.id.et_campaignadd_endtime://弹出结束时间格式
				mPickerEndTime.show();
				mPickerDialog.show();
				break;
			case R.id.campaign_save://保存活动

				if (mUpLoadImgPathList != null && mUpLoadImgPathList.size() > 0) {//修改了图文页的图片
					uploadImage(mUpLoadImgPathList);
				} else if(mUpLoadImgPathList != null && mUpLoadImgPathList.size() == 0) { //进入了图文页界面后未修改图片
					/*processUpLoadInfo("");*/
					for (int i = 0; i < mUnChangeUpLaods.size(); i++) {
						ActivityUpLaod activityUpLaod = mUnChangeUpLaods.get(i);
						if(activityUpLaod.getType() == 0){ //文字
							mBuffer.append(activityUpLaod.getContent());
							//TODO
						}else if(activityUpLaod.getType() == 1){ //图片
							mBuffer.append("<img src=\""+activityUpLaod.getNetPath()+"\""+" /><BR />");
						}
					}
					updateActivity(mBuffer.toString());
				}else if(mUpLoadImgPathList == null){ //未进入编辑图文页界面
					updateActivity(mCampaign.getRichTextContent());
				}
				break;
			case R.id.ly_campaign_type://活动类型
				intent = new Intent(getMyActivity(), UpdateCampaignTypeActivity.class);
				intent.putExtra(UpdateCampaignTypeFragment.ACT_TYPE, mActType);
				startActivityForResult(intent, UPP_CAMPAIGN_TYPE_FLAG);
				DB.saveBoolean(ShopConst.UppActStatus.UPP_ACTIVITY, true);
				break;
			case R.id.ly_campaign_intro_detail://活动介绍
				intent = new Intent(getMyActivity(), UpdateCampaignIntroducesActivity.class);
				intent.putExtra("UpLoadList", (ArrayList<ActivityUpLaod>)mActivityUpLaods);
				startActivityForResult(intent, CAMPAIGN_INTRO_FLAG);
				break;
			case R.id.ly_campaign_specifications://活动规格
				intent = new Intent(getMyActivity(), UpdateCampaignFreeActivity.class);
				intent.putExtra(UpdateCampaignFreeFragment.CAMPAIGN_OBJ, mCampaign);
				startActivityForResult(intent, CAMPAIGN_FREE_FLAG);
				break;
			default:
				break;
		}
	}

	/**
	 * 点击退款的一行
	 */
	OnClickListener rlRefundClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (null == mRefundPopupWindow || !mRefundPopupWindow.isShowing()) {
				refundWindow();
			} else if (mRefundPopupWindow.isShowing()) {
				mRefundPopupWindow.dismiss();
			}
		}
	};

	/**
	 * 显示错误信息的popuwindow
	 */
	private void refundWindow () {
		if (null == mPopWindowView ) {
			mPopWindowView = View.inflate(getMyActivity(), R.layout.popwindow_refund, null);
			mListView = (ListView) mPopWindowView.findViewById(R.id.lv_1);
			mListView.setOnItemClickListener(selectRefundListener);
			GetActRefundChoiseAdapter adapter = new GetActRefundChoiseAdapter(getMyActivity(), mCampaignRefundData);
			mListView.setAdapter(adapter);
		}
		if (null == mRefundPopupWindow) {
			mRefundPopupWindow = new PopupWindow(mPopWindowView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mRefundPopupWindow.setFocusable(true);  //能够焦点获得
			mRefundPopupWindow.setBackgroundDrawable(new BitmapDrawable());  //设置背景
			mRefundPopupWindow.setOutsideTouchable(true);  //外部点击
		}
		mRefundPopupWindow.showAsDropDown(mRlCampaignRefund);
	}

	/**
	 * 退款的点击事件
	 */
	OnItemClickListener selectRefundListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Campaign campaign = (Campaign) mListView.getItemAtPosition(position);
			String campaignTypeCode = campaign.getRefundValue();
			mRefundRequired = campaignTypeCode;
			String campaignRefundName = campaign.getRefundName();
			mTvCampaignRefund.setText(campaignRefundName);
			mRefundPopupWindow.dismiss();
		}
	};


	/**
	 * 退款的信息
	 */
	private void getActRefundChoice () {
		new GetActRefundChoiceTask(getMyActivity(), new GetActRefundChoiceTask.Callback() {
			@Override
			public void getResult(JSONArray result) {
				if (null == result) {
					return;
				}
				mCampaignRefundData = new Gson().fromJson(result.toString(), new TypeToken<List<Campaign>>() {}.getType());
				mRefundRequired = mCampaign.getRefundRequired();
				//默认选中item
				if(!Util.isEmpty(mRefundRequired) && Integer.parseInt(mRefundRequired)-1 >= 0){
					Campaign campaign = mCampaignRefundData.get(Integer.parseInt(mRefundRequired)-1);
					mTvCampaignRefund.setText(campaign.getRefundName());
				}
			}
		}).execute();
	}

	/**
	 * 修改活动
	 */
	public void updateActivity(String richTextContent){
		String actImg = !Util.isEmpty(mTmpPic)  ? mTmpPic : mActImg;//活动图片
		String actTitle = !Util.isEmpty(mEtCampaignTitle.getText().toString())  ? mEtCampaignTitle.getText().toString() : mActTitle;
		String actAddress = !Util.isEmpty(mEtCampaignAddress.getText().toString())  ? mEtCampaignAddress.getText().toString() : mActAddress;//活动地址
		String actStartTime = !Util.isEmpty(mEtCamaddStartTime.getText().toString())  ? mEtCamaddStartTime.getText().toString() : mActStartTime;//活动开始时间
		String atcEndTime = !Util.isEmpty(mEtCamaddEndTime.getText().toString())  ? mEtCamaddEndTime.getText().toString() : mActEndTime;//活动结束时间
		String person = !Util.isEmpty(mEtCampaignPerson.getText().toString())  ? mEtCampaignPerson.getText().toString() : mActPerson;//联系人
		String personPhone = !Util.isEmpty(mEtCampaginPhone.getText().toString())  ? mEtCampaginPhone.getText().toString() : mActPersonMobernbr;//联系人电话
		String json = !Util.isEmpty(mActivityfication) ? mActivityfication : new Gson().toJson(mCampaign.getFeeScale());//规格
		String dayOfBooking = !Util.isEmpty(mEtCampaignAdvance.getText().toString())  ? mEtCampaignAdvance.getText().toString() : mDayOfBooking;//提前预约天数
		String registerNbrRequired = !Util.isEmpty(mEtCampaignSingle.getText().toString())  ? mEtCampaignSingle.getText().toString() : mRegisterNbrRequired;//单人报名人数
		String limitedParticipators = !Util.isEmpty(mEtCampaignMaxPerson.getText().toString())  ? mEtCampaignMaxPerson.getText().toString() : mLimitedParticipators;//活动最大参与人数

		//没有编辑活动就不可修改
		if (!mTmpPic.equals(mActImg) || !mActTitle.equals(mEtCampaignTitle.getText().toString()) || !mActAddress.equals(mEtCampaignAddress.getText().toString())
				|| !mActStartTime.equals(mEtCamaddStartTime.getText().toString()) || !mActEndTime.equals(mEtCamaddEndTime.getText().toString())
				|| !mActPerson.equals(mEtCampaignPerson.getText().toString()) || !mActPersonMobernbr.equals(mEtCampaginPhone.getText().toString())
				|| !new Gson().toJson(mCampaign.getFeeScale()).equals(mActivityfication) || !mDayOfBooking.equals(mEtCampaignAdvance.getText().toString())
				|| !mRegisterNbrRequired.equals(mEtCampaignSingle.getText().toString()) || !mLimitedParticipators.equals(mEtCampaignMaxPerson.getText().toString())
				|| !mBuffer.toString().equals(mCampaign.getRichTextContent())) {//编辑了

			mBtnSaveAct.setEnabled(true);
			String[] params = {mActivityCode, actTitle, actStartTime, atcEndTime, actAddress, richTextContent,
					limitedParticipators, actImg, mActType, person, personPhone, json, mRefundRequired,
					registerNbrRequired, dayOfBooking};

			new UpdateActivityTask(getActivity(), new UpdateActivityTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					if (result == null) {
						return;
					} else {
						if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
							/*Intent intent = new Intent(getMyActivity(), ActivityManagerActivity.class);
							startActivity(intent);*/
							getMyActivity().finish();
						} else {
							Util.getContentValidate(R.string.update_fail);
						}
					}
				}
			}).execute(params);
		} else {
			mBtnSaveAct.setEnabled(false);
		}
	}

	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void backClick(View view){
		getMyActivity().finish();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mUppFlag) {
			DB.saveBoolean(ShopConst.UppActStatus.UPP_ACTIVITY,false);
			//getActivityInfo(mView);//获得活动详情
		}
	}
}
