package cn.suanzi.baomi.shop.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.model.AddSubAlbumTask;
import cn.suanzi.baomi.shop.model.UpdateSubAlbumTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 添加相册
 * @author qian.zhou
 */
public class AddPhotoActivity extends Activity {
	public static final String TAG = "AddPhotoActivity";
	/** setResult()用的，如果确实修改了信息 */
	public final static int UPP_RESP_SAVED = 2;
	/** 标示是首页还是产品详细页面的*/
	public final static String INDEX = "index";
	/** 子相册的名称*/
	public final static String PHOTO_NAME = "photo_name";
	/** 子相册编码*/
	public final static String CODE = "code";
	private String mPhotoName;
	private EditText mEtPhotoName;
	/**完成按钮*/
	private TextView mTvFinish;
	/** 标示*/
	private String mIndex;
	/** 子相册编码*/
	private String mCode;
	/** 子相册名称的最大长度*/
	private int BIG_LENGTH = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addphoto);
		Util.addActivity(AddPhotoActivity.this);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}

	private void init() {
		//取值
		Intent intent = this.getIntent();
		mIndex = intent.getStringExtra(INDEX);
		//头顶标题
		LinearLayout ivTurnin = (LinearLayout) findViewById(R.id.layout_turn_in);
		ivTurnin.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		mTvFinish = (TextView) findViewById(R.id.tv_msg);
		mTvFinish.setText(getString(R.string.finish));
		//产品相册编辑框
		mEtPhotoName =  (EditText) findViewById(R.id.et_photo_name);
		//完成
		mTvFinish.setOnClickListener(addListener);
		if (!Util.isEmpty(mIndex)) {
			
			if ("add".equals(mIndex)) {
				tvContent.setText(getString(R.string.tv_album_photo));
			} else {
				tvContent.setText(getString(R.string.tv_upp_albumphoto));
				String photoName = intent.getStringExtra(PHOTO_NAME);
				mCode = intent.getStringExtra(CODE);
				mEtPhotoName.setText(photoName);
			}
		} else {
			//TODO
		}
	}
	
	/**
	 * 完成（添加产品相册名称）
	 */
	OnClickListener addListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPhotoName = mEtPhotoName.getText().toString();
			Util.inputFilterSpace(mEtPhotoName);//去空格
			switch (v.getId()) {
			case R.id.tv_msg:
				if (Util.isEmpty(mEtPhotoName.getText().toString())) {
					Util.getContentValidate(R.string.no_photoname);
					break;
				}
				if ((mEtPhotoName.getText().toString().length()) <= Util.NUM_ZERO) {
					Util.getContentValidate(R.string.no_photoname);
					break;
				}
				if ((mEtPhotoName.getText().toString().length()) > BIG_LENGTH) {
					Util.getContentValidate(R.string.photoname_biglength);
					break;
				}
			default:
				mTvFinish.setEnabled(false);
				if (!Util.isEmpty(mIndex)) {
					
					if ("upp".equals(mIndex)) {
						new UpdateSubAlbumTask(AddPhotoActivity.this, new UpdateSubAlbumTask.Callback() {
							@Override
							public void getResult(int retCode) {
								mTvFinish.setEnabled(true);
								if (ErrorCode.SUCC == retCode) {
									Util.getContentValidate(R.string.upp_fail);
								}
							}
						}).execute(mCode, mPhotoName);
					} else {
						new AddSubAlbumTask(AddPhotoActivity.this, new AddSubAlbumTask.Callback() {
							@Override
							public void getResult(int retCode) {
								mTvFinish.setEnabled(true);
							}
						}).execute(mPhotoName);
					}
				} else {
					//TODO
				}
				break;
			}
		}
	};
	
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
