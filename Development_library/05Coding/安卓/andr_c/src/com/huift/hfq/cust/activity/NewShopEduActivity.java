package com.huift.hfq.cust.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import u.aly.bu;

import com.huift.hfq.cust.adapter.HomeShopListAdapter;
import com.huift.hfq.cust.adapter.NewShopScrollAdapter;
import com.huift.hfq.cust.fragment.NewEduShopCouProFragment;
import com.huift.hfq.cust.fragment.NewEduShopHomeFragment;
import com.huift.hfq.cust.fragment.NewEduShopHoronurFragment;
import com.huift.hfq.cust.fragment.NewEduShopMasterFragment;
import com.huift.hfq.cust.fragment.NewEduShopSheduleFragment;
import com.huift.hfq.cust.fragment.VipChatFragment;
import com.huift.hfq.cust.model.GetShopInfoTask;
import com.umeng.analytics.MobclickAgent;

import net.minidev.json.JSONObject;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.Messages;
import com.huift.hfq.base.pojo.NewShopInfoData;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.ShopDecoration;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.cust.R;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView.ScaleType;

/***
 * 教育行业的店铺详情
 * @author yingchen
 *
 */
public class NewShopEduActivity extends Activity implements OnClickListener{
	protected static final String TAG = NewShopEduActivity.class.getSimpleName();

	/**附近商家列表*/
	private ListView mShopListView;
	
	/**回退*/
	private ImageView mBack;
	/**联系*/
	private ImageView mConnect;
	/**分享*/
	private ImageView mShare;
	
	/**置顶视图*/
	private View mTopView;
	
	/**置顶视图中的控件*/
	/**店铺名称*/
	private TextView mShopName;
	/**工行Logo*/
	private ImageView mICBCLogo;
	/**工行折扣数*/
	private TextView mICBCDiscount;
	/**电话*/
	private ImageView mPhone;
	
	
	/**店铺首页(置顶视图)*/
	private TextView mEduTopHomePage;
	/**优惠/活动(置顶视图)*/
	private TextView mEduTopCouponPro;
	/**课程表(置顶视图)*/
	private TextView mEduTopSheducle;
	/**荣誉墙(置顶视图)*/
	private TextView mEduTopHorour;
	/**校长之语(置顶视图)*/
	private TextView mEduTopMaster;
	
	/**店铺首页(listview头部)*/
	private TextView mEduListViewHomePage;
	/**优惠/活动(listview头部)*/
	private TextView mEduListViewShopCouponPro;
	/**课程表(listview头部)*/
	private TextView mEduListViewSheducle;
	/**荣誉墙(listview头部)*/
	private TextView mEduListViewHorour;
	/**校长之语(listview头部)*/
	private TextView mEduListViewMaster;
	
	
	/**请求API返回的数据*/
	private NewShopInfoData mShopInfoData;
	
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	/**当前显示的Fragment*/
	private Fragment mCurrentFragment;
	
	private NewEduShopHomeFragment mEduShopHomeFragment;
	private NewEduShopCouProFragment mEduShopCouProFragment;
	private NewEduShopSheduleFragment mEduShopSheduleFragment;
	private NewEduShopHoronurFragment mEduShopHoronurFragment;
	private NewEduShopMasterFragment mEduShopMasterFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_shop_edu);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		String shopCode = getIntent().getStringExtra("shopCode"); 
		
		initView();
		
		initData(shopCode);
		
	}
	
	/***
	 *初始化数据 请求API
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
						Util.showToastZH("获取店铺信息失败");
						Log.d(TAG, "转化失败"+e.getMessage());
					}
				}
			}
		}).execute(shopCode);
	}

	/**
	 * 显示列表
	 */
	private void showShopListView() {
		//添加listview的头部视图
		setViewPager();
		
		setShopInfo();
		
		setTabArea();
		
		
		View tipView = View.inflate(this, R.layout.top_listview_shop, null);
		mShopListView.addHeaderView(tipView);
		
		//设置适配器
		List<Shop> aroundShop = mShopInfoData.getAroundShop();
		mShopListView.setAdapter(new HomeShopListAdapter(this, aroundShop));
		
		//置顶
		mShopListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem>=1){
					mTopView.setVisibility(View.VISIBLE);
				}else{
					mTopView.setVisibility(View.GONE);
				}
			}
		});
		
		//默认显示首页
		changeHomePage();
	}


	/**
	 * 设置5个标签对应的内容区域
	 */
	private void setTabArea() {
		View fragmentContainer = View.inflate(this, R.layout.shopinfo_head3, null);
		
		mShopListView.addHeaderView(fragmentContainer);
	}

	

	/**
	 * 设置商家详情
	 */
	private void setShopInfo() {
		//商家信息
		Shop shop = mShopInfoData.getShopInfo();
		
		View shopInfo = View.inflate(this, R.layout.shopinfo_edu_head2, null);
		
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
		
		/**listview头部视图的5个切换标签*/
		mEduListViewHomePage = (TextView) shopInfo.findViewById(R.id.edu_homepage_shop);
		mEduListViewHomePage.setOnClickListener(this);
		mEduTopHomePage.setOnClickListener(this);
		
		mEduListViewShopCouponPro = (TextView) shopInfo.findViewById(R.id.edu_coupon_promotion);
		mEduListViewShopCouponPro.setOnClickListener(this);
		mEduTopCouponPro.setOnClickListener(this);
		
		mEduListViewSheducle = (TextView) shopInfo.findViewById(R.id.edu_shechudle);
		mEduListViewSheducle.setOnClickListener(this);
		mEduTopSheducle.setOnClickListener(this);
		
		mEduListViewHorour = (TextView) shopInfo.findViewById(R.id.edu_honour);
		mEduListViewHorour.setOnClickListener(this);
		mEduTopHorour.setOnClickListener(this);
		
		mEduListViewMaster = (TextView) shopInfo.findViewById(R.id.edu_master);
		mEduListViewMaster.setOnClickListener(this);
		mEduTopMaster.setOnClickListener(this);
		
		mShopListView.addHeaderView(shopInfo);
	}
	
	/**
	 * 设置滚屏
	 */
	private void setViewPager() {
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
	
	/***
	 * 初始基本化视图
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
		
		mEduTopHomePage = (TextView) findViewById(R.id.edu_homepage_shop);
		mEduTopCouponPro = (TextView) findViewById(R.id.edu_coupon_promotion);
		mEduTopSheducle = (TextView) findViewById(R.id.edu_shechudle);
		mEduTopHorour = (TextView) findViewById(R.id.edu_honour);
		mEduTopMaster = (TextView) findViewById(R.id.edu_master);
		
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in: //回退
			finish();
			break;

		case R.id.iv_connect: //联系
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
			break;
			 
		case R.id.iv_share: //分享
			
			break;
			
		case R.id.iv_phone: //电话
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
			
		case R.id.edu_homepage_shop: //首页
			changeHomePage();
			
			break;
		
		case R.id.edu_coupon_promotion: //优惠/活动
			changeCouPro();
			break;
			
		case R.id.edu_shechudle: //课程表
			changeShedule();
			break;
			
		case R.id.edu_honour: //荣誉墙
			changeHonour();
			break;
			
		case R.id.edu_master: //校长之语
			changeMaster();
			break;
			
		default:
			break;
		}
	
	}
	
	/**
	 * 切换5个选项卡的状态
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 */
	private void setTabStatus(boolean homePage, boolean couponPromotion, boolean shedule, boolean honour, boolean master) {
		setTabEnabled(mEduTopHomePage, homePage);
		setTabEnabled(mEduListViewHomePage, homePage);
		
		setTabEnabled(mEduTopCouponPro, couponPromotion);
		setTabEnabled(mEduListViewShopCouponPro, couponPromotion);
		
		setTabEnabled(mEduTopSheducle, shedule);
		setTabEnabled(mEduListViewSheducle, shedule);
		
		setTabEnabled(mEduTopHorour, honour);
		setTabEnabled(mEduListViewHorour, honour);
		
		setTabEnabled(mEduTopMaster, master);
		setTabEnabled(mEduListViewMaster, master);
		
	}
	
	private void  setTabEnabled(TextView view,boolean enabled){
		if(enabled){
			view.setEnabled(true);
			view.setTextColor(Color.BLACK);
		}else{
			view.setEnabled(false);
			view.setTextColor(Color.RED);
		}
	}

	/**
	 * 切换到校长之语
	 */
	private void changeMaster() {
		if(null == mEduShopMasterFragment){
			Bundle bundle = new Bundle();
			mEduShopMasterFragment = NewEduShopMasterFragment.newInstance(bundle);
		}
		switchContent(mCurrentFragment, mEduShopMasterFragment);
		setTabStatus(true,true,true,true,false);
	}

	/**
	 * 切换到荣誉墙
	 */
	private void changeHonour() {
		if(null == mEduShopHoronurFragment){
			Bundle bundle = new Bundle();
			mEduShopHoronurFragment = NewEduShopHoronurFragment.newInstance(bundle);
		}
		switchContent(mCurrentFragment, mEduShopHoronurFragment);
		setTabStatus(true,true,true,false,true);
	}

	/**
	 * 切换到课程表
	 */
	private void changeShedule() {
		if(null == mEduShopSheduleFragment){
			Bundle bundle = new Bundle();
			mEduShopSheduleFragment = NewEduShopSheduleFragment.newInstance(bundle);
		}
		Log.d(TAG, "课程表=="+(mEduShopSheduleFragment==null));
		switchContent(mCurrentFragment, mEduShopSheduleFragment);
		setTabStatus(true,true,false,true,true);
	}

	/**
	 * 切换到优惠/活动
	 */
	private void changeCouPro() {
		if(null == mEduShopCouProFragment){
			Bundle bundle = new Bundle();
			bundle.putSerializable("couponInfo", mShopInfoData.getCouponList());
			bundle.putSerializable("promotionInfo", (ArrayList<Activitys>)mShopInfoData.getActList());
			bundle.putSerializable("shopInfo",mShopInfoData.getShopInfo());
			mEduShopCouProFragment = NewEduShopCouProFragment.newInstance(bundle);
		}
		switchContent(mCurrentFragment, mEduShopCouProFragment);
		setTabStatus(true,false,true,true,true);
	}

	/**
	 * 切换到首页
	 */
	private void changeHomePage() {
		if(null == mEduShopHomeFragment){
			Bundle bundle = new Bundle();
			bundle.putSerializable("shoInfoData", mShopInfoData);
			mEduShopHomeFragment = NewEduShopHomeFragment.newInstance(bundle);
		}
		switchContent(mCurrentFragment, mEduShopHomeFragment);
		setTabStatus(false,true,true,true,true);
	}

	/**
	 * 切换Fragment
	 * @param from
	 * @param to
	 */
	public void switchContent(Fragment from, Fragment to) {
		if(null == mFragmentManager){
			mFragmentManager = this.getFragmentManager();
			
		}
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
}
