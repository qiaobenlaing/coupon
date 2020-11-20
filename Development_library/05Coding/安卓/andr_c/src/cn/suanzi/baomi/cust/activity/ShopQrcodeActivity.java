package cn.suanzi.baomi.cust.activity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ImageDownloadCallback;
import cn.suanzi.baomi.base.utils.QrCodeUtils;
import cn.suanzi.baomi.base.utils.ThreeDES;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 店铺二维码
 * @author Zhonghui.Dong
 */
public class ShopQrcodeActivity extends Activity {
	public static final String SHOP_CODE = "shop_code";
	public static final String SHOP_LOGO = "mImageUrl";
	public static final String SHOP_OBJ = "shop";
	private ImageView qrCodeImg;
	private String qType = "qr002";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_qrcode);
		ViewUtils.inject(this);
		init();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	private void init() {
		qrCodeImg = (ImageView) findViewById(R.id.shop_qr_code);
		 Intent intent = getIntent();
		 Shop shop = (Shop) intent.getSerializableExtra(SHOP_OBJ);
		 if(shop != null){
			 if(!Util.isEmpty(shop.getShopCode())){
				 String shopCodelen = shop.getShopCode();//获取的商店编码
				 String shopcode = shopCodelen.substring(shopCodelen.length()-6,shopCodelen.length());//截取后的字符串
				 //加密SSRC
				 String shopCodeEnd = ThreeDES.encryptMode(Util.KeyBytes, shopcode.getBytes()); 
				 
				 final JSONObject arObj = new JSONObject();
				 arObj.put(CustConst.TwoCode.QTYPE, qType);
				 arObj.put(CustConst.TwoCode.SHOP_CODE, shopCodelen);
				 arObj.put(CustConst.TwoCode.SSRC, shopcode);
				 arObj.put(CustConst.TwoCode.SCODE, shopCodeEnd);
				
				 String url = Const.IMG_URL + shop.getLogoUrl();
				 if(!"".equals(shop.getLogoUrl())){
					 Log.d("tag", "有图片的路径：：：：" +url);
					 Util.getLocalOrNetBitmap(url, new ImageDownloadCallback(){
							@Override
							public void success(final Bitmap bitmap) {
								//生成二维码
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										qrCodeImg.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString(), bitmap));
									}
								});
							}
							@Override
							public void fail() {      
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Log.d("TAG", "出问题啦。。。。。。。。。。。");
										 qrCodeImg.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
									}
								});
							}
						 });
				 } else{
					 Log.d("tag", "没有图片的路径：：：：" +url);
					 qrCodeImg.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
				 }
			 }
			 ImageView ivShopLogo = (ImageView) findViewById(R.id.shop_logo);//商店头像
			 TextView tvShopName = (TextView) findViewById(R.id.iv_shoptetail_name);//商店名称
			 TextView tvShopAddress = (TextView) findViewById(R.id.tv_shop_address);//店铺地址    
			//赋值
			 tvShopName.setText(shop.getShopName());
			 tvShopAddress.setText(shop.getProvince() + shop.getCity() + shop.getStreet());
			 Util.showImage(ShopQrcodeActivity.this, shop.getLogoUrl(), ivShopLogo);
		 } 
	}

	@OnClick({ R.id.backup })
	private void click(View v) {
		switch (v.getId()) {
		case R.id.backup:
			finish();
			break;
		}
	}
	
	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	        AppUtils.setContext(getApplicationContext());
	    }
}
