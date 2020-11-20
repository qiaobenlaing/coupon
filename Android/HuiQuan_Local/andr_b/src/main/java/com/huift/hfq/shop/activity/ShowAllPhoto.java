package com.huift.hfq.shop.activity;

import java.util.ArrayList;

import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.FinishActivityUtils;
import com.huift.hfq.shop.adapter.AlbumGridViewAdapter;
import com.huift.hfq.shop.util.Bimp;
import com.huift.hfq.shop.util.ImageItem;
import com.huift.hfq.shop.util.PublicWay;
import com.huift.hfq.shop.util.Res;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.huift.hfq.shop.R;

/**
 * 这个是显示一个文件夹里面的所有图片时的界面
 * 
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日 下午11:49:10
 */
public class ShowAllPhoto extends Activity {
	
	private GridView mGridView;
	private ProgressBar mProgressBar;
	private AlbumGridViewAdapter mGridImageAdapter;
	/** 完成按钮*/
	private TextView mOkButton;
	/** 预览按钮*/
	private TextView mPreview;
	/** 返回按钮*/ 
	private ImageView mBack;
	/** 取消按钮*/ 
	private Button mCancel;
	/** 标题*/ 
	private TextView mHeadTitle;
	private Intent mIntent;
	private Context mContext;
	public static ArrayList<ImageItem> mDataList = new ArrayList<ImageItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_show_all_photo);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		PublicWay.activityList.add(this);
		mContext = this;
		mBack = (ImageView) findViewById(Res.getWidgetID("showallphoto_back"));
		// cancel = (Button)
		// findViewById(Res.getWidgetID("showallphoto_cancel"));
		mPreview = (TextView) findViewById(Res.getWidgetID("showallphoto_preview"));
		mOkButton = (TextView) findViewById(Res.getWidgetID("showallphoto_ok_button"));
		mHeadTitle = (TextView) findViewById(Res.getWidgetID("showallphoto_headtitle"));
		this.mIntent = getIntent();
		String folderName = mIntent.getStringExtra("folderName");
		if (folderName.length() > 8) {
			folderName = folderName.substring(0, 9) + "...";
		}
		mHeadTitle.setText(folderName);
		// cancel.setOnClickListener(new CancelListener());
		mBack.setOnClickListener(new BackListener(mIntent));
		mPreview.setOnClickListener(new PreviewListener());
		init();
		initListener();
		isShowOkBt();
		FinishActivityUtils.addActivity(ShowAllPhoto.this);
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			mGridImageAdapter.notifyDataSetChanged();
		}
	};

	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.tempSelectBitmap.size() > 0) {
				mIntent.putExtra("position", "2");
				mIntent.setClass(ShowAllPhoto.this, GalleryActivity.class);
				startActivity(mIntent);
			}
		}

	}

	private class BackListener implements OnClickListener {// 返回按钮监听
		Intent intent;

		public BackListener(Intent intent) {
			this.intent = intent;
		}

		public void onClick(View v) {
			intent.setClass(ShowAllPhoto.this, ImageFile.class);
			startActivity(intent);
		}

	}

	/*
	 * private class CancelListener implements OnClickListener {// 取消按钮的监听
	 * public void onClick(View v) { //清空选择的图片 Bimp.tempSelectBitmap.clear();
	 * intent.setClass(mContext, ImageUploadActivity.class);
	 * startActivity(intent); } }
	 */

	private void init() {
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		mProgressBar = (ProgressBar) findViewById(Res.getWidgetID("showallphoto_progressbar"));
		mProgressBar.setVisibility(View.GONE);
		mGridView = (GridView) findViewById(Res.getWidgetID("showallphoto_myGrid"));
		mGridImageAdapter = new AlbumGridViewAdapter(this, mDataList, Bimp.tempSelectBitmap, this, null);
		mGridView.setAdapter(mGridImageAdapter);
		mOkButton = (TextView) findViewById(Res.getWidgetID("showallphoto_ok_button"));
	}

	private void initListener() {
		mGridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
			public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, Button button) {
				if (Bimp.tempSelectBitmap.size() >= PublicWay.num && isChecked) {
					button.setVisibility(View.GONE);
					toggleButton.setChecked(false);
					Toast.makeText(ShowAllPhoto.this, Res.getString("only_choose_num"), 200).show();
					return;
				}

				if (isChecked) {
					button.setVisibility(View.VISIBLE);
					Bimp.tempSelectBitmap.add(mDataList.get(position));
					mOkButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				} else {
					button.setVisibility(View.GONE);
					Bimp.tempSelectBitmap.remove(mDataList.get(position));
					mOkButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				}
				isShowOkBt();
			}
		});

		mOkButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mOkButton.setClickable(false);
				// if (PublicWay.photoService != null) {
				// PublicWay.selectedDataList.addAll(Bimp.tempSelectBitmap);
				// Bimp.tempSelectBitmap.clear();
				// PublicWay.photoService.onActivityResult(0, -2,
				// intent);
				// }
				mIntent.setClass(mContext, ImageUploadActivity.class);
				startActivity(mIntent);
				// Intent intent = new Intent();
				// Bundle bundle = new Bundle();
				// bundle.putStringArrayList("selectedDataList",
				// selectedDataList);
				// intent.putExtras(bundle);
				// intent.setClass(ShowAllPhoto.this, UploadPhoto.class);
				// startActivity(intent);
				finish();
				FinishActivityUtils.exit();
			}
		});

	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			mOkButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			mPreview.setPressed(true);
			mOkButton.setPressed(true);
			mPreview.setClickable(true);
			mOkButton.setClickable(true);
			mOkButton.setTextColor(Color.RED);
			mPreview.setTextColor(Color.RED);
		} else {
			mOkButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			mPreview.setPressed(false);
			mPreview.setClickable(false);
			mOkButton.setPressed(false);
			mOkButton.setClickable(false);
			mOkButton.setTextColor(Color.parseColor("#5D5D5D"));
			mPreview.setTextColor(Color.parseColor("#5D5D5D"));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			mIntent.setClass(ShowAllPhoto.this, ImageFile.class);
			startActivity(mIntent);
		}

		return false;

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		isShowOkBt();
		super.onRestart();
	}

	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

}
