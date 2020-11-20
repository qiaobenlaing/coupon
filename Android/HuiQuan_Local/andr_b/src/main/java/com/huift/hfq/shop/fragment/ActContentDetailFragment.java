package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.shop.R;

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
public class ActContentDetailFragment extends Fragment {
	
	private final static String TAG = "ActDetailFragment";
	/**传过来的编码号**/
	final static String ACT_CODE = "actCode";
	/**设置标题**/
	private final static String ACT_TITLE = "活动详情";
	/**返回图片**/
	@ViewInject(R.id.layout_turn_in)
	LinearLayout mIvBackup;
	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	TextView mTvdesc;
	/**加载网页版**/
	private WebView mWebview;
	/** 数据加载进度条*/
	private LinearLayout mLyNoData;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	
	public static ActContentDetailFragment newInstance() {
		Bundle args = new Bundle();
		ActContentDetailFragment fragment = new ActContentDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_actdetail, container,false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getActivity());
		//设置标题
		mTvdesc.setText(ACT_TITLE);
		mIvBackup.setVisibility(View.VISIBLE);
		init(view);
		return view;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化
	 */
	private void init(View v) {
		mLyNoData =  (LinearLayout) v.findViewById(R.id.ly_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		//获取传过来的编码值
		Intent intent = getActivity().getIntent();
		Bundle bundle = intent.getExtras();
		String actDetailCode = bundle.getString(ACT_CODE);
		Log.i(TAG, "actDetailCode=============="+actDetailCode);
		mWebview = (WebView) v.findViewById(R.id.webView);
		//设置WebView属性，能够执行Javascript脚本
		mWebview.getSettings().setJavaScriptEnabled(true);
        
        if(Util.isNetworkOpen(getMyActivity())){
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
        	
        }else{
        	Util.getContentValidate(R.string.toast_internet);
        }
        //加载需要显示的网页
		mWebview.loadUrl("http://baomi.suanzi.cn/Api/Browser/sGetActInfo/actCode/"+actDetailCode);
	    //设置Web视图
		mWebview.setWebViewClient(new HelloWebViewClient ());
	}
	
	 //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    
	/**
	 * 点击返回查看到活动列表
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddDetailBackClick(View view) {
		getMyActivity().finish();
	}
}
