package cn.suanzi.baomi.cust.activity;



import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.fragment.CollectionPromotionFragment;
import cn.suanzi.baomi.cust.fragment.FinishPromotionFragment;
import cn.suanzi.baomi.cust.fragment.ProcessPromotionFragment;
/**
 * 我的活动
 * @author yingchen
 *
 */
public class MyPromotionActivity extends Activity implements OnClickListener{
	/**进行中*/
	private RadioButton mProcess;
	
	/**已完成*/
	private RadioButton mFinish;
	
	/**收藏*/
	private RadioButton mCollection;
	
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	
	/**进行中的活动Fragment*/
	private ProcessPromotionFragment mProcessFragment;
	
	/**已结束的活动*/
	private FinishPromotionFragment mFinishFragment;
	
	/**收藏的活动*/
	private CollectionPromotionFragment mCollectFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypromotion);
	    AppUtils.setActivity(this);
	    AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	    
	    initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		//标题
		TextView title = (TextView) findViewById(R.id.tv_mid_content);
		title.setText("我的活动");
		
		//回退
		ImageView back = (ImageView) findViewById(R.id.iv_turn_in);
		back.setOnClickListener(this);
		
		mProcess = (RadioButton) findViewById(R.id.rb_process);
		mFinish = (RadioButton) findViewById(R.id.rb_finish);
		mCollection = (RadioButton) findViewById(R.id.rb_collection);
		mProcess.setOnClickListener(this);
		mFinish.setOnClickListener(this);
		mCollection.setOnClickListener(this);
		
	
		mFragmentManager = getFragmentManager();
		
		//默认显示正在进行中的活动
		if(null == mProcessFragment){
			mProcessFragment = ProcessPromotionFragment.newInstance();
		}
		changeFragment(this,R.id.ll_promotion_content, mProcessFragment);
		setRadioButtonStatus(false,true,true);
	}

	/**
	 * 切换Fragment
	 * @param id
	 * @param fragment
	 */
	public void changeFragment(Activity activity, int id, Fragment fragment) {
		mFragmentManager = activity.getFragmentManager();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.replace(id, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	/**
	 * 点击的回掉
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in: //回退
			this.finish();
			break;

		case R.id.rb_process: //正在进行的活动
			if(null == mProcessFragment){
				mProcessFragment = ProcessPromotionFragment.newInstance();
			}
			changeFragment(this,R.id.ll_promotion_content, mProcessFragment);
			setRadioButtonStatus(false,true,true);
			break;
			
		case R.id.rb_finish: //已经完成的活动
			if(null == mFinishFragment){
				mFinishFragment = FinishPromotionFragment.newInstance();
			}
			changeFragment(this,R.id.ll_promotion_content, mFinishFragment);
			setRadioButtonStatus(true,false,true);
			break;
			
		case R.id.rb_collection: //已经收藏的活动
			if(null == mCollectFragment){
				mCollectFragment = CollectionPromotionFragment.newInstance();
			}
			changeFragment(this,R.id.ll_promotion_content, mCollectFragment);
			setRadioButtonStatus(true,true,false);
			break;
			
		default:
			break;
		}
	}

	/**
	 * 设置单选按钮的状态
	 * @param processEnabled  进行中
	 * @param finishEnabled   已完成
	 * @param collectEnabled  收藏
	 */
	private void setRadioButtonStatus(boolean processEnabled, boolean finishEnabled, boolean collectEnabled) {
		if(processEnabled){
			mProcess.setEnabled(true);
			mProcess.setBackgroundDrawable(null);
		}else{
			mProcess.setEnabled(false);
			mProcess.setBackgroundResource(R.drawable.bottom_red_border);
		}
		
		if(finishEnabled){
			mFinish.setEnabled(true);
			mFinish.setBackgroundDrawable(null);
		}else{
			mFinish.setEnabled(false);
			mFinish.setBackgroundResource(R.drawable.bottom_red_border);
		}
		
		if(collectEnabled){
			mCollection.setEnabled(true);
			mCollection.setBackgroundDrawable(null);
		}else{
			mCollection.setEnabled(false);
			mCollection.setBackgroundResource(R.drawable.bottom_red_border);
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK ){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
