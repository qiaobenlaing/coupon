package cn.suanzi.baomi.cust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 扫一扫结果
 * @author wensi.yu
 *
 */
public class ScanResultActivity extends Activity {

	private static final String TAG = ScanResultActivity.class.getSimpleName();
	
	private static final String SCAN_TITLE = "扫一扫结果";
	/**返回图片**/
	@ViewInject(R.id.iv_turn_in)
	ImageView mIvBackup;
	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	TextView mTvdesc;
	/**结果类容**/
	@ViewInject(R.id.tv_scanresult)
	TextView mScanResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanresult);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		//添加
		Util.addActivity(this);
		//设置标题
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc.setText(SCAN_TITLE);
		ImageView scanAdd = (ImageView) findViewById(R.id.iv_add);
		scanAdd.setVisibility(View.GONE);
		
		Intent intent = this.getIntent();
		String scanResult = intent.getStringExtra("resultString");
		mScanResult.setText(scanResult);
	}

	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnActAddBackClick(View view) {		
		finish();
	}
	
	 public void onResume(){
	    	super.onResume();
	    	AppUtils.setActivity(this);
			AppUtils.setContext(getApplicationContext());
	    }
}
