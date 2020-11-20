package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.CouponSelectBatch;
import cn.suanzi.baomi.cust.R;
/**
 * 抵扣券
 * @author yingchen
 *
 */
public class CouponSelectdeDuctionAdapter extends BaseAdapter {
	

	private Context context;
	
	private List<CouponSelectBatch> list;
	
	public CouponSelectdeDuctionAdapter(Context context, List<CouponSelectBatch> list) {
		super();
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_coupon_duction, null);
			holder.couponMoney = (TextView) convertView.findViewById(R.id.tv_item_coupon_use);
			holder.couponTotal = (TextView) convertView.findViewById(R.id.tv_item_coupon_man);
			holder.batch = (TextView) convertView.findViewById(R.id.tv_item_coupon_batch);
			holder.date = (TextView) convertView.findViewById(R.id.tv_item_coupon_durdate);
			holder.time = (TextView) convertView.findViewById(R.id.tv_item_coupon_durtime);
			holder.count = (TextView) convertView.findViewById(R.id.tv_item_coupon_total_count);
			holder.opreate = (RelativeLayout) convertView.findViewById(R.id.rl_item_coupon_operate);
			holder.add = (ImageView) convertView.findViewById(R.id.iv_item_coupon_add);
			holder.use = (TextView) convertView.findViewById(R.id.tv_item_coupon_use_count);
			holder.minus = (ImageView) convertView.findViewById(R.id.iv_item_coupon_minus);
			holder.send =  (TextView) convertView.findViewById(R.id.tv_item_coupon_send);
			holder.function = (TextView) convertView.findViewById(R.id.tv_item_coupon_describle);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		CouponSelectBatch couponSelectBatch = list.get(position);
		holder.couponMoney.setText("¥"+couponSelectBatch.getInsteadPrice());
		holder.couponTotal.setText("满"+couponSelectBatch.getAvailablePrice()+"可用");
		holder.batch.setText("批次号"+couponSelectBatch.getBatchNbr());
		holder.date.setText(couponSelectBatch.getStartUsingTime()+"-"+couponSelectBatch.getExpireTime());
		holder.time.setText(couponSelectBatch.getDayStartUsingTime()+"-"+couponSelectBatch.getDayEndUsingTime());
		holder.function.setText(couponSelectBatch.getFunction());
		
		if(couponSelectBatch.isShowCouponCount()){  //显示优惠券数量
			holder.count.setVisibility(View.VISIBLE);
			holder.opreate.setVisibility(View.GONE);
			
			holder.count.setText("X"+couponSelectBatch.getUserCount());
		}else{ //显示优惠券操作
			holder.count.setVisibility(View.GONE);
			holder.opreate.setVisibility(View.VISIBLE);
		}
		
		//是否显示"送"的标识
		if("0".equals(couponSelectBatch.getIsSend())){
			holder.send.setVisibility(View.GONE);
		}else if("1".equals(couponSelectBatch.getIsSend())){
			holder.send.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	public class ViewHolder{
		public TextView couponMoney;
		public TextView couponTotal;
		public TextView batch;
		public TextView date;
		public TextView time;
		public TextView count;
		public RelativeLayout opreate;
		public ImageView add;
		public TextView use;
		public ImageView minus;
		public TextView send;
		public TextView function;
	}
	
	
	
	
}
