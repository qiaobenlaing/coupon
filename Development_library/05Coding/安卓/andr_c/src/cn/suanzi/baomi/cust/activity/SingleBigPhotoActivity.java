package cn.suanzi.baomi.cust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;

/**
 * 查看单个大图
 * @author qian.zhou
 */
public class SingleBigPhotoActivity extends Activity {
	public static final String TAG = "BigPhotoActivity";
	public static final String IMAGEURL = "imageUrl";
	public static final  int CROP_PIC = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_singlebigphoto);
		Util.addActivity(SingleBigPhotoActivity.this);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		Intent intent = this.getIntent();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		String imageUrl = intent.getStringExtra(IMAGEURL);
		final ImageView ivShowPhoto = (ImageView) findViewById(R.id.iv_show_bigphoto);
		Util.showImage(this, imageUrl, ivShowPhoto);
		
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
