package cn.suanzi.baomi.cust.adapter;

import java.util.HashMap;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.LoginActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.sGrabCouponTask;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.umeng.analytics.MobclickAgent;

/**
 * 分类检索商户
 * @author qian.zhou
 */
public class LikeAdapter extends CommonListViewAdapter<Shop> {
	private final static String TAG = LikeAdapter.class.getSimpleName();
	/**获得一个用户信息对象**/
	private UserToken mUserToken;
	/**用户登录后获得的令牌 **/
	private String mTokenCode;
	private String mUserCode;
	
	public LikeAdapter(Activity activity, List<Shop> datas) {
		super(activity, datas);
	}

	@Override
	public void setItems(List<Shop> shops) {
		super.setItems(shops);
	}
	@Override
	public void addItems(List<Shop> shops) {
		super.addItems(shops);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_shop, position);
		final Shop shop = (Shop) getItem(position);
		//店铺详情
		LinearLayout layout = holder.getView(R.id.ly_shopdetail);
		final ImageView showImageView = holder.getView(R.id.shopImage);
		final TextView tvShopName = holder.getView(R.id.shopName);
		tvShopName.setText(shop.getShopName());
		Util.showImage(mActivity, shop.getLogoUrl() , showImageView);
		final TextView mTvIsCoupon = holder.getView(R.id.rob);
		final TextView tvIsVip = holder.getView(R.id.tv_isvip);
		final ImageView ivIsHaveCopon  = holder.getView(R.id.iv_ishave_coupon);
		final TextView mTvMyMoney = holder.getView(R.id.tv_mymoney);
		final TextView tvMoney = holder.getView(R.id.tv_money);
		final TextView tvDiscount = holder.getView(R.id.tv_discount);//折扣
        //用户是否拥有该家商店的优惠券
        if(Util.isEmpty(shop.getBatchCouponCode())){//登陆用户没有该家商店优惠券
        	mTvIsCoupon.setVisibility(View.GONE);
        	ivIsHaveCopon.setVisibility(View.GONE);
        	mTvMyMoney.setVisibility(View.GONE);
        	tvMoney.setVisibility(View.GONE);
        	tvDiscount.setVisibility(View.GONE);
        } else{
        	if("0".equals(shop.getDiscountPercent()) || "".equals(shop.getDiscountPercent())){
        		if(!"0".equals(shop.getInsteadPrice()) || !"".equals(shop.getInsteadPrice())){
        			mTvMyMoney.setVisibility(View.VISIBLE);
        			tvDiscount.setVisibility(View.GONE);
             		tvMoney.setText(String.valueOf(shop.getInsteadPrice()));
        		}
         	} else{
         		mTvMyMoney.setVisibility(View.GONE);
         		tvDiscount.setVisibility(View.VISIBLE);
         		tvMoney.setText(String.valueOf(shop.getDiscountPercent()));
         	}
        	 if(shop.getIsUserCanGrab() == Util.NUM_ONE){//用户可以抢该优惠券
             	mTvIsCoupon.setVisibility(View.VISIBLE);
             	tvMoney.setVisibility(View.VISIBLE);
             	ivIsHaveCopon.setVisibility(View.GONE);
     		} else if(shop.getIsUserCanGrab() == Util.NUM_ZERO){//用户不可以抢优惠券
     			mTvIsCoupon.setVisibility(View.GONE);//抢
     			ivIsHaveCopon.setVisibility(View.VISIBLE);
     			tvMoney.setVisibility(View.VISIBLE);
     		}
        }
        if(shop.getIsUserHasCard() == Util.NUM_ONE){//有会员卡
        	tvIsVip.setVisibility(View.VISIBLE);
        } else if(shop.getIsUserHasCard() == Util.NUM_ZERO){//没有会员卡
        	tvIsVip.setVisibility(View.GONE);
        }
		
		//判断传过来的距离是否大于一千米
		String distance = shop.getDistance().replace(".", "").replace("," , "");
		if(Integer.parseInt(distance) >= 100000){
			((TextView) holder.getView(R.id.distance)).setText(mActivity.getResources().getString(R.string.distance));
		} else {
			if(Integer.parseInt(distance) >= 1000){
				Integer sdistance = Integer.parseInt(distance)/1000;
				((TextView) holder.getView(R.id.distance)).setText(sdistance + "KM");
			} else{
				((TextView) holder.getView(R.id.distance)).setText(distance + "M");
			}
		}
		    ((TextView) holder.getView(R.id.street)).setText(shop.getStreet());
	   /* if(mType == String.valueOf(Util.NUM_ONE)){
	    	Util.getContentValidate(R.string.friend_look));
	    } else if(mType == String.valueOf(Util.NUM_ZERO)){
	    	Util.getContentValidate(R.string.all_look));
	    } else if(mType == String.valueOf(Util.NUM_TWO)){
	    	Util.getContentValidate(R.string.someone_nolook));
	    }*/
		final boolean loginFlag = DB.getBoolean(DB.Key.CUST_LOGIN);
	    mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
    	if(loginFlag){//如果登陆了
    		if(mUserToken!=null){
    			mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
    			mUserCode = mUserToken.getUserCode();//获取用户编码
    		}
    	} else{
    		mUserCode = "";
    	}
	    //点击抢活动的时候
	    mTvIsCoupon.setTag(position);
	    mTvIsCoupon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loginFlag){//登陆了
					final int index = (Integer) v.getTag(); 
					String BatchCouponCode = mDatas.get(index).getBatchCouponCode();
					//友盟统计
					HashMap<String,String> map = new HashMap<String,String>();
					map.put("shopName", shop.getShopName());
					map.put("shopcode", shop.getShopCode()); 
					MobclickAgent.onEvent(mActivity, "shop_robcoupon", map);
					new sGrabCouponTask(mActivity, new sGrabCouponTask.Callback() {
						@Override
						public void getResult(JSONObject result) {
							if(String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())){
								mTvIsCoupon.setVisibility(View.GONE);
								ivIsHaveCopon.setVisibility(View.VISIBLE);
								shop.setIsUserCanGrab(0);
								Util.getContentValidate(R.string.codesuccess);
							} else{
								if(String.valueOf(CustConst.Coupon.GRAB_OVER).equals(result.get("code").toString())){
									Util.getContentValidate(R.string.coupon_over);
								} else if(String.valueOf(CustConst.Coupon.GRAB_EXPIRED).equals(result.get("code").toString())){
									Util.getContentValidate(R.string.coupon_expried);
								} else if(String.valueOf(CustConst.Coupon.GRAB_LIMIT).equals(result.get("code").toString())){
									Util.getContentValidate(R.string.coupon_limit);
								} else if(String.valueOf(CustConst.Coupon.GRAB_NOEXIT).equals(result.get("code").toString())){
									Util.getContentValidate(R.string.coupon_noexit);
								} 
							} 
						}
					}).execute(BatchCouponCode, mUserCode, String.valueOf(Util.NUM_TWO), mTokenCode);
				} else{//没有登陆
					Util.getContentValidate(R.string.no_login);
					Intent intent = new Intent(mActivity, LoginActivity.class);
			    	mActivity.startActivity(intent);
				}
			}
		});
		
	    //点击头像到店铺详情
	    showImageView.setTag(position);
	    showImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag();
				String shopCode = mDatas.get(index).getShopCode();
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, shopCode);
				//友盟统计
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("shopcode", shopCode); 
				map.put("shopName", shop.getShopName()); 
				MobclickAgent.onEvent(mActivity, "shop_shoplogo", map);
			}
		});
	    tvShopName.setTag(position);
	    tvShopName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag();
				String shopCode = mDatas.get(index).getShopCode();
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, shopCode);
			}
		});
		return holder.getConvertView();
	}
}
