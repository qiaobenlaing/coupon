package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.NewShopProduct;
import cn.suanzi.baomi.cust.R;

public class NewShopServiceAdapter extends BaseAdapter {
	private List<NewShopProduct> datas ;
	private Context context;
	
	public NewShopServiceAdapter(List<NewShopProduct> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_new_shop_service, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.iv_service_icon);
			holder.name = (TextView) convertView.findViewById(R.id.tv_service_name);
			holder.des =  (TextView) convertView.findViewById(R.id.tv_service_des);
			holder.finalPrice = (TextView) convertView.findViewById(R.id.tv_service_price);
			holder.orginPriceFrameLayout = (FrameLayout) convertView.findViewById(R.id.fl_original_price);
			holder.orginlPriceTextView = (TextView) convertView.findViewById(R.id.tv_orginal_price);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		NewShopProduct product = datas.get(position);
		
		Util.showFirstImages((Activity)context, product.getUrl(), holder.icon);
		holder.name.setText(product.getProductName());
		
		if(Util.isEmpty(product.getDes())){
			holder.des.setVisibility(View.INVISIBLE);
		}else{
			holder.des.setVisibility(View.VISIBLE);
			holder.des.setText(product.getDes());
		}
		
		holder.finalPrice.setText("￥"+product.getFinalPrice());
		
		if(product.getFinalPrice()<product.getOriginalPrice()){
			holder.orginPriceFrameLayout.setVisibility(View.VISIBLE);
			holder.orginlPriceTextView.setText("原价"+product.getOriginalPrice());
		}else{
			holder.orginPriceFrameLayout.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	class ViewHolder{
		ImageView icon;
		TextView name;
		TextView des;
		TextView finalPrice;
		FrameLayout orginPriceFrameLayout;
		TextView orginlPriceTextView;
		
	}
}
