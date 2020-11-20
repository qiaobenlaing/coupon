// ---------------------------------------------------------
// @author    Weiping Liu
// @version   1.0.0
// @copyright 版权所有 (c) 2014 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------

package com.huift.hfq.base;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.data.Storage;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ImageDownloadCallback;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用类和方法
 *
 * @author Weiping Liu
 * @version 1.0.0
 */
public class Util {
    /**
     * debug tag
     */
    public static final int NUM_ZERO = 0;
    public static final int NUM_ONE = 1;
    public static final int NUM_TWO = 2;
    public static final int NUM_THIRD = 3;
    public static final int NUM_FOUR = 4;
    public static final int NUM_FIVE = 5;
    public static final int NUM_SIX = 6;
    public static final int NUM_SEVEN = 7;
    public static final int NUM_EIGTH = 8;
    public static final int NUM_THIRTYTHREE = 33;
    public static final int RESULT_LOAD_IMAGE = 2;// 图库
    public static final int TAKE_PHOTO = 1;// 相机
    public static final String COUPON_IMAGE = "couponimage";
    private static String mResult;

    public static final byte[] KeyBytes = {0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38, 0x28, 0x25, 0x79,
            0x51, (byte) 0xCB, (byte) 0xDD, 0x55, 0x66, 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36, (byte) 0xE2}; // 24字节的密钥

    /**
     * 转化Unix Timestamp为时间"HH:mm"
     * <p>
     * 提示： 获取时间及本地化显示的例子： TimeZone tz = TimeZone.getTimeZone("GMT+08"); Calendar
     * cal = Calendar.getInstance(tz); SimpleDateFormat sdf = new
     * SimpleDateFormat(); sdf.setTimeZone(cal.getTimeZone());
     * System.out.println(sdf.format(cal.getTime())); 输出： 10-3-22 下午7:58
     * <p>
     * SimpleDateFormat类可以以字符串参数形式设置输出时间的格式，例如：
     * sdf.applyPattern("yyyy年MM月dd日HH时mm分ss秒"); 输出： 2010年03月22日20时02分35秒
     *
     * @param unixSeconds Unix Timestamp(秒)
     * @return 时间"HH:mm"
     */
    public static String unixTS2Time(long unixSeconds, Locale locale) {
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert
        // seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", locale);
        // sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    /**
     * 把DT19时间转化成Unix时间戳
     *
     * @param dt19   yyyy-MM-dd HH:mm:ss形式的时间
     * @param locale 客户端时区，如Locale.CHINA。
     * @return Unix时间戳（秒）
     */
    public static long dtToUnixTS(String dt19, Locale locale) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
        Date fuwuDate = sdf.parse(dt19);
        return fuwuDate.getTime() / 1000;
    }

    /**
     * 判断一个字符串是否为空
     *
     * @param s 待判断的字符串
     * @return 字符串为null或“”
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    /**
     * 判断多个输入内容不为空
     *
     * @param s 输入的多个数组
     * @return 一个boolean类型的数组
     */
    public static boolean[] isEmptys(String[] s) {
        boolean[] flag = new boolean[s.length];
        for (int i = 0; i < s.length; i++) {
            if (isEmpty(s[i])) {
                flag[i] = true;
            } else {
                flag[i] = false;
            }
        }
        return flag;
    }

    /**
     * 判断输入的内容不能太长
     *
     * @param s
     * @return
     */
    public static boolean isInputLength(String s) {
        boolean flag = false;
        if (s.length() > 6) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查一个primitive元素是否在数组中。简单来说，就是使用==来比较是否相等。
     *
     * @param <T> 数组和元素的类型
     * @param arr 数组
     * @param ele 待检查的元素
     * @return
     */
    public static <T> boolean inArray(T ele, T[] arr) {
        for (T e : arr) {
            if (e == ele) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查一个对象元素(除primitive)是否在数组中。简单来说，就是使用equals来比较是否相等。注意：null和null不相等。
     *
     * @param <T> 数组和元素的类型
     * @param arr 数组
     * @param ele 待检查的元素
     * @return
     */
    public static <T> boolean inObjArray(T ele, T[] arr) {
        for (T e : arr) {
            if (e != null && ele != null && e.equals(ele)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 切换系统的语言地区. 注意：时间需要另外处理。
     *
     * @param locale 地区。Locale.XXX，例 Local.China, Local.US。
     */
    public static void changeLocale(Locale locale) {
        Locale.setDefault(locale);
        ;
    }

    /**
     * MD5
     *
     * @param input 源数据
     * @return md5签名。签名失败时，返回null。
     */
    public static String md5(String input) {
        try {
            // 拿到一个MD5转换器
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 输入的字符串转换成字节数组
            byte[] inputByteArray = input.getBytes();
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 将字节数组转化成16进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteArrayToHex(byte[] byteArray) {
        // 初始化字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // new一个字符数组，用来组成结果字符串
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组
        int index = 0;
        for (byte b : byteArray) {
            // 把一个byte切割成2位十六进制字符
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    /**
     * 判断一个对象是否为数组（任意数组）
     *
     * @param o 待检测的对象
     * @return ture: 如果给定对象为一个数组，false：反之。
     */
    public static boolean isArray(Object o) {
        Class<?> c = o.getClass();
        return c.isArray();
    }

    /**
     * 将一个json字符串转换为一个对象。<br/>
     * <strong>注意：</strong>json中所有字段都必须在clz中存在。clz中的字段不一定必须在json中。
     *
     * @param json json字符串
     * @param clz  目标class
     * @return 转换成功的对象
     */
    public static <T> T json2Obj(String json, Class<T> clz) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(json, clz);
    }

    /**
     * 检测本地网络是否打开
     *
     * @return true：本地网络开启；false：本地网络关闭。
     */
    public static boolean isNetworkOpen(Context ctx) {
        ConnectivityManager connManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo().isAvailable();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断WiFi是否打开
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断WiFi还是3g网络
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断GPS是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    /**
     * 判断网络是否有效
     *
     * @param url
     * @return
     */
    public static boolean checkURL(String url) {
        boolean value = false;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            int code = conn.getResponseCode();
            if (code != 200) {
                value = false;
            } else {
                value = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 保留两位小数
     *
     * @param num double类型的小数
     * @return 放回两位类型的小数
     */
    public static BigDecimal saveTwoDecima(Double num) {
        BigDecimal bdNum = new BigDecimal(Double.valueOf(num));
        bdNum = bdNum.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bdNum;
    }

    /**
     * Toast的封住类
     *
     * @param activity 调用者的上下文
     * @param content  提示内容
     * @param showtime 显示时间
     * @param gravity  提示框放置的位置
     * @param x        显示为X轴
     * @param y        显示为Y轴
     */
    public static void addToast(Activity activity, String content, int showtime, int gravity, int x, int y) {
        LinearLayout toastLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.toast_layout, null);
        TextView toastMsg = (TextView) toastLayout.findViewById(R.id.tv_toastmsg);
        toastMsg.setText(content);
        // Toast toast = Toast.makeText(activity, content, showtime);
        Toast toast = new Toast(activity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toast.setGravity(gravity, x, y);
        toast.show();
    }

    /**
     * Toast的封住类
     */
    public static void addToast(int id) {
        Toast.makeText(AppUtils.getContext(), getString(id), Toast.LENGTH_SHORT).show();
    }

    /**
     * Toast的封住类
     *
     * @param activity 调用者的上下文
     * @param content  提示内容
     */
    public static void addToast(Activity activity, String content, int showTime) {
        Toast.makeText(activity, content, showTime).show();
    }

    /**
     * 下载图片显示
     *
     * @param imageUrl
     *            url
     * @param imageView
     *            显示的控件
     */
    /*
     * public static void showImage(String imageUrl, ImageView imageView) { //
     * 加载图片 DisplayImageOptions options = new
     * DisplayImageOptions.Builder().showImageOnLoading(R.drawable.image_fail)
     * // 加载过程中显示的图片 .showImageForEmptyUri(R.drawable.image_fail) // 加载内容为空显示的图片
     * .showImageOnFail(R.drawable.image_fail).cacheInMemory(true) // 加载失败显示的图片
     * .cacheOnDisk(true).considerExifParams(true).displayer(new
     * RoundedBitmapDisplayer(10)).build(); // 设置图片的缓存 // 根据对象属性改变界面
     * ImageLoader.getInstance().displayImage(imageUrl, imageView, options); //
     * 加载图片
     *
     * }
     */

    /**
     * 加载网络图片 店铺logo
     */
    public static void showImage(Activity act, String imageUrl, ImageView imageView) {
        Glide.with(act).load(Const.IMG_URL + imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_shoplogo).crossFade().error(R.drawable.no_shoplogo).into(imageView);
    }

    /**
     * 加载网络图片(活动展示)
     */
    public static void showImagePhoto(Activity act, String imageUrl, ImageView imageView) {
        try {
            BitmapUtils bitmapUtils = new BitmapUtils(act);
            bitmapUtils.configDefaultLoadingImage(R.drawable.no_shoplogo);//默认背景图片
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.no_shoplogo);//加载失败图片
            bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型
            // 加载网络图片
            bitmapUtils.display(imageView, imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载网络图片 店铺详情
     */
    public static void showShopDetailImage(Activity act, String imageUrl, ImageView imageView) {
        // try {
        // BitmapUtils bitmapUtils = new BitmapUtils(act);
        // bitmapUtils.configDefaultLoadingImage(R.drawable.no_shopdetail_url);//
        // 默认背景图片
        // bitmapUtils.configDefaultLoadFailedImage(R.drawable.no_shopdetail_url);//
        // 加载失败图片
        // bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
        // 设置图片压缩类型
        // // 加载网络图片
        // bitmapUtils.display(imageView, Const.IMG_URL + imageUrl);
        // } catch (Exception e) {
        // Log.e(TAG, "显示图片2=" + e.getMessage());
        // }

        Glide.with(act).load(Const.IMG_URL + imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_shopdetail_url).crossFade().error(R.drawable.no_shopdetail_url)
                .into(imageView);
    }

    /**
     * 加载本地图片
     */
    public static void showShopDetailImages(Activity act, String imageUrl, ImageView imageView) {
        try {
            BitmapUtils bitmapUtils = new BitmapUtils(act);
            bitmapUtils.configDefaultLoadingImage(R.drawable.no_shopdetail_url);//默认背景图片
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.no_shopdetail_url);//加载失败图片
            bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型
            // 加载网络图片
            bitmapUtils.display(imageView, imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 食全食美
     */
    public static void showImages(Activity act, String imageUrl, ImageView imageView) {
        try {
            Glide.with(act).load(Const.IMG_URL + imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.no_portrait).crossFade().error(R.drawable.no_portrait).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 首页分类图片显示
     */
    public static void showFirstImages(Activity act, String imageUrl, ImageView imageView) {
        try {
            Glide.with(act).load(Const.IMG_URL + imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().error(R.drawable.no_portrait).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 加载网络图片 首页展示的图片
     */
    public static void showBannnerImage(Activity act, String imageUrl, ImageView imageView) {
        try {
            Glide.with(act).load(Const.IMG_URL + imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.no_shopdetail_url).crossFade().error(R.drawable.no_shopdetail_url).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用showHead时，需要重写
     *
     * @author ad
     */
    public interface onImageLoad {
        public void onImageLoadSuccess();
    }

    /**
     * 二维码中间的图片
     */
    public static void getLocalOrNetBitmap(final String url, final ImageDownloadCallback callback) {
        new Thread() {
            public void run() {
                Bitmap bitmap = null;
                InputStream in = null;
                BufferedOutputStream out = null;
                try {
                    in = new BufferedInputStream(new URL(url).openStream(), 1024);
                    final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                    out = new BufferedOutputStream(dataStream, 1024);
                    copy(in, out);
                    out.flush();
                    byte[] data = dataStream.toByteArray();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    data = null;
                    if (callback != null) {
                        callback.success(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.fail();
                    }
                } catch (Error e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.fail();
                    }
                } finally {/*
                 * if (out != null) { try { out.close(); if (in !=
                 * null) { in.close(); } } catch (IOException e) {
                 * // TODO Auto-generated catch block Log.e(TAG,
                 * "e=" + e.getMessage()); } }
                 */
                }

            }
        }.start();

    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    /**
     * 拍照图片路径
     */
    public static String takePhoto(Context context, int resultCode, Intent data, ImageView imageView) {
        String fileName = "";
        if (resultCode == Activity.RESULT_OK) {
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            // 文件输出流
            FileOutputStream fileout = null;
            // 判断文件是否为空或者名称重复()
            String path = Storage.getExtDir(context) + "/image";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fileName = path + "/" + name;
            try {
                fileout = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileout);// 把数据写入文件
                // \
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileout.flush();
                    fileout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (imageView == null) {
                // imageView.setBackgroundResource(R.drawable.iamge_fail);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
        return fileName;
    }


    /**
     * 手机图库路径
     */
    public static String mapdepot(Activity activity, int resultCode, Intent data, ImageView imageView) {
        String picturePath = "";
        if (resultCode == activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 10;
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
            cursor.close();
            if (imageView == null) {
                // imageView.setBackgroundResource(R.drawable.iamge_fail);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
        return picturePath;
    }

    /**
     * 手机图库路径
     */
    public static String mapdepot1(Activity activity, int resultCode, Intent data) {
        String picturePath = "";
        if (resultCode == activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 10;
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
            cursor.close();
        }
        return picturePath;
    }

    public interface onUploadFinish {

        public void getImgUrl(String img);
    }

    /**
     * 图片上传
     */
    public static void getImageUpload(final Activity activity, final String iamgeUrl) {
        getImageUpload(activity, iamgeUrl, null);
    }

    /**
     * 图片上传
     */
    public static void getImageUpload(final Activity activity, final String iamgeUrl, final onUploadFinish onFinish) {
        // 图片上传
        new Thread(new Runnable() {
            public void run() {
                Bitmap bitmap = null;
                // 创建HttpClient
                BasicHttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
                HttpConnectionParams.setSoTimeout(httpParams, 5000);
                HttpClient client = new DefaultHttpClient(httpParams);

                File imgFile = new File(iamgeUrl);
                FileOutputStream fileout = null;
                try {
                    bitmap = Bimp.revitionImageSize(iamgeUrl);
                    fileout = new FileOutputStream(imgFile);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

				/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		        int options = 100;
		        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
		            baos.reset();//重置baos即清空baos
		            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
		            options -= 10;//每次都减少10
		        }
		        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		        Bitmap bitmaps = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片*/
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileout);// 把数据写入文件(压缩)

                // 创建http对象 post
                HttpPost post = new HttpPost(Const.ApiAddr.GZBANK + "/uploadImg");
                // 定义 MultipartEntity 保存数据，用于上传
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                // http 协议上传数据
                HttpResponse response = null;
                SharedPreferences sharedPreferences = activity.getSharedPreferences("image", Context.MODE_PRIVATE);
                Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                try {
                    // 照片的图片文件
                    FileBody fileBody = new FileBody(imgFile, "image/jpg");
                    // imagefile 是服务端读取文件的 key
                    entity.addPart("imagefile", fileBody);
                    UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
                    ContentBody contentBody = new StringBody(userToken.getShopCode());
                    entity.addPart("shopCode", contentBody);
                    // 权限及token信息
                    post.setEntity(entity);
                    // 执行post并获取结果
                    response = client.execute(post);
                    String result = EntityUtils.toString(response.getEntity(), "utf-8");
                    try {
                        org.json.JSONObject iamgeObj = new org.json.JSONObject(result);
                        mResult = iamgeObj.getString("code");
                        if (onFinish != null) {
                            onFinish.getImgUrl(mResult);
                        }
                        editor.putString(COUPON_IMAGE, mResult);
                        editor.commit();

                    } catch (JSONException e) {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Util.addToast(R.string.toast_image_error);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 上传多张图片
     * imgPathList  多张图片的集合
     */
    public static void getMultiImageUpLoad(final Activity activity, final List<String> imgPathList, final onUploadFinish onFinish) {
        final ProgressDialog progressBar = new ProgressDialog(activity);
        progressBar.setCancelable(false);
        progressBar.setMessage("正在上传图片~~~");
        progressBar.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null == imgPathList || imgPathList.size() == 0) {
                    return;
                }

                boolean sdCardExist = Environment.getExternalStorageState()
                        .equals(android.os.Environment.MEDIA_MOUNTED);

                if (!sdCardExist) {
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                BasicHttpParams httpParams = null;
                HttpClient client = null;

                // 创建HttpClient
                httpParams = new BasicHttpParams();

                client = new DefaultHttpClient(httpParams);
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 10000);
                // 创建http对象 post
                HttpPost post = new HttpPost(Const.ApiAddr.COMM + "/uploadImg");
                // 定义 MultipartEntity 保存数据，用于上传
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                // http 协议上传数据
                HttpResponse response = null;

                for (String imgPath : imgPathList) {

                    File file = new File(imgPath);

                    Bitmap decodeFile = BitmapFactory.decodeFile(imgPath);

                    compressBmpToFile(decodeFile, file);

                    // 照片的图片文件
                    FileBody fileBody = new FileBody(file, "image/jpg");

                    // imagefile 是服务端读取文件的 key
                    entity.addPart("imagefile", fileBody);
                    // 权限及token信息
                    post.setEntity(entity);

                    // 执行post并获取结果
                    try {
                        response = client.execute(post);
                        String result = EntityUtils.toString(response.getEntity(), "utf-8");
                        if (null != result && result.length() > 0) { //请求成功
                            org.json.JSONObject iamgeObj = new org.json.JSONObject(result);
                            mResult = iamgeObj.getString("code").toString();
                            buffer.append(mResult + "|");
                        } else {  //请求失败
                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    progressBar.dismiss();
                                    getContentValidate(R.string.toast_upload_fail);
                                }
                            });
                        }
                    } catch (Exception e) {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progressBar.dismiss();
                                getContentValidate(R.string.toast_upload_fail);
                            }
                        });
                        e.printStackTrace();
                        return;
                    }
                }
                if (onFinish != null && buffer.toString().length() > 0) {
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.dismiss();
                        }
                    });
                    onFinish.getImgUrl(buffer.toString());
                }
            }
        }).start();
    }

    /**
     * 压缩图片
     *
     * @param bmp
     * @return
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            if (options <= 0) {
                options = 10;
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
                break;
            }
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);

        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 活动介绍（压缩）
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getImage(String srcPath, Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        float width = wm.getDefaultDisplay().getWidth();
        float height = wm.getDefaultDisplay().getHeight();

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = width;// 这里设置高度为800f
        float ww = 600f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 崩溃日志上传
     */
    public static void getCrashLogUpload(final Activity activity, final String crashFilePath,
                                         final onUploadFinish onFinish) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BasicHttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
                HttpConnectionParams.setSoTimeout(httpParams, 5000);
                HttpClient httpclient = new DefaultHttpClient(httpParams);

                HttpPost post = new HttpPost(Const.ApiAddr.COMM + "/uploadCrashLog");

                FileBody fileBody = new FileBody(new File(crashFilePath));

                MultipartEntity entity = new MultipartEntity();
                entity.addPart("log", fileBody);

                post.setEntity(entity);
                HttpResponse response = null;
                try {
                    response = httpclient.execute(post);
                    String result = EntityUtils.toString(response.getEntity(), "utf-8");
                    org.json.JSONObject iamgeObj = new org.json.JSONObject(result);
                    mResult = iamgeObj.getString("code").toString();
                    if (onFinish != null) {
                        onFinish.getImgUrl(mResult);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取手机唯一标识
     */
    public static String getUqId(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }

    /**
     * 判断是否为手机号
     *
     * @param inputText
     * @return
     */
    public static boolean isPhone(Activity activity, String inputText) {
        boolean flag = false;
        Pattern p = Pattern.compile("^((13[0-9])|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);

        switch (1) {
            case 1:
                if (!m.matches()) {
                    // 请输入正确的手机格式
                    getContentValidate(R.string.hint_right_phonenum);
                    flag = true;
                    break;
                }

                break;

            default:
                break;
        }
        return flag;
    }

    /***
     * 登录
     */
    public static boolean getLoginValidate(Activity activity, String mobile) {
        boolean flag = false;
        switch (1) {
            case 1:
                if (isEmpty(mobile)) {
                    // 请输入账号
                    getContentValidate(R.string.hint_right_userCode);
                    flag = true;
                    break;
                }

                if (mobile.length() != 11) {
                    getContentValidate(R.string.error_telnum);
                    flag = true;
                    break;
                }

                break;
            default:
                break;
        }
        return flag;
    }

    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        if (listView == null) return;

        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        // listAdapter.getCount()返回数据项的数目
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            // 一是Adapter中getView方法返回的View的必须由LinearLayout组成，
            // 因为只有LinearLayout才有measure()方法，
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 过滤登录 edittext输入空格的代码
     *
     * @param edit
     */
    public static void inputFilterSpace(final EditText edit) {
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11), new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                if (src.length() < 1) {
                    return null;
                } else {
                    char temp[] = (src.toString()).toCharArray();
                    char result[] = new char[temp.length];
                    for (int i = 0, j = 0; i < temp.length; i++) {
                        if (temp[i] == ' ') {
                            continue;
                        } else {
                            result[j++] = temp[i];
                        }
                    }
                    return String.valueOf(result).trim();
                }
            }
        }});

    }

    /**
     * 此方法只是关闭软键盘
     *
     * @param activity
     */
    public static void hintKbTwo(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 判断两个时间差
     *
     * @param activity
     * @param endTime
     * @param startTime
     * @return
     */
    public static long timeSize(Activity activity, String endTime, String startTime) {
        long time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null; // 截止使用日期
        Date endDate = null; // 截止使用日期
        switch (1) {
            case 1:
                try {
                    if (isEmpty(endTime) || isEmpty(startTime)) {
                        // 输入内容不能为空
                        Util.addToast(R.string.toast_time_null);
                        break;
                    } else {
                        startDate = sdf.parse(startTime);
                        endDate = sdf.parse(endTime);
                    }

                } catch (ParseException e) {
                    // 转换出错
                    Util.addToast(R.string.toast_time_fail);
                    break;
                }
                time = (endDate.getTime() - startDate.getTime());

        }
        return time;
    }

    /**
     * 判断两个时间差
     *
     * @param activity
     * @param endTime
     * @param startTime
     * @return
     */
    public static long timeSizeData(Activity activity, String endTime, String startTime) {
        long time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = null; // 截止使用日期
        Date endDate = null; // 截止使用日期
        switch (1) {
            case 1:
                try {
                    if (isEmpty(endTime) || isEmpty(startTime)) {
                        // 输入内容不能为空
                        Util.addToast(R.string.toast_time_null);
                        break;
                    } else {
                        startDate = sdf.parse(startTime);
                        endDate = sdf.parse(endTime);
                    }

                } catch (ParseException e) {
                    // 转换出错
                    Util.addToast(R.string.toast_time_fail);
                    break;
                }
                time = (endDate.getTime() - startDate.getTime());

        }
        return time;
    }

    /**
     * 判断两个时间差
     *
     * @param activity
     * @param endTime
     * @param startTime
     * @return
     */
    public static long timeSizeDay(Activity activity, String endTime, String startTime) {
        long time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date startDate = null; // 截止使用日期
        Date endDate = null; // 截止使用日期
        switch (1) {
            case 1:
                try {
                    if (isEmpty(endTime) || isEmpty(startTime)) {
                        // 输入内容不能为空
                        Util.addToast(R.string.toast_time_null);
                        break;
                    } else {
                        startDate = sdf.parse(startTime);
                        endDate = sdf.parse(endTime);
                    }

                } catch (ParseException e) {
                    // 转换出错
                    Util.addToast(R.string.toast_time_fail);
                    break;
                }
                time = (endDate.getTime() - startDate.getTime());

        }
        return time;
    }

    /**
     * 判断两个时间差
     *
     * @param activity
     * @param endTime
     * @param startTime
     * @return
     */
    public static long timeSizes(Activity activity, String endTime, String startTime) {
        long time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null; // 截止使用日期
        Date endDate = null; // 截止使用日期
        switch (1) {
            case 1:
                try {
                    if (isEmpty(endTime) || isEmpty(startTime) || endTime.equals(NUM_ZERO + "")
                            || startTime.equals(NUM_ZERO + "")) {

                        break;
                    } else {
                        startDate = sdf.parse(startTime);
                        endDate = sdf.parse(endTime);
                    }

                } catch (ParseException e) {
                    // 转换出错
                    Util.addToast(R.string.toast_time_fail);
                    break;
                }
                time = (endDate.getTime() - startDate.getTime());

        }
        return time;
    }

    private static long lastClick = System.currentTimeMillis();
    private static Toast sToast = null;


    /**
     * 显示toast
     *
     * @param id
     */
    public static void getContentValidate(int id) {
        // 大于一秒方可通过
        if (System.currentTimeMillis() - lastClick <= 2500) {
            showToast(id);
            lastClick = System.currentTimeMillis();
            return;
        } else {
            lastClick = System.currentTimeMillis();
            int date = (int) (System.currentTimeMillis() - lastClick);
            showToast(id);
        }
    }

    /**
     * 输入中文的toast
     *
     * @param content
     */
    public static void showToastZH(String content) {
        // 大于一秒方可通过
        if (System.currentTimeMillis() - lastClick <= 2500) {
            showToast(content);
            lastClick = System.currentTimeMillis();
            return;
        } else {
            lastClick = System.currentTimeMillis();
            int date = (int) (System.currentTimeMillis() - lastClick);
            showToast(content);
        }
    }

    /**
     * 显示toast
     *
     * @param activity
     * @param content
     */
    public static void getToastBottom(Activity activity, String content) {
        // 大于一秒方可通过
        if (System.currentTimeMillis() - lastClick <= 2500) {
            showToastBottom(activity, content);
            lastClick = System.currentTimeMillis();
        } else {
            lastClick = System.currentTimeMillis();
            int date = (int) (System.currentTimeMillis() - lastClick);
            showToastBottom(activity, content);
        }
    }

    /**
     * 显示toast
     */
    private static void showToast(int id) {
        if (sToast == null) {
            sToast = Toast.makeText(AppUtils.getContext(), getString(id), Toast.LENGTH_SHORT);
        } else {
            sToast.setText(getString(id));
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();
    }

    /**
     * 直接输入中文
     *
     * @param content
     */
    private static void showToast(String content) {
        if (sToast == null) {
            sToast = Toast.makeText(AppUtils.getContext(), content, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(content);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();
    }

    /**
     * 显示toast
     *
     * @param activity
     * @param content
     */
    private static void showToastBottom(Activity activity, String content) {
        if (sToast == null) {
            sToast = Toast.makeText(activity, content, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(content);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

    /**
     * 日期对话框
     *
     * @param activity
     * @param editText 输入参数
     * @return
     */
    public static DatePickerDialog getDateDialog(final Activity activity, final EditText editText) {
        DatePickerDialog pickerDialogEndUse = null;
        // 获取一个日历
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        // 创建一个时间对话框
        pickerDialogEndUse = new DatePickerDialog(activity, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + ((monthOfYear + 1) > 9 ? (monthOfYear + 1) : "0" + (monthOfYear + 1))
                        + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth));
            }
        }, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
        pickerDialogEndUse.dismiss();

        return pickerDialogEndUse;
    }

    /**
     * 时间对话框
     *
     * @param activity
     * @param editText
     * @return
     */
    public static TimePickerDialog getTimeDialog(final Activity activity, final EditText editText) {
        TimePickerDialog pickerStartTime = null;
        // 获取一个日历
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        pickerStartTime = new TimePickerDialog(activity, new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                editText.setText((hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":"
                        + (minute > 9 ? minute : "0" + minute));

            }
        }, c.get(c.HOUR_OF_DAY), c.get(c.MINUTE), true);
        return pickerStartTime;
    }

    /**
     * 添加
     */
    public abstract class OnResListener {
        public void onOk() {
        }

        ;
    }

    private static int mTime = 0;

    /**
     * 时间对话框
     *
     * @param activity
     * @param editText
     * @return
     */
    public static TimePickerDialog getTimeDialog(final Activity activity, final EditText editText, final OnResListener listener) {
        TimePickerDialog pickerStartTime = null;
        // 获取一个日历
        Calendar c = Calendar.getInstance();
        int hour = 8;
        int minute = 0;
        c.setTime(new Date());
        pickerStartTime = new TimePickerDialog(activity, new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                editText.setText((hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":"
                        + (minute > 9 ? minute : "0" + minute));
                mTime++;
                if (mTime % 2 == 0) {
                    listener.onOk();
                }
            }
        }, hour, minute, true);
        return pickerStartTime;
    }


    /**
     * 时间对话框
     *
     * @param activity
     * @param editText
     * @return
     */
    public static TimePickerDialog getTimeDialog(final Activity activity, final TextView editText, final OnResListener listener) {
        TimePickerDialog pickerStartTime = null;
        // 获取一个日历
        Calendar c = Calendar.getInstance();
        int hour = 8;
        int minute = 0;
        c.setTime(new Date());
        pickerStartTime = new TimePickerDialog(activity, new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                editText.setText((hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":"
                        + (minute > 9 ? minute : "0" + minute));
                mTime++;
                if (mTime % 2 == 0) {
                    listener.onOk();
                }
            }
        }, hour, minute, true);
        return pickerStartTime;
    }

    /**
     * 日期对话框
     *
     * @param activity
     * @param editText 输入参数
     * @return
     */
    public static DatePickerDialog getDateDialog(final Activity activity, final EditText editText, final OnResListener listener) {
        DatePickerDialog pickerDialogEndUse = null;
        // 获取一个日历
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        // 创建一个时间对话框
        pickerDialogEndUse = new DatePickerDialog(activity, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + ((monthOfYear + 1) > 9 ? (monthOfYear + 1) : "0" + (monthOfYear + 1))
                        + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth));
                mTime++;
                if (mTime % 2 == 0) {
                    listener.onOk();
                }
            }
        }, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
        pickerDialogEndUse.dismiss();

        return pickerDialogEndUse;
    }

    /**
     * 获取当前的版本号
     *
     * @param activity
     * @return 返回版本号
     */
    public static String getAppVersionCode(Activity activity) {
        String version = null;
        try {
            PackageManager pm = activity.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(activity.getPackageName(), 0);
            version = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 定义一个activity的list集合
     */
    public static List<Activity> activityList = new ArrayList<Activity>();
    /**
     * homeFragment要退出整个应用程序
     */
    public static List<Activity> homeActivityList = new ArrayList<Activity>();
    /**
     * 定义一个activity的list集合（多方登陆）
     */
    public static List<Activity> activityLoginList = new ArrayList<Activity>();
    /**
     * 推荐码的页面销毁
     */
    public static List<Activity> activityRecommonedList = new ArrayList<Activity>();
    /**
     * 顾客端活动页面销毁
     */
    public static List<Activity> actList = new ArrayList<Activity>();

    /**
     * 向链表中添加数据
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 向链表中添加数据
     *
     * @param activity
     */
    public static void addHomeActivity(Activity activity) {
        homeActivityList.add(activity);
    }

    /**
     * 向链表中添加数据（多方登陆）
     *
     * @param activity
     */
    public static void addLoginActivity(Activity activity) {
        activityLoginList.add(activity);
    }

    /**
     * 向链表中添加数据（推荐码）
     *
     * @param activity
     */
    public static void addRecommonedActivity(Activity activity) {
        activityRecommonedList.add(activity);
    }

    /**
     * 向链表中添加数据（活动）
     *
     * @param activity
     */
    public static void addActActivity(Activity activity) {
        actList.add(activity);
    }

    /**
     * 结束当前应用程序
     */
    public static void exit() {
        // 遍历链表
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }


    /**
     * 结束掉homeActivity应用程序
     */
    public static void exitHome() {
        // 遍历链表
        for (Activity activity : homeActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        // 杀掉这个用程序释放内存
        /*
         * int id = android.os.Process.myPid(); if (id != 0) {
         * android.os.Process.killProcess(id); }
         */
    }

    /**
     * 结束掉应用程序（多方登陆）
     */
    /*
     * public static boolean exitLogin() { boolean flag = false; for (int i = 0;
     * i < activityLoginList.size(); i++) { Activity activity =
     * activityLoginList.get(i); if (!activity.isFinishing()) {
     * activity.finish(); } if (i == (activityLoginList.size()-1)) { flag =
     * true; } }
     *
     * return flag; }
     */
    public static void exitLogin() {
        // 遍历链表
        for (Activity activity : activityLoginList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束推荐相关的页面
     */
    public static void exitRecommoned() {
        // 遍历链表
        for (Activity activity : activityRecommonedList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束推荐相关的页面
     */
    public static void exitAct() {
        // 遍历链表
        for (Activity activity : actList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    /**
     * 從URL中提取參數鍵值對。例如: http://suanzi.cn/home/page1?id=xxx&name=yyy&other。
     * 則提取的結果為： {id: "xxx", name: "yyy", other: ""}
     *
     * @param url 待分析的URL.
     * @return 形如{id: "xxx", name: "yyy", other: ""}的map。
     * @throws SzException 当分析URL异常时抛出
     */
    public static Map<String, String> getUrlParams(String url) throws SzException {
        try {
            URI uri = URI.create(url);
            String query = uri.getQuery();
            String[] qryParts = query.split("&");
            Map<String, String> params = new HashMap<String, String>();
            for (String qryPart : qryParts) {
                String[] keyValPair = qryPart.split("=");
                params.put(keyValPair[0], keyValPair.length > 1 ? keyValPair[1] : "");
            }
            return params;
        } catch (Exception e) {
            throw new SzException("分析URL异常。");
        }
    }

    /**
     * 保存文件
     *
     * @param savaPath  保存到的路径
     * @param orginFile 源文件
     */
    public static void saveFile(final String savaPath, final File orginFile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Util.isEmpty(savaPath) || orginFile == null) {
                    return;
                }

                try {
                    File savaFile = new File(savaPath);
                    if (!savaFile.exists()) {
                        savaFile.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(savaFile);
                    FileInputStream fis = new FileInputStream(orginFile);
                    byte[] buff = new byte[1024];
                    int len = -1;
                    while ((len = fis.read(buff)) != -1) {
                        fos.write(buff, 0, len);
                    }
                    fis.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 判断两个文件的内容是否一样
     *
     * @param file1
     * @param file2
     */
    public static boolean judgeTwoFile(File file1, File file2) {
        if (null == file1 || null == file2) {
            return false;
        }
        try {
            FileInputStream fis1 = new FileInputStream(file1);
            FileInputStream fis2 = new FileInputStream(file2);
            String str1 = inputStream2String(fis1);
            String str2 = inputStream2String(fis2);
            return str1.equals(str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将InputStream变成字符串
     */
    public static String inputStream2String(InputStream fis) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = fis.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }
        baos.close();
        fis.close();
        return baos.toString();
    }


    /**
     * 获取屏幕的宽和高
     *
     * @param context
     * @return 数组[0]:宽度  数组[1]:高度
     */
    public static int[] getWindowWidthAndHeight(Context context) {
        int[] arr = new int[2];
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        arr[0] = manager.getDefaultDisplay().getWidth();
        arr[1] = manager.getDefaultDisplay().getHeight();
        return arr;
    }


    /**
     * 得到字符串
     *
     * @param id 输 入的Id
     * @return 返回字符串
     */
    public static String getString(int id) {
        return AppUtils.getContext().getResources().getString(id);
    }

    /**
     * 自动弹出软键盘
     */
    public static void initDate(final EditText etInput) {
        etInput.setFocusable(true);
        etInput.setFocusableInTouchMode(true);
        etInput.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {

                InputMethodManager inputManager = (InputMethodManager) etInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etInput, 0);
            }

        }, 500);
    }


    /**
     * 不使用Gilde显示网络图片
     */
    public static void showOwnImagView(final Activity activity, final String imgaUrl, final ImageView imageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL(Const.IMG_URL + imgaUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);

                    connection.connect();

                    if (connection.getResponseCode() == 200) {
                        InputStream inputStream = connection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    } else {
                        imageView.setBackgroundResource(R.drawable.no_shopdetail_url);
                    }

                } catch (Exception e) {
                    imageView.setBackgroundResource(R.drawable.no_shopdetail_url);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
