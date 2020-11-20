package com.huift.hfq.base.utils;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util.onUploadFinish;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.api.API.SessStatus;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;

import android.app.Activity;
import android.util.Log;

/**
 * 上传方法类
 * @author whhft
 *
 */
public class UploadUtils {

	/**
	 * 上传图片
	 * @param activity
	 * @param imageUrl
	 * @param onFinish
	 */
	public static void uploadImage(final Activity activity,final String imageUrl,final String tokenCode,onUploadFinish onFinish){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					File imgFile = new File(imageUrl);
					
					URL serverURL = new URL(Const.ApiAddr.COMM);
					
					String method = "uploadImg";
					
					JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
					mySession.getOptions().setAllowedResponseContentTypes(new String[] {"text/json;charset=utf-8", "application/json"});
					
					JSONRPC2Response response = null;
					LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
					params.put("Img", imgFile);
					params.put("reqtime", System.currentTimeMillis() / 1000);
					switch (API.getTokenStatus()) {
					case SessStatus.NO_USER_INFO://从未登陆:
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// 提示需要登录
							}
						});
						break;
					case SessStatus.SESS_EXPIRED://session过期
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// 提示用户密码不匹配
								
							}
						});
						break;
					default://session有效
						break;
					}
					String mGenSign=tokenCode+API.genSign(method, API.getFirstParam(params));//调用签名
					params.put("vcode", mGenSign);
					
					int id = API.nextId();
					JSONRPC2Request request = new JSONRPC2Request(method, params, id);
					Log.d("UploadUtils", ">>>>>" + request.toString());
					response = mySession.send(request);
					Log.d("UploadUtils", "<<<<< " + response.toJSONString());
				
					if (response.indicatesSuccess()) {
						JSONObject result =  (JSONObject) response.getResult();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
