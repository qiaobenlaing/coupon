package cn.suanzi.baomi.cust.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.Tools;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.LoginTask;
import cn.suanzi.baomi.base.pojo.Activitys;
import cn.suanzi.baomi.base.pojo.Messages;
import cn.suanzi.baomi.base.pojo.NewShopInfoData;
import cn.suanzi.baomi.base.pojo.NewShopProduct;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.ShopDecoration;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.HomeShopListAdapter;
import cn.suanzi.baomi.cust.adapter.NewShopScrollAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.fragment.NewShopCouponFragment;
import cn.suanzi.baomi.cust.fragment.NewShopPromotionFragment;
import cn.suanzi.baomi.cust.fragment.NewShopServiceFragment;
import cn.suanzi.baomi.cust.fragment.ShopHomePageFragment;
import cn.suanzi.baomi.cust.fragment.ShopPayBillFragment;
import cn.suanzi.baomi.cust.fragment.VipChatFragment;
import cn.suanzi.baomi.cust.model.GetShopInfoTask;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

/**
 * H5改原生 商家详情
 * @author yingchen
 */

public class NewShopInfoActivity extends Activity implements View.OnClickListener{
	protected static final String TAG = NewShopInfoActivity.class.getSimpleName();

	/**店铺列表*/
	private ListView mShopListView;
	
	/**回退*/
	private ImageView mBack;
	/**联系*/
	private ImageView mConnect;
	/**分享*/
	private ImageView mShare;
	
	/**置顶视图*/
	private View mTopView;
	/**店铺名称*/
	private TextView mShopName;
	/**工行Logo*/
	private ImageView mICBCLogo;
	/**工行折扣数*/
	private TextView mICBCDiscount;
	/**电话*/
	private ImageView mPhone;
	/**餐饮四个标签*/
	private LinearLayout mFoodLinearLayout;
	/**点餐*/
	private LinearLayout mTakeOrder;
	/**外卖*/
	private LinearLayout mTakeOut;
	/**预定*/
	private LinearLayout mBookFood;
	/**订单*/
	private LinearLayout mOrderFood;
	/**店铺首页*/
	private TextView mShopHomePage;
	/**优惠券*/
	private TextView mShopCoupon;
	/**活动*/
	private TextView mShopPromotion;
	/**商品/服务*/
	private TextView mShopService;
	
	/**店铺所有信息*/
	private NewShopInfoData mShopInfoData;
	
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private ShopHomePageFragment shopHomePageFragment;
	private NewShopCouponFragment mCouponFragment;
	private NewShopPromotionFragment mPromotionFragment;
	private NewShopServiceFragment mServiceFragment;
	
	/**店铺首页选项卡*/
	private TextView mHomePage;
	/**优惠券选项卡*/
	private TextView mCoupon;
	/**商家活动选项卡*/
	private TextView mPromotion;
	/**商品/服务选项卡*/
	private TextView mService;

	
	/**支付按钮*/
	private Button mPayButton;
	
	/**选项卡显示的当前的Fragment*/
	private Fragment mCurrentFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_shop_info);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		
		String shopCode = getIntent().getStringExtra("shopCode");
		
		initView();
		
		//获取服务端数据
		initData(shopCode);
	
	}

	/**
	 * 初始化数据
	 */
	private void initData(String shopCode) {
		new GetShopInfoTask(this, new GetShopInfoTask.CallBack() {
			
			@Override
			public void getResult(JSONObject result) {
				if(null != result){
					try {
						mShopInfoData = Util.json2Obj(result.toString(), NewShopInfoData.class);
						Log.d(TAG, "转化成功==="+mShopInfoData.getShopInfo().getShopName());
						
						showShopListView();
					} catch (Exception e) {
						Log.d(TAG, "转化失败"+e.getMessage());
					}
				}
			}
		}).execute(shopCode);
	}

	/**
	 *显示商家列表
	 */
	private void showShopListView() {
		//附近商家数据
		List<Shop> aroundShop = mShopInfoData.getAroundShop();
		
		setViewPager();
	
		setShopInfo();
		
		setTabArea();
		
		setPayButton();
		
		
		View tipView = View.inflate(this, R.layout.top_listview_shop, null);
		mShopListView.addHeaderView(tipView);
		
		View footView = View.inflate(this, R.layout.footerview_newshop_list, null);
		mShopListView.addFooterView(footView);
		//显示列表
		Log.d(TAG, "附近商家数量==="+aroundShop.size());
		mShopListView.setAdapter(new HomeShopListAdapter(this, aroundShop));
	
	}
	
	/**
	 * 设置支付按钮状态
	 */
	private void setPayButton() {
		Log.d(TAG, "ShowPayBtn==="+mShopInfoData.getShopInfo().getShowPayBtn());
		Log.d(TAG, "ShowPayBtn1==="+mShopInfoData.getShopInfo().getIfCanPay());
		
		if(mShopInfoData.getShopInfo().getShowPayBtn()==1){ //显示支付按钮
			mPayButton.setVisibility(View.VISIBLE);
			int ifCanPay = mShopInfoData.getShopInfo().getIfCanPay();
			if(ifCanPay==1){ //正常 可以支付
				mPayButton.setEnabled(true);
				mPayButton.setBackgroundResource(R.drawable.btn_can_pay);
			}else if(ifCanPay==0){ //不可以支付
				mPayButton.setBackgroundResource(R.drawable.btn_canonot_pay);
				mPayButton.setEnabled(false);
			}
		}else{ //隐藏支付按钮
			mPayButton.setVisibility(View.GONE);
		}
	}

	/**
	 * 四个标签切换Fragment的区域
	 */
	private void setTabArea() {
		View fragmentContainer = View.inflate(this, R.layout.shopinfo_head3, null);
		
		mShopListView.addHeaderView(fragmentContainer);
		
		//默认显示店铺首页Fragment
		/*if (null == shopHomePageFragment) {
			Bundle args = new Bundle();
			args.putSerializable("shopdata", mShopInfoData);
			shopHomePageFragment = ShopHomePageFragment.newInstance(args);
		}
		changeFragment(this,R.id.rl_fragment_container, shopHomePageFragment);
		setTabStatus(false,true,true,true);*/
		changeShopHomePage();
		
		//置顶
		mShopListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int firstVisiblePosition = mShopListView.getFirstVisiblePosition();
		
				if(firstVisibleItem>=1){
					mTopView.setVisibility(View.VISIBLE);
					//mTopView.setBackgroundColor(Color.WHITE);
					//shopInfo.setVisibility(View.GONE);
				}else{
					mTopView.setVisibility(View.GONE);
					//shopInfo.setVisibility(View.VISIBLE);
				}
				
				Log.d(TAG, "位置   firstVisibleItem==="+firstVisibleItem);
				Log.d(TAG, "位置   firstVisiblePosition==="+firstVisiblePosition);
			}
		});
	}

	/**
	 * 设置商家信息
	 */
	private void setShopInfo() {
		//商家信息
		Shop shop = mShopInfoData.getShopInfo();
		
		View shopInfo = View.inflate(this, R.layout.shopinfo_head2, null);
		
		//店铺名称
		TextView shopName = (TextView) shopInfo.findViewById(R.id.tv_shopname);
		shopName.setText(shop.getShopName());
		mShopName.setText(shop.getShopName());
		
		//电话
		ImageView phone = (ImageView) shopInfo.findViewById(R.id.iv_phone);
		phone.setOnClickListener(this);
		mPhone.setOnClickListener(this);
	
		//工行卡标识
		ImageView icbcLog = (ImageView) shopInfo.findViewById(R.id.iv_icbc_log);
		//工行卡折扣数
		TextView icbcDisCount = (TextView) shopInfo.findViewById(R.id.tv_icbc_discount);
		
		//餐饮行业标签
		LinearLayout foodTab = (LinearLayout) shopInfo.findViewById(R.id.ll_food_shop);
	
		//点餐
		LinearLayout takeOrder = (LinearLayout) shopInfo.findViewById(R.id.ll_take_order);
		takeOrder.setOnClickListener(this);
		mTakeOrder.setOnClickListener(this);
		//外卖
		LinearLayout takeOut = (LinearLayout) shopInfo.findViewById(R.id.ll_take_out);
		takeOut.setOnClickListener(this);
		mTakeOut.setOnClickListener(this);
		//预定
		LinearLayout bookFood = (LinearLayout) shopInfo.findViewById(R.id.ll_book_food);
		bookFood.setOnClickListener(this);
		mBookFood.setOnClickListener(this);
		//订单
		LinearLayout orderFood = (LinearLayout) shopInfo.findViewById(R.id.ll_order_food);
		orderFood.setOnClickListener(this);
		mOrderFood.setOnClickListener(this);
			
		mHomePage = (TextView) shopInfo.findViewById(R.id.homepage_shop);
		mHomePage.setOnClickListener(this);
		mShopHomePage.setOnClickListener(this);
		mCoupon = (TextView) shopInfo.findViewById(R.id.coupon_shop);
		mCoupon.setOnClickListener(this);
		mShopCoupon.setOnClickListener(this);
		mPromotion = (TextView) shopInfo.findViewById(R.id.promotion_shop);
		mPromotion.setOnClickListener(this);
		mShopPromotion.setOnClickListener(this);
		mService = (TextView) shopInfo.findViewById(R.id.service_shop);
		mService.setOnClickListener(this);
		mShopService.setOnClickListener(this);
		
		//工行卡折扣
		
		double onlinePaymentDiscount = Util.isEmpty(shop.getOnlinePaymentDiscount())?10:Double.parseDouble(shop.getOnlinePaymentDiscount());
		if(onlinePaymentDiscount<10){
			icbcLog.setVisibility(View.VISIBLE);
			mICBCLogo.setVisibility(View.VISIBLE);
			icbcDisCount.setVisibility(View.VISIBLE);
			mICBCDiscount.setVisibility(View.VISIBLE);
			icbcDisCount.setText(onlinePaymentDiscount+"折");
			mICBCDiscount.setText(onlinePaymentDiscount+"折");
		}else{
			icbcLog.setVisibility(View.INVISIBLE);
			mICBCLogo.setVisibility(View.INVISIBLE);
			icbcDisCount.setVisibility(View.INVISIBLE);
			mICBCDiscount.setVisibility(View.INVISIBLE);
		}
		
		//是否是餐饮行业
		int isCatering = shop.getIsCatering();
		if(isCatering ==1){
			foodTab.setVisibility(View.VISIBLE);
			mFoodLinearLayout.setVisibility(View.VISIBLE);
			
		}else{
			foodTab.setVisibility(View.GONE);
			mFoodLinearLayout.setVisibility(View.GONE);
			
		}
	
		mShopListView.addHeaderView(shopInfo);	
	
	}

	/**
	 * 设置滚屏
	 */
	private void setViewPager(){
		View scrollView  = View.inflate(this, R.layout.shopinfo_head1, null);
		
		ViewPager viewPager = (ViewPager) scrollView.findViewById(R.id.vp_shopinfo);
		
		final TextView count = (TextView) scrollView.findViewById(R.id.tv_count_shopinfo);
		
		//初始化滚屏数据
		final List<ShopDecoration> shopDecoration = mShopInfoData.getShopDecoration();
		Log.d(TAG, "滚屏数量==="+shopDecoration.size());
		
		//默认显示第一张
		count.setText("1/"+shopDecoration.size());
		
		List<ImageView> list = new ArrayList<ImageView>();

		for (int i = 0; i < shopDecoration.size(); i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.FIT_XY);
			Util.showBannnerImage(this, shopDecoration.get(i).getImgUrl(), imageView);
			list.add(imageView);
		}
		
		viewPager.setAdapter(new NewShopScrollAdapter(list));
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				count.setText((position+1)+"/"+shopDecoration.size());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		mShopListView.addHeaderView(scrollView);
	}
	

	/**
	 * 初始化基本控件
	 */
	private void initView() {
		mShopListView = (ListView) findViewById(R.id.shop_list);
		mBack = (ImageView) findViewById(R.id.iv_turn_in);
		mBack.setOnClickListener(this);
		
		mConnect = (ImageView) findViewById(R.id.iv_connect);
		mConnect.setOnClickListener(this);

		mShare = (ImageView) findViewById(R.id.iv_share);
		mShare.setOnClickListener(this);
		
		mTopView = findViewById(R.id.ll_shopinfo_topview);
		mPhone = (ImageView) findViewById(R.id.iv_phone);
		mShopName = (TextView) findViewById(R.id.tv_shopname);
		mICBCLogo = (ImageView) findViewById(R.id.iv_icbc_log);
		mICBCDiscount = (TextView) findViewById(R.id.tv_icbc_discount);
		mFoodLinearLayout = (LinearLayout) findViewById(R.id.ll_food_shop);
		mTakeOrder = (LinearLayout) findViewById(R.id.ll_take_order);
		mTakeOut = (LinearLayout) findViewById(R.id.ll_take_out);
		mBookFood = (LinearLayout) findViewById(R.id.ll_book_food);
		mOrderFood = (LinearLayout) findViewById(R.id.ll_order_food);
		
		mShopHomePage = (TextView) findViewById(R.id.homepage_shop);
		mShopCoupon = (TextView) findViewById(R.id.coupon_shop);
		mShopPromotion = (TextView) findViewById(R.id.promotion_shop);
		mShopService = (TextView) findViewById(R.id.service_shop);
		
		mPayButton = (Button) findViewById(R.id.btn_pay);
		mPayButton.setOnClickListener(this);
	}

	
	/**
	 * 点击回掉
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			finish();
			break;
		case R.id.iv_connect: //联系
			if (!DB.getBoolean(DB.Key.CUST_LOGIN)) {
				Intent intentPay = new Intent(this, LoginActivity.class);
				intentPay.putExtra(LoginTask.ALL_LOGIN, "newShopInfo");
				this.startActivity(intentPay);
			}else{
				Messages message = new Messages();
				message.setShopCode(mShopInfoData.getShopInfo().getShopCode());
				if (!Util.isEmpty(mShopInfoData.getShopInfo().getShopName())) {
					message.setShopName(mShopInfoData.getShopInfo().getShopName());
				} else {
					message.setShopName("商家");
				}
				Intent intent = new Intent(this, VipChatActivity.class);
				intent.putExtra(VipChatFragment.MSG_OBJ, (Serializable) message);
				startActivity(intent);
			}
			break;
		
		case R.id.iv_share://分享
			//Util.showToastZH("敬请期待");
			shareShop();
			
			break;
			
		case R.id.iv_phone: //电话
			//Util.showToastZH("电话");
			if(!Util.isEmpty(mShopInfoData.getShopInfo().getTel())){
				DialogUtils.showDialog(this, "联系商家", "确认拨打"+mShopInfoData.getShopInfo().getTel(), "确认", "取消", 
						new DialogUtils().new OnResultListener() {
							public void onOK() {
								Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(Util.getString(R.string.tel) + mShopInfoData.getShopInfo().getTel()));
								AppUtils.getActivity().startActivity(intent);
								// 友盟统计
								MobclickAgent.onEvent(AppUtils.getActivity(), "myhome_fragment_phone");
							};
						}
				);
			}else{
				Util.showToastZH("商家电话号码未保存");
			}
			break;
			
		case R.id.homepage_shop: //切换店铺首页Fragment
			changeShopHomePage();
			break;
			
		case R.id.coupon_shop: //先换优惠券Fragment
			changeCoupons();
			break;
			
		case R.id.promotion_shop: //切换商品活动的Fragment
			changePromotion();
			break;
			
		case R.id.service_shop: //切换商品/服务Fragment
			changeService();
			break;
			
		case R.id.ll_take_order: //点餐
			//Util.showToastZH("点餐");
			isLogin("take_order");
			break;
			
		case R.id.ll_take_out: //外卖
			//Util.showToastZH("外卖");
			isLogin("take_out");
			break;
			
		case R.id.ll_book_food: //预定
			//Util.showToastZH("预定");
			isLogin("book_food");
			break;
			
		case R.id.ll_order_food: //订单
			//Util.showToastZH("订单");
			isLogin("order_food");
			break;
			
		case R.id.btn_pay: //去支付
			//判断是否登录
			if (!DB.getBoolean(DB.Key.CUST_LOGIN)) {
				Intent intentPay = new Intent(this, LoginActivity.class);
				intentPay.putExtra(LoginTask.ALL_LOGIN, "newShopInfo");
				this.startActivity(intentPay);
			}else{
				Shop shop = mShopInfoData.getShopInfo();
				if (mShopInfoData == null && mShopInfoData.getShopInfo() == null) {
					Util.showToastZH("商家信息为空");
				} else {
					SkipActivityUtil.skipPayBillActivity(this, shop, "",ShopPayBillFragment.JOIN_DISCOUNT,ShopPayBillFragment.NOT_MEAL);
				}
			}
			
			break;
			
		default:
			break;
			
		}
	}

	/**
	 * 分享店铺
	 */
	private void shareShop() {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = "";
		if(null != userToken){
			userCode = userToken.getUserCode();
		}
		if (mShopInfoData == null && mShopInfoData.getShopInfo() == null) {
			Util.showToastZH("商家信息查询为空");
			return;
		}
		String url = Const.H5_URL + "Browser/getShopInfo?shopCode=" + mShopInfoData.getShopInfo().getShopCode() + "&userCode=" + userCode;
	    String title = "分享一家商铺:"+mShopInfoData.getShopInfo().getShopName();
	    String logoUrl = "";
	    if(!Util.isEmpty(mShopInfoData.getShopInfo().getLogoUrl())){
	    	logoUrl = mShopInfoData.getShopInfo().getLogoUrl();
	    }
	    String describe = "这家商店性价比超好,商品物超所值";
		String filePath = Tools.getFilePath(this) + Tools.APP_ICON;
		
		
		Tools.showShare(this, url, describe, title, filePath, logoUrl);
		
	}

	private void  isLogin(String action){
		if (!DB.getBoolean(DB.Key.CUST_LOGIN)) {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginTask.ALL_LOGIN, "newShopInfo");
			this.startActivity(intent);
		}else{
			toH5(action);
		}
	}
	
	/**
	 * 点餐 外卖 预定 订单 跳到H5界面
	 * @param string    take_order---点餐    take_out---外卖  book_food--预定  order_food---订单
	 */
	private void toH5(String action) {
//		Intent intent = new Intent(this, NewShopInfoActivtyToH5Activity.class);
		
		if("book_food".equals(action)){ //预定
			Util.showToastZH("敬请期待");
			return;
		}
//		intent.putExtra("ACTION_TYPE", action);
//		intent.putExtra("SHOP_INFO", mShopInfoData.getShopInfo());
//		SkipActivityUtil.skipH5ShopDetailActivity(this, mShopInfoData.getShopInfo().getShopCode(), action);
		
//		this.startActivity(intent);
		
		Intent intent = new Intent(this, H5ShopDetailActivity.class);
		if (null == mShopInfoData && null == mShopInfoData.getShopInfo()) {
			Util.showToastZH("商家信息为空");
			return ;
		}
		intent.putExtra("shopCode", mShopInfoData.getShopInfo().getShopCode());
		intent.putExtra(H5ShopDetailActivity.TYPE, action);
		startActivity(intent);
	}

	/**
	 * 切换到商品/服务
	 */
	public void changeService() {
		if(null == mServiceFragment){
			Bundle args = new Bundle();
			args.putSerializable("Service", (ArrayList<NewShopProduct>)mShopInfoData.getShopPhotoList());
			args.putString("shopCode", mShopInfoData.getShopInfo().getShopCode());
			mServiceFragment = NewShopServiceFragment.newInstance(args);
		}
		changeFragment(this,/*R.id.rl_fragment_container,*/ mServiceFragment);
		setTabStatus(true,true,true,false);
	}

	/**
	 * 切换到活动
	 */
	public void changePromotion() {
		if(null == mPromotionFragment){
			Bundle args = new Bundle();
			args.putSerializable("Promotion", (ArrayList<Activitys>)mShopInfoData.getActList());
			mPromotionFragment = NewShopPromotionFragment.newInstance(args);
		}
		changeFragment(this,/*R.id.rl_fragment_container,*/ mPromotionFragment);
		setTabStatus(true,true,false,true);
	}

	/**
	 * 切换到优惠券
	 */
	public void changeCoupons(){
		if(null == mCouponFragment){
			Bundle args = new Bundle();
			args.putSerializable("coupons", mShopInfoData.getCouponList());
			args.putSerializable("shop", mShopInfoData.getShopInfo());
			mCouponFragment = NewShopCouponFragment.newInstance(args);
		}
		changeFragment(this,/*R.id.rl_fragment_container, */mCouponFragment);
		setTabStatus(true,false,true,true);
	}

	/**
	 * 切换到店铺首页
	 */
	public void changeShopHomePage() {
		//Util.showToastZH("店铺首页");
		if (null == shopHomePageFragment) {
			Bundle args = new Bundle();
			args.putSerializable("shopdata", mShopInfoData);
			shopHomePageFragment = ShopHomePageFragment.newInstance(args);
		}
		changeFragment(this,/*R.id.rl_fragment_container, */shopHomePageFragment);
		setTabStatus(false,true,true,true);
	}
	
	

	/**
	 * 设置四个选项卡的状态
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 */
	
	private void setTabStatus(boolean shopHomePage, boolean coupon, boolean promotion, boolean service) {
		setTextViewStatus(mHomePage, shopHomePage);
		setTextViewStatus(mShopHomePage, shopHomePage);
		
		setTextViewStatus(mCoupon, coupon);
		setTextViewStatus(mShopCoupon, coupon);
		
		setTextViewStatus(mPromotion, promotion);
		setTextViewStatus(mShopPromotion, promotion);
		
		setTextViewStatus(mService, service);
		setTextViewStatus(mShopService, service);
	}

	public void setTextViewStatus(TextView view,boolean enabled){
		if(enabled){
			view.setEnabled(true);
			view.setTextColor(Color.BLACK);
		}else{
			view.setEnabled(false);
			view.setTextColor(Color.RED);
		}
	}
	/**
	 * 切换Fragment
	 * @param activity
	 * @param id
	 * @param fragment
	 */
	public void changeFragment(Activity activity, /*int id,*/ Fragment tofragment) {
		/*mCurrentFragment = fragment;
		mFragmentManager = activity.getFragmentManager();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.replace(id, fragment);
		transaction.addToBackStack(null);
		transaction.commit();*/
		
		if(null == mFragmentManager){
			mFragmentManager = activity.getFragmentManager();
			
		}
		
		Fragment from = mCurrentFragment;
		
		switchContent(from, tofragment);
	}
	
	
	public void switchContent(Fragment from, Fragment to) {
		FragmentTransaction trx = mFragmentManager.beginTransaction();
		if (mCurrentFragment != null) {
			trx.hide(mCurrentFragment);
		} else if (from != null) {
			trx.hide(from);
		}
		if (!to.isAdded()) {
			trx.add(R.id.rl_fragment_container, to);
		}
		mCurrentFragment = to;
		try {
			trx.show(to).commit();
		} catch (Exception e) {
			try {
				trx.show(to).commitAllowingStateLoss();
			} catch (Exception ee) {
			}
		}
		
    }
	
	/**
	 * 重写back事件
	 */
	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		  if (keyCode == KeyEvent.KEYCODE_BACK ) {
			  finish();
		  }
		  return super.onKeyDown(keyCode, event);
	 }
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(null != mCouponFragment && mCurrentFragment instanceof NewShopCouponFragment ){
			mCouponFragment.updateCouponInfo();
		}
	}
}
