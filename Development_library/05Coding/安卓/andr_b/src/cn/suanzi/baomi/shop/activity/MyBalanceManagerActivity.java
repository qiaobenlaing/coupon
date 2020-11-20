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
import cn.suanzi.baomi.shop.fragment.MyBalanceManagerFailFragment;
import cn.suanzi.baomi.shop.fragment.MyBalanceManagerSusFragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
 
/**
 * 订单管理的选项卡切换
 * @author qian.zhou
 */
public class MyBalanceManagerActivity extends Activity {
	private RadioButton mRbtnSuggest, mRbtnFail;
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancehome);
        ViewUtils.inject(this);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        init();
    }
    
	private void init() {
		LinearLayout ivBack = (LinearLayout) findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.order_manager));
		mRbtnSuggest = (RadioButton) findViewById(R.id.rbtn_orderlist_suggset);
		mRbtnFail = (RadioButton) findViewById(R.id.rbtn_orderlist_fail);
		mFragmentManager = getFragmentManager();
		// 给已完成按钮一个默认背景颜色
		changeFragment(MyBalanceManagerActivity.this, R.id.linear_cardlist_content, new MyBalanceManagerSusFragment());
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
	@OnClick({ R.id.rbtn_orderlist_suggset, R.id.rbtn_orderlist_fail, R.id.layout_turn_in })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbtn_orderlist_suggset:// 加载到订单完成页面
			changeFragment(MyBalanceManagerActivity.this, R.id.linear_cardlist_content, new MyBalanceManagerSusFragment());
			mRbtnSuggest.setEnabled(false);
			mRbtnFail.setEnabled(true);
			break;
		case R.id.rbtn_orderlist_fail:// 加载到订单未完成页面
			changeFragment(MyBalanceManagerActivity.this, R.id.linear_cardlist_content, new MyBalanceManagerFailFragment());
			mRbtnSuggest.setEnabled(true);
			mRbtnFail.setEnabled(false);
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
