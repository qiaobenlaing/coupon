package cn.suanzi.baomi.cust.adapter;

import java.util.HashMap;
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
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.CardItem;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ActIcBcDetailActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.fragment.ActIcBcDetailFragment;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.umeng.analytics.MobclickAgent;

/**
 *	获得足迹
 * @author wensi.yu
 *
 */
public class MyCommonFootPrintAdapter extends CommonListViewAdapter<CardItem>{
	
	private static final String TAG = "MyCommonFootPrintAdapter";
	private static final int HAS_LIST = 0;
	private static final String HAS_ACT = "1";
	/** 登陆人的信息*/
	private User mUser;
	
	public MyCommonFootPrintAdapter(Activity activity, List<CardItem> datas) {
		super(activity, datas);
	}
	
	@Override
	public void setItems(List<CardItem> cardItem) {
		super.setItems(cardItem);
	}
	@Override
	public void addItems(List<CardItem> cardItem) {
		super.addItems(cardItem);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_mycommonlist_footprint, position);
		final CardItem cardItem = mDatas.get(position);
		mUser = DB.getObj(DB.Key.CUST_USER, User.class);
		//商店logo
		final ImageView ivShopLogo = holder.getView(R.id.iv_mycommon_logo);
		Util.showImage(mActivity, cardItem.getLogoUrl(), ivShopLogo);
		
		((TextView) holder.getView(R.id.tv_mycommon_shopname)).setText(cardItem.getShopName());//商店名称
		((TextView) holder.getView(R.id.tv_mycommon_address)).setText(cardItem.getStreet());//街道
		((TextView) holder.getView(R.id.tv_mycommon_popularoty)).setText("人气："+cardItem.getPopularity());//人气
		TextView tvType = holder.getView(R.id.tv_mycommon_food);
		GridView gvPhoto = holder.getView(R.id.gv_mycommon_newphotolist);//上新的图片
		TextView hasLinePhoto = holder.getView(R.id.tv_line_photo);//活动线
		RelativeLayout hasAct = holder.getView(R.id.rl_mycommon_act); //是否有活动
		TextView hasLineAct = holder.getView(R.id.tv_line_act);//活动线
		TextView newActName = holder.getView(R.id.tv_mycommon_actname); //活动名称
		//首单立减
		TextView tvLessMoney = holder.getView(R.id.tv_commonless_money);
		//工银优惠        
		TextView tvBankDiscount = holder.getView(R.id.tv_common_bankdiscount);
		//优惠
		TextView tvCoupon = holder.getView(R.id.tv_common_coupon);
		
		//进入店铺
		LinearLayout lyIntoShopDetail = holder.getView(R.id.ly_into_shopdetail);
		RelativeLayout  ryIntoShopDetail = holder.getView(R.id.ry_into_shopdetail);
		
		if ("1".equals(cardItem.getType())) {//美食
			
			tvType.setText(mActivity.getString(R.string.food_home));
			
		} else if ("2".equals(cardItem.getType())) {//咖啡
			
			tvType.setText(mActivity.getString(R.string.coffee_home));
			
		} else if ("3".equals(cardItem.getType())) {//健身
			
			tvType.setText(mActivity.getString(R.string.fit_home));
			
		} else if ("4".equals(cardItem.getType())) {//娱乐
			
			tvType.setText(mActivity.getString(R.string.entertain_home));
			
		} else if ("5".equals(cardItem.getType())) {//服装
			
			tvType.setText(mActivity.getString(R.string.type_Clothing));
			
		} else if ("6".equals(cardItem.getType())) {//其他
			
			tvType.setText(mActivity.getString(R.string.type_other));
		}
		
		//判断传过来的距离是否大于一千米
		String distance = cardItem.getDistance().replace(".", "").replace("," , "");
		if (Integer.parseInt(distance) >= 100000) {
			((TextView) holder.getView(R.id.tv_common_distance)).setText(mActivity.getResources().getString(R.string.distance));
		} else {
			if (Integer.parseInt(distance) >= 1000) {
				Integer sdistance = Integer.parseInt(distance)/1000;
				((TextView) holder.getView(R.id.tv_common_distance)).setText(sdistance + "KM");
			} else {
				((TextView) holder.getView(R.id.tv_common_distance)).setText(distance + "M");
			}
		}  
		
		//是否有上新相册
		if (HAS_ACT.equals(cardItem.getHasNew())) {//上新
			hasLinePhoto.setVisibility(View.VISIBLE);
			NewPhotoAdapter newPhotoAdapter = new NewPhotoAdapter(mActivity, cardItem.getNewPhotoList());
			gvPhoto.setAdapter(newPhotoAdapter);
		} else {
			if (cardItem.getNewPhotoList().size() == HAS_LIST) { 
				gvPhoto.setVisibility(View.GONE);
				hasLinePhoto.setVisibility(View.GONE);
			} else {
				NewPhotoAdapter newPhotoAdapter = new NewPhotoAdapter(mActivity, cardItem.getNewPhotoList());
				gvPhoto.setAdapter(newPhotoAdapter);
			}
		}
		
		//是否有活动
		if (!"".equals(cardItem.getActCode())) {
			hasAct.setVisibility(View.VISIBLE);
			hasLineAct.setVisibility(View.VISIBLE);
			newActName.setText(cardItem.getActName());
		} else {
			hasLineAct.setVisibility(View.GONE);
			hasAct.setVisibility(View.GONE);
		}
		hasAct.setTag(position);
		hasAct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String typeflag = "0"; 
				Intent intent = new Intent(mActivity, ActIcBcDetailActivity.class);
				intent.putExtra(ActIcBcDetailFragment.ACTIVITY_CODE, cardItem.getActCode());
				intent.putExtra(ActIcBcDetailFragment.newInstance().TYPE, typeflag);
				mActivity.startActivity(intent);
			}
		});
		
		//是否有优惠券
		getIsFirst(cardItem.getIsFirst(), tvLessMoney);
		isHaveBankDiscount(cardItem.getHasIcbcDiscount(), tvBankDiscount);
		isHasCoupon(cardItem.getHasCoupon(), tvCoupon);
		
		//点击进入店铺
		lyIntoShopDetail.setTag(position);
		lyIntoShopDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, cardItem.getShopCode());
				//友盟统计
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("userName", mUser.getRealName());
				map.put("userCode", mUser.getUserCode());
				map.put("mobileNbr", mUser.getMobileNbr()); 
				MobclickAgent.onEvent(mActivity, "carddetail_cancel_shop", map);
			}
		});
		
		//点击进入店铺
		ryIntoShopDetail.setTag(position);
		ryIntoShopDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, cardItem.getShopCode());
			}
		});
		
		return holder.getConvertView();
	}
	
	//是否有首单立减
		private void getIsFirst(int isFirst, TextView lessMoney){
			if (isFirst == 1) {
				lessMoney.setVisibility(View.VISIBLE);
			} else {
				lessMoney.setVisibility(View.GONE);
			}
		}
		
		//判断是否有工银优惠
		private void isHaveBankDiscount(int hasIcbcDiscount, TextView bankDiscount){
			if (hasIcbcDiscount == 1) {
				bankDiscount.setVisibility(View.VISIBLE);
			} else {
				bankDiscount.setVisibility(View.GONE);
			}
		}
		
		//判断是否有优惠券
		private void isHasCoupon(int hasCoupon, TextView tvCoupon){
			if (hasCoupon == 1) {
				tvCoupon.setVisibility(View.VISIBLE);
			} else {
				tvCoupon.setVisibility(View.GONE);
			}
		}
}
