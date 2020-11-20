package cn.suanzi.baomi.cust.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.FinishActivityUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.AlbumGridViewAdapter;
import cn.suanzi.baomi.cust.utils.AlbumHelper;
import cn.suanzi.baomi.cust.utils.Bimp;
import cn.suanzi.baomi.cust.utils.ImageBucket;
import cn.suanzi.baomi.cust.utils.ImageItem;
import cn.suanzi.baomi.cust.utils.PublicWay;
import cn.suanzi.baomi.cust.utils.Res;

/**
 * 相册的主界面(活动)
 * @author wensi.yu
 */
public class AlbumActivity extends Activity {
	
	private final static String TAG = "AlbumActivity";
	
	/** 显示手机里的所有图片的列表控件 */
	private GridView mGridView;
	/** 当手机里没有图片时，提示用户没有图片的控件 */
	private TextView mTvFinish;
	/** gridView的adapter */
	private AlbumGridViewAdapter mGridImageAdapter;
	/** 完成按钮 */
	private TextView mOkButton;
	/** 返回按钮 */
	private ImageView mBack;
	/** 取消按钮 */
	private Button mCancel;
	/** 跳转 */
	private Intent mIntent;
	/** 预览按钮 */
	private TextView mPreview;

	private Context mContext;
	private ArrayList<ImageItem> mDataList;
	private AlbumHelper mHelper;
	public static List<ImageBucket> mContentList;
	public static Bitmap mBitmap;
	private String mImagePath;
	private ArrayList<String> mListFiles;
	private List<String> mImageFile;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_album);
		PublicWay.activityList.add(this);
		mContext = this;
		// 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		mBitmap = BitmapFactory.decodeResource(getResources(), Res.getDrawableID("plugin_camera_no_pictures"));
		init();
		initListener();
		mImageFile = new ArrayList<String>();
		// 这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
		FinishActivityUtils.addActivity(AlbumActivity.this);
		Util.addLoginActivity(AlbumActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mContext.unregisterReceiver(this);
			// TODO Auto-generated method stub
			mGridImageAdapter.notifyDataSetChanged();
		}
	};

	/**
	 * 预览按钮的监听
	 * 
	 * @author ad
	 * 
	 */
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.tempSelectBitmap.size() > 0) {
				mIntent.putExtra("position", "1");
				mIntent.setClass(AlbumActivity.this, GalleryActivity.class);
				startActivity(mIntent);
			}
		}
	}

	/**
	 * 完成按钮的监听
	 * @author ad
	 * 
	 */
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
			overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
			mIntent.setClass(mContext, ImageUploadActivity.class);
			startActivity(mIntent);
			//setResult(RESULT_OK, mIntent);
			finish();

		}
	}

	/**
	 * 返回按钮监听
	 * @author ad
	 * 
	 */
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
			mIntent.setClass(AlbumActivity.this, ImageFile.class);
			startActivity(mIntent);
		}
	}

	/**
	 * 取消按钮的监听
	 * @author ad
	 * 
	 */
	/*
	 * private class CancelListener implements OnClickListener { public void
	 * onClick(View v) { Bimp.tempSelectBitmap.clear();
	 * intent.setClass(mContext, ImageUploadActivity.class);
	 * startActivity(intent); } }
	 */

	/**
	 * 初始化，给一些对象赋值
	 */
	private void init() {
		mListFiles = new ArrayList<String>();

		mHelper = AlbumHelper.getHelper();
		mHelper.init(getApplicationContext());

		mContentList = mHelper.getImagesBucketList(false);
		mDataList = new ArrayList<ImageItem>();
		String imagePath = null;
		for (int i = 0; i < mContentList.size(); i++) {
			// Collections.reverse(mDataList);//图片的排序s
			Log.d(TAG, "bucketName==" + mContentList.get(i).bucketName);
			for (int j = 0; j < mContentList.get(i).imageList.size(); j++) {
				imagePath = mContentList.get(i).imageList.get(j).imagePath;
				Log.d(TAG, "imagePatth====" + imagePath);
				if (imagePath.startsWith("/storage/emulated/0/DCIM")) {
					Log.d(TAG, "进来了。。。。。。。。。。");
					mDataList.add(0, mContentList.get(i).imageList.get(j));
				} else {
					mDataList.add(mContentList.get(i).imageList.get(j));
				}

			}
			// mDataList.addAll(mContentList.get(i).imageList );
		}

		mBack = (ImageView) findViewById(Res.getWidgetID("back"));
		// cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		// cancel.setOnClickListener(new CancelListener());
		mBack.setOnClickListener(new BackListener());
		mPreview = (TextView) findViewById(Res.getWidgetID("preview"));
		mPreview.setOnClickListener(new PreviewListener());
		mIntent = getIntent();
		Bundle bundle = mIntent.getExtras();
		mGridView = (GridView) findViewById(Res.getWidgetID("myGrid"));
		Log.d(TAG, "AlbumGridViewAdapter进来了");
		mGridImageAdapter = new AlbumGridViewAdapter(this, mDataList, Bimp.tempSelectBitmap, AlbumActivity.this, new AlbumGridViewAdapter.CallBackUrl() {

			@Override
			public void getItemData(String iamgeUrl) {
				mImageFile.add(iamgeUrl);
				Log.d(TAG, "iamgeUrl=" + iamgeUrl);
				for (int i = 0; i < mImageFile.size(); i++) {
					String imageUrl = mImageFile.get(i);
					Log.d(TAG, "imageUrl=========" + imageUrl);

				}
			}
		});

		mGridView.setAdapter(mGridImageAdapter);
		// tv = (TextView) findViewById(Res.getWidgetID("myText"));
		mTvFinish = (TextView) findViewById(R.id.myText);
		mGridView.setEmptyView(mTvFinish);
		mOkButton = (TextView) findViewById(Res.getWidgetID("ok_button"));
		mOkButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
	}

	private void initListener() {
		mGridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, Button chooseBt) {
				if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
					toggleButton.setChecked(false);
					chooseBt.setVisibility(View.GONE);
					if (!removeOneData(mDataList.get(position))) {
						Toast.makeText(AlbumActivity.this, Res.getString("only_choose_num"), 200).show();
					}
					return;
				}
				if (isChecked) {
					chooseBt.setVisibility(View.VISIBLE);
					Bimp.tempSelectBitmap.add(mDataList.get(position));
					mOkButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				} else {
					Bimp.tempSelectBitmap.remove(mDataList.get(position));
					chooseBt.setVisibility(View.GONE);
					mOkButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				}
				// gridImageAdapter.addOrRemoveItem(position);
				isShowOkBt();
			}
		});

		mOkButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(ImageItem imageItem) {
		if (Bimp.tempSelectBitmap.contains(imageItem)) {
			Bimp.tempSelectBitmap.remove(imageItem);
			mOkButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			return true;
		}
		return false;
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
			mIntent.setClass(AlbumActivity.this, ImageUploadActivity.class);
			startActivity(mIntent);
		}
		return false;
	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}

	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
}
