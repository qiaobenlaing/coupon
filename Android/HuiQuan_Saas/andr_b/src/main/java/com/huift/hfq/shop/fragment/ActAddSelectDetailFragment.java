package com.huift.hfq.shop.fragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Image;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ActListActivity;
import com.huift.hfq.shop.adapter.MarketingAdapter;
import com.huift.hfq.shop.model.ActAddContentTask;
import com.huift.hfq.shop.util.PublicWay;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author  wensi.yu
 * 营销活动的预览
 */
public class ActAddSelectDetailFragment extends Fragment {

	private final static String ACT_ISPREPAYREQUIRED = "需要预付费";
	private final static String ACT_ISREGISTERREGUIRED = "需要报名";
	private final static String ACTADD_ONE = "1";
	/** 提交*/
	@ViewInject(R.id.tv_msg)
	private TextView mBtnActaddDetailCommit;
	/** 功能*/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvActaddDetail;
	/** 返回查看*/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvActaddDetailBack;
	/** 图片*/
	private String mActivityImage = "";
	/** 传过来的图片的集合*/
	private ArrayList<String> mListFiles;
	private ViewPager mViewPager;
	/** 滚屏图片数组 */
	private String[] mSrollPics;
	private ImageView[] mPoints;
	private List<ImageView> mImagesList;
	private int mTime = 0;
	private int mPreviousSelectPosition;

	public static ActAddSelectDetailFragment newInstance() {
		Bundle args = new Bundle();
		ActAddSelectDetailFragment fragment = new ActAddSelectDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_actlist_select, container,false);
		ViewUtils.inject(this, view);
		//添加
		Util.addActivity(getActivity());
		ActivityUtils.add(getActivity());
		//设置标题
		mTvActaddDetail.setText(R.string.tv_actlist_title);
		mIvActaddDetailBack.setVisibility(View.VISIBLE);
		mBtnActaddDetailCommit.setText(R.string.tv_actlist_commit);
		//查看详情
		init(view);
		return view;
	}

	/**
	 * 查看添加的详情
	 */
	private void init(View view){
		TextView mTheam = (TextView) view.findViewById(R.id.tv_actDetail_theam);
		TextView mCreateDate = (TextView) view.findViewById(R.id.tv_actDetail_createDate);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
		mCreateDate.setText(simpleDateFormat.format(new Date()));
		TextView mShopName = (TextView) view.findViewById(R.id.tv_actDetail_shopName);
		TextView mStartTime = (TextView) view.findViewById(R.id.tv_actDetail_startTime);
		TextView mEndTime = (TextView) view.findViewById(R.id.tv_actDetail_endTime);
		TextView mActivityLocation = (TextView) view.findViewById(R.id.tv_actDetail_activityLocation);
		TextView mLimitedParticipators = (TextView) view.findViewById(R.id.tv_actDetail_limitedParticipators);
		TextView mIsRegisterRequired = (TextView) view.findViewById(R.id.tv_actDetail_isRegisterRequired);
		TextView mIsPrepayRequired = (TextView) view.findViewById(R.id.tv_actDetail_isPrepayRequired);
		TextView mPrePayment = (TextView) view.findViewById(R.id.tv_actDetail_prePayment);
		TextView mRichTextContent = (TextView) view.findViewById(R.id.tv_actDetail_richTextContent);

		//获得商家名字
		SharedPreferences nameSharedPreferences = getActivity().getSharedPreferences(ShopConst.ActListName.ACT_SHOPNAME, Context.MODE_PRIVATE);
		String actShopName = nameSharedPreferences.getString("shopName", "");

		SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(ShopConst.ActAdd.ACT_ADD, Context.MODE_PRIVATE);
		String activityName = mSharedPreferences.getString("activityName", "");
		String shopName = mSharedPreferences.getString("mTvShopName", "");
		String startTime = mSharedPreferences.getString("startTime", "");
		String endTime = mSharedPreferences.getString("endTime","");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		Date startDate = null;
		Date endDate = null;
		if (Util.isEmpty(startTime) || Util.isEmpty(endTime)) {
			mStartTime.setText(0+"");
			mEndTime.setText(0+"");
		} else {
			String startDatestr = startTime.replaceAll("-",".");
			String endDatestr = endTime.replaceAll("-",".");
			try {
				startDate = sdf.parse(startDatestr);
				endDate = sdf.parse(endDatestr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		String activityLocation = mSharedPreferences.getString("activityLocation", ""); //活动地点
		String txtContent = mSharedPreferences.getString("txtContent", ""); //活动内容
		String limitedParticipators = mSharedPreferences.getString("limitedParticipators", "");
		String isPrepayRequired = mSharedPreferences.getString("isPrepayRequired", "");//是否需要付费
		if (isPrepayRequired.equals(ACTADD_ONE)){
			isPrepayRequired = ACT_ISPREPAYREQUIRED;
		} else {
			isPrepayRequired = "";
		}
		String prePayment = mSharedPreferences.getString("prePayment", ""); //付费金额	
		String isRegisterRequired = mSharedPreferences.getString("isRegisterRequired", "");//是否需要报名
		if (isRegisterRequired.equals(ACTADD_ONE)){
			isRegisterRequired = ACT_ISREGISTERREGUIRED;
		} else {
			isRegisterRequired = "";
		}
		
		mTheam.setText(activityName);
		mShopName.setText(actShopName);
		mStartTime.setText(sdf.format(startDate));
		mEndTime.setText(sdf.format(endDate));
		mActivityLocation.setText(activityLocation);
		mLimitedParticipators.setText(limitedParticipators);
		mRichTextContent.setText(txtContent);
		mIsRegisterRequired.setText(isRegisterRequired);
		mIsPrepayRequired.setText(isPrepayRequired);
		mPrePayment.setText(prePayment);
		/*try{
			//显示图片
			Bitmap bitmap= Bimp.revitionImageSize(activityImage);
			mIvActaddDetailImg.setImageBitmap(bitmap);
		}catch(Exception e){
			e.printStackTrace();
		}*/

		//传过来的图片路径----拍照
		/*Intent intent = getMyActivity().getIntent();
		Bundle bundle= intent.getExtras();
		if (null != bundle) {
			if (bundle.getStringArrayList("Files") != null) {
				mListFiles = bundle.getStringArrayList("Files");
				pics = new String [mListFiles.size()];
				for (int i = 0; i < mListFiles.size(); i++) {
					pics[i] = mListFiles.get(i);
				}
				initViewPager(view, pics);
			}
		}*/

		//传过来的图片路径----相册
		Intent intent = getMyActivity().getIntent();
		Bundle bundle= intent.getExtras();
		if (null != bundle) {
			if (null != bundle.getStringArrayList("mUpLoadIamge")) {
				mListFiles = bundle.getStringArrayList("mUpLoadIamge");
				mSrollPics = new String [mListFiles.size()];
				for (int i = 0; i < mListFiles.size(); i++) {
					mSrollPics[i] = mListFiles.get(i);
				}
				initViewPager(view, mSrollPics);
			}
		}
	}

	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void initViewPager(View v, String[] ShopPics) {
		// 查看大图的路径集合
		List<Image> imageList = new ArrayList<Image>();
		mImagesList = new ArrayList<ImageView>();
		for (int i = 0; i < ShopPics.length; i++) {
			final ImageView imageView = new ImageView(getMyActivity());
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			if (Util.isEmpty(ShopPics[i])) {
				imageView.setBackgroundResource(R.drawable.no_data);
			} else {
				Bitmap bitmap = savBitmapToJpg1(ShopPics[i]);
				imageView.setImageBitmap(bitmap);
			}
			Image image = new Image();
			image.setImageUrl(ShopPics[i]);
			imageList.add(image);
			mImagesList.add(imageView);
		}
		LinearLayout mLyGroupViews = (LinearLayout) v.findViewById(R.id.ly_gropview);
		mLyGroupViews.removeAllViews();
		// 显示的点
		mPoints = new ImageView[mImagesList.size()];
		for (int i = 0; i < mImagesList.size(); i++) {
			ImageView imageView = new ImageView(getMyActivity());
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(20, 20);
			p.leftMargin = 10;
			p.rightMargin = 10;
			imageView.setLayoutParams(p);
			mPoints[i] = imageView;
			if (i == 0) {
				// 默认选中第一张图片
				mPoints[i].setBackgroundResource(R.drawable.dot_focused);
			} else {
				mPoints[i].setBackgroundResource(R.drawable.dot_normal);
			}
			mLyGroupViews.addView(mPoints[i]);
		}
		mViewPager = (ViewPager) v.findViewById(R.id.viewPager);
		MarketingAdapter adapter = new MarketingAdapter(getActivity(), mImagesList,imageList,mSrollPics,mSrollHandler,mSrollRunnable);
		mViewPager.setAdapter(adapter);
		// 切换图片时 点点发生改变
		mViewPager.setOnPageChangeListener(new MyListener());
		mViewPager.setCurrentItem(300);
		if (mSrollHandler != null && mSrollRunnable != null) {
			mSrollHandler.removeCallbacks(mSrollRunnable);
		}
		mSrollHandler.postDelayed(mSrollRunnable, 1000);
	}

	/**
	 * viewPage的改变事件
	 * @author ad
	 */
	class MyListener implements OnPageChangeListener {
		// 当滑动状态改变时调用
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		// 当当前页面被滑动时调用
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		// 当新的页面被选中时调用
		@Override
		public void onPageSelected(int position) {
			if (position > 2) {
				position = position % mImagesList.size();
			}
			if (mImagesList.size() > 1) {
				for (int i = 0; i < mPoints.length; i++) {
					mPoints[position].setBackgroundResource(R.drawable.dot_focused);
					if (position != i) {
						mPoints[i].setBackgroundResource(R.drawable.dot_normal);
					}
				}
			}
		}
	}

	/**
	 * 启动一个线程
	 */
	private Runnable mSrollRunnable = new Runnable() {
		@Override
		public void run() {
			if (mSrollPics == null || mSrollPics.length == 0) { return;// TODO
			}
			/*** 更新界面 **/
			mSrollHandler.obtainMessage().sendToTarget();
			mSrollHandler.postDelayed(this, 4000);
		}
	};

	/***
	 * 切换图片
	 * @author ad
	 */
	private Handler mSrollHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 判断20秒后温馨提示自动关闭
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
		}

	};

	public static Bitmap savBitmapToJpg1(String bitName) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(bitName,newOpts);//此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		return  bitmap = BitmapFactory.decodeFile(bitName, newOpts);
	}

	/**
	 * 点击提交按钮
	 * @param view
	 * @throws IOException
	 */
	@OnClick(R.id.tv_msg)
	public void btnActAddDetailCommitClick(View view) {
		switch (view.getId()) {
			case R.id.tv_msg:
				SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(ShopConst.ActAdd.ACT_ADD, Context.MODE_PRIVATE);
				String activityName = mSharedPreferences.getString("activityName", " ");  //活动主题
				String startTime = mSharedPreferences.getString("startTime", ""); //活动开始时间
				String endTime = mSharedPreferences.getString("endTime", ""); //活动结束时间
				String activityLocation = mSharedPreferences.getString("activityLocation", ""); //活动地点
				String txtContent = mSharedPreferences.getString("txtContent", ""); //活动内容
				String limitedParticipators = mSharedPreferences.getString("limitedParticipators", ""); //活动人数上限
				String isPrepayRequired = mSharedPreferences.getString("isPrepayRequired", "");//是否需要付费
				String prePayment = mSharedPreferences.getString("prePayment", ""); //付费金额	
				String isRegisterRequired = mSharedPreferences.getString("isRegisterRequired", "");//是否需要报名
				String activityLogo = mSharedPreferences.getString("imagePath", "");

				mActivityImage = mSharedPreferences.getString("imagePath", "");

				SharedPreferences imageSharedPreferences = getActivity().getSharedPreferences("image", Context.MODE_PRIVATE);
				Editor editor = imageSharedPreferences.edit();
				String url = imageSharedPreferences.getString(Util.COUPON_IMAGE, null);
				String log = imageSharedPreferences.getString(Util.COUPON_IMAGE, null);

				if (Util.isEmpty(url)) {
					// 图片正在上传，请稍等
					Util.getContentValidate(R.string.toast_coupon_upload);
					break;
				} else if (String.valueOf(ErrorCode.PIC_UPLOAD_FORM).equals(url)) {
					// 图片格式有问题
					Util.getContentValidate(R.string.toast_coupon_errorform);
					break;
				} else if (String.valueOf(ErrorCode.PIC_UPLOAD_SIZE).equals(url)) {
					// 图片尺寸有问题
					Util.getContentValidate(R.string.toast_coupon_errorsize);
					break;
				}
				//清除sharedPreferences里面的数据
				editor.clear();
				editor.commit();

				mBtnActaddDetailCommit.setEnabled(false);
				new ActAddContentTask(getActivity(), new ActAddContentTask.Callback() {
					@Override
					public void getResult(JSONObject result) {
						mBtnActaddDetailCommit.setEnabled(true);
						if (null == result){
							return;
						}

						if (result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
							for (int i = 0;i < PublicWay.activityList.size();i++) {
								if (null != PublicWay.activityList.get(i)) {
									PublicWay.activityList.get(i).finish();
								}
							}
							Intent intent = new Intent(getActivity(), ActListActivity.class);
							getActivity().startActivity(intent);

							System.exit(0);
						}
					}
				}).execute(activityName,startTime,endTime,activityLocation,txtContent,limitedParticipators,
						isPrepayRequired,prePayment,isRegisterRequired,url,log);

				break;
			default:
				break;
		}
	};

	/**
	 * 点击返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddDetailBackClick(View view) {
		getActivity().finish();
	}
}
