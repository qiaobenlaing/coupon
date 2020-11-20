package com.huift.hfq.base;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.huift.hfq.base.api.API;


public class HttpClict extends API{

	//httpClient封装类
		public HttpResponse gethttpClient(String url,List<NameValuePair> nameparis) throws ClientProtocolException, IOException{
			HttpClient client = new DefaultHttpClient();
			UrlEncodedFormEntity httpen = new UrlEncodedFormEntity(nameparis,"utf-8");
			HttpPost Post = new HttpPost(url);
			Post.setEntity(httpen);
			HttpResponse response = client.execute(Post);
			return response;
			
		}
		
		public String getSign (String method,String tokenCode,String firstParam) {
			String genSign="";//调用签名
			if(Util.isEmpty(tokenCode)){
				//调用API的时候不需要tokenCode
				genSign=genSign(method, firstParam);
			}else{
				//调用API的时候需要tokenCode
				genSign=tokenCode + genSign(method, firstParam);
			}
			return genSign;
		}
}
