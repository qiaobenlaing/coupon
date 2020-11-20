package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.Coupon;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;

public class ShopPayCouponsAdapter extends Adapter<ShopPayCouponsAdapter.ViewHolder> {
	
	private static final String TAG = ShopPayCouponsAdapter.class.getSimpleName();
	private List<Coupon> coupons;
	private OnItemClickListener mOnItemClickLitener;
	private Activity mActivity;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView tvInsteadPrice, tvTimeAvailable,tvAvailablePrice,tvMoney,tvSymbol,tvExruledes;

		public ViewHolder(View arg0) {
			super(arg0);
			tvInsteadPrice = (TextView) arg0.findViewById(R.id.instead_price);
			tvTimeAvailable = (TextView) arg0.findViewById(R.id.time_available);
			tvAvailablePrice = (TextView) arg0.findViewById(R.id.tv_availablePrice);
			tvMoney = (TextView) arg0.findViewById(R.id.tv_money);
			tvSymbol = (TextView) arg0.findViewById(R.id.tv_symbol);
			tvExruledes = (TextView) arg0.findViewById(R.id.tv_exruledes);
		}
	}

	public ShopPayCouponsAdapter(Activity activity, List<Coupon> coupons) {
		this.coupons = coupons;
		this.mActivity= activity; 
	}

	@Override
	public int getItemCount() {
		return coupons.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		Coupon coupon = coupons.get(position);
		holder.itemView.setTag(coupon);
		holder.tvTimeAvailable.setText("最后使用时间：" + coupon.getExpireTime());
		// 说明优惠券
		String canMoney = ""; 
		String insteadPrice = "";
		double aPrice = Double.parseDouble(coupon.getAvailablePrice());
		if (CustConst.Coupon.DEDUCT.equals(coupon.getCouponType())) { // 满就减 满XX元立减 
			canMoney = "满" + coupon.getAvailablePrice()+"元立减" + coupon.getInsteadPrice() + "元";
			holder.tvSymbol.setVisibility(View.GONE);
			holder.tvMoney.setVisibility(View.VISIBLE);
			holder.tvExruledes.setVisibility(View.GONE);
			holder.tvInsteadPrice.setVisibility(View.VISIBLE);
			insteadPrice = coupon.getInsteadPrice();
		} else if (CustConst.Coupon.DISCOUNT.equals(coupon.getCouponType())) { // 折扣券
			canMoney = "满" + coupon.getAvailablePrice()+"元打" + coupon.getDiscountPercent() + "折";
			holder.tvSymbol.setVisibility(View.GONE);
			holder.tvMoney.setVisibility(View.VISIBLE);
			holder.tvInsteadPrice.setVisibility(View.VISIBLE);
			insteadPrice = coupon.getDiscountPercent();
		} else if (CustConst.Coupon.N_BUY.equals(coupon.getCouponType())) { // N元购
			canMoney = coupon.getFunction();
			insteadPrice = coupon.getInsteadPrice();
			holder.tvSymbol.setVisibility(View.GONE);
			holder.tvMoney.setVisibility(View.VISIBLE);
			holder.tvExruledes.setVisibility(View.GONE);
			holder.tvInsteadPrice.setVisibility(View.VISIBLE);
			// 实物券和体验券
		} else if (CustConst.Coupon.REAL_COUPON.equals(coupon.getCouponType()) 
				|| CustConst.Coupon.EXPERIENCE.equals(coupon.getCouponType())) {
			canMoney = "满" + coupon.getAvailablePrice()+"可使用";
			holder.tvSymbol.setVisibility(View.VISIBLE);
			holder.tvInsteadPrice.setVisibility(View.GONE);
			if (CustConst.Coupon.REAL_COUPON.equals(coupon.getCouponType())) { // 实物券
				holder.tvSymbol.setText("实物券");
			} else { // 体验券
				holder.tvSymbol.setText("体验券");
			}
			holder.tvMoney.setVisibility(View.GONE);
			holder.tvExruledes.setVisibility(View.VISIBLE);
			holder.tvExruledes.setText(coupon.getExRuleDes());
			insteadPrice = coupon.getInsteadPrice();
		}
		
		holder.tvInsteadPrice.setText(insteadPrice+"");
		holder.tvAvailablePrice.setText(canMoney);
		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {
			holder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickLitener.onItemClick(null, holder.itemView, position, holder.getItemId());
				}
			});
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_coupon, arg0, false);
		ViewHolder viewHolder = new ViewHolder(v);
		return viewHolder;
	}

	public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
}
