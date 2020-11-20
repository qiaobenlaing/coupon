package com.huift.hfq.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

public class OnlinePayProcotolFragment extends Fragment {
	
	private final static String TAG = OnlinePayProcotolFragment.class.getSimpleName();
	
	/**返回图标*/
	@ViewInject(R.id.iv_turn_in)
	ImageView mIvBackUp;
	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	TextView mTvdesc;
	
	/**加载网页版协议**/
	private WebView webview;
	
	public static OnlinePayProcotolFragment newInstance() {
		Bundle args = new Bundle();
		OnlinePayProcotolFragment fragment = new OnlinePayProcotolFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_onlinepay_procotol, container, false);// 说明v，注释// e.g:Fragment的view
		ViewUtils.inject(this, v);
		//保存
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		//设置标题
      /*  ImageView msg = (ImageView) v.findViewById(R.id.iv_add);
        msg.setVisibility(View.GONE);*/
		mTvdesc.setText(R.string.onlien_pay_procotol);
		mIvBackUp.setVisibility(View.VISIBLE);
		init(v);
		return v;
	}
	
	/**
	 * 保证activity不为空
	 * @return activity
	 */
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
		webview = (WebView) v.findViewById(R.id.wv_show_procotol);
		//设置WebView属性，能够执行Javascript脚本
	    webview.getSettings().setJavaScriptEnabled(true);
	    //加载需要显示的网页
	    webview.loadUrl(Const.H5_URL+"Browser/onlinePayProtocol");
	    //设置Web视图
        webview.setWebViewClient(new HelloWebViewClient ());
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
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void trunIdenCode(View view) {
		getMyActivity().finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(OnlinePayProcotolFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(OnlinePayProcotolFragment.class.getSimpleName()); //统计页面
	}
}
