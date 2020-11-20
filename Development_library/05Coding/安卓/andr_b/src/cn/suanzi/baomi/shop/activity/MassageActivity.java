// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.21
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Messages;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.fragment.HomeFragment;
import cn.suanzi.baomi.shop.fragment.MsgCardFragment;
import cn.suanzi.baomi.shop.fragment.MsgCouponFragment;
import cn.suanzi.baomi.shop.fragment.MsgShopFragment;
import cn.suanzi.baomi.shop.fragment.MsgVipFragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 会员卡设置
 * @author yanfang.li
 */
public class MassageActivity extends Activity {
	
	/** 跳转过来的ID **/
	private final static String TAG = MassageActivity.class.getSimpleName();
	public final static String MESSAGE_OBJ = "messgaeObj";

	/** 会员沟通按钮 */
	@ViewInject(R.id.btn_msgvip)
	private Button mBtnVip;
	/** 会员卡按钮 */
	@ViewInject(R.id.btn_msgcard)
	private Button mBtnCard;
	/** 优惠券按钮 */
	@ViewInject(R.id.btn_msgcoupon)
	private Button mBtnCoupon;
	/** “会员卡”按钮 */
	@ViewInject(R.id.btn_msgshop)
	private Button mBtnShop;
	/**显示消息数据*/
	private TextView mTvCouponCount;
	private TextView mTvCardCount;
	private TextView mTvCommCount;
	private TextView mTvShopCount;
	/** 四个选项卡对应的fragment*/
	private Fragment mMsgVipFragment;
	private Fragment mMsgCardFragment;
	private Fragment mMsgCouponFragment;
	private Fragment mMsgShopFragment;
	public int currentTabIndex=0;
	private Fragment currentFragment=null;
	private Messages mMessages;

	/** Fragment管理器 */
	private static FragmentManager mFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_massage);
		ViewUtils.inject(this);
		//Util.addLoginActivity(MassageActivity.this);
		mFragmentManager = getFragmentManager();
		mMessages = (Messages) getIntent().getSerializableExtra(MESSAGE_OBJ);
		init(null);
		// 返回
		LinearLayout ivReturn = (LinearLayout) findViewById(R.id.layout_turn_in);
		TextView tvTitle = (TextView) findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.mymsg));
		ivReturn.setOnClickListener(returnClickListener);
		Util.addLoginActivity(MassageActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	/**
	 * 退出
	 */
	OnClickListener returnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			getReturn();
			
		}
	};
	
	/**
	 * 带参数退出
	 */
	private void getReturn () {
		Intent intent = new Intent(MassageActivity.this, HomeActivity.class);
		if (mMessages == null) {
			mMessages = new Messages();
		}
			mMessages.setCard(MsgCardFragment.setMsgCleared());
			mMessages.setCoupon(MsgCouponFragment.setMsgCleared()); 
			mMessages.setShop(MsgShopFragment.setMsgCleared());
			mMessages.setCommunication(MsgVipFragment.setMsgCleared()); 
		intent.putExtra(HomeFragment.MESSAGE_OBj, mMessages);
		setResult(HomeFragment.REQUEST_SUCC, intent);
		finish();
	}
	
	public void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		init(intent);
	}
	
	/**
	 * 重写点击返回按钮方法，
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			getReturn();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@OnClick({ R.id.btn_msgvip, R.id.btn_msgcard, R.id.btn_msgcoupon, R.id.btn_msgshop })
	public void tt(View v) {
		switch (v.getId()) {
		case R.id.btn_msgcoupon:{
			mTvCouponCount.setVisibility(View.GONE);
			if(mMsgCouponFragment==null){
				mMsgCouponFragment = new MsgCouponFragment();
			}
			changeFrament(mMsgCouponFragment, null, "MsgCouponFragment");
			currentTabIndex = 0;
			mBtnCoupon.setBackgroundResource(R.drawable.msg_tableft_sel);
			mBtnCoupon.setTextColor(getResources().getColor(R.color.white));
			mBtnVip.setBackgroundResource(R.drawable.msg_tabcenter_nosel);
			mBtnVip.setTextColor(getResources().getColor(R.color.red));
			mBtnCard.setBackgroundResource(R.drawable.msg_tabcenter_nosel);
			mBtnCard.setTextColor(getResources().getColor(R.color.red));
			mBtnShop.setBackgroundResource(R.drawable.msg_tabright_nosel);
			mBtnShop.setTextColor(getResources().getColor(R.color.red));
			break;
		}
		case R.id.btn_msgcard:{
			mTvCardCount.setVisibility(View.GONE);
			if(mMsgCardFragment==null){
				mMsgCardFragment = new MsgCardFragment();
			}
			changeFrament(mMsgCardFragment, null, "MsgCardFragment");
			currentTabIndex = 1;
			mBtnCoupon.setBackgroundResource(R.drawable.msg_tableft_nosel);
			mBtnCoupon.setTextColor(getResources().getColor(R.color.red));
			mBtnCard.setBackgroundResource(R.drawable.msg_tabcenter_sel);
			mBtnCard.setTextColor(getResources().getColor(R.color.white));
			mBtnVip.setBackgroundResource(R.drawable.msg_tabcenter_nosel);
			mBtnVip.setTextColor(getResources().getColor(R.color.red));
			mBtnShop.setBackgroundResource(R.drawable.msg_tabright_nosel);
			mBtnShop.setTextColor(getResources().getColor(R.color.red));
			break;
		}
		case R.id.btn_msgvip:{
			mTvCommCount.setVisibility(View.GONE);
			if (mMsgVipFragment == null) {
				mMsgVipFragment = new MsgVipFragment();
			}
			changeFrament(mMsgVipFragment, null, "MsgVipFragment");
			currentTabIndex = 2;
			mBtnCoupon.setBackgroundResource(R.drawable.msg_tableft_nosel);
			mBtnCoupon.setTextColor(getResources().getColor(R.color.red));
			mBtnCard.setBackgroundResource(R.drawable.msg_tabcenter_nosel);
			mBtnCard.setTextColor(getResources().getColor(R.color.red));
			mBtnVip.setBackgroundResource(R.drawable.msg_tabcenter_sel);
			mBtnVip.setTextColor(getResources().getColor(R.color.white));
			mBtnShop.setBackgroundResource(R.drawable.msg_tabright_nosel);
			mBtnShop.setTextColor(getResources().getColor(R.color.red));
			break;
		}
		case R.id.btn_msgshop:{
			mTvShopCount.setVisibility(View.GONE);
			if(mMsgShopFragment==null){
				mMsgShopFragment = new MsgShopFragment();
			}
			changeFrament(mMsgShopFragment, null, "MsgShopFragment");
			currentTabIndex = 3;
			mBtnCoupon.setBackgroundResource(R.drawable.msg_tableft_nosel);
			mBtnCoupon.setTextColor(getResources().getColor(R.color.red));
			mBtnCard.setBackgroundResource(R.drawable.msg_tabcenter_nosel);
			mBtnCard.setTextColor(getResources().getColor(R.color.red));
			mBtnVip.setBackgroundResource(R.drawable.msg_tabcenter_nosel);
			mBtnVip.setTextColor(getResources().getColor(R.color.red));
			mBtnShop.setBackgroundResource(R.drawable.msg_tabright_sel);
			mBtnShop.setTextColor(getResources().getColor(R.color.white));
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 初始化操作
	 */
	public void init(Intent intent) {
		mTvCouponCount = (TextView) findViewById(R.id.tv_couponcount);
		mTvCouponCount.setVisibility(View.GONE);
		mTvCardCount = (TextView) findViewById(R.id.tv_cardcount);
		mTvCardCount.setVisibility(View.GONE);
		mTvCommCount = (TextView) findViewById(R.id.tv_commcount);
		mTvCommCount.setVisibility(View.GONE);
		mTvShopCount = (TextView) findViewById(R.id.tv_shopcount);
		mTvShopCount.setVisibility(View.GONE);
		
		if (null != mMessages) {
			if (mMessages.getCoupon() > 0) {
				mTvCouponCount.setVisibility(View.GONE);
			} else {
				mTvCouponCount.setVisibility(View.GONE);
			}
			if (mMessages.getCard() > 0) {
				mTvCardCount.setVisibility(View.VISIBLE);
				if (mMessages.getCard() > 99) {
					mTvCardCount.setText("99+");
				} else {
					mTvCardCount.setText("" + mMessages.getCard());
				}
				
			} else {
				mTvCardCount.setVisibility(View.GONE);
			}
			
			if (mMessages.getCommunication() > 0) {
				mTvCommCount.setVisibility(View.VISIBLE);
				if (mMessages.getCommunication() > 99) {
					mTvCommCount.setText("99+");
				} else {
					mTvCommCount.setText("" + mMessages.getCommunication());
				}
			} else {
				mTvCommCount.setVisibility(View.GONE);
			}
			
			if (mMessages.getShop() > 0) {
				mTvShopCount.setVisibility(View.VISIBLE);
				if (mMessages.getShop() > 99) {
					mTvShopCount.setText("99+");
				} else {
					Log.d(TAG, "shopcount="+mMessages.getShop());
					mTvShopCount.setText("" + mMessages.getShop());
				}
			} else {
				mTvShopCount.setVisibility(View.GONE);
			}
			
		}
		
		if(mMsgCouponFragment == null){
			mMsgCouponFragment = new MsgCouponFragment();
		}
		
		showFragment(null,mMsgCouponFragment);
		mBtnCoupon.setBackgroundResource(R.drawable.msg_tableft_sel);
		mBtnCoupon.setTextColor(getResources().getColor(R.color.white));
	}
	
	// 更换Fragment
	public void changeFrament(Fragment tofragment, Bundle bundle,
			String tag) {
		/*
		for (int i = 0, count = mFragmentManager.getBackStackEntryCount(); i < count; i++) {
			mFragmentManager.popBackStack();
		}*/
		Fragment framfragment=null;
		if (currentTabIndex == 0){
			framfragment = mMsgCouponFragment;
		} else if (currentTabIndex == 1) {
			framfragment = mMsgCardFragment;
		} else if (currentTabIndex == 2) {
			framfragment = mMsgVipFragment;
		} else if (currentTabIndex == 3) {
			framfragment = mMsgShopFragment;
		} else {
			return ;
		}
		if(framfragment.getClass() == tofragment.getClass()){
			return ;
		}
		showFragment(framfragment,tofragment);		
	}
	
	public void showFragment(Fragment fromfragment,Fragment tofragment){
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		if(currentFragment!=null){
			trx.hide(currentFragment);
		}else if (fromfragment != null) {
			trx.hide(fromfragment);
		}
		if (!tofragment.isAdded()) {
			trx.add(R.id.ly_msgcontent, tofragment);
		}
		currentFragment = tofragment;
		try {
			trx.show(tofragment).commit();
		} catch (Exception e){
			try {
				trx.show(tofragment).commitAllowingStateLoss();
			} catch (Exception ee){}
		}
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
	
}
