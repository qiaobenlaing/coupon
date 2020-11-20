package com.huift.hfq.cust.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.PromotionPrice;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.cust.R;
/**
 * 购买活动的价格适配器
 * @author yingchen
 *
 */
public class PromotionPrcieAdaapter extends BaseAdapter implements View.OnClickListener{
	private static final String TAG = PromotionPrcieAdaapter.class.getSimpleName();
	private Activitys mData;
	private Context mContext;
	private CallBack mCallBack;
	private ViewHolder holder;
	
	
	public PromotionPrcieAdaapter(Activitys mData, Context mContext,CallBack mCallBack) {
		super();
		this.mData = mData;
		this.mContext = mContext;
		this.mCallBack = mCallBack;
	}

	@Override
	public int getCount() {
		return mData.getFeeScale().size();
	}

	@Override
	public Object getItem(int position) {
		return mData.getFeeScale().get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_promotion_price, null);
			
			holder.type = (TextView) convertView.findViewById(R.id.tv_promotion_type);
			holder.price = (TextView) convertView.findViewById(R.id.tv_promotion_price);
			holder.minus = (TextView) convertView.findViewById(R.id.tv_promotion_minus);
			holder.count = (TextView) convertView.findViewById(R.id.tv_promotion_count);
			holder.plus = (TextView) convertView.findViewById(R.id.tv_promotion_plus);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		PromotionPrice promotionPrice =  mData.getFeeScale().get(position);
		
		holder.type.setText(promotionPrice.getDes());
		
		holder.price.setText(String.valueOf(promotionPrice.getPrice()));

		holder.count.setText(String.valueOf(promotionPrice.getNbr()));
		
		
		holder.plus.setOnClickListener(this);
		holder.plus.setTag(position);
		
		holder.minus.setOnClickListener(this);
		holder.minus.setTag(position);
		
		return convertView;
	}
	
	
	public void showMentionedMesg(String msg){
		DialogUtils.showDialogSingle((Activity)mContext, msg, R.string.cue, R.string.ok,null);
	}
	
	public class ViewHolder{
		public TextView type;
		public TextView price;
		public TextView minus;
		public TextView count;
		public TextView plus;
	}

	/**
	 * 总价格的回掉
	 * @author yingchen
	 *
	 */
	public interface CallBack{
		void getTotalPrice(double totalPrice);
	}

	@Override
	public void onClick(View v) {
		Integer position = (Integer) v.getTag();
		switch (v.getId()) {
		case R.id.tv_promotion_plus:
			//Toast.makeText(mContext, ""+position, 0).show();
			PromotionPrice plusPromotionPrice = mData.getFeeScale().get(position);
			boolean checkTotalCount = checkTotalCount();
			if(checkTotalCount){
				plusPromotionPrice.setNbr(plusPromotionPrice.getNbr()+1);
				this.notifyDataSetChanged();				
			}
			double totalPrice1 = getTotalPrice();
			Log.d(TAG, "totalPrice==="+totalPrice1);
			mCallBack.getTotalPrice(totalPrice1);
			break;

		case R.id.tv_promotion_minus:
			//Toast.makeText(mContext, ""+position, 0).show();
			PromotionPrice minusPromotionPrice = mData.getFeeScale().get(position);
			if(minusPromotionPrice.getNbr()-1<0){
				showMentionedMesg("购买的数量不能小于0");
			}else{
				minusPromotionPrice.setNbr(minusPromotionPrice.getNbr()-1);
				this.notifyDataSetChanged();
			}
			double totalPrice2 = getTotalPrice();
			Log.d(TAG, "totalPrice==="+totalPrice2);
			mCallBack.getTotalPrice(totalPrice2);
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 检查购买所有的数量
	 */
	private boolean checkTotalCount(){
		int totalCount = getTotalCount();
		if(totalCount>=mData.getRegisterNbrRequired()){
			showMentionedMesg("购买的总数量超过限制");
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 获取购买的总数量
	 * @return
	 */
	private int getTotalCount(){
		int totalCount = 0;
		for(int i=0;i<mData.getFeeScale().size();i++){
			totalCount+=mData.getFeeScale().get(i).getNbr();
		}
		return totalCount;
	}
	
	private double  getTotalPrice(){
		double totalPrice = 0;
		for(int i=0;i<mData.getFeeScale().size();i++){
			PromotionPrice promotionPrice = mData.getFeeScale().get(i);
			totalPrice+=promotionPrice.getPrice()*promotionPrice.getNbr();
		}
		return totalPrice;
	}
	
	
}
