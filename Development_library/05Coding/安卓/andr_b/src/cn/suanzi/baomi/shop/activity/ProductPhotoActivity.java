package cn.suanzi.baomi.shop.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.Tools;
import cn.suanzi.baomi.base.pojo.Decoration;
import cn.suanzi.baomi.base.pojo.ShopDecoration;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.adapter.ProductPhotoAdapter;
import cn.suanzi.baomi.shop.model.AddSubAlbumPhotoTask;
import cn.suanzi.baomi.shop.model.DelSubAlbumPhotoTask;
import cn.suanzi.baomi.shop.model.GetSubAlbumPhotoTask;
import cn.suanzi.baomi.shop.model.UpdateSubAlbumTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 产品相册
 * @author qian.zhou
 */
public class ProductPhotoActivity extends Activity {
	public static final String TAG = "ProductPhotoActivity";
	public static final String CODE = "code";
	public static final String NAME = "name";
	/** startActivityForResult()的requestCode: 添加产品子相册图片信息 */
	public static final int INTENT_PHOTO_ADD_INFO = Util.NUM_ONE;
	/** startActivityForResult()的requestCode: 修改子相册类别名称信息 */
	public static final int UPP_PHOTO_INFO = Util.NUM_TWO;
	/** startActivityForResult()的requestCode: 删除子相册图片的某一张图片信息 */
	public static final int DEL_PHOTO_INFO = Util.NUM_THIRD;
	private final static int SELECT_PIC = 0;
	/** 子相册编码*/
	private String mCode;
	/** 显示照片*/
	private GridView  mGvPhoto;
	/**上传*/
	private TextView mTvUploadPhoto;
	/** 没有照片*/
	private TextView mTvNoData;
	/**装相片的集合*/
	private List<Decoration> mDecorationList;
	private ProductPhotoAdapter mPhotoProductAdapter;
	/** 标题内容*/
	private EditText mEtContent;
	/** 子相册的名称*/
	private String mName;
	private TextView mTvMsg;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	/** 正在加载的内容*/
	private RelativeLayout mLyContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_photo);
		Util.addActivity(ProductPhotoActivity.this);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}

	private void init() {
		//取值
		Intent intent = this.getIntent();
		mCode = intent.getStringExtra(CODE);
		mName = intent.getStringExtra(NAME);
		//头顶标题
		mEtContent = (EditText) findViewById(R.id.et_mid_content);
		mEtContent.setText(!Util.isEmpty(mName) ? mName : "");
		mTvMsg = (TextView) findViewById(R.id.tv_msg);//编辑
		//初始化数据
		mGvPhoto = (GridView) findViewById(R.id.gv_photo_product);
		mTvUploadPhoto = (TextView) findViewById(R.id.tv_upload_photo);//上传图片
		mLyNodate = (LinearLayout) findViewById(R.id.ly_nodate);
		mLyContent = (RelativeLayout) findViewById(R.id.ry_content);
		mTvNoData = (TextView) findViewById(R.id.tv_nodata);
		mTvUploadPhoto.setOnClickListener(upLoadListener);
		mTvMsg.setOnClickListener(uppListener);
		//gridview的点击事件
		mGvPhoto.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> v, View view, int position, long id) {
				Decoration decoration = (Decoration) mGvPhoto.getItemAtPosition(position);
				Intent intent = new Intent(ProductPhotoActivity.this, ProductBigPhotoActivity.class);
				intent.putExtra(ProductBigPhotoActivity.DECORATION, decoration);
				intent.putExtra(ProductBigPhotoActivity.IMAGE_LIST, (Serializable)mDecorationList);
				intent.putExtra(ProductBigPhotoActivity.IMAG_INDEX, (position % mDecorationList.size()));
				startActivityForResult(intent, DEL_PHOTO_INFO);
			}
		});
		setData(0); // 正在加载数据
		//根据code查询子相册图片列表
		getCodeAlbumPhoto();
	}
	
	/**
	 * 设置数据
	 * @param type 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData (int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyContent.setVisibility(View.GONE);
		}
	}
	
	//修改子相册名称
	OnClickListener uppListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//友盟统计
			MobclickAgent.onEvent(ProductPhotoActivity.this, "shop_pro_upp_photoname");
			Intent intent = new Intent(ProductPhotoActivity.this, AddPhotoActivity.class);
			intent.putExtra(AddPhotoActivity.INDEX, "upp");
			intent.putExtra(AddPhotoActivity.CODE, mCode);
			intent.putExtra(AddPhotoActivity.PHOTO_NAME, mName);
			startActivityForResult(intent, UPP_PHOTO_INFO);
		}
	};
	
	
	//上传图片
	OnClickListener upLoadListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//友盟统计
			MobclickAgent.onEvent(ProductPhotoActivity.this, "shop_pro_uploadphoto");
			try {
				
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				if (i.resolveActivity(ProductPhotoActivity.this.getPackageManager()) != null) {
					startActivityForResult(i, SELECT_PIC);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.d(TAG, "调用系统相册失败。。。。。。" + e.getMessage());
			}
		}
	};
	
	public void getCodeAlbumPhoto(){
		new GetSubAlbumPhotoTask(ProductPhotoActivity.this, new GetSubAlbumPhotoTask.Callback() {
			@Override
			public void getResult(JSONArray jsonArray) {
				if (jsonArray == null) {
					return;
				} else {
					setData(1); // 有数据
					if (jsonArray.size() == 0) {
						mTvNoData.setVisibility(View.VISIBLE);
						mGvPhoto.setVisibility(View.GONE);
					} else {
						mTvNoData.setVisibility(View.GONE);
						mGvPhoto.setVisibility(View.VISIBLE);
						mDecorationList = new ArrayList<Decoration>();
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							Decoration decoration = Util.json2Obj(jsonObject.toString(), Decoration.class);
							mDecorationList.add(decoration);
						}
						mPhotoProductAdapter = new ProductPhotoAdapter(ProductPhotoActivity.this, mDecorationList);
						mGvPhoto.setAdapter(mPhotoProductAdapter);
					}
				}
			}
		}).execute(mCode);
	}
	
	/**
	 * 给图片命名
	 * @param context
	 * @return
	 */
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
	 * @param bitmap
	 */
	private void updateHead(Bitmap bitmap) {
		try {
			
			if (bitmap == null) { return; }
			String mPicPath = getFilePath(ProductPhotoActivity.this) + "/" + System.currentTimeMillis() + ".jpg";
			Tools.savBitmapToJpg(bitmap, mPicPath);
			Intent intent1 = new Intent(ProductPhotoActivity.this, UploadPhotoActivity.class);
			intent1.putExtra(UploadPhotoActivity.IMAGE_URL, mPicPath);
			intent1.putExtra(UploadPhotoActivity.FLAG, "two");
			intent1.putExtra(UploadPhotoActivity.NAME, mName);
			intent1.putExtra(UploadPhotoActivity.CODE, mCode);
			startActivityForResult(intent1, INTENT_PHOTO_ADD_INFO);
		} catch (Exception e) {
			Log.d(TAG, "上传照片失败....." + e.getMessage());
		}
	}
	
	/**
	 * 拍照和调用图库时要执行的方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	   if (requestCode == SELECT_PIC) {//选择图片
		   if (resultCode == Activity.RESULT_OK) {
			   if (intent == null) {
                   return;
               }
			   try {
				    Uri uri = intent.getData();
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProductPhotoActivity.this.getContentResolver(), uri);
					updateHead(bitmap);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
		   }
	   }
	   if (requestCode == INTENT_PHOTO_ADD_INFO) {//上传
     	 if (resultCode == UploadPhotoActivity.INTENT_PRODUCT_SAVED) {
     		Decoration addDecoration = (Decoration) intent.getSerializableExtra(AddSubAlbumPhotoTask.DECORATION);
     		mDecorationList = new ArrayList<Decoration>();
     		mDecorationList.add(addDecoration);
     		if (mPhotoProductAdapter == null) {
     			mPhotoProductAdapter = new ProductPhotoAdapter(ProductPhotoActivity.this, mDecorationList);
     			mGvPhoto.setAdapter(mPhotoProductAdapter);
     			mGvPhoto.setVisibility(View.VISIBLE);
     		} else {
     			mPhotoProductAdapter.setItems(mDecorationList);
     		}
     	 }
       }
	   
	   if (requestCode == UPP_PHOTO_INFO) {//修改子相册名称
		 if (resultCode == AddPhotoActivity.UPP_RESP_SAVED) {
			 ShopDecoration uppShopDecoration = (ShopDecoration) intent.getSerializableExtra(UpdateSubAlbumTask.SHOPDECORATION);
			 mEtContent.setText(uppShopDecoration.getName());
			 mName = uppShopDecoration.getName();
		 }
	   }
	   
	   if (requestCode == DEL_PHOTO_INFO) {//删除子相册里面的某一张图片
		   if (resultCode == ProductBigPhotoActivity.DEL_RESP_PHOTO) {
			   Decoration decoration = (Decoration) intent.getSerializableExtra(DelSubAlbumPhotoTask.DECORATION);
			   mDecorationList.remove(decoration.getCode());
			   mPhotoProductAdapter.notifyDataSetChanged();
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
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); //统计页面
		getCodeAlbumPhoto();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}
}
