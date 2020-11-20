package com.huift.hfq.base.utils;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.util.Log;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util.onUploadFinish;

/**
 * 上传工具类
 * @author whhft
 *
 */
public class HttpUploadUtils {
	
	/**
	 * 上传图片
	 * @param activity
	 * @param imageUrl
	 * @param onFinish
	 */
	public static void getImageUpload(final Activity activity,
			final String imageUrl,final String tokenCode, final onUploadFinish onFinish){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String mBaseUrl = Const.ApiAddr.COMM;
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(mBaseUrl);
				
				try {
					HashMap<String, String> strMap = new HashMap<String, String>();
					strMap.put("tokenCode", tokenCode);
					JSONObject params = buildParam(new HashMap<String, Integer>(),new HashMap<String, String>(),"uploadImg");
					String entity = params.toString();
					
					HttpParams httpParams = httpClient.getParams();
					HttpConnectionParams.setConnectionTimeout(httpParams, 100000);
					HttpConnectionParams.setSoTimeout(httpParams, 100000);
					
					httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
			        httpPost.setEntity(new StringEntity(entity));
		        
		        	HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					if (httpEntity == null) throw new Exception("");
					InputStream stream = httpEntity.getContent();
					
					if (stream != null) {
						String strResponse = StringUtil.streamToString(stream);
						JSONObject jsonResponse = (JSONObject) new JSONTokener(strResponse).nextValue();
						String mResult = jsonResponse.getString("code").toString();
						if (onFinish != null) {
							onFinish.getImgUrl(mResult);
						} else {
							Log.d("UploadImageUtils",
									"我是为空了....................");
						}
						stream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private static JSONObject buildParam(HashMap<String, Integer> mIntParams,HashMap<String, String> mStrParams,String mMethod) {
		JSONObject object = new JSONObject();
		JSONObject params = new JSONObject();
		try {
			if (mIntParams.size() > 0) {
				for (String key : mIntParams.keySet()) {
					params.put(key, mIntParams.get(key));
				}
			}
			if (mStrParams.size() > 0) {
				for (String key : mStrParams.keySet()) {
					params.put(key, mStrParams.get(key));
				}
			}
			object.put("jsonrpc", 	"2.0");
			object.put("method", 	mMethod);
			object.put("params", 	params);
			object.put("id", 		1);
			
		} catch (Exception e) { }
		return object;
	}
}
