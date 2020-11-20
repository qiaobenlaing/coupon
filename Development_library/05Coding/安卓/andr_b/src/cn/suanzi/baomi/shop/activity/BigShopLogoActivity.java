package cn.suanzi.baomi.shop.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;

import com.lidroid.xutils.ViewUtils;

/**
 * 查看商店logo大图
 * @author qian.zhou
 */
public class BigShopLogoActivity extends Activity{
	public static final String TAG = "BigPhotoActivity";
	public static final String IMAGEURL = "imageUrl";
	public static final  int CROP_PIC = 1;
    private ImageView mIvShowPhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showshoplogo);
		Util.addActivity(BigShopLogoActivity.this);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		//取值
		Intent intent = this.getIntent();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		String imageUrl = intent.getStringExtra(IMAGEURL);
		mIvShowPhoto = (ImageView) findViewById(R.id.iv_show_bigphoto);
		//显示图片
		if(!Util.isEmpty(imageUrl)){
			
			Util.showImage(this, imageUrl, mIvShowPhoto);
		}
		LinearLayout lyPhoto = (LinearLayout) findViewById(R.id.layout_bigimage);
		lyPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
   public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
   }
}
