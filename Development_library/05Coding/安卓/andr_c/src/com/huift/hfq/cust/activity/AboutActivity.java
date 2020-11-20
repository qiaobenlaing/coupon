package com.huift.hfq.cust.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;
 
@SuppressLint("SetJavaScriptEnabled")
public class AboutActivity extends Activity {
    private WebView webview;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext()); // 保存context
        setContentView(R.layout.activity_about);
        
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
        	return;
        }
        String title = bundle.getString("title");
        Boolean isShow = bundle.getBoolean("isShow");
        String url = bundle.getString("url");
        bundle.clear();
        if(isShow){
        	findViewById(R.id.tv_enroll).setVisibility(View.GONE);
        }
        
        TextView titleView = (TextView) findViewById(R.id.tv_mid_content);
        titleView.setText(title);
        TextView tvShare = (TextView) findViewById(R.id.iv_share);
        tvShare.setVisibility(View.GONE);
        
        webview = (WebView) findViewById(R.id.webView);
        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new HelloWebViewClient ());
        
        findViewById(R.id.iv_share).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showShare();
			}
		});
        
        findViewById(R.id.iv_turn_in).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AboutActivity.this.finish();
			}
		});
    }
    
    public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext()); // 保存context
    }
    
    /**
     * open system share dialog
     * @param context context
     * @param text text to share
     * @return true if success or false
     */
    public static void systemShare(Context context, String text) {

        Intent intent = new Intent(Intent.ACTION_SEND);
      //  intent.putExtra(Intent.EXTRA_SUBJECT, text);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "请选择应用"));
    }

    /**
     * open system share dialog
     * @param context context
     * @param text text to share
     * @param filePath file to share, usually is picture
     * @return true if success or false
     */
	public static void systemShare(Context context, String text,
			String filePath) {

		File f = new File(filePath);
		if (!f.exists()) {

			return ;
		}
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, text);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "请选择应用"));
	}
	
    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO 自动生成的方法存根
			super.onPageFinished(view, url);
		} 
     
    }
      
    private void showShare() {
    	 ShareSDK.initSDK(this);
    	 OnekeyShare oks = new OnekeyShare();
    	 //关闭sso授权
    	 oks.disableSSOWhenAuthorize(); 

    	// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
    	 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
    	 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
    	 oks.setTitle(getString(R.string.share));
    	 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
    	 oks.setTitleUrl("http://sharesdk.cn");
    	 // text是分享文本，所有平台都需要这个字段
    	 oks.setText("我是分享文本");
    	 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    	 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
    	 // url仅在微信（包括好友和朋友圈）中使用
    	 oks.setUrl("http://sharesdk.cn");
    	 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
    	 oks.setComment("我是测试评论文本");
    	 // site是分享此内容的网站名称，仅在QQ空间使用
    	 oks.setSite(getString(R.string.app_name));
    	 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
    	 oks.setSiteUrl("http://sharesdk.cn");

    	// 启动分享GUI
    	 oks.show(this);
    	 }
 
}
