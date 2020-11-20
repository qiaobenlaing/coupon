package cn.suanzi.baomi.cust.adapter;

import java.util.HashMap;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
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
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.ApplyCardTask;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.umeng.analytics.MobclickAgent;

/**
 * 分类检索会员卡
 * @author qian.zhou
 */
public class CardAdapter extends CommonListViewAdapter<Shop> {
	
	/**获得一个用户信息对象**/
	private UserToken mUserToken;
	/**用户登录后获得的令牌 **/
	private String mTokenCode;
	private String mUserCode;
	private Shop shop;
	private Activity activity;
	
	public CardAdapter(Activity activity, List<Shop> datas) {
		super(activity, datas);
		this.activity = activity;
		
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
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_card, position);
		shop = (Shop) getItem(position);
		//店铺详情
		LinearLayout layout = holder.getView(R.id.ly_shopdetail);
		final TextView tvShopName = holder.getView(R.id.shopName);//商店名称
		tvShopName.setText(shop.getShopName());
		final ImageView showImageView = holder.getView(R.id.shopImage);//店铺头像
		Util.showImage(mActivity, shop.getLogoUrl() , showImageView);
		TextView tvIsVip = holder.getView(R.id.tv_isvip);//VIP
		final TextView tvApplyCard = holder.getView(R.id.tv_apply_card);//申请会员卡
        if(shop.getIsUserHasCard() == 1){//用户有会员卡
        	tvIsVip.setVisibility(View.VISIBLE);
        	tvApplyCard.setVisibility(View.GONE);
        	
		} else if(shop.getIsUserHasCard() == 0){//用户没有会员卡
			tvApplyCard.setVisibility(View.VISIBLE);
			tvIsVip.setVisibility(View.GONE);
		}
		//判断传过来的距离是否大于一千米
		String distance = shop.getDistance().replace(".", "").replace(",", "");
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
	    
	    //点击头像到店铺详情
	    showImageView.setTag(position);
	    showImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag();
				String shopCode = mDatas.get(index).getShopCode();
				SkipActivityUtil.skipNewShopDetailActivity(mActivity,shopCode);
			}
		});
	    //点击名称到店铺详情
	    tvShopName.setTag(position);
	    tvShopName.setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		final int index = (Integer) v.getTag();
	    		String shopCode = mDatas.get(index).getShopCode();
	    		SkipActivityUtil.skipNewShopDetailActivity(mActivity,shopCode);
	    	}
	    });
		    
		/*//查看店铺详情
		layout.setTag(position);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag();
				String ShopCode = mDatas.get(index).getShopCode();
				Log.d("TAG", "传过来的shopcode的值为：：：：：：：：：：：：：：："+ShopCode);
				Intent intent = new Intent(mActivity, ShopDetailActivity.class);
				intent.putExtra(ShopDetailFragment.SHOP_CODE, ShopCode);
				mActivity.startActivity(intent);
			}
		});*/
		
		//申请会员卡
		tvApplyCard.setTag(position);
		tvApplyCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag();
				String CardCode = mDatas.get(index).getCardCode();
				//友盟统计
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("shopcode",CardCode);
				map.put("shopName",shop.getShopName()); 
				MobclickAgent.onEvent(mActivity, "cardinfo_applycard", map); 
				
				tvApplyCard.setEnabled(false);
				new ApplyCardTask(mActivity, new ApplyCardTask.Callback() {
					@Override
					public void getResult(JSONObject result) {
						if(String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())){
							tvApplyCard.setEnabled(true);
							tvApplyCard.setVisibility(View.GONE);
							Util.getContentValidate(R.string.redbag_succ);
						} else{
							tvApplyCard.setEnabled(true);
							if(String.valueOf(CustConst.Card.BEEN_APPLY).equals(result.get("code").toString())){
								Util.getContentValidate(R.string.tv_been_apply);
							} else if(String.valueOf(CustConst.Card.OVER_COUNT).equals(result.get("code").toString())){
								Util.getContentValidate(R.string.tv_over_count);
							} else if(String.valueOf(CustConst.Card.NO_APPLY_CRITERIA).equals(result.get("code").toString())){
								Util.getContentValidate(R.string.tv_not_apply_criteria);
							} else if(String.valueOf(CustConst.Card.NO_USERCODE).equals(result.get("code").toString())){
								Util.getContentValidate(R.string.tv_no_usercode);
							} else if(String.valueOf(CustConst.Card.NO_CARDCODE).equals(result.get("code").toString())){
								Util.getContentValidate(R.string.tv_no_cardcode);
							}
						}
					}
				}).execute(CardCode);
			}
		});
		return holder.getConvertView();
	}
}
