package com.huift.hfq.base.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.R;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.Util.onUploadFinish;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

/**
 * 上传图片的接口
 * 
 * @author whhft
 * 
 */
public class UploadImageUtils {

	public final static String TAG = "HTTP";
	private final static int CONNECT_TIME = 10000;
	private final static int READ_TIME = 10000;

	/**
	 * 上传图片的方法
	 * 
	 * @param activity
	 * @param imageUrl
	 * @param onFinish
	 */
	public static void getImageUpload(final Activity activity,
			final String imageUrl, final onUploadFinish onFinish) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Log.i("******", "activityImage******4444444444:" + imageUrl);
				HttpURLConnection conn = null;
				
				// / boundary就是request头和上传文件内容的分隔符(可自定义任意一组字符串)
				String BOUNDARY = "******";
				// 用来标识payLoad+文件流的起始位置和终止位置(相当于一个协议,告诉你从哪开始,从哪结束)
				String preFix = ("\r\n--" + BOUNDARY + "--\r\n");
				try {
					URL url = new URL(Const.ApiAddr.COMM + "/uploadImg");
					conn = getHttpURLConnection(Const.ApiAddr.COMM + "/uploadImg", "POST"); 
					
					conn.setRequestProperty("Content-Type",
							"multipart/form-data; boundary=" + BOUNDARY);
					// 获取写输入流
					OutputStream out = new DataOutputStream(conn
							.getOutputStream());
					// 获取上传文件
					boolean sdCardExist = Environment.getExternalStorageState()
							.equals(android.os.Environment.MEDIA_MOUNTED);
					File sdDir = null;
					if (sdCardExist) {
						sdDir = Environment.getExternalStorageDirectory()
								.getAbsoluteFile();
						Log.i("aa", "sdDir" + sdDir.toString());
					}
					File file = new File(imageUrl);
					// 要上传的数据
					StringBuffer strBuf = new StringBuffer();

					// 标识payLoad + 文件流的起始位置
					strBuf.append(preFix);

					// 下面这三行代码,用来标识服务器表单接收文件的name和filename的格式
					// 在这里,我们是file和filename.后缀[后缀是必须的]。
					// 这里的fileName必须加个.jpg,因为后台会判断这个东西。
					// 这里的Content-Type的类型,必须与fileName的后缀一致。
					// 如果不太明白,可以问一下后台同事,反正这里的name和fileName得与后台协定！
					// 这里只要把.jpg改成.txt，把Content-Type改成上传文本的类型，就能上传txt文件了。
					String fileName = imageUrl.substring(
							imageUrl.lastIndexOf("/"),
							imageUrl.lastIndexOf(".") - 1);
					strBuf.append("Content-Disposition: form-data; name=\"file\"; filename=\""
							+ fileName + ".jpg" + "\"\r\n");
					strBuf.append("Content-Type: image/jpeg" + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());

					// 获取文件流
					FileInputStream fileInputStream = new FileInputStream(file);
					DataInputStream inputStream = new DataInputStream(
							fileInputStream);

					// 每次上传文件的大小(文件会被拆成几份上传)
					int bytes = 0;
					// 每次上传的大小
					byte[] bufferOut = new byte[1024];
					// 上传文件
					while ((bytes = inputStream.read(bufferOut)) != -1) {
						// 上传文件(一份)
						out.write(bufferOut, 0, bytes);
					}
					// 关闭文件流
					inputStream.close();

					// 标识payLoad + 文件流的结尾位置
					out.write(preFix.getBytes());

					// 输出所有数据到服务器
					out.flush();
					// 关闭网络输出流
					out.close();

					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						InputStream inStream = conn.getInputStream();
						byte[] number = read(inStream);
						String json = new String(number);

						Log.i("UploadImageUtils", "mResult******3"+ json);
						org.json.JSONObject iamgeObj = new org.json.JSONObject(json);
						String mResult = iamgeObj.getString("code").toString();
						if (onFinish != null) {
							onFinish.getImgUrl(mResult);
						} else {
							Log.d("UploadImageUtils",
									"我是为空了....................");
						}
					}
				} catch (Exception e) {
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Util.addToast(R.string.toast_image_error);
						}
					});
				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}).start();
	}

	public static HttpURLConnection getHttpURLConnection(String urlstr, String method)
			throws IOException {
		URL url = new URL(urlstr);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(CONNECT_TIME);
		connection.setReadTimeout(READ_TIME);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod(method);

		// 头字段
		connection.setRequestProperty("Accept", "*/*");
		connection.setRequestProperty("Accept-Charset", "UTF-8,*;q=0.5");
		connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
		connection.setRequestProperty("Accept-Language", "zh-CN");
		connection.setRequestProperty("User-Agent", "Android WYJ");
		return connection;

	}

	/**
	 * 读取输入流数据 InputStream
	 * 
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] read(InputStream inStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
}
