package cn.suanzi.baomi.shop.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.Util.onUploadFinish;
import cn.suanzi.baomi.base.api.Tools;
import cn.suanzi.baomi.base.pojo.ActivityUpLaod;
import cn.suanzi.baomi.base.pojo.Campaign;
import cn.suanzi.baomi.base.pojo.PromotionPrice;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.CampaignFreeActivity;
import cn.suanzi.baomi.shop.activity.CampaignIntroducesActivity;
import cn.suanzi.baomi.shop.activity.CampaignTypeActivity;
import cn.suanzi.baomi.shop.adapter.GetActRefundChoiseAdapter;
import cn.suanzi.baomi.shop.model.AddActivityTask;
import cn.suanzi.baomi.shop.model.GetActRefundChoiceTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.Util.onUploadFinish;
import cn.suanzi.baomi.base.api.Tools;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.CampaignIntroducesActivity;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 添加活动
 * @author wensi.yu
 *
 */
public class CampaignAddFragment extends Fragment {
	
	private final static String TAG = "CampaignAddFragment";
	
	private final static String CAMPAIGN_TITLE= "新建活动";
	private final static String ACT_ZERO = "0";
	private final static int TIME_NUMBER = 9;
	private final static int TIME_ZERO = 0;
	/** 裁剪图片*/
	private final static int CROP_PIC = 1;
	/** 从图库选择图片*/
	private final static int SELECT_PIC = 0;
	/** 拍照*/
	private final static int TAKE_PIC = 2;
	/** 活动类型*/
	public final static int CAMPAIGN_TYPE_FLAG = 1001;
	/** 活动类型返回成功*/
	public final static int CAMPAIGN_TYPE_SUCC = 1002;
	/** 活动规格的标志*/
	public final static int CAMPAIGN_FREE_FLAG = 1003;
	/** 活动规格成功*/
	public final static int CAMPAIGN_FREE_SUCC = 1004;
	/** 活动类型成功的key*/
	public final static String CAMPAIGN_TYPE_KEY = "campaignTypeKey";
	/** 活动类型成功的Name*/
	public final static String CAMPAIGN_TYPE_NAME= "campaignTypeName";
	/** 得到的活动规格的list*/
	public final static String CAMPAIGN_FREE_LIST = "list";
	/** 活动介绍的标志*/
	public final static int CAMPAIGN_INTRO_FLAG = 1005;
	/** 活动介绍的成功*/
	public final static int CAMPAIGN_INTRO_SUCC = 1006;
	/** 得到活动介绍*/
	public final static String CAMPAIGN_INTRO_LIST = "introList";
	/** 活动开始时间*/
	private EditText mEtCamaddStartTime;
	/** 开始时间*/
	private String mStartTime;
	/** 活动结束时间*/
	private EditText mEtCamaddEndTime;
	/** 结束时间*/
	private String mEndTime;
	/** 当前时间*/
	private String mNowDate;
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
	/** 保存活动*/
	private Button mBtnSave;
	/** 退款*/
	private RelativeLayout mRlCampaignRefund;
	/** 退款的值*/
	private TextView mTvCampaignRefund;
	/** 记录点击次数*/
	private int mClickNum;
	/** 选择视图的索引*/
	private int mClickPicId;
	/** PopupWindow是一个容器 **/
	private PopupWindow mPopupWindow;
	private File mTemp;
	/** 点击弹出相册*/
	private RelativeLayout mRlCampaignClick;
	/** 显示的图片的控件 */
	private ImageView mIvaddimage;
	/** 选择图片路径*/
	private String mTmpPic;
	/** 得打的返回页面的值*/
	private String mCampaignTypeValue;
	/** 得到活动类型返回的值*/
	private String mCampaignTypeName;
	/** 显示退款信息的popu*/
	private PopupWindow mRefundPopupWindow;
	/** 退款信息的ListView*/
	private ListView mListView;
	/** 得到的是哪个退款信息的code*/
	private String mCampaignTypeCode;
	/** 得到的是哪个退款信息的值*/
	private String mCampaignRefundName;
	/** 新建活动*/
	private Campaign mCampaign;
	/** 规格的种类*/
	private TextView mTvCampaignSpecifications;
	/** 价格*/
	private TextView mEtCampaignPrice;
	/** 活动介绍*/
	private ActivityUpLaod mActivityUpLaod;
	/** 切割刀的图盘路径*/
	private List<String> mUpLoadImgPathList;
	/** 图文页的文字内容*/
	private List<ActivityUpLaod> mTextActivityUpLaods;
	/** 传递的图片路径*/
	private String mMultiImgPath;
	/** 拼接的内容*/
	private StringBuffer mBuffer;
	/** 视图*/
	private View view;
	/** 活动类型*/
	private TextView mTvCampignTypeName;
	/** 图片上显示的文字*/
	private LinearLayout mLyCamapignAddText;
	/** 得到的价格*/
	private double mPrice = 0.0;
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
	/** 活动标题*/
	private String mCampaignTitle;
	/** 获得过地址*/
	private String mCampaignAddress;
	/** 联系人*/
	private String mCampaignPerson;
	/** 电话*/
	private String mCampaginPhone;
	/** 提前预约 */
	private String mCampaignAdvance;
	/** 单人*/
	private String mCampaignSingle;
	/** 最大参与人数*/
	private String mCampaignMaxPerson;
	/** 结束时间*/
	private String mCamEndTime;
	/** String feeJson*/
	private String mFeeJson;
	/** view*/
	private View popWindowView;
	/** 退款内容的数据*/
	private List<Campaign> mCampaignRefundData;
	/** 活动的介绍*/
	private LinearLayout mLyCamapignIntro;
	/** 进度条*/
	private ProgressDialog mProcessDialog;
	/** 活动介绍*/
	private TextView tvCampaignIntro;
	/** 动态添加的文字*/
	private TextView textView;
	/** 动态添加的图片*/
	private ImageView imageView;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				addActivity();//保存活动
				break;

			default:
				break;
			}
		};
	};
	
	public static CampaignAddFragment newInstance() {
		Bundle args = new Bundle();
		CampaignAddFragment fragment = new CampaignAddFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_campaign_add,container, false);
		getMyActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		ViewUtils.inject(this, view);
		init();
		if(null == mCampaign){
			mCampaign = new Campaign();
		}
		if(null == mActivityUpLaod){
			mActivityUpLaod = new ActivityUpLaod();
		}
		return view;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;       
	}

	private void init() {
		initView();
		setActAddTime();
		//默认时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		mNowDate = sdf.format(new Date());
		initCampaignView();
		//获取退款原因
		getActRefundChoice();
	}

	private void initView () {
		TextView tvCampaignName = (TextView) view.findViewById(R.id.tv_mid_content);//标题
		tvCampaignName.setText(CAMPAIGN_TITLE);
		TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);//添加
		tvSet.setVisibility(View.GONE);
		mLyCamapignAddText = (LinearLayout) view.findViewById(R.id.ly_camapignadd_text);//图片上的文字
		mIvaddimage = (ImageView) view.findViewById(R.id.tv_campaign_upload);//显示的图片
		mIvaddimage.setOnClickListener(ivAddImageClick);
		mEtCamaddStartTime = (EditText) view.findViewById(R.id.et_campaignadd_starttime);//开始时间
		mEtCamaddEndTime = (EditText) view.findViewById(R.id.et_campaignadd_endtime);//结束时间
		mBtnSave = (Button) view.findViewById(R.id.campaign_save);//保存活动
		mRlCampaignRefund = (RelativeLayout) view.findViewById(R.id.ly_refund_line);//退款
		mRlCampaignRefund.setOnClickListener(rlRefundClick);
		mTvCampaignRefund = (TextView) view.findViewById(R.id.tv_select_error);//退款的值
		final ImageView campaignSetArrow = (ImageView) view.findViewById(R.id.iv_campaign_arrow);//图标
		RelativeLayout campaignSet = (RelativeLayout) view.findViewById(R.id.rl_campaign_set);//高级设置的一行
		final LinearLayout campaignAdvancedset = (LinearLayout) view.findViewById(R.id.ly_campaign_advancedset);//高级设置的多中选项
		RelativeLayout campaignIntroduces = (RelativeLayout) view.findViewById(R.id.rl_campaign_introduces);//
		mTvCampaignSpecifications = (TextView) view.findViewById(R.id.et_campaignadd_specifications);//退款的规格种类
		mEtCampaignPrice = (TextView) view.findViewById(R.id.et_campaignadd_price);//价格
		mTvCampignTypeName = (TextView) view.findViewById(R.id.et_campaignadd_type);//活动类型
		mEtCampaignAdvance = (EditText) view.findViewById(R.id.et_campaignadd_advance);//预约
		
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
					Log.d(TAG, "进来了2..");
				} else {
					campaignSetArrow.setImageResource(R.drawable.upc_arrow);
					campaignAdvancedset.setVisibility(View.VISIBLE);
					Log.d(TAG, "进来了1..");
				}
			}
		});
		//mFeeJson = "";
	}
	
	/**
	 * 赋值
	 */
	private void initCampaignView() {
		mEtCampaignTitle = (EditText) view.findViewById(R.id.et_campaignadd_title);//活动标题
		mEtCampaignAddress = (EditText) view.findViewById(R.id.et_campaignadd_address);//活动地址
		mEtCampaignPerson = (EditText) view.findViewById(R.id.et_campaignadd_person);//联系人
		mEtCampaginPhone = (EditText) view.findViewById(R.id.et_campaignadd_phone);//联系电话
		mEtCampaignSingle = (EditText) view.findViewById(R.id.et_campaignadd_singelperson);//单人报名人数
		mEtCampaignMaxPerson = (EditText) view.findViewById(R.id.et_campaignadd_maxperson);//活动最大参与人数
		mLyCamapignIntro = (LinearLayout) view.findViewById(R.id.ly_campaign_intro_detail);//活动介绍
		tvCampaignIntro = (TextView) view.findViewById(R.id.et_errorinfo_input);//活动介绍
	}
	
	/**
	 *设置时间格式 
	 */
	private void setActAddTime(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		//开始时间年月日
		mDatePickerDialog = new DatePickerDialog(getMyActivity(), new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				
				mStartDatePicker = year +"-"+ ((monthOfYear+1) > TIME_NUMBER ? (monthOfYear+1) : ACT_ZERO + (monthOfYear+1)) + "-" + (dayOfMonth>9?dayOfMonth : ACT_ZERO + dayOfMonth);
				Log.i(TAG, "startDatePicker=="+mStartDatePicker);
				
				mDatePickerDialog.dismiss();
			}
		}, cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));
		
		//开始时间时分秒
		mPickerStartTime = new TimePickerDialog(getMyActivity(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mStartTimePicker = (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
					
					mStartTime = mStartDatePicker +" "+mStartTimePicker;
					Log.i(TAG, "startTime=="+mStartTime);   
					mEtCamaddStartTime.setText(mStartTime);
					
					String nowtime = mNowDate;
					String startDate = mEtCamaddStartTime.getText().toString();
					
					double actStartTime = Util.timeSizeData(getActivity(), startDate, nowtime)/1000;
					Log.d(TAG, "actStartTime=="+actStartTime);
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
					
					mEndTime = mEndDatePicker +" "+mEndTimePicker;
					Log.i(TAG, "endTime=="+mEndTime);
					mEtCamaddEndTime.setText(mEndTime);
					
					String nowtime = mNowDate;
					String endDate = mEtCamaddEndTime.getText().toString();
					
					double actEndTime = Util.timeSizeData(getActivity(), endDate, nowtime)/1000;
					Log.d(TAG, "actEndTime=="+actEndTime);
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
			mClickPicId = v.getId();
			View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.select_pic, null);
			mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btncardset));
			mPopupWindow.setOutsideTouchable(true);
			// 设置mPopupWindow的显示位置
			mPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
			//Button btnPhoto = (Button) view.findViewById(R.id.btn_take_photo);// 拍照
			Button btnImage = (Button) view.findViewById(R.id.btn_pick_photo);// 图库
			Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);// 取消
			// 点击拍照按钮
//			btnPhoto.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
//                    //下面这句指定调用相机拍照后的照片存储的路径 
//                    intent.putExtra("output", Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"))); 
//					startActivityForResult(intent, TAKE_PIC);
//					mPopupWindow.dismiss();
//				}
//			});
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
	 * 拍照后裁剪图片
	 * @param uri 图片资源
	 */
	private void cropPic(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("output", Uri.fromFile(mTemp));
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);// 宽高比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        startActivityForResult(intent, CROP_PIC);
    }
	
	/**
	 * 从图库选择图片并裁剪
	 * @param uri 图片资源
	 */
	private void cropPic1(Uri uri) {
		if (uri == null) {
			Log.e(TAG, "uri is null , return ");
			return;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 3);  
        intent.putExtra("aspectY", 2);  
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
			break;
		case TAKE_PIC://拍照
			mTemp = new File(Environment.getExternalStorageDirectory()  + "/xiaoma.jpg"); 
			cropPic(Uri.fromFile(mTemp)); 
			break;
		case SELECT_PIC://相册
			Log.d(TAG, "picture chuli 1111");
			 if (resultCode == Activity.RESULT_OK) {
                 if (data == null) {
                     return;
                 } else {
                	 Uri uri = data.getData();
                     cropPic1(uri);
                 }
             }
             break;
		case CAMPAIGN_TYPE_FLAG://活动类型
			Log.d(TAG, "resultCode == " + resultCode);
			if (resultCode == CAMPAIGN_TYPE_SUCC) {
				mCampaignTypeValue = data.getStringExtra(CAMPAIGN_TYPE_KEY);
				mCampaignTypeName = data.getStringExtra(CAMPAIGN_TYPE_NAME);
				Log.d(TAG, "campaignTypeValue == " + mCampaignTypeValue);
				Log.d(TAG, "campaignTypeName == " + mCampaignTypeName);
				mTvCampignTypeName.setText(!Util.isEmpty(mCampaignTypeName) ? mCampaignTypeName : "");
			}
			break;
			
		case CAMPAIGN_FREE_FLAG://活动规格
			 if (resultCode == CAMPAIGN_FREE_SUCC) {
				ArrayList<PromotionPrice> list = (ArrayList<PromotionPrice>) data.getSerializableExtra(CAMPAIGN_FREE_LIST);
				mCampaign.setFeeScale(list);
				Log.d(TAG, "活动规格返回的值="+list.toString());
				List<Double> listPric = new ArrayList<Double>();
				
				//double mPrice = 0.0;
				for (int i = 0; i < list.size(); i++) {
					PromotionPrice pro = list.get(i);
					mPrice = pro.getPrice();
					Log.d(TAG, "mPrice == " +mPrice);
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
				Log.d(TAG, "max == " + max + "-----min=="+min);
				
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
				Log.d(TAG,"mPrice ---- " + mPrice);
				
				setEnable(mPrice);
				
			 }
			break;
		case CAMPAIGN_INTRO_FLAG://活动介绍
			 Log.d(TAG, "活动介绍--------");
			 if (resultCode == CAMPAIGN_INTRO_SUCC) {
				 ArrayList<ActivityUpLaod> introList = (ArrayList<ActivityUpLaod>) data.getSerializableExtra(CAMPAIGN_INTRO_LIST);
				 Log.d(TAG, "返回得到的introList === " + introList.toString());
				 if (null != introList && introList.size() > 0) {
					 Log.d(TAG, "introList ----- data");
					 //tvCampaignIntro.setVisibility(View.GONE);
				 } else {
					 Log.d(TAG, "introList ----- null");
					 //tvCampaignIntro.setVisibility(View.VISIBLE);
				 }
				 //showActivityDetail(introList);
				 mTextActivityUpLaods = new ArrayList<ActivityUpLaod>();
				 mCampaign.setUpload(introList);
				 mUpLoadImgPathList = new ArrayList<String>();
				 for (int i = 0; i < introList.size(); i++) {
					ActivityUpLaod upload = introList.get(i);
					if(upload.getType() == 1){//1表示图片
						String imgPath = upload.getLoaclPath();
						mUpLoadImgPathList.add(imgPath);
					} else {
						mTextActivityUpLaods.add(upload);
					}
				 }
				 Log.d(TAG, "upLoadSplisList==" + mUpLoadImgPathList.toString());
				 
			 }
			break;
		default:
			break;
		}
	}
	
	/**
	 * 显示活动的图文页
	 */
	private void showActivityDetail(List<ActivityUpLaod> activityUpLaods){
		if ( null == activityUpLaods || activityUpLaods.size() == 0){
			return;
		}
		for (ActivityUpLaod upLaod:activityUpLaods) {
			if (upLaod.getType() == 0) { //文字
				textView = new TextView(getActivity());
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layoutParams.setMargins(10, 10, 10, 10);
				textView.setText(upLaod.getContent());
				Log.d(TAG, "showActivityDetail 文字--- " + upLaod.getContent());
				mLyCamapignIntro.addView(textView, mLyCamapignIntro.getChildCount(),layoutParams);
				Log.d(TAG, "showActivityDetail--------11" );
			} else if (upLaod.getType() == 1) { //图片
				imageView = new ImageView(getActivity());
				imageView.setScaleType(ScaleType.FIT_XY);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 500);
				layoutParams.setMargins(10, 10, 10, 10);
				Log.d(TAG, "showActivityDetail 路径--- " + upLaod.getContent());
				Log.d(TAG, "showActivityDetail--------22" );
				if (!Util.isEmpty(upLaod.getLoaclPath())) { //本地图片
					Bitmap bitmap = BitmapFactory.decodeFile(upLaod.getLoaclPath());
					if (null != bitmap) {
						imageView.setImageBitmap(bitmap);
					}
				}
				mLyCamapignIntro.addView(imageView,mLyCamapignIntro.getChildCount(),layoutParams);
			}
		}	
	}
	
	/**
	 * 如果价格是0，则退款，提前预约，报名，最大参与人数都不可点击
	 */
	private void setEnable(double price){
		Log.d(TAG,"camapign  price == " + price);
		if (!Util.isEmpty(String.valueOf(price))) {
			if (price == 0.0) {
				Log.d(TAG,"price 是 0");
				mRlCampaignRefund.setEnabled(false);
				mRlCampaignRefund.setFocusable(false);
				mRlCampaignRefund.setFocusableInTouchMode(false);
				
				mEtCampaignAdvance.setEnabled(false);
				mEtCampaignAdvance.setFocusable(false);
				mEtCampaignAdvance.setFocusableInTouchMode(false);
				
				mEtCampaignSingle.setEnabled(false);
				mEtCampaignSingle.setFocusable(false);
				mEtCampaignSingle.setFocusableInTouchMode(false);
				
				mEtCampaignMaxPerson.setEnabled(false);
				mEtCampaignMaxPerson.setFocusable(false);
				mEtCampaignMaxPerson.setFocusableInTouchMode(false);
				
			} else {
				Log.d(TAG,"price 不是 0");
				mRlCampaignRefund.setEnabled(true);
				mRlCampaignRefund.setFocusable(true);
				mEtCampaignAdvance.setFocusableInTouchMode(true);
				mEtCampaignAdvance.requestFocus();
				
				mEtCampaignAdvance.setEnabled(true);
				mEtCampaignAdvance.setFocusable(true);
				mEtCampaignAdvance.setFocusableInTouchMode(true);
				mEtCampaignAdvance.requestFocus();
				
				mEtCampaignSingle.setEnabled(true);
				mEtCampaignSingle.setFocusable(true);
				mEtCampaignSingle.setFocusableInTouchMode(true);
				mEtCampaignSingle.requestFocus();
				
				mEtCampaignMaxPerson.setEnabled(true);
				mEtCampaignMaxPerson.setFocusable(true);
				mEtCampaignMaxPerson.setFocusableInTouchMode(true);
				mEtCampaignMaxPerson.requestFocus();
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"onResume----------------");
		setEnable(mPrice);
	}
	
	/**
	 * 活动介绍的图片上传
	 * @param upLoadIamge
	 */
	private void uploadImage(List<String> upLoadIamge) {
	
		mProcessDialog = new ProgressDialog(getMyActivity());
		mProcessDialog.setCancelable(false);
		mProcessDialog.setMessage(Util.getString(R.string.toast_uploading));
		mProcessDialog.show();
		
		Util.getMultiImageUpLoad(getMyActivity(),upLoadIamge, new Util.onUploadFinish() {
			
			@Override
			public void getImgUrl(String multiImgPath) {
				if(!Util.isEmpty(multiImgPath)){
					Log.d(TAG, "上传多张图片以后的路径集合===="+multiImgPath); 
					mMultiImgPath = multiImgPath;
					processUpLoadInfo(mMultiImgPath);
				}
			}
		
		});
	}
	
	/**
	 * 根据从网络返回的路径 拼接上传信息的字符串
	 * @param mMultiImgPath
	 */
	private void processUpLoadInfo(String mMultiImgPath) {
		//新建集合来确保原来的顺序
		List<ActivityUpLaod> activityUpLaods = new ArrayList<ActivityUpLaod>();
		if(!Util.isEmpty(mMultiImgPath)){
			String[] split = mMultiImgPath.split("\\|");
			Log.d(TAG, "split == " +split);
			List<String> noSpace = new ArrayList<String>();
			//去空格
			for (int i = 0; i < split.length; i++) {
				if(!"".equals(split[i])){
					noSpace.add(split[i]);
				}
			}
			
			Log.d(TAG, "图片==="+noSpace.size());
			Log.d(TAG, "文字==="+mTextActivityUpLaods.size());
			
			for (int i = 0; i < noSpace.size(); i++) {
				ActivityUpLaod  activityUpLaod = new ActivityUpLaod(1, 0, "", "", noSpace.get(i));
				activityUpLaods.add(activityUpLaod);
			}
		}
		
		for (int i = 0; i < mTextActivityUpLaods.size(); i++) {
			ActivityUpLaod activityUpLaod = mTextActivityUpLaods.get(i);
			activityUpLaods.add(activityUpLaod.getPosition()-1, activityUpLaod);
		}
		mBuffer = new StringBuffer();//TODO
		
		//拼接字符串
		for (int i = 0; i < activityUpLaods.size(); i++) {
			ActivityUpLaod activityUpLaod = activityUpLaods.get(i);
			if(activityUpLaod.getType() == 0){ //文字
				mBuffer.append(activityUpLaod.getContent());
			}else if(activityUpLaod.getType() == 1){ //图片
				mBuffer.append("<img src = \""+activityUpLaod.getNetPath()+"\"/><BR/>");
			}
		}
		
		Log.d(TAG, "原来-===="+activityUpLaods.toString());
		Log.d(TAG, "拼接字符串-===="+mBuffer.toString());
		handler.sendEmptyMessage(0);//TODO
		
	}
	
	/** 给拍照或者从图库选择的图片命名*/
	private String getFilePath(Context context) {
		File f = context.getExternalFilesDir(null);
		if (f == null) {
			f = Environment.getExternalStorageDirectory();
			if (f == null) {
				f = context.getFilesDir();
			} else {
				f = new File(f.getAbsolutePath() + "/suanzi/");
				f.mkdirs();
			}
		}
		return f == null ? null : f.getAbsolutePath();                              
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
				Glide.with(getMyActivity()).load(uri).centerCrop().dontAnimate().placeholder(R.drawable.no_message).into(mIvaddimage);
				mLyCamapignAddText.setVisibility(View.GONE);
				
				if (bmap == null) { return; }
				String picPath = getFilePath(getMyActivity()) + "/" + System.currentTimeMillis() + ".jpg";
				Tools.savBitmapToJpg(bmap, picPath);
				Util.getImageUpload(getMyActivity(), picPath, new onUploadFinish() {
					@Override
					public void getImgUrl(String img) {
						Log.d(TAG,"上传返回的图片路径==="+img);
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
			mCampaignTitle = mEtCampaignTitle.getText().toString();
			mCampaignAddress = mEtCampaignAddress.getText().toString();
			String camStartTime = mEtCamaddStartTime.getText().toString();
			String camEndTime = mEtCamaddEndTime.getText().toString();
			mCampaignPerson = mEtCampaignPerson.getText().toString();
			mCampaginPhone = mEtCampaginPhone.getText().toString();
			String campaignAdvance = mEtCampaignAdvance.getText().toString();
			if (Util.isEmpty(campaignAdvance)) {
				mCampaignAdvance = "";
			} else {
				mCampaignAdvance = mEtCampaignAdvance.getText().toString();
			}
			
			String campaignSingle =  mEtCampaignSingle.getText().toString();
			String campaignMaxPerson =  mEtCampaignSingle.getText().toString();
			
			if (getJudge(camStartTime,camEndTime,campaignSingle,campaignMaxPerson)) {
				break;
			}
			
			if (null != mUpLoadImgPathList &&mUpLoadImgPathList.size() > 0 ) {
				uploadImage(mUpLoadImgPathList);//上传图片
			} else if(null!=mTextActivityUpLaods  && mTextActivityUpLaods.size()>0){
				processUpLoadInfo("");
			} else{
				mBuffer = new StringBuffer();
				handler.sendEmptyMessage(0);
			}
			
			break;
		case R.id.ly_campaign_type://活动类型
			intent = new Intent(getMyActivity(), CampaignTypeActivity.class);
			startActivityForResult(intent, CAMPAIGN_TYPE_FLAG);
			break;
		case R.id.ly_campaign_intro_detail://活动介绍
			intent = new Intent(getMyActivity(), CampaignIntroducesActivity.class);
			mLyCamapignIntro.removeView(textView);
			mLyCamapignIntro.removeView(imageView);
			startActivityForResult(intent, CAMPAIGN_INTRO_FLAG);
			
			break;
		case R.id.ly_campaign_specifications://活动规格
			intent = new Intent(getMyActivity(), CampaignFreeActivity.class);
			intent.putExtra(CampaignFreeFragment.CAMPAIGN_OBJ, mCampaign);
			startActivityForResult(intent, CAMPAIGN_FREE_FLAG);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 判断标题，地址，时间，联系人，电话是否为空
	 * @return
	 */
	private boolean getJudge (String camStartTime,String camEndTime,String campaignSingle,String campaignMaxPerson) {
		boolean flag = false;
		switch (1) {
		case 1:
			if (Util.isEmpty(mTmpPic)) {
				Util.getContentValidate(R.string.toast_actadd_pic);
				flag = true;
				break;
			} 
			
			if (Util.isEmpty(mCampaignTitle)) {
				Util.getContentValidate(R.string.toast_actadd_theam);
				flag = true;
				break;
			}
			
			if (Util.isEmpty(mCampaignAddress)) {
				Util.getContentValidate(R.string.toast_actadd_address);
				flag = true;
				break;
			}
			
			//判断开始时间与结束时间
			/*if (mStartDateFlag){
				Util.getContentValidate(R.string.toast_time_start_now);
				break;
			}
			if (mEndDateFlag){
				Util.getContentValidate(R.string.toast_time_end_now);
				break;
			}*/
			
			if (Util.isEmpty(camStartTime)) {
				Util.getContentValidate(R.string.toast_actadd_starttime);
				flag = true;
				break;
			}
			
			Log.d(TAG, "结束时间 == " +campaignSingle);
			if (Util.isEmpty(mEndTime)) {
				mCamEndTime = "";
			} else {
				mCamEndTime = mEndDatePicker +" "+mEndTimePicker;
				
				double date = Util.timeSizeData(getMyActivity(), camEndTime, camStartTime);
				Log.d(TAG,"camapign data ----" +date);
				if (date <= TIME_ZERO) {
					Util.getContentValidate(R.string.toast_actlist_time);
					break;
				}
			}
			
			if (Util.isEmpty(mCampaignTypeName)) {
				Util.getContentValidate(R.string.toast_actadd_type);
				flag = true;
				break;
			}
			
			if (Util.isEmpty(mCampaignPerson)) {
				Util.getContentValidate(R.string.toast_actadd_preson);
				flag = true;
				break;
			}
			
			if (Util.isEmpty(mCampaginPhone)) {
				Util.getContentValidate(R.string.toast_actadd_phone);
				flag = true;
				break;
			}
			
			if (Util.isPhone(getMyActivity(), mCampaginPhone)) {
				flag = true;
				break;
			}
			
			Log.d(TAG, "单人报名 == " +campaignSingle);
			if (Util.isEmpty(campaignSingle)) {
				mCampaignSingle = "";
			} else {
				mCampaignSingle = mEtCampaignSingle.getText().toString();
			}
			
			Log.d(TAG, "最大人数限制 == " +campaignMaxPerson);
			if (Util.isEmpty(campaignMaxPerson)) {
				mCampaignMaxPerson = "";
			} else {
				mCampaignMaxPerson = mEtCampaignMaxPerson.getText().toString();
			}
			
			break;

		default:
			break;
		}
		return flag;
		
	}
	
	/**
	 * 点击退款的一行
	 */
	OnClickListener rlRefundClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(null == mRefundPopupWindow || !mRefundPopupWindow.isShowing()){
				refundWindow ();
			} else {
				mRefundPopupWindow.dismiss();
			}
			
		}
	};
	
	/**
	 * 显示错误信息的popuwindow
	 */
	private void refundWindow () {
		if(null == popWindowView){
			popWindowView = View.inflate(getMyActivity(), R.layout.popwindow_refund, null);
			mListView = (ListView) popWindowView.findViewById(R.id.lv_1);
			mListView.setAdapter(new GetActRefundChoiseAdapter(getActivity(), mCampaignRefundData));
			mListView.setOnItemClickListener(selectRefundListener);
		}
		if(null == mRefundPopupWindow){
			mRefundPopupWindow = new PopupWindow(popWindowView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mRefundPopupWindow.setFocusable(true);  //能够获得焦点
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
			Log.d(TAG, "点击了退款----");
			Campaign campaign = (Campaign) mListView.getItemAtPosition(position);
			mCampaignTypeCode = campaign.getRefundValue();
			mCampaignRefundName = campaign.getRefundName();
			Log.d(TAG, "退款的值=="+mCampaignRefundName);
			mTvCampaignRefund.setText(mCampaignRefundName);
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
				Log.d(TAG, "result=="+result);
				if (null == result) {
					return;
				}
				mCampaignRefundData = new Gson().fromJson(result.toString(), new TypeToken<List<Campaign>>() {}.getType());
				
				for (int i = 0; i < result.size(); i++) {
					JSONObject jsonObject = (JSONObject) result.get(0);
					String jsString = jsonObject.get("refundName").toString();
					String jsValue = jsonObject.get("refundValue").toString();
					mTvCampaignRefund.setText(jsString);
					mCampaignTypeCode = jsValue;
				}
			}
		}).execute();
	}
	
	/**
	 * 添加活动的内容
	 */
	private void addActivity () {
		Log.d(TAG, "添加活动------------");
		
		String json = new Gson().toJson(mCampaign.getFeeScale());
		if (Util.isEmpty(json)) {
			mFeeJson = "";
		} else {
			mFeeJson = new Gson().toJson(mCampaign.getFeeScale());
		}
		Log.d(TAG, "json------"+json);
		Log.d(TAG, "mFeeJson-----"+mFeeJson);
		mBtnSave.setEnabled(false);
		new AddActivityTask(getMyActivity(), new AddActivityTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				mBtnSave.setEnabled(true);
				Log.d(TAG, "调用了AddActivityTask-----");
				if (null == result) {
					return;
				}
				
				if (ErrorCode.SUCC == Integer.parseInt(result.get("code").toString())) {
					getMyActivity().finish();
					
					if (mProcessDialog != null) {
						mProcessDialog.dismiss();
					}
				}
				
			}
		}).execute(mCampaignTitle,mStartTime,mCamEndTime,mCampaignAddress,mBuffer.toString(),mCampaignMaxPerson,mTmpPic,mCampaignTypeValue,
				mCampaignPerson,mCampaginPhone,mFeeJson,mCampaignTypeCode,mCampaignSingle,mCampaignAdvance);
	}
	
}
