package cn.suanzi.baomi.shop.activity;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.ShopDecoration;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.adapter.ProductPhotoCheckTypeAdapter;
import cn.suanzi.baomi.shop.model.ListSubAlbumTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 产品相册(选择照片上传的类别)
 * @author qian.zhou
 */
public class ProductPhotoTypeActivity extends Activity {
	public static final String TAG = "ProductPhotoTypeActivity";
	/** setResult()用的，如果确实选择了信息 */
	public final static int INTENT_RESP_SAVED = 1;
	/** 显示子相册列表*/
	private ListView  mLvPhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_type);
		Util.addActivity(ProductPhotoTypeActivity.this);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		//初始化数据
		init();
		//根据code查询子相册名称列表
		getCodeAlbumPhoto();
	}

	private void init() {
		//标题
		LinearLayout ivTurnin = (LinearLayout) findViewById(R.id.layout_turn_in);
		ivTurnin.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getString(R.string.check_type));
		//初始化数据
		mLvPhoto = (ListView) findViewById(R.id.lv_phototype);
		//listview的点击事件
		mLvPhoto.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent();
				ShopDecoration shopDecoration = (ShopDecoration) mLvPhoto.getItemAtPosition(position);
				intent.putExtra(UploadPhotoActivity.SHOPDECORATION, shopDecoration);
				setResult(ProductPhotoTypeActivity.INTENT_RESP_SAVED, intent);
				finish();
			}
		});
	}
	
	/**
	 * 获得产品的子相册
	 */
	public void getCodeAlbumPhoto(){
		new ListSubAlbumTask(ProductPhotoTypeActivity.this, new ListSubAlbumTask.Callback() {
			@Override
			public void getResult(JSONArray jsonArray) {
				if (jsonArray == null) {
					return;
				} else {
					List<ShopDecoration> shopList = new ArrayList<ShopDecoration>();
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							ShopDecoration shopDecoration = Util.json2Obj(jsonObject.toString(), ShopDecoration.class);
							shopList.add(shopDecoration);
						}
						ProductPhotoCheckTypeAdapter adapter = new ProductPhotoCheckTypeAdapter(ProductPhotoTypeActivity.this, shopList);
						mLvPhoto.setAdapter(adapter);
				}
			}
		}).execute();
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
