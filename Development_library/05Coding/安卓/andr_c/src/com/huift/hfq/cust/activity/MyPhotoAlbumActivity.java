package com.huift.hfq.cust.activity;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.fragment.MyPhotoEnviromentFragment;
import com.huift.hfq.cust.fragment.MyPhotoProductFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
 
/**
 * 商家相册的选项卡切换
 * @author yanfang.li
 */
public class MyPhotoAlbumActivity extends Activity {
	
	private static final String TAG = MyPhotoAlbumActivity.class.getSimpleName();
	public static final String SHOP_CODE = "shopCode";
	/** 产品*/
	private RadioButton mRbtnProduct;
	/** 环境*/
	private RadioButton mRbtnEnvironment;
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        ViewUtils.inject(this);
        init();
    }
    
	private void init() {
		ImageView ivBack = (ImageView) findViewById(R.id.iv_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.shop_photo));
		
		
		mRbtnProduct = (RadioButton) findViewById(R.id.rbtn_photo_product);
		mRbtnEnvironment = (RadioButton) findViewById(R.id.rbtn_photo_environment);
		mFragmentManager = getFragmentManager();
		// 给关注商家按钮一个默认背景颜色
		changeFragment(MyPhotoAlbumActivity.this, R.id.linear_cardlist_content, new MyPhotoEnviromentFragment());
		mRbtnEnvironment.setChecked(true);
	}
	
	/**
	 * 更换页面
	 * @param id
	 * @param fragment
	 */
	public void changeFragment(Activity activity, int id, Fragment fragment) {
		mFragmentManager = activity.getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(id, fragment);
		mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();
	}
	
	/**
	 * 添加fragment的跳转
	 * @param v
	 */
	@OnClick({ R.id.rbtn_photo_product, R.id.rbtn_photo_environment, R.id.iv_turn_in })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbtn_photo_product:// 加载到商家相册的产品页面
			changeFragment(MyPhotoAlbumActivity.this, R.id.linear_cardlist_content, new MyPhotoProductFragment());
			mRbtnProduct.setEnabled(false);
			mRbtnEnvironment.setEnabled(true);
			break;
		case R.id.rbtn_photo_environment:// 加载商家环境页面
			changeFragment(MyPhotoAlbumActivity.this, R.id.linear_cardlist_content, new MyPhotoEnviromentFragment());
			mRbtnProduct.setEnabled(true);
			mRbtnEnvironment.setEnabled(false);
			break;
		case R.id.iv_turn_in://返回
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
