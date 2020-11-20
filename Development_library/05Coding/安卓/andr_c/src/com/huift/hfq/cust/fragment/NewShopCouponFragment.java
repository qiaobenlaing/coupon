package com.huift.hfq.cust.fragment;

import java.util.List;

import com.huift.hfq.cust.activity.BatchCouponDetailActivity;
import com.huift.hfq.cust.adapter.NewCouponAdapter;
import com.huift.hfq.cust.adapter.TimeLimitAdapter;
import com.huift.hfq.cust.adapter.NewCouponAdapter.OnGrabCoupon;
import com.huift.hfq.cust.model.GetShopUseCouponTask;

import net.minidev.json.JSONObject;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Coupons;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.cust.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NewShopCouponFragment extends Fragment {
	
	private static final String TAG = NewShopCouponFragment.class.getSimpleName();
	/**优惠券*/
	private LinearLayout mCouponLinearLayout;
	/**我的优惠券*/
	private ListView mCouponListView;
	/**未领取的优惠券*/
	private ListView mNoGetCouponListView;

	/**默认图片*/
	private LinearLayout mNoDataLinearLayout;
	
	/**商家*/
	private Shop mShop;
	
	
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static NewShopCouponFragment newInstance(Bundle args) {
		NewShopCouponFragment fragment = new NewShopCouponFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_new_shop_coupon, null);
		
		initView(view);
		
		Bundle arguments = getArguments();
		Coupons coupons = (Coupons) arguments.getSerializable("coupons");
		mShop = (Shop) arguments.getSerializable("shop");
		
		//判断是否显示优惠券列表
		isShowCouponList(coupons);
		
		return view;
	}

	/**
	 * 判断是否显示优惠券列表
	 * @param coupons
	 */
	private void isShowCouponList(Coupons coupons) {
		if(null == coupons ||(coupons.getUserCoupon().size()==0 && coupons.getShopCoupon().size()==0)){
			mCouponLinearLayout.setVisibility(View.GONE);
			mNoDataLinearLayout.setVisibility(View.VISIBLE);
		}else{
			mCouponLinearLayout.setVisibility(View.VISIBLE);
			mNoDataLinearLayout.setVisibility(View.GONE);
			showView(coupons);
		}
	}

	
	
	/**
	 * 显示视图
	 */
	private void showView(Coupons coupons) {
		//已经领取的优惠券
		final List<BatchCoupon> userCoupon = coupons.getUserCoupon();
		Log.d(TAG, "优惠券--已领取==="+userCoupon.size());
		mCouponListView.setAdapter(new NewCouponAdapter(userCoupon, getActivity(),1,mShop,null));
		Util.setListViewHeight(mCouponListView);
		
		mCouponListView.setOnItemClickListener(new  OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(getActivity(), BatchCouponDetailActivity.class);
				intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_CODE, userCoupon.get(position).getBatchCouponCode());
				intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_TYPE, userCoupon.get(position).getCouponType());
				startActivity(intent);
			}
		});
		
		
		//未领取的优惠券
		List<BatchCoupon> shopCoupon = coupons.getShopCoupon();
		Log.d(TAG, "优惠券--未领取==="+shopCoupon.size());
		mNoGetCouponListView.setAdapter(new NewCouponAdapter(shopCoupon, getActivity(),2,mShop,grabCouponListener));
		Util.setListViewHeight(mNoGetCouponListView);
		
	}
	
	private NewCouponAdapter.OnGrabCoupon grabCouponListener = new OnGrabCoupon() {
		@Override
		public void grabCoupon(boolean success) {
			if(success){
				updateCouponInfo();
			}
		}
	};
	
	/**
	 * 更新优惠券信息
	 */
	public  void updateCouponInfo(){
		new GetShopUseCouponTask(getActivity(), new GetShopUseCouponTask.CallBack() {
			
			@Override
			public void getResult(JSONObject result) {
				if(null != result){
					Coupons coupons = Util.json2Obj(result.toString(), Coupons.class);
					isShowCouponList(coupons);
				}
			}
		}).execute(mShop.getShopCode());
	}
	/**
	 * 初始化视图
	 */
	private void initView(View view ) {
		mCouponLinearLayout =  (LinearLayout) view.findViewById(R.id.ll_coupons);
		mCouponListView = (ListView) view.findViewById(R.id.lv_my_coupons);
		mNoGetCouponListView = (ListView) view.findViewById(R.id.lv_noget_coupons);
		mNoDataLinearLayout =  (LinearLayout) view.findViewById(R.id.ll_no_data);
	}
	
	
}
