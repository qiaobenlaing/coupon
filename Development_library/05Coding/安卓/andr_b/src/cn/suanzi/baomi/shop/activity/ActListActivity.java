package cn.suanzi.baomi.shop.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.fragment.ActListContentFragment;
import cn.suanzi.baomi.shop.fragment.ActListMoneyFragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author wensi.yu
 * 营销活动主页
 *
 */   
public class ActListActivity extends Activity {
	
	private final static String TAG = "ActListActivity";
	
	public static final String STATUS_CONTENT = "1";
	public static final String STATUS_MONEY = "0";
	/** 跳转过来的ID */
	public final static String FRAG_ID = "fragid";
	/** 活动按钮*/
	@ViewInject(R.id.btn_aclist_content)
	private RadioButton mBtnAcListContent;
	/** 红包按钮*/
	@ViewInject(R.id.btn_aclist_money)
	private RadioButton mBtnAcListMoney;
	/** 返回*/
	@ViewInject(R.id.iv_actlist_back)
	private ImageView mIvActListBack;
	/** 添加*/
	@ViewInject(R.id.iv_actlist_add)
	private ImageView mIvActListAdd;
	@ViewInject(R.id.rg_coupon_type)
	private RadioGroup mCouponTypeGroup;
	private String mStatus;
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private ActListContentFragment mAactFragment;
	private ActListMoneyFragment mMoneyFragmet;
	private Fragment mCurrentFragment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actlist);
		ViewUtils.inject(this);
		mFragmentManager = getFragmentManager();
		init(null);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	/**
	 *  初始化 
	 */
	private void init(Intent intent) {
		//status = STATUS_CONTENT;
		changeFragment(R.id.linear_aclist_content, new ActListContentFragment());
		mCouponTypeGroup.check(mBtnAcListContent.getId());
	}
	
	/**
	 * 更换页面
	 * @param id
	 * @param fragment
	 */
	private void changeFragment(int id, Fragment fragment) {
		if (mCurrentFragment==fragment) {
			return ;
		}
		mFragmentTransaction = mFragmentManager.beginTransaction();
		//mFragmentTransaction.replace(id, fragment);
		if (mCurrentFragment==null) {
			if (!fragment.isAdded()) {
				mFragmentTransaction.add(id,fragment);
			} else {
				mFragmentTransaction.show(fragment);
			}
		} else {
			if (!fragment.isAdded()) {
				mFragmentTransaction.hide(mCurrentFragment).add(id,fragment);
			} else {
				mFragmentTransaction.hide(mCurrentFragment).show(fragment);
			}
		}
		
		mCurrentFragment=fragment;
		mFragmentTransaction.commit();
	}

	/**
	 * 添加fragment的跳转
	 * @param v
	 */
	@OnClick({ R.id.btn_aclist_content,R.id.btn_aclist_money })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_aclist_content://加载到活动页面
			mStatus = STATUS_CONTENT;
			if (mAactFragment==null) {
				mAactFragment=new ActListContentFragment();
			}
			changeFragment(R.id.linear_aclist_content,mAactFragment);
			
			mIvActListAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(ActListActivity.this, ActAddActivity.class);
					startActivity(intent);
				}
			});
			
			break;
		case R.id.btn_aclist_money://加载红包页面
			mStatus = STATUS_MONEY;
			if (mMoneyFragmet==null) {
				mMoneyFragmet=new ActListMoneyFragment();
			}
			changeFragment(R.id.linear_aclist_content,mMoneyFragmet);
			
			mIvActListAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Intent intent=new Intent(ActListActivity.this, ActAddMoneyActivity.class);
					startActivity(intent);*/
				}
			});
			
			break;
		default:
			break;
		}
	}
	
	/**
	 * 点击返回按钮 返回到主页面
	 * @param view
	 */
	@OnClick(R.id.iv_actlist_back)
	public void turnAclistBackClick(View view){	
		finish();
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
