package com.huift.hfq.shop.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Bimp;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.Util.onUploadFinish;
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.FinishActivityUtils;
import com.huift.hfq.shop.model.AddShopDecImgTask;
import com.huift.hfq.shop.model.UppShopDecImgTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 上传图片
 * @author qian.zhou
 */
public class UploadEnvironmentPhotoActivity extends Activity {
	public static final String TAG = "UploadEnvironmentPhotoActivity";
	public static final String DECORATION = "decoration";
	public static final String IMAGE_URL = "imageUrl";
	public static final String FLAG = "flag";
	/** setResult()用的，如果确实添加了信息 */
	public final static int INTENT_RESP_SAVED = 1;
	/** setResult()用的，如果取消了添加信息 */
	public final static int INTENT_RESP_CANCELED = 2;
	/** setResult()用的，如果取消了修改了信息 */
	public final static int UPP_DECORATION_IMAGE = 3;
	private String mPhotoTitle;
	/**保存按钮*/
	private TextView mTvFinish;
	/** 图片路径*/
	private String mPicPath;
	/** 装修的标题*/
	private EditText mEtPhotoTitle;
	/** 标示的flag*/
	private String mFlag;
	/** 环境图片对应的编码*/
	private String mDecorationCode;
	/** 环境标题的最大长度*/
	private int PRODUCT_BIG_LENGTH = 10;
	/** 内容*/
	private TextView mTvContent;
	/** 显示上传的图片*/
	private ImageView mIvPhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_envoron_photo);
		FinishActivityUtils.addActivity(UploadEnvironmentPhotoActivity.this);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}

	private void init() {
		//展示图片
		mIvPhoto = (ImageView) findViewById(R.id.iv_upload_photo);
		//标题
		LinearLayout ivTurnin = (LinearLayout) findViewById(R.id.layout_turn_in);
		ivTurnin.setVisibility(View.VISIBLE);
		mTvContent = (TextView) findViewById(R.id.tv_mid_content);
		mTvFinish = (TextView) findViewById(R.id.tv_msg);
		mTvFinish.setText(getString(R.string.btn_save));
		//装修标题
		mEtPhotoTitle =  (EditText) findViewById(R.id.et_product_title);
		//初始化数据
		initData();
	}
	
	/**
	 * 初始化数据
	 */
	public void initData(){
		//取值
		Intent intent = this.getIntent();
		mFlag = intent.getStringExtra(FLAG);
		//完成
		mTvFinish.setOnClickListener(addListener);
		if (!Util.isEmpty(mFlag)) {
			
			if ("one".equals(mFlag)) {//如果是点击的上传图片
				String imageUrl = intent.getStringExtra(IMAGE_URL);
				mTvContent.setText(getString(R.string.upload_photo));
				Log.d(TAG, "从图库获取的图片的路径为：：" + imageUrl);
				if (!Util.isEmpty(imageUrl)) {
					Bitmap bitmap = null;
					File imgFile = new File(imageUrl);
					FileOutputStream fileout = null;
					try {
						bitmap = Bimp.revitionImageSize(imageUrl);
						fileout = new FileOutputStream(imgFile);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileout);// 把数据写入文件
					mIvPhoto.setImageBitmap(bitmap);
					Util.getImageUpload(UploadEnvironmentPhotoActivity.this, imageUrl, new onUploadFinish() {
						@Override
						public void getImgUrl(String img) {
							mPicPath = img;
						}
					});
				} else {
					//TODO 
					Log.d(TAG, "图片路径为空..........");
				}
			} else {//大图的编辑的修改图片
				mTvContent.setText(getString(R.string.edit_photo));
				Decoration decoration = (Decoration) intent.getSerializableExtra(DECORATION);
				if (null != decoration) {
					
					mEtPhotoTitle.setText(decoration.getTitle());
					Util.showImage(UploadEnvironmentPhotoActivity.this, decoration.getImgUrl(), mIvPhoto);
					mDecorationCode = decoration.getDecorationCode();
					mPicPath = decoration.getImgUrl();
				}
				
			}
		}
	}
	
	/**
	 * 完成（添加产品名称）
	 */
	OnClickListener addListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPhotoTitle = mEtPhotoTitle.getText().toString();
			Util.inputFilterSpace(mEtPhotoTitle);//去空格
			switch (v.getId()) {
			case R.id.tv_msg:
				if ("".equals(mEtPhotoTitle.getText().toString())) {
					Util.addToast(R.string.no_environment_title);
					break;
				}
				if ((mEtPhotoTitle.getText().toString().length()) <= Util.NUM_ZERO) {
					Util.addToast(R.string.no_environment_title);
					break;
				}
				if ((mEtPhotoTitle.getText().toString().length()) > PRODUCT_BIG_LENGTH) {
					Util.addToast(R.string.environment_title_long);
					break;
				}
			default:
				mTvFinish.setEnabled(false);
				if (!Util.isEmpty(mFlag) && (!Util.isEmpty(mPicPath))) {
					if ("one".equals(mFlag)) {
						new AddShopDecImgTask(UploadEnvironmentPhotoActivity.this, new AddShopDecImgTask.Callback() {
							@Override
							public void getResult(int retCode) {
								mTvFinish.setEnabled(true);
							}
						}).execute(mPicPath, mPhotoTitle);
					} else {
						updateShopDecImg();
					}
				}
				break;
			}
		}
	};
	
	/**
	 * 修改装修的环境图片
	 */
	public void updateShopDecImg(){
		mTvFinish.setEnabled(false);
		new UppShopDecImgTask(UploadEnvironmentPhotoActivity.this, new UppShopDecImgTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mTvFinish.setEnabled(true);
				if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
					FinishActivityUtils.exit();
					Util.getContentValidate(R.string.toast_upp_succ);
				} else {
					Util.getContentValidate(R.string.update_fail);
				}
			}
		}).execute(mDecorationCode, mPicPath, mPhotoTitle);
	}
	
	/**
	 * 点击返回查看到活动列表
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnBackClick(View view) {
		finish();
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
