package cn.suanzi.baomi.cust.activity;


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
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.fragment.MyOrderManagerFailFragment;
import cn.suanzi.baomi.cust.fragment.MyOrderManagerSusFragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
 
/**
 * 订单管理的选项卡切换
 * @author qian.zhou
 */
public class MyOrderManagerActivity extends Activity {
	private RadioButton mRbtnSuggest, mRbtnFail;
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	//标识是否显示支付成功的订单详情 已经完成为true
	private boolean mFlagShowSuccess = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhome);
        ViewUtils.inject(this);
        AppUtils.setActivity(this);
        init();
    }
    
	private void init() {
		ImageView ivBack = (ImageView) findViewById(R.id.iv_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.order_manager));
		ImageView tvMsg = (ImageView) findViewById(R.id.iv_add);
		tvMsg.setVisibility(View.GONE);
		mRbtnSuggest = (RadioButton) findViewById(R.id.rbtn_orderlist_suggset);
		mRbtnFail = (RadioButton) findViewById(R.id.rbtn_orderlist_fail);
		mFragmentManager = getFragmentManager();
		// 给关注商家按钮一个默认背景颜色
		changeFragment(MyOrderManagerActivity.this, R.id.linear_cardlist_content, new MyOrderManagerSusFragment());
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
	@OnClick({ R.id.rbtn_orderlist_suggset, R.id.rbtn_orderlist_fail, R.id.iv_turn_in })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbtn_orderlist_suggset:// 加载完成订单页面
			changeFragment(MyOrderManagerActivity.this, R.id.linear_cardlist_content, new MyOrderManagerSusFragment());
			mRbtnSuggest.setEnabled(false);
			mRbtnFail.setEnabled(true);
			break;
		case R.id.rbtn_orderlist_fail:// 加载未支付订单页面
			changeFragment(MyOrderManagerActivity.this, R.id.linear_cardlist_content, new MyOrderManagerFailFragment());
			mRbtnSuggest.setEnabled(true);
			mRbtnFail.setEnabled(false);
			break;
		case R.id.iv_turn_in:
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
	
	/**
	 *从订单详情返回时  重新调用方法  做数据的更新
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		mFlagShowSuccess = DB.getBoolean(CustConst.Key.CANCEL_ORDER_ISSUCCESS);
		if (mFlagShowSuccess) {
			DB.saveBoolean(CustConst.Key.CANCEL_ORDER_ISSUCCESS,false);
			changeFragment(MyOrderManagerActivity.this, R.id.linear_cardlist_content, new MyOrderManagerFailFragment());
		}
	}
	
   public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
  }
}
