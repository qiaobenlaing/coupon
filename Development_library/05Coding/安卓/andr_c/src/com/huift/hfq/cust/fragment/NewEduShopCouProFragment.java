package com.huift.hfq.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import com.huift.hfq.cust.activity.BatchCouponDetailActivity;
import com.huift.hfq.cust.adapter.ActIssueAdapter;
import com.huift.hfq.cust.adapter.NewCouponAdapter;
import com.huift.hfq.cust.adapter.NewCouponAdapter.OnGrabCoupon;
import com.huift.hfq.cust.model.GetShopUseCouponTask;

import net.minidev.json.JSONObject;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Coupons;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.ListViewHeight;
import com.huift.hfq.cust.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 教育行业商户的优惠/活动
 * @author yingchen
 *
 */
public class NewEduShopCouProFragment extends Fragment {
	private static final String TAG = NewEduShopCouProFragment.class.getSimpleName();
	/**优惠券信息*/
	private Coupons mCoupons;
	/**活动信息*/
	private ArrayList<Activitys>  mActivitys;
	/**商家信息*/
	private Shop mShop;
	
	/**有数据*/
	private LinearLayout mHavaData;
	/**没有数据的默认视图*/
	private LinearLayout mNoData;
	
	/**我的优惠券列表*/
	private ListView mGetCouponListView;
	/**商家发行的优惠券列表*/
	private ListView mNoGetCouponListView;
	/**商家发行的活动列表*/
	private ListView mPromotionListView;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static NewEduShopCouProFragment newInstance(Bundle args) {
		NewEduShopCouProFragment fragment = new NewEduShopCouProFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_edu_shop_cou_prot, null);
		Bundle bundle = getArguments();
		mCoupons = (Coupons) bundle.getSerializable("couponInfo");
		mActivitys = (ArrayList<Activitys>) bundle.getSerializable("promotionInfo");
		mShop = (Shop) bundle.getSerializable("shopInfo");
		
		initView(view);
		
		isShowView(mCoupons);
		
		showPromotionListView();
		return view;
	}
	
	/**
	 * 判断是否显示列表  当没有任何优惠券和活动时显示默认视图
	 * @param mCoupons2
	 */
	private void isShowView(Coupons coupons) {
		if((null== coupons||(coupons.getShopCoupon().size()==0&&coupons.getUserCoupon().size()==0))&&(null==mActivitys||mActivitys.size()==0)){
			//显示默认视图
			mHavaData.setVisibility(View.GONE);
			mNoData.setVisibility(View.VISIBLE);
		}else{
			mHavaData.setVisibility(View.VISIBLE);
			mNoData.setVisibility(View.GONE);
			showCouponListView(coupons);
		}
	}

	/**
	 * 显示活动列表
	 */
	private void showPromotionListView() {
		
		Log.d(TAG, "活动--"+mActivitys.size());
		mPromotionListView.setAdapter(new ActIssueAdapter(getActivity(), mActivitys));
		ListViewHeight.setListViewHeightBasedOnChildren(mPromotionListView);
	}

	/**
	 * 显示优惠券列表
	 */
	private void showCouponListView(Coupons coupons) {
		Log.d(TAG, "显示优惠券列表");
		//显示我的优惠券列表
		final List<BatchCoupon> userCoupon = coupons.getUserCoupon();
		Log.d(TAG, "优惠券--已领取==="+userCoupon.size());
		mGetCouponListView.setAdapter(new NewCouponAdapter(userCoupon, getActivity(),1,mShop,null));
		Util.setListViewHeight(mGetCouponListView);
		
		mGetCouponListView.setOnItemClickListener(new  OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(getActivity(), BatchCouponDetailActivity.class);
				intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_CODE, userCoupon.get(position).getBatchCouponCode());
				intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_TYPE, userCoupon.get(position).getCouponType());
				startActivity(intent);
			}
		});
		
		//显示未领取的优惠券列表
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
					isShowView(coupons);
				}
			}
		}).execute(mShop.getShopCode());
	}
	/**
	 * 初始化控件
	 * @param view
	 */
	private void initView(View view) {
		mGetCouponListView = (ListView) view.findViewById(R.id.lv_edu_my_coupons);
		mNoGetCouponListView = (ListView) view.findViewById(R.id.lv_edu_noget_coupons);
		mPromotionListView = (ListView) view.findViewById(R.id.lv_edu_promotion);
		mHavaData = (LinearLayout) view.findViewById(R.id.ll_edu_cou_pro);
		mNoData = (LinearLayout) view.findViewById(R.id.ll_no_data);
	}
	
}
