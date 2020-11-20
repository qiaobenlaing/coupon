package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.Bimp;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.Util.onUploadFinish;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.base.pojo.ShopDecoration;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.FinishActivityUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.model.AddSubAlbumPhotoTask;
import com.huift.hfq.shop.model.UpdateSubAlbumPhotoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 上传图片
 *
 * @author qian.zhou
 */
public class UploadPhotoActivity extends Activity {
    public static final String IMAGE_URL = "imageUrl";
    public static final String SHOPDECORATION = "shopDecoration";
    public static final String DECORATION = "decoration";
    public static final String NAME = "name";
    public static final String CODE = "code";
    /**
     * setResult()用的，如果确实添加了信息
     */
    public final static int INTENT_PRODUCT_SAVED = 1;
    /**
     * setResult()用的，如果取消了添加信息
     */
    public final static int INTENT_PRODUCT_CANCELED = 2;
    /**
     * startActivityForResult()的requestCode: 选择产品子相册类别名称信息
     */
    public static final int INTENT_REQ_CHECK_INFO = Util.NUM_ONE;
    public static final String FLAG = "one";
    private String mPhotoName;
    private EditText mEtPhotoName;
    /**
     * 保存按钮
     */
    private TextView mTvFinish;
    /**
     * 图片路径
     */
    private String mPicPath;
    /**
     * 产品的价格
     */
    private String mPrice;
    private EditText mEtProductPrice;
    /**
     * 上传的类别
     */
    private TextView mTvPhotoType;
    /**
     * 上传子相册类别的编码
     */
    private String mCode;
    /**
     * 标示
     */
    private String mFlag;
    /**
     * 图片对应的编码
     */
    private String mPhotoCode;
    /**
     * 产品标题的最大长度
     */
    private int PRODUCT_BIG_LENGTH = 10;
    /**
     * 产品价格最大的位数
     */
    private int PRODUCT_PRICE_LENGTH = 9;
    /**
     * 标题
     */
    private TextView mTvContent;
    /**
     * 上传的展示的图片
     */
    private ImageView mIvPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        FinishActivityUtils.addActivity(UploadPhotoActivity.this);
        ViewUtils.inject(this);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        init();
    }

    private void init() {
        //初始化视图
        LinearLayout ivTurnin = (LinearLayout) findViewById(R.id.layout_turn_in);
        ivTurnin.setVisibility(View.VISIBLE);
        mTvContent = (TextView) findViewById(R.id.tv_mid_content);
        mTvFinish = (TextView) findViewById(R.id.tv_msg);
        mTvFinish.setText(getString(R.string.btn_save));
        //产品相册编辑框
        mIvPhoto = (ImageView) findViewById(R.id.iv_upload_photo);
        mEtPhotoName = (EditText) findViewById(R.id.et_product_name);
        mEtProductPrice = (EditText) findViewById(R.id.et_product_price);
        mTvPhotoType = (TextView) findViewById(R.id.tv_photo_type);
        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        Intent intent = this.getIntent();
        mFlag = intent.getStringExtra(FLAG);
        //完成
        mTvFinish.setOnClickListener(addListener);
        if (!Util.isEmpty(mFlag)) {

            if ("one".equals(mFlag)) {//从大图编辑页面过来
                //大图页面的编辑传过来的对象
                Decoration decoration = (Decoration) intent.getSerializableExtra(DECORATION);
                mTvContent.setText(getString(R.string.edit_photo));
                if (null != decoration) {

                    Util.showImage(UploadPhotoActivity.this, decoration.getUrl(), mIvPhoto);
                    mPicPath = decoration.getUrl();
                    mPhotoCode = decoration.getCode();//子相册图片对应的编码
                    mCode = decoration.getSubAlbumCode();//子相册编码
                    mEtPhotoName.setText(decoration.getTitle());
                    mEtProductPrice.setText(decoration.getPrice());
                    mTvPhotoType.setText(decoration.getSubAlbumName());
                }

            } else {
                //上传图片页面传过来的值
                String name = intent.getStringExtra(NAME);
                String imageUrl = intent.getStringExtra(IMAGE_URL);
                mCode = intent.getStringExtra(CODE);
                mTvContent.setText(getString(R.string.upload_photo));
                mTvPhotoType.setText(!Util.isEmpty(name) ? name : "");
                if (!Util.isEmpty(imageUrl)) {

                    Bitmap bitmap = null;
                    File imgFile = new File(imageUrl);
                    FileOutputStream fileout = null;
                    try {
                        bitmap = Bimp.revitionImageSize(imageUrl);
                        fileout = new FileOutputStream(imgFile);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileout);// 把数据写入文件
                    mIvPhoto.setImageBitmap(bitmap);
                    Util.getImageUpload(UploadPhotoActivity.this, imageUrl, new onUploadFinish() {
                        @Override
                        public void getImgUrl(String img) {
                            mPicPath = img;
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == INTENT_REQ_CHECK_INFO) {
            if (resultCode == ProductPhotoTypeActivity.INTENT_RESP_SAVED) {
                ShopDecoration shopDecoration = (ShopDecoration) intent.getSerializableExtra(SHOPDECORATION);
                if (null != shopDecoration) {

                    mTvPhotoType.setText(shopDecoration.getName());
                    mCode = shopDecoration.getCode();
                } else {
                    //TODO
                }
            }
        }
    }

    ;

    /**
     * 完成（添加产品名称）
     */
    OnClickListener addListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mPhotoName = mEtPhotoName.getText().toString();
            mPrice = mEtProductPrice.getText().toString();
            Util.inputFilterSpace(mEtPhotoName);//去空格
            Util.inputFilterSpace(mEtProductPrice);//去空格
            switch (v.getId()) {
                case R.id.tv_msg:
                    if ("".equals(mEtPhotoName.getText().toString())) {
                        Util.getContentValidate(R.string.no_product_name);
                        break;
                    }
                    if ((mEtPhotoName.getText().toString().length()) <= Util.NUM_ZERO) {
                        Util.getContentValidate(R.string.no_product_name);
                        break;
                    }
                    if ((mEtPhotoName.getText().toString().length()) > PRODUCT_BIG_LENGTH) {
                        Util.getContentValidate(R.string.product_title_long);
                        break;
                    }
                    if ((mEtProductPrice.getText().toString().length()) <= Util.NUM_ZERO) {
                        Util.getContentValidate(R.string.no_productprice);
                        break;
                    }
                    if ("".equals(mEtProductPrice.getText().toString())) {
                        Util.getContentValidate(R.string.no_productprice);
                        break;
                    }
                    if ((mEtProductPrice.getText().toString().length()) > PRODUCT_PRICE_LENGTH) {
                        Util.getContentValidate(R.string.product_price_length);
                        break;
                    }
                default:
                    mTvFinish.setEnabled(false);
                    if (!Util.isEmpty(mFlag) && !Util.isEmpty(mCode) && !Util.isEmpty(mPicPath)) {

                        if ("one".equals(mFlag)) {
                            updateSubAlbumPhoto();
                        } else {
                            new AddSubAlbumPhotoTask(UploadPhotoActivity.this, new AddSubAlbumPhotoTask.Callback() {
                                @Override
                                public void getResult(int retCode) {
                                    mTvFinish.setEnabled(true);
                                    if (ErrorCode.SUCC != retCode) {
                                        Util.showToastZH("上传图片失败");
                                    } else {
                                        DB.saveBoolean(ShopConst.Key.UPP_ALBUM_PHOTO, true);
                                    }
                                }
                            }).execute(mCode, mPicPath, mPhotoName, mPrice, "无");
                        }
                    } else {
                        //TODO
                    }
                    break;
            }
        }
    };

    /**
     * 修改子相册产品图片
     */
    public void updateSubAlbumPhoto() {
        new UpdateSubAlbumPhotoTask(UploadPhotoActivity.this, new UpdateSubAlbumPhotoTask.Callback() {
            @Override
            public void getResult(int retCode) {
                if (ErrorCode.SUCC == retCode) {
                    DB.saveBoolean(ShopConst.Key.UPP_DECORATION, true);
                    FinishActivityUtils.exit();
                }
            }
        }).execute(mCode, mPicPath, mPhotoName, mPrice, "无", mPhotoCode);
    }

    /**
     * 点击返回查看到活动列表
     *
     * @param view
     */
    @OnClick({R.id.layout_turn_in, R.id.ry_check_type})
    public void btnBackClick(View view) {
        switch (view.getId()) {
            case R.id.layout_turn_in:
                finish();
                break;
            case R.id.ry_check_type:
                Intent intent = new Intent(UploadPhotoActivity.this, ProductPhotoTypeActivity.class);
                startActivityForResult(intent, INTENT_REQ_CHECK_INFO);
                break;
        }
    }

    public void onResume() {
        super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
