package com.huift.hfq.shop.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.shop.ShopConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author  wensi.yu
 * 营销活动的详情
 */
@SuppressLint("SetJavaScriptEnabled")
public class ActContentDetailActivity extends Activity {

	private final static String TAG = "ActContentDetailActivity";
	
	public final static String TYPE = "type";
	/**活动编码*/
	public final static String ACT_CODE = "actCode";
	/**设置标题**/
	private final static String ACT_TITLE = "活动详情";
	/**返回图片**/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 加载网页版 **/
	private WebView mWebview;
	/**所属类别*/
	private String mType;
	/**活动编码*/
	private String activityCode;
	/** 数据加载进度条*/
	private LinearLayout mLyNoData;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actdetail);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		//设置标题
		mTvdesc.setText(ACT_TITLE);
		mIvBackup.setVisibility(View.VISIBLE);
		init();
	}
	
	 public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
	}

	/**
	 * 初始化
	 */
	private void init() {
		//传过来的值
		Intent intent = ActContentDetailActivity.this.getIntent();
		mType = intent.getStringExtra(TYPE);
		activityCode = intent.getStringExtra(ACT_CODE);
		
		mWebview = (WebView) findViewById(R.id.wb_actdetail);
		mLyNoData =  (LinearLayout) findViewById(R.id.ly_nodata);
		mProgView = (ProgressBar) findViewById(R.id.prog_nodata);
		mIvView = (ImageView) findViewById(R.id.iv_nodata);
		
		// 设置WebView属性，能够执行Javascript脚本
		mWebview.getSettings().setJavaScriptEnabled(true);
		
		if(Util.isNetworkAvailable(this)){
			ViewSolveUtils.setNoData(mWebview, mLyNoData, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
			mWebview.setWebChromeClient(new WebChromeClient(){
	     		   @Override
	     			public void onProgressChanged(WebView view, int newProgress) {
	     				super.onProgressChanged(view, newProgress);
	     				 if(newProgress == 100){  
	     					 ViewSolveUtils.setNoData(mWebview, mLyNoData, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 已经加载完了
	     			     } 
	     			}
	     	    });
			
			//活动详情
			if(ShopConst.WebPage.ACT_DETAIL.equals(mType)){
				mWebview.loadUrl(Const.H5_URL + "Browser/sGetActInfo/actCode/"+activityCode);
			}
			
		}else{
			Util.getToastBottom(this, "请连接网络");
		}
	}
	
	/**
	 * 点击返回查看到活动列表
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddDetailBackClick(View view) {
		finish();
	}
}
