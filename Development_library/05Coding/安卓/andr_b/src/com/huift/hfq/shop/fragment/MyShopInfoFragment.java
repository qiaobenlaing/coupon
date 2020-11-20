package com.huift.hfq.shop.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.bumptech.glide.Glide;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.Util.onUploadFinish;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.view.MyGridView;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.BigShopLogoActivity;
import com.huift.hfq.shop.activity.RestaurantSetActivity;
import com.huift.hfq.shop.activity.UpdateShopDecActivity;
import com.huift.hfq.shop.activity.UpdateShopTimeActivity;
import com.huift.hfq.shop.adapter.TimeShopAdapter;
import com.huift.hfq.shop.model.GetisHasProductTask;
import com.huift.hfq.shop.model.SgetShopBasicInfoTask;
import com.huift.hfq.shop.model.UpdateShopInfoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 店铺信息
 * @author qian.zhou
 */
public class MyShopInfoFragment extends Fragment {
	private static final String TAG = "MyShopInfoFragment";
	public final static String IMAGEURL = "mImagUrl";
	public final  static String SHOP_OBJ = "shop";
	/** 修改店铺应营业时间*/
	public final static int RE_UPDATE_OPTIME = 101;
	/** 修改店铺简介*/
	public final static int RE_UPDATE_SHOPDEC = 1001;
	/** 调用api成功后返回*/
	public final static int DE_UPDATE_OPTIME = 102;
	/** 从图库选择照片*/
	private final static int SELECT_PIC = 0;
	/** 保存数据的对象 */
	private Shop mShop;
	/** 全局视图*/
	private View mView;
	/** 修改的图片路径*/
	private String mImageUrl;
	/** 商家logo*/
	private ImageView mIvShopLogo;
	/** 线程*/
	private Handler mHandler;
	/** PopupWindow是一个容器 **/
	private PopupWindow mPopupWindow;
	/** 店铺简介*/
	private TextView mTvShopDesc;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	/** 当商家的店铺信息*/
	private boolean mUppFlag = false;
	/** 显示时间*/
	private MyGridView mLvBusinessTime;
	private TimeShopAdapter mShopAdapter;
	/** 正在加载数据 */
	private LinearLayout mLyNodate;
	/** 正在加载的内容 */
	private LinearLayout mLyContent;

	/**
	 * 需要传递参数时有利于解耦
	 * @return MyShopInfoFragment
	 */
	public static MyShopInfoFragment newInstance() {
		Bundle args = new Bundle();
		MyShopInfoFragment fragment = new MyShopInfoFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_myshopinfo, container, false);
		ViewUtils.inject(this, mView);
		Util.addActivity(getActivity());
		Util.addLoginActivity(getActivity());
		init(mView);// 初始化控件
		return mView;
	}

	/**
	 * 初始化控件
	 */
	private void init(View v) {
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		mHandler = new Handler();
		// 加载数据
		mLyNodate = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mLyContent = (LinearLayout) v.findViewById(R.id.ly_content);
		//标题
		LinearLayout ivBack = (LinearLayout) v.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.shop_message));
		TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
		tvMsg.setVisibility(View.GONE);
		mLvBusinessTime = (MyGridView) v.findViewById(R.id.gv_myshopinfo_time);
		setData(0); // 没有数据
		//赋值
		MyShopInfo(v);
		//查询商铺是否有上架的商品
		getIsHasProduct();
	}
	
	/**
	 * 设置数据
	 * @param type
	 * 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData(int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyContent.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 判断activity不为空
	 * @return
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 商铺查询的异步任务类
	 */
	public void MyShopInfo(final View v) {
		// 调用MyHomeTask异步请求，然后修改UI控件
		new SgetShopBasicInfoTask(getActivity(), new SgetShopBasicInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				}
				setData(1); // 有数据
				mShop = Util.json2Obj(result.toString(), Shop.class);
				TextView shopName = (TextView) v.findViewById(R.id.tv_myshopinfo_name);
				TextView shopTel = (TextView) v.findViewById(R.id.tv_myshopinfo_tel);
				TextView shopAddress = (TextView) v.findViewById(R.id.tv_shopaddress);
				TextView tvManagerNumber = (TextView) v.findViewById(R.id.tv_manager_number);//店长号码
				mTvShopDesc = (TextView) v.findViewById(R.id.tv_shop_des);
				mIvShopLogo = (ImageView) v.findViewById(R.id.iv_shopinfo_shoplogo);
				Util.showImage(getActivity(), mShop.getLogoUrl(), mIvShopLogo);
				ImageView updateLogo = (ImageView) v.findViewById(R.id.iv_shopinfo_arrow);
				ImageView uppshoptime = (ImageView) v.findViewById(R.id.iv_shop_arrow);
				RelativeLayout ryBusinessTime = (RelativeLayout) v.findViewById(R.id.ry_business_time);
				LinearLayout lyShortDec =  (LinearLayout) v.findViewById(R.id.ly_short_dec);
				//餐厅设置
				RelativeLayout ryRestaurantset = (RelativeLayout) v.findViewById(R.id.ry_restaurantset);
				ryRestaurantset.setOnClickListener(clickListener);
				//头像点击事件
				mIvShopLogo.setOnClickListener(clickListener);
				//修改商家头像
				updateLogo.setOnClickListener(clickListener);
				//设置营业时间
				ryBusinessTime.setOnClickListener(clickListener);
				//设置营业时间
				uppshoptime.setOnClickListener(clickListener);
				//店铺简介
				lyShortDec.setOnClickListener(clickListener);
				//判断该商家是否有堂食
				if (mShop.getIsCatering() == 1){
					
				}
				//判断该商家是否为餐饮类商铺（IsCatering 1 是   0 否）
				ryRestaurantset.setVisibility(mShop.getIsCatering() == 1 ? View.VISIBLE : View.GONE);
				if (!Util.isEmpty(mShop.getShopName())) {
					shopName.setText(mShop.getShopName());
				} 
				//判断电话号码
				if (mShop.getTel() == null) {
					if (!Util.isEmpty(mShop.getMobileNbr())) {
						shopTel.setText(mShop.getMobileNbr());
					}
				} else {
					shopTel.setText(mShop.getTel());
				}
				//店长号码
				if (mShop.getTel() == null) {
					if (!Util.isEmpty(mShop.getMobileNbr())) {
						tvManagerNumber.setText(mShop.getMobileNbr());
					}
				} else {
					tvManagerNumber.setText(mShop.getTel());
				}
				//店铺地址
				if (!Util.isEmpty(mShop.getProvince()) && !Util.isEmpty(mShop.getCity()) && !Util.isEmpty(mShop.getStreet())) {
					shopAddress.setText(mShop.getProvince() + mShop.getCity() + mShop.getStreet());
				}
				//店铺简介
				if (!Util.isEmpty(mShop.getShortDes())) {
					mTvShopDesc.setText(mShop.getShortDes());
				}
				//多段营业时间
				setShopTime(result);
			}
		}).execute();
	}
	
	/**
	 * 多段营业时间
	 * @param result
	 */
	public void setShopTime(JSONObject result){
		List<Shop> shopTimeList = new ArrayList<Shop>();
		JSONArray array = (JSONArray) result.get("businessHours");
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			Shop shop = Util.json2Obj(object.toString(), Shop.class);
			shopTimeList.add(shop);
		}
		mShopAdapter  = new TimeShopAdapter(getMyActivity(), shopTimeList);
		mLvBusinessTime.setAdapter(mShopAdapter);
	}
	
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_shopinfo_arrow://修改商家logo     
				if (mSettledflag) {
					View view = LayoutInflater.from(getActivity()).inflate(R.layout.select_pic, null);
					// 设置mPopupWindow的宽高
					mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					// 获得焦点，点击mPopupWindow以外的地方，窗体消失
					mPopupWindow.setFocusable(true);
					mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btncardset));
					mPopupWindow.setOutsideTouchable(true);
					// 设置mPopupWindow的显示位置
					mPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
					Button btnImage = (Button) view.findViewById(R.id.btn_pick_photo);// 图库
					Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);// 取消
					// 点击图库按钮
					btnImage.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							if (i.resolveActivity(getActivity().getPackageManager()) != null) {
								startActivityForResult(i, SELECT_PIC);
							}
							mPopupWindow.dismiss();
						}
					});
					// 点击取消按钮
					btnCancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mPopupWindow.dismiss();
						}
					});
				} else {
					mShopApplication.getDateInfo(getActivity());
				}
				break;
			case R.id.ry_business_time:// 设置营业时间
				if (mSettledflag) {
					Intent intent = new Intent(getActivity(), UpdateShopTimeActivity.class);
					startActivityForResult(intent, RE_UPDATE_OPTIME);
				} else {
					mShopApplication.getDateInfo(getActivity());
				}
				break;
			case R.id.iv_shop_arrow:// 设置营业时间
				if (mSettledflag) {
					Intent intent = new Intent(getActivity(), UpdateShopTimeActivity.class);
					startActivityForResult(intent, RE_UPDATE_OPTIME);
				} else {
					mShopApplication.getDateInfo(getActivity());
				}
				break;
			case R.id.ry_restaurantset:// 餐厅设置
				if (mSettledflag) {
					Intent setIntent = new Intent(getActivity(), RestaurantSetActivity.class);
					setIntent.putExtra(RestaurantSetFragment.SHOP_OBJ, mShop);
					startActivity(setIntent);
				} else {
					mShopApplication.getDateInfo(getActivity());
				}
				break;
			case R.id.ly_short_dec:// 店铺简介
				if (mSettledflag) {
					Intent decIntent = new Intent(getActivity(), UpdateShopDecActivity.class);
					decIntent.putExtra(UpdateShopDecFragment.SHOPOBJ, mShop);
					startActivityForResult(decIntent, RE_UPDATE_SHOPDEC);
				} else {
					mShopApplication.getDateInfo(getActivity());
				}
				break;
			case R.id.iv_shopinfo_shoplogo://点击商家logo查看大图
				Intent logoIntent = new Intent(getMyActivity(), BigShopLogoActivity.class);
				logoIntent.putExtra(BigShopLogoActivity.IMAGEURL, mShop.getLogoUrl());
				startActivity(logoIntent);
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 给上传的图片命名
	 * @param context
	 * @return
	 */
	private String getFilePath(Context context) {
		File f = context.getExternalFilesDir(null);
		if (f == null) {
			f = Environment.getExternalStorageDirectory();
			if (f == null) {
				f = context.getFilesDir();
			} else {
				f = new File(f.getAbsolutePath() + "/suanzi/");
				f.mkdirs();
			}
		}
		return f == null ? null : f.getAbsolutePath();
	}
	
	/**
	 * 上传图片
	 * @param bitmap
	 */
	private void updateHead(Bitmap bitmap) {
		if (bitmap == null) { return; }
		String mPicPath = getFilePath(getActivity()) + "/" + System.currentTimeMillis() + ".jpg";
		Tools.savBitmapToJpg(bitmap, mPicPath);
		Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null,null));
		Glide.with(getActivity()).load(uri).centerCrop().into(mIvShopLogo);
	    Util.getImageUpload(getActivity(), mPicPath, new onUploadFinish() {
			@Override
			public void getImgUrl(String img) {
				mImageUrl = img;
				mHandler.post(upDateHead);
			}
		});
	}
	
	private Runnable upDateHead = new Runnable() {
		@Override
		public void run() {
			if (Util.isNetworkOpen(getActivity())) {//联网
				updatShop();
			} else {//没有连接网络
				Util.getToastBottom(getActivity(), "请连接网络");
			}
		}
	};
	
	/**
	 * 拍照和调用图库时要执行的方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	   if (requestCode == SELECT_PIC) {
		   if (resultCode == Activity.RESULT_OK) {
			   if (intent == null) {
                   return;
               }
			   try {
			    Uri uri = intent.getData();
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
				updateHead(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   }     
	   } else if (requestCode == RE_UPDATE_OPTIME) {//修改商家的营业时间
		   if (resultCode == DE_UPDATE_OPTIME) {
			   
			   MyShopInfo(mView);
		   }
	   } else if (requestCode == RE_UPDATE_SHOPDEC) {//修改商家的简介信息
		   if (resultCode == DE_UPDATE_OPTIME) {
			   Shop uppShop = (Shop) intent.getSerializableExtra(UpdateShopDecFragment.SHOPOBJ);
			   mTvShopDesc.setText(uppShop.getShortDes());
			   mShop.setShortDes(uppShop.getShortDes());
		   }
	   }
	};
	
	/**
	 * 修改商铺信息
	 */
	public void updatShop(){
		String updateKey  = "logoUrl"; 
		String updateValue = mImageUrl;
		new UpdateShopInfoTask(getActivity(), new UpdateShopInfoTask.Callback() {
			@Override
			public void getResult(int retCode) {
				if (ErrorCode.SUCC == retCode) {
					MyShopInfo(mView);
					DB.saveBoolean(ShopConst.Key.UPP_SHOPINFO,true);
				} else {
					Util.getContentValidate(R.string.upp_file);
				}
			}
		}).execute(updateKey , updateValue);
	}
	
	/**
	 * 获得堂食点餐的商铺是否有上架的商品
	 */
	public void getIsHasProduct(){
		new GetisHasProductTask(getActivity(), new GetisHasProductTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
			    DB.saveStr(ShopConst.Key.IS_HAVE_PRODUCT, result.get("code").toString());
			}
		}).execute();
	}
	
	/**
	 * 点击事件
	 */
	@OnClick({ R.id.layout_turn_in})
	private void ivClickTo(View v) {
		switch (v.getId()) {
		case R.id.layout_turn_in:// 返回
			getActivity().finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}
	@Override
	public void onResume() {
		super.onResume();
		Activity act = getMyActivity();
		if (act == null) {
			return;
		}
		mUppFlag = DB.getBoolean(ShopConst.Key.UPP_SHOPINFO);
		if (mUppFlag) {
			DB.saveBoolean(ShopConst.Key.UPP_SHOPINFO,false);
			MyShopInfo(mView);
		}
		MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
}
