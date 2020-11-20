package cn.suanzi.baomi.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.db.sqlite.CursorUtils.FindCacheSequence;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Coupons;
import cn.suanzi.baomi.base.pojo.NewShop;
import cn.suanzi.baomi.base.pojo.NewShopInfoData;
import cn.suanzi.baomi.base.pojo.NewShopProduct;
import cn.suanzi.baomi.base.pojo.NewShopTime;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.NewShopInfoActivity;
import cn.suanzi.baomi.cust.activity.NewShopRecentVisistActivity;

public class ShopHomePageFragment extends Fragment implements View.OnClickListener{
	/**营业时间*/
	private TextView mTimeTextView;
	/**人气*/
	private TextView mPopTextView;
	/**地址*/
	private TextView mStreetTextView;
	
	/**最近访问*/
	private LinearLayout mRecentVisit;
	/**用户头像1*/
	private ImageView mUserImg1;
	/**用户头像2*/
	private ImageView mUserImg2;
	/**用户头像3*/
	private ImageView mUserImg3;
	
	/**商品集合*/
	private LinearLayout mProducts;
	private LinearLayout mProductInfos;
	/**商品1的头像*/
	private ImageView mProductLogo1;
	/**商品1的名称*/
	private TextView mProductName1;
	/**商品1的价格*/
	private TextView mProductPrice1;
	
	/**商品2的头像*/
	private ImageView mProductLogo2;
	/**商品2的名称*/
	private TextView mProductName2;
	/**商品2的价格*/
	private TextView mProductPrice2;
	
	
	/**商品1的头像*/
	private ImageView mProductLogo3;
	/**商品1的名称*/
	private TextView mProductName3;
	/**商品1的价格*/
	private TextView mProductPrice3;
	
	/**优惠券*/
	private LinearLayout mCoupon;
	
	/**活动*/
	private LinearLayout mPromotion;
	
	/**店铺简介*/
	private TextView mShopDescribtion;
	private TextView mShopDescribtionDetail;
	
	private NewShopInfoData newShop;
	
	public static ShopHomePageFragment newInstance(Bundle args) {
		ShopHomePageFragment fragment = new ShopHomePageFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.shop_homepage_fragment, null);
		newShop = (NewShopInfoData) getArguments().getSerializable("shopdata");
		
		initView(view);
		
		showView();
		return view;
	}
	
	/**
	 * 显示视图
	 */
	private void showView() {
		//营业时间
		List<NewShopTime> businessHours = newShop.getShopInfo().getBusinessHours();
		if(null != businessHours && businessHours.size()>0){
			StringBuffer buffer = new StringBuffer();
			for (NewShopTime shopTime:businessHours) {
				buffer.append(shopTime.getOpen()+"-"+shopTime.getClose()+" ");
			}
			mTimeTextView.setText(buffer.toString());
		}else{
			mTimeTextView.setText("暂无营业时间");
		}
		
		
		//人气
		mPopTextView.setText("人气:"+newShop.getShopInfo().getPopularity());
		
		//地址
		Shop shopInfo = newShop.getShopInfo();
		StringBuffer buffer = new StringBuffer();
		if(!Util.isEmpty(shopInfo.getCity())){
			buffer.append(shopInfo.getCity());
		}
		if(!Util.isEmpty(shopInfo.getDistrict())){
			buffer.append(shopInfo.getDistrict());
		}
		if(!Util.isEmpty(shopInfo.getStreet())){
			buffer.append(shopInfo.getStreet());
		}
		mStreetTextView.setText(buffer.toString());
		
		//最近访问
		//如果访问人数小于3
		if(newShop.getRecentVisitor().size()==0){
			mUserImg1.setVisibility(View.GONE);
			mUserImg2.setVisibility(View.GONE);
			mUserImg3.setVisibility(View.GONE);
		}else if(newShop.getRecentVisitor().size()==1){
			Util.showFirstImages(getActivity(), newShop.getRecentVisitor().get(0).getAvatarUrl(), mUserImg1);
			mUserImg2.setVisibility(View.GONE);
			mUserImg3.setVisibility(View.GONE);
		}else if(newShop.getRecentVisitor().size()==2){
			Util.showFirstImages(getActivity(), newShop.getRecentVisitor().get(0).getAvatarUrl(), mUserImg1);
			Util.showFirstImages(getActivity(), newShop.getRecentVisitor().get(1).getAvatarUrl(), mUserImg2);
			mUserImg3.setVisibility(View.GONE);
		}else{
			Util.showFirstImages(getActivity(), newShop.getRecentVisitor().get(0).getAvatarUrl(), mUserImg1);
			Util.showFirstImages(getActivity(), newShop.getRecentVisitor().get(1).getAvatarUrl(), mUserImg2);
			Util.showFirstImages(getActivity(), newShop.getRecentVisitor().get(2).getAvatarUrl(), mUserImg3);
		}
		
		//商品服务
		if(null == newShop.getShopPhotoList() || newShop.getShopPhotoList().size() == 0){
			mProducts.setVisibility(View.GONE);
		}else if(newShop.getShopPhotoList().size() == 1){
			setProductVisib(mProductLogo1, mProductName1, mProductPrice1, true, newShop.getShopPhotoList().get(0));
			setProductVisib(mProductLogo2, mProductName2, mProductPrice2, false, null);
			setProductVisib(mProductLogo3, mProductName3, mProductPrice3, false, null);
		}else if(newShop.getShopPhotoList().size() == 2){
			setProductVisib(mProductLogo1, mProductName1, mProductPrice1, true, newShop.getShopPhotoList().get(0));
			setProductVisib(mProductLogo2, mProductName2, mProductPrice2, true, newShop.getShopPhotoList().get(1));
			setProductVisib(mProductLogo3, mProductName3, mProductPrice3, false, null);
		}else{
			setProductVisib(mProductLogo1, mProductName1, mProductPrice1, true, newShop.getShopPhotoList().get(0));
			setProductVisib(mProductLogo2, mProductName2, mProductPrice2, true, newShop.getShopPhotoList().get(1));
			setProductVisib(mProductLogo3, mProductName3, mProductPrice3, true, newShop.getShopPhotoList().get(2));
		}
		
		//优惠券
		Coupons couponList = newShop.getCouponList();
		if(null == couponList || (couponList.getShopCoupon().size()==0&&couponList.getUserCoupon().size()==0)){
			mCoupon.setVisibility(View.GONE);
		}else{
			mCoupon.setVisibility(View.VISIBLE);
		}
		
		//活动
		if(null == newShop.getActList() || newShop.getActList().size()==0){
			mPromotion.setVisibility(View.GONE);
		}else{
			mPromotion.setVisibility(View.VISIBLE);
		}
		
		//店铺简介
		if(Util.isEmpty(newShop.getShopInfo().getShortDes())){
			mShopDescribtion.setVisibility(View.GONE);
			mShopDescribtionDetail.setVisibility(View.GONE);
		}else{
			mShopDescribtion.setVisibility(View.VISIBLE);
			mShopDescribtionDetail.setVisibility(View.VISIBLE);
			mShopDescribtionDetail.setText(newShop.getShopInfo().getShortDes());
		}
	}

	public void setProductVisib(ImageView head,TextView name,TextView price,boolean visibile,NewShopProduct product){
		if(visibile){
			Util.showFirstImages(getActivity(), product.getUrl(), head);
			name.setText(product.getProductName());
			price.setText("￥"+product.getFinalPrice());
		}else{
			head.setVisibility(View.INVISIBLE);
			name.setVisibility(View.INVISIBLE);
			price.setVisibility(View.INVISIBLE);
		}
	}
	
	
	/**
	 * 初始化视图
	 * @param view
	 */
	private void initView(View view) {
		mTimeTextView = (TextView) view.findViewById(R.id.tv_shop_time);
		mPopTextView = (TextView) view.findViewById(R.id.tv_shop_popularity);
		mStreetTextView = (TextView) view.findViewById(R.id.address);
		
		mRecentVisit = (LinearLayout) view.findViewById(R.id.ll_recentvisit);
		mRecentVisit.setOnClickListener(this);
		mUserImg1 = (ImageView) view.findViewById(R.id.visit_user1);
		mUserImg2 = (ImageView) view.findViewById(R.id.visit_user2);
		mUserImg3 = (ImageView) view.findViewById(R.id.visit_user3);
		
		mProducts = (LinearLayout) view.findViewById(R.id.service);
		mProducts.setOnClickListener(this);
		mProductInfos = (LinearLayout) view.findViewById(R.id.ll_shop_products);
		mProductLogo1 = (ImageView) view.findViewById(R.id.iv_product_logo1);
		mProductName1 = (TextView) view.findViewById(R.id.name_product_logo1);
		mProductPrice1 = (TextView) view.findViewById(R.id.price_product_logo1);
		
		mProductLogo2 = (ImageView) view.findViewById(R.id.iv_product_logo2);
		mProductName2 = (TextView) view.findViewById(R.id.name_product_logo2);
		mProductPrice2 = (TextView) view.findViewById(R.id.price_product_logo2);
		
		mProductLogo3 = (ImageView) view.findViewById(R.id.iv_product_logo3);
		mProductName3 = (TextView) view.findViewById(R.id.name_product_logo3);
		mProductPrice3 = (TextView) view.findViewById(R.id.price_product_logo3);
		
		mCoupon = (LinearLayout) view.findViewById(R.id.ll_coupons);
		mCoupon.setOnClickListener(this);
		mPromotion = (LinearLayout) view.findViewById(R.id.ll_promotions);
		mPromotion.setOnClickListener(this);
		
		mShopDescribtion = (TextView) view.findViewById(R.id.tv_shop_introduce);
		mShopDescribtionDetail = (TextView) view.findViewById(R.id.tv_shop_introduce_detail);
	}

	/**
	 * 点击的回调
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_recentvisit:
			//Util.showToastZH("最近访客");
			Intent intent = new Intent(getActivity(), NewShopRecentVisistActivity.class);
			intent.putExtra("recentVisit",(ArrayList<User>) newShop.getRecentVisitor());
			startActivity(intent);
			break;
		case R.id.service:
			//Util.showToastZH("商品/服务");
			((NewShopInfoActivity)getActivity()).changeService();
			break;
		case R.id.ll_coupons:
			//Util.showToastZH("优惠券");
			((NewShopInfoActivity)getActivity()).changeCoupons();
			break;
		case R.id.ll_promotions:
			//Util.showToastZH("店铺活动");
			((NewShopInfoActivity)getActivity()).changePromotion();
			break;

		default:
			break;
		}
	}
}
