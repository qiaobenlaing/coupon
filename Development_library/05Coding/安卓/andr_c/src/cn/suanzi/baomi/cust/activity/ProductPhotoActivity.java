package cn.suanzi.baomi.cust.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Decoration;
import cn.suanzi.baomi.base.pojo.ShopDecoration;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XGridView;
import cn.suanzi.baomi.base.view.XGridView.IXGridViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.ProductPhotoAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.cGetSubAlbumPhotoTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 产品相册
 * 
 * @author yanfang.li
 */
public class ProductPhotoActivity extends Activity implements IXGridViewListener {
	public static final String TAG = ProductPhotoActivity.class.getSimpleName();
	public static final String DECORATION = "decotation";

	/** 显示照片 */
	private XGridView mGvPhoto;
	/** 装相片的集合 */
	private List<Decoration> mDecorationList;
	private ProductPhotoAdapter mPhotoProductAdapter;
	/** 相册对象 */
	private ShopDecoration mDecoration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_photo);
		Util.addActivity(ProductPhotoActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		mDecoration = (ShopDecoration) this.getIntent().getSerializableExtra(DECORATION);
		// 头顶标题
		TextView mEtContent = (TextView) findViewById(R.id.tv_mid_content);
		mGvPhoto = (XGridView) findViewById(R.id.gv_photo_product);
		mGvPhoto.setPullLoadEnable(false);
		mGvPhoto.setXGridViewListener(this);
		if (null == mDecoration || null == mDecoration.getName() || Util.isEmpty(mDecoration.getName())) {
			mEtContent.setText("相册");
		} else {
			mEtContent.setText(mDecoration.getName());
		}
		cGetSubAlbumPhoto(); // 根据code查询子相册图片列表

	}

	/**
	 * 子相册
	 */
	public void cGetSubAlbumPhoto() {
		final LinearLayout lyView = (LinearLayout) findViewById(R.id.ly_nodate);
		final ImageView ivView = (ImageView) findViewById(R.id.iv_nodata);
		final ProgressBar progView = (ProgressBar) findViewById(R.id.prog_nodata);
		
		ViewSolveUtils.setNoData(mGvPhoto, lyView, ivView, progView, CustConst.DATA.LOADIMG);
		if (null == mDecoration || null == mDecoration.getCode() || Util.isEmpty(mDecoration.getCode())) { return; }
		new cGetSubAlbumPhotoTask(ProductPhotoActivity.this, new cGetSubAlbumPhotoTask.Callback() {
			@Override
			public void getResult(JSONArray result) {
				if (result == null || result.size() == 0) {
					// 处理没有数据
					ViewSolveUtils.setNoData(mGvPhoto, lyView, ivView, progView,CustConst.DATA.NO_DATA);
				} else {
					ViewSolveUtils.setNoData(mGvPhoto, lyView, ivView, progView,CustConst.DATA.HAVE_DATA);
					mDecorationList = new ArrayList<Decoration>();
					for (int i = 0; i < result.size(); i++) {
						JSONObject jsonObject = (JSONObject) result.get(i);
						Decoration decoration = Util.json2Obj(jsonObject.toString(), Decoration.class);
						mDecorationList.add(decoration);
					}
					mPhotoProductAdapter = new ProductPhotoAdapter(ProductPhotoActivity.this, mDecorationList);
					mGvPhoto.setAdapter(mPhotoProductAdapter);
					// 查看大图
					mGvPhoto.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> v, View view, int position, long id) {
							Decoration decoration = (Decoration) mGvPhoto.getItemAtPosition(position);
							Intent intent = new Intent(ProductPhotoActivity.this, ShopBigPhotoActivity.class);
							intent.putExtra(ShopBigPhotoActivity.DECORATION, decoration);
							intent.putExtra(ShopBigPhotoActivity.DECORATION_LIST, (Serializable)mDecorationList);
							intent.putExtra(ShopBigPhotoActivity.POSITION, position);
							intent.putExtra(ShopBigPhotoActivity.TYPE, "1"); // 0
							// 代表是环境的图片
							startActivity(intent);
						}
					});
				}
			}
		}).execute(mDecoration.getCode());
	}

	/**
	 * 点击返回查看到活动列表
	 * 
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnBackClick(View view) {
		finish();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}
	
	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	        AppUtils.setContext(getApplicationContext());
	}

}
