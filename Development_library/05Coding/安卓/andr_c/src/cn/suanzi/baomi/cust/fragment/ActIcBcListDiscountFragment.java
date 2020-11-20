package cn.suanzi.baomi.cust.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 工银特惠
 * @author wenis.yu
 */
public class ActIcBcListDiscountFragment extends Fragment {

	private static final String TAG = "ActIcBcListDiscountFragment";

	/** 返回图标*/
	@ViewInject(R.id.iv_turn_in)
	private ImageView mIvBackUp;
	/** 功能描述文本*/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 加载网页版协议*/
	private WebView webview;
	/** 我的活动*/
	private TextView mActAdd;

	public static ActIcBcListDiscountFragment newInstance() {
		Bundle args = new Bundle();
		ActIcBcListDiscountFragment fragment = new ActIcBcListDiscountFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_acticbclist, container, false);
		ViewUtils.inject(this, v);
		init(v);
		return v;
	}
	
	/**
	 * 初始化
	 */
	private void init(View v) {
		mTvdesc.setText(R.string.act_titlename);
		mIvBackUp.setVisibility(View.VISIBLE);
		mActAdd = (TextView) v.findViewById(R.id.tv_add);
		mActAdd.setVisibility(View.GONE);
		webview = (WebView) v.findViewById(R.id.webView);
		//设置WebView属性，能够执行Javascript脚本
	    webview.getSettings().setJavaScriptEnabled(true);
	    //加载需要显示的网页
	    webview.loadUrl(Const.H5_URL + "Browser/icbcAct");
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
		getActivity().finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageEnd(ActIcBcListDiscountFragment.class.getSimpleName());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ActIcBcListDiscountFragment.class.getSimpleName());
	}
}
