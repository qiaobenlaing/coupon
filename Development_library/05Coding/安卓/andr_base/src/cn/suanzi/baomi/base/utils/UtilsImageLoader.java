package cn.suanzi.baomi.base.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Environment;
import android.widget.ImageView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.R;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

public class UtilsImageLoader {
	private static BitmapUtils bitmapUtils;  
    private Context mContext; 
    private Bitmap bitmap;
    /** 首先默认个文件保存路径 */
    private static final String SAVE_PIC_PATH=Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
    /** 保存的确切位置*/
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + Const.SAVE_IMAGE;
    
    public UtilsImageLoader(Context context) {  
        // TODO Auto-generated constructor stub  
        this.mContext = context;  
        bitmapUtils = new BitmapUtils(mContext);  
        bitmapUtils.configDefaultLoadingImage(R.drawable.iamge_fail);//默认背景图片  
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.iamge_fail);//加载失败图片  
  
    }  
    /** 
     *  
     * @author sunglasses 
     * @category 图片回调函数 
     */  
    public class CustomBitmapLoadCallBack extends  DefaultBitmapLoadCallBack<ImageView> {  
        @Override  
        public void onLoading(ImageView container, String uri,  
                BitmapDisplayConfig config, long total, long current) {  
        }  
  
        @Override  
        public void onLoadCompleted(ImageView container, String uri,  
                Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {  
            fadeInDisplay(container, bitmap);  
        }  
  
        @Override  
        public void onLoadFailed(ImageView container, String uri,  
                Drawable drawable) {  
        }  
    }  
  
    private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(  
            android.R.color.transparent);  
    /** 
     * @author sunglasses 
     * @category 图片加载效果 
     * @param imageView 
     * @param bitmap 
     */  
    private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {//目前流行的渐变效果  
        final TransitionDrawable transitionDrawable = new TransitionDrawable(  
                new Drawable[] { TRANSPARENT_DRAWABLE,  
                        new BitmapDrawable(imageView.getResources(), bitmap) });  
        imageView.setImageDrawable(transitionDrawable);  
        transitionDrawable.startTransition(500);  
    }  
    
    public static void display(ImageView container,String url){//外部接口函数  
         bitmapUtils.display(container, url);  
    }  
    
    /**
     * 保存图片
     * @param bm
     * @param fileName
     * @param path
     * @throws IOException
     */
    public static void saveFile(Bitmap bm, String fileName) throws IOException {
    	String subForder = SAVE_REAL_PATH;
    	File foder = new File(subForder);
    	if (!foder.exists()) {
    	foder.mkdirs();
    	}
    	File myCaptureFile = new File(subForder, fileName);
    	if (!myCaptureFile.exists()) {
    	myCaptureFile.createNewFile();
    	}
    	BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
    	bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
    	bos.flush();
    	bos.close();
    	}
}
