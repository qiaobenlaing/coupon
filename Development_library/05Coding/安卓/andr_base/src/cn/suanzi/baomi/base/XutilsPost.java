package cn.suanzi.baomi.base;


public class XutilsPost {//自动实现异步处理
	 /*public void doPost(String url, RequestParams params,final IOAuthCallBack iOAuthCallBack) {  
	  
	        HttpUtils http = new HttpUtils();  
	        http.configCurrentHttpCacheExpiry(1000 * 10);  
	        http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {  
	  
	            @Override  
	            public void onFailure(HttpException arg0, String arg1) {  
	           
	            }  
	  
	            @Override  
	            public void onSuccess(ResponseInfo<String> info) {  
	                iOAuthCallBack.getIOAuthCallBack(info.result);  
	            }  
	        });  
	    }  
	  
	    public void doPostLogin(int cityId, IOAuthCallBack iOAuthCallBack) {  
	        String url = "http://xxxxxxxxxxxx";  
	        RequestParams params = new RequestParams();  
	        params.addQueryStringParameter("currentCityId", cityId + "");  
	        params.addBodyParameter("path", "/apps/postCatch");  
	        doPost(url, params, iOAuthCallBack);  
	    }  */
}
