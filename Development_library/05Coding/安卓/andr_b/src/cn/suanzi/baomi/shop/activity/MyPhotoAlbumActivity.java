package cn.suanzi.baomi.shop.activity;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.fragment.MyPhotoEnviromentFragment;
import cn.suanzi.baomi.shop.fragment.MyPhotoProductFragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
 
/**
 * 商家相册的选项卡切换
 * @author qian.zhou
 */
public class MyPhotoAlbumActivity extends Activity {
	private RadioButton mRbtnProduct, mRbtnEnvironment;
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        ViewUtils.inject(this);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        init();
    }
    
	private void init() {
		LinearLayout ivBack = (LinearLayout) findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.shop_photo));
		TextView tvMsg = (TextView) findViewById(R.id.tv_msg);
		tvMsg.setBackgroundResource(R.drawable.album_add);
		
		mRbtnProduct = (RadioButton) findViewById(R.id.rbtn_photo_product);
		mRbtnEnvironment = (RadioButton) findViewById(R.id.rbtn_photo_environment);
		mFragmentManager = getFragmentManager();
		// 给关注商家按钮一个默认背景颜色
		changeFragment(MyPhotoAlbumActivity.this, R.id.linear_cardlist_content, new MyPhotoProductFragment());
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
	@OnClick({ R.id.rbtn_photo_product, R.id.rbtn_photo_environment, R.id.layout_turn_in })
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
		case R.id.layout_turn_in://返回
			finish();
			break;
		default:
			break;
		}
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
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
