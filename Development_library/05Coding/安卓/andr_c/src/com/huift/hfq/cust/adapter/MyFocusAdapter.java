package com.huift.hfq.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.CardItem;
import com.huift.hfq.base.pojo.User;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.ActIcBcDetailActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.fragment.ActIcBcDetailFragment;
import com.huift.hfq.cust.util.SkipActivityUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 会员卡的关注商家列表
 * @author qian.zhou
 */
public class MyFocusAdapter extends CommonListViewAdapter<CardItem> {
	private static final String TAG = "MyFocusAdapter";
	/** 登陆人的信息*/
	private User mUser;
	
	public MyFocusAdapter(Activity activity, List<CardItem> cardItem) {
		super(activity, cardItem);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_myfocuslist, position);
		final CardItem cardItem = (CardItem) getItem(position);
		mUser = DB.getObj(DB.Key.CUST_USER, User.class);
		final TextView tvShopName = holder.getView(R.id.tv_myfocus_shopname);//商铺名称
		final ImageView ivShopLogo = holder.getView(R.id.iv_myfoucus_logo);//商店logo
		TextView tvAddress = holder.getView(R.id.tv_myfocus_address);//街道
		TextView tvPopularoty = holder.getView(R.id.tv_myfocus_popularoty);//人气
		TextView tvType = holder.getView(R.id.tv_myfocus_type);//所属类别
		//上新
		GridView gvPhoto = holder.getView(R.id.gv_myfocus_newphotolist);//上新的图片
		TextView tvLine = holder.getView(R.id.tv_line);
		//活动
		RelativeLayout ryActivity = holder.getView(R.id.ry_have_activity);//是否有活动
		TextView tvAcitvityLine = holder.getView(R.id.tv_have_activityline);
		TextView tvActName = holder.getView(R.id.tv_myfocus_actname);//活动名称
		//进入店铺
		LinearLayout lyIntoShopDetail = holder.getView(R.id.ly_into_shopdeatil);
		RelativeLayout  ryIntoShopDetail = holder.getView(R.id.ry_into_shopdetail);
		
		//首单立减         
		TextView tvLessMoney = holder.getView(R.id.tv_less_money);
		//工银优惠        
		TextView tvBankDiscount = holder.getView(R.id.tv_bankdiscount);
		//优惠
		TextView tvCoupon = holder.getView(R.id.tv_coupon);
		
		//赋值
		tvShopName.setText(cardItem.getShopName());
		Util.showImage(mActivity, cardItem.getLogoUrl(), ivShopLogo);
		tvAddress.setText(cardItem.getStreet());
		//判断传过来的距离是否大于一千米
		String distance = cardItem.getDistance().replace(".", "").replace("," , "");
		if(Integer.parseInt(distance) >= 100000){
			((TextView) holder.getView(R.id.tv_focus_distance)).setText(mActivity.getResources().getString(R.string.distance));
		} else {
			if(Integer.parseInt(distance) >= 1000){
				Integer sdistance = Integer.parseInt(distance)/1000;
				((TextView) holder.getView(R.id.tv_focus_distance)).setText(sdistance + "KM");
			} else{
				((TextView) holder.getView(R.id.tv_focus_distance)).setText(distance + "M");
			}
		}  
		
		tvPopularoty.setText("人气  : " + cardItem.getPopularity());
		if("1".equals(cardItem.getType())){//美食
			tvType.setText(mActivity.getString(R.string.food_home));
		} else if("2".equals(cardItem.getType())){//咖啡
			tvType.setText(mActivity.getString(R.string.coffee_home));
		} else if("3".equals(cardItem.getType())){//健身
			tvType.setText(mActivity.getString(R.string.fit_home));
		} else if("4".equals(cardItem.getType())){//娱乐
			tvType.setText(mActivity.getString(R.string.entertain_home));
		} else if("5".equals(cardItem.getType())){//服装
			tvType.setText(mActivity.getString(R.string.type_Clothing));
		} else if("6".equals(cardItem.getType())){//其他
			tvType.setText(mActivity.getString(R.string.type_other));
		}
		
		//是否有优惠券
		getIsFirst(cardItem.getIsFirst(), tvLessMoney);
		isHaveBankDiscount(cardItem.getHasIcbcDiscount(), tvBankDiscount);
		isHasCoupon(cardItem.getHasCoupon(), tvCoupon);
		
		//判断是否有上新
		if("0".equals(cardItem.getHasNew())){//没有
			if(cardItem.getNewPhotoList().size() ==0){//没有图片
				gvPhoto.setVisibility(View.GONE);
				tvLine.setVisibility(View.GONE);
			} else{
				gvPhoto.setVisibility(View.VISIBLE);
				tvLine.setVisibility(View.VISIBLE);
			}
		} else{
			gvPhoto.setVisibility(View.VISIBLE);
			tvLine.setVisibility(View.VISIBLE);
		}
		NewPhotoAdapter newPhotoAdapter = new NewPhotoAdapter(mActivity, cardItem.getNewPhotoList());
		gvPhoto.setAdapter(newPhotoAdapter);
		
		//判断是否有活动
		if(Util.isEmpty(cardItem.getActName())){
			ryActivity.setVisibility(View.GONE);
			tvAcitvityLine.setVisibility(View.GONE);
		} else{
			ryActivity.setVisibility(View.VISIBLE);
			tvAcitvityLine.setVisibility(View.VISIBLE);
			tvActName.setText(cardItem.getActName());
		}
		
		//点击进入店铺
		ryIntoShopDetail.setTag(position);
		ryIntoShopDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag(); 
				String shopCode = mDatas.get(index).getShopCode();//商店编码
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, shopCode);
				//友盟统计
				MobclickAgent.onEvent(mActivity, "carddetail_focus_shop");
			}
		});
		
		//进入店铺
		lyIntoShopDetail.setTag(position);
		lyIntoShopDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag(); 
				String shopCode = mDatas.get(index).getShopCode();//商店编码
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, shopCode);
			}
		});
		
		
		//进入活动
		ryActivity.setTag(position);
		ryActivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String typeflag = "0"; 
				Intent intent = new Intent(mActivity, ActIcBcDetailActivity.class);
				intent.putExtra(ActIcBcDetailFragment.ACTIVITY_CODE, cardItem.getActCode());
				intent.putExtra(ActIcBcDetailFragment.newInstance().TYPE, typeflag);
				mActivity.startActivity(intent);
			}
		});
		
		return holder.getConvertView();
	}
	
	
	//是否有首单立减
	private void getIsFirst(int isFirst, TextView lessMoney){
		if(isFirst == 1){
			lessMoney.setVisibility(View.VISIBLE);
		} else{
			lessMoney.setVisibility(View.GONE);
		}
	}
	
	//判断是否有工银优惠
	private void isHaveBankDiscount(int hasIcbcDiscount, TextView bankDiscount){
		if(hasIcbcDiscount == 1){
			bankDiscount.setVisibility(View.VISIBLE);
		} else{
			bankDiscount.setVisibility(View.GONE);
		}
	}
	
	//判断是否有优惠券
	private void isHasCoupon(int hasCoupon, TextView tvCoupon){
		if(hasCoupon == 1){
			tvCoupon.setVisibility(View.VISIBLE);
		} else{
			tvCoupon.setVisibility(View.GONE);
		}
	}
}
